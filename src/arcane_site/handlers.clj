(ns arcane-site.handlers
  (:require [clojure.pprint :as pprint]
            [bouncer.core :as bouncer]
            [bouncer.validators :as v]
            [org.httpkit.client :as http]
            [environ.core :as environ]
            [cheshire.core :as chesh]
            [arcane-site.db :as db]
            [arcane-site.util :as util]))

(v/defvalidator staff-name
  {:default-message-format "Must give a valid staff name"}
  [name]
  ;;We get the valid staff usernames from the forum.
  (let [req-url (str "https://forum.arcaneminecraft.com/admin/users/list/staff.json?api_key="
                     (environ/env :forum-api-key))
        resp (http/get req-url)
        ;;convert json->edn and extract usernames
        valid-usernames (map :username (chesh/parse-string (:body @resp) true))]
    (util/string-in-coll-case-ignore? name valid-usernames)))

(def app-validator
  {:ign v/required
   :bio v/required
   :age v/required
   :referral v/required
   :staff-ign staff-name
   :email #(or (= "" %) (v/email %))})

(defn submit-app
  "On successful validation, insert the request from the application form into the db."
  [req]
  (let [app-map (update (:params req) :age #(Integer. %))]
    (pprint/pprint (bouncer/validate app-map app-validator)))
  )
