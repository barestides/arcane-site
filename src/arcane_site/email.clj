(ns arcane-site.email
  (:require [clojure.pprint :as pprint]
            [hiccup.core :as h]
            [postal.core :as postal]
            [environ.core :as environ]))

(def smtp-info {:host "smtp.sparkpostmail.com"
                :user "SMTP_Injection"
                :pass (environ/env :sparkpost-api-key)
                :port 587})

(defn send-email [address {:keys [subject body plain-text]}]
  (postal/send-message smtp-info
                       {:from "admin@arcaneminecraft.com"
                        :to address
                        :subject subject
                        :body [:alternative
                               {:type "text/plain"
                                :content plain-text}
                               {:type "text/html"
                                :content (h/html body)}]}))

(def signature
  [:div
   "Thanks,"
   [:br]
   "Arcane Staff"])

(defn acceptance-email [username comments]
  {:subject "Welcome to Arcane!"
   :body [:div
          [:h2 (str "Welcome, " username ", your greylist application has been accepted!")]
          [:div {:style "font-size: 16px;"}
           [:p  comments]
           [:p "You can now build freely on Arcane."]
           [:p "Please review our rules at "
            [:a {:href "arcaneminecraft.com/rules"} "arcaneminecraft.com/rules"]]
           [:span  "Server IP: " [:code {:style {:text-decoration :none}} "game.arcaneminecraft.com"]]
           [:p  "Also be sure to join our forums at "
            [:a {:href "forum.arcaneminecraft.com"} "forum.arcaneminecraft.com"]]
           signature]]
   :plain-text  "Your application has been accepted!"})

(defn denied-email [username comments])

(defn email-app-review
  [username email decision comments]
  (let [email-content (case decision
                        :accept acceptance-email
                        :deny denied-email)]
    (send-email email (email-content username comments))))
