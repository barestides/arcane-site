(ns arcane-site.handlers
  (:require [clojure.pprint :as pprint]
            [bouncer.core :as bouncer]
            [bouncer.validators :as v]
            [ring.util.response :as resp]
            [bidi.bidi :as bidi]
            [arcane-site.views.pages :as pages]
            [arcane-site.authentication :as auth]
            [arcane-site.email :as email]
            [arcane-site.routes :as routes]
            [arcane-site.database :as db]
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

(defn greylist-player [username]
  (let [greylist-command (str "lp user " username " parent add trusted")]
    (util/execute-game-command greylist-command)))

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
  [server-state req]
  (let [app-map (-> req
                    :params
                    (update :age #(Integer. %)))
        [validation-errors app-map] (bouncer/validate app-map app-validator)
        db (:db @server-state)]
    (if (nil? validation-errors)
      ;;If we have no validation errors, insert the app into the database, and send the user to the
      ;;success page
      (do (db/insert-application db (dissoc app-map :staff-ign :email-toggle))
          (redirect :app-success))
      ;;If validation fails, send error to user
      (error-response (apply concat (vals validation-errors))))))

(defn review-app
  [server-state req]
  (let [{:keys [username comments id accept staff-name]} (:params req)
        decision (if accept :accept :deny)
        db (:db @server-state)
        email (db/get-email db id)]
    (do (db/review-app! db id comments decision staff-name)
        (when (not (empty? email))
          (email/email-app-review username email decision comments))
        (when accept (greylist-player username))
        (resp/response ""))))

(defn review-page
  [req server-state]
  ;;there should be an if let with multiple bindings, and it only is true if all bindings work out.
  ;;could have another one for if any bindings work, so an if-let-and and an if-let-or
  (if-let [sso (get-in req [:params :sso])]
    (let [sig (get-in req [:params :sig])
          response (auth/decode-auth-response sso sig server-state)]
      ;;jinkies do a 403 response
      (if response
        (pages/review server-state)
        (redirect :index)))
    (resp/redirect (auth/authentication-url (bidi/path-for routes/routes :review) server-state))))
