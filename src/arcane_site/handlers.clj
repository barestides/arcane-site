(ns arcane-site.handlers
  (:require [clojure.pprint :as pprint]
            [bouncer.core :as bouncer]
            [bouncer.validators :as v]
            [org.httpkit.client :as http]
            [environ.core :as environ]
            [cheshire.core :as chesh]
            [ring.util.response :as resp]
            [bidi.bidi :as bidi]
            [arcane-site.routes :as routes]
            [arcane-site.db :as db]
            [arcane-site.util :as util]))

(defn redirect [route]
  (resp/redirect (bidi/path-for routes/routes route)))

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
  {:username v/required
   :bio v/required
   :age v/required
   :referral v/required
   :staff-ign staff-name
   ;;Email is optional, but if the field is filled out, it should match the email validation.
   :email #(or (= "" %) (v/email %))})

(defn submit-app
  "On successful validation, insert the request from the application form into the db."
  [req]
  (let [app-map (update (:params req) :age #(Integer. %))
        [validation-errors app-map] (bouncer/validate app-map app-validator)]
    (if (nil? validation-errors)
      ;;If we have no validation errors, insert the app into the database, and send the user to the
      ;;success page
      (do
        (-> app-map
            ;;We just want to ensure that the applicant entered a valid staff name; we don't need
            ;;to store it.
            (dissoc :staff-ign)
            db/insert-application)
        (redirect :app-success))
      ;;If validation fails, send error to user
      (pprint/pprint "You have app errors")
      )
    )
  )
