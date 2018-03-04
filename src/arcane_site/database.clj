(ns arcane-site.database
  (:require [clojure.java.jdbc :as jdbc]
            [arcane-site.util :as util]))

(def table-specs
  [;;meta stuff
   [:id :int "not null" "auto_increment" "primary key"]
   [:date :datetime]
   ;;Status of application, pending, accepted, denied
   [:status "varchar(7)"]
   ;;Username of user reviewing application
   [:reviewed_by "varchar(15)"]
   ;;Comment that staff member can make when reviewing app.
   [:comment "varchar(255)"]

   ;;Application fields (what the user enters)
   [:username "varchar(15)" "not null"]
   [:email "varchar(255)"]
   [:age "tinyint(255)"]
   [:bio "text"]
   [:referral "varchar(255)"]])

(defn create-db
  [db]
  (jdbc/db-do-commands db (jdbc/create-table-ddl :applications table-specs)))

(def sample-app
  {:username "jugglingman456"
   :email "barestides@gmail.com"
   :age 20
   :bio "I administer this server; I shouldn't need to apply!"
   :referral "myself"})

(defn insert-application
  [db app-map]
  (jdbc/insert! db "applications"
                (merge app-map
                       {:date (new java.util.Date)
                        :status "PENDING"})))

(defn get-applications
  [db]
  (jdbc/query
   db "SELECT * FROM applications"))

(defn get-pending-apps
  [db]
  (jdbc/query
   db "SELECT * FROM applications WHERE status = 'PENDING'"))

(defn get-email
  [db id]
  (:email (first (jdbc/query db
                             ["SELECT email FROM applications WHERE id = ?" id] ))))

(defn review-app!
  [db id comments decision staff-name]
  (let [status (case decision
                 :accept "ACCEPT"
                 :deny "DENY")]
    (jdbc/update! db :applications {:status status :comment comments :reviewed_by staff-name}
                  ["id = ? " id])))
