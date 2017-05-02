(ns arcane-site.email
  (:require [clojure.pprint :as pprint]
            [hiccup.core :as h]
            [postal.core :as postal]
            [environ.core :as environ]))

(def smtp-info {:host "smtp.sparkpostmail.com"
                :user "SMTP_Injection"
                :pass (environ/env :sparkpost-api-key)
                :port 587})

(defn send-email [address subject body]
  (postal/send-message smtp-info
                       {:from "admin@arcaneminecraft.com"
                        :to address
                        :subject subject
                        :body body}))

(defn email-app-review
  [username email decision]


  )
