(ns arcane-site.db
  (:require [clojure.java.jdbc :as jdbc]
            [honeysql.core :as sql]
            [honeysql.helpers :as hs]
            [environ.core :as environ]))

;; (def table-specs
;;   [;;meta stuff
;;    [:id :int "not null" "auto_increment"]
;;    [:date :datetime]
;;    ;;Status of application, pending, accepted, denied
;;    [:status "varchar(8)"]
;;    ;;Username of user reviewing application
;;    [:reviewed_by "varchar(16)"]

;;    ;;Application fields (what the user enters)
;;    [:username "varchar(16)" "not null"]
;;    [:email "varchar(255)"]
;;    [:age "tinyint(255)"]
;;    [:bio "blob"]
;;    [:referral "varchar(255)"]])

(def database
  {:dbtype "mysql"
   :dbname "arcane_site_test"
   :host "localhost"
   :user (environ/env :database-user)
   :password (environ/env :database-password)})

(def sample-app
  {:ign "jugglingman456"
   :email "barestides@gmail.com"
   :age 20
   :bio "I administer this server; I shouldn't need to apply!"
   :referral "myself"})

(defn insert-application
  [app-map]
  (jdbc/insert! database "applications"
                (merge app-map
                       {:date (new java.util.Date)
                        :status "PENDING"}))
  )
