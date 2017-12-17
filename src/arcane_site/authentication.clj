(ns arcane-site.authentication
  (:require [clj-http.client :as http]
            [clj-http.util :as http-util]
            [environ.core :as environ]
            [crypto.random :as crypto-random]
            [pandect.algo.sha1 :as pandect]
            [arcane-site.util :as util]))

;;I used this
;;https://meta.discourse.org/t/using-discourse-as-a-sso-provider/32974

(defn authentication-url []
  (let [nonce (crypto-random/base64 20)
        return-url "http://home.arestides.com:8080/review"
        payload (str "nonce=" (http-util/url-encode nonce) "&return_sso_url="
                     (http-util/url-encode return-url))
        payload-base64  (http-util/base64-encode (.getBytes payload))
        payload-base64-url (http-util/url-encode payload-base64)
        signature (pandect/sha1-hmac payload-base64 (environ/env :forum-sso-secret))
        hex-signature (util/hexify-str signature)]
    (prn signature)
    (prn hex-signature)
    (str (environ/env :forum-sso-url) "sso=" payload-base64-url "&sig=" signature)))
