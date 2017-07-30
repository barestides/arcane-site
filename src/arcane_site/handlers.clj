(ns arcane-site.handlers
  (:require [clojure.pprint :as pprint]
            [bouncer.core :as bouncer]
            [bouncer.validators :as v]
            [environ.core :as environ]
            [ring.util.response :as resp]
            [bidi.bidi :as bidi]
            [arcane-site.email :as email]
            [arcane-site.routes :as routes]
            [arcane-site.db :as db]
            [arcane-site.util :as util]))

(defn redirect [route]
  (resp/redirect (bidi/path-for routes/routes route)))

(v/defvalidator staff-name
  {:default-message-format "Must give a valid staff name"}
  [name]
  ;;We get the valid staff usernames from the forum.
  (util/string-in-coll-case-ignore? name (util/get-staff-usernames)))

(defn error-response [body]
  {:status 400
   :body body})

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
  (let [app-map (-> req
                    :params
                    (update :age #(Integer. %)))
        [validation-errors app-map] (bouncer/validate app-map app-validator)]
    (prn validation-errors)
    (if (nil? validation-errors)
      ;;If we have no validation errors, insert the app into the database, and send the user to the
      ;;success page
      (do
        (-> app-map
            ;;We just want to ensure that the applicant entered a valid staff name; we don't need
            ;;to store it.
            (dissoc :staff-ign :email-toggle)
            db/insert-application)
        (redirect :app-success))
      ;;If validation fails, send error to user
      (error-response (apply concat (vals validation-errors))))))

(defn review-app [req]
  (pprint/pprint (:params req))
  (let [{:keys [username comments id accept staff-name]} (:params req)
        decision (if accept :accept :deny)
        email (db/get-email id)]
    (do (db/review-app! id comments decision staff-name)
        (when email (email/email-app-review username email decision comments))
        (resp/response ""))))
