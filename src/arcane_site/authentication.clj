(ns arcane-site.authentication
  (:require [clj-http.client :as http]
            [clj-http.util :as http-util]
            [environ.core :as environ]
            [crypto.random :as crypto-random]
            [pandect.algo.sha256 :as pandect]
            [base64-clj.core :as base64]
            [arcane-site.util :as util]))

;;I used this
;;https://meta.discourse.org/t/using-discourse-as-a-sso-provider/32974

(defn authentication-url
  [route server-state]
  (let [nonce (crypto-random/base64 20)
        return-url (str (environ/env :web-url) route)
        payload (str "nonce=" (http-util/url-encode nonce) "&return_sso_url=" return-url)
        payload-base64  (http-util/base64-encode (.getBytes payload))
        payload-base64-url (http-util/url-encode payload-base64)
        signature (pandect/sha256-hmac payload-base64 (environ/env :forum-sso-secret))]
    (swap! server-state update :nonces conj nonce)
    (str (environ/env :forum-sso-url) "sso=" payload-base64-url "&sig=" signature)))

;;Auth can fail if the sig doesn't match, the nonce isn't in the set of sent nonces, or if the user
;;is not a moderator.

;;jinkies add more detailed error messages for when auth fails
(defn decode-auth-response
  [sso sig server-state]
  (prn server-state)
  (let [decoded-sig (pandect/sha256-hmac sso (environ/env :forum-sso-secret))]
    (if (= decoded-sig sig)
      ;;The linebreaks mess it up.. not sure why they're included..
      (let [query-string (http-util/url-decode (base64/decode (util/remove-newlines sso)))
            query-map (util/parse-query-string query-string)
            {:keys [nonce moderator]} query-map]
        (if (contains? (:nonces @server-state) nonce)
          (do
            (swap! server-state update :nonce disj nonce)
            (if (= "true" (:moderator query-map))
              true
              ;;user is not moderator
              false))
          ;;nonce wasn't setn
          false))
      ;;signature doesn't match
      false)))
