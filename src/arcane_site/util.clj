(ns arcane-site.util
  (:require [clojure.string :as str]
            [cheshire.core :as chesh]
            [org.httpkit.client :as http]
            [environ.core :as environ]))

(defn string-in-coll-case-ignore?
  [string coll]
  (let [upper-string (str/upper-case string)
        upper-coll (map (partial str/upper-case) coll)]
    (boolean (some #{upper-string} upper-coll))))

(defn get-staff-usernames []
  (let [req-url (str "https://forum.arcaneminecraft.com/admin/users/list/staff.json?api_key="
                     (environ/env :forum-api-key))
        resp (http/get req-url)
        ;;convert json->edn and extract usernames
        valid-usernames (map :username (chesh/parse-string (:body @resp) true))]
    (remove (partial = "system") valid-usernames)
    )
  )
