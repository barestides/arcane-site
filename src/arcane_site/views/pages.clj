(ns arcane-site.views.pages
  (:require [hiccup.page :as page]
            [bidi.bidi :as b]
            [clojure.pprint :as pprint]
            [arcane-site.analytics :as analytics]
            [arcane-site.views.copy :as copy]
            [arcane-site.views.components :as components]))

(defn gen-page [title body]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (page/html5
          {:lang "en"}
          [:head
           [:title (str title " &middot; Arcane Survival")]
           [:meta {:name "viewport" :content "width=device-width, initial-scale=1"}]
           (page/include-css "https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css")
           (page/include-css "/css/site.css")
           (page/include-js "https://code.jquery.com/jquery-3.2.1.min.js")
           (page/include-js "https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js")
           (page/include-js "/js/site.js")]
          [:body
           (components/nav title)
           [:div.container body]
           [:div.overlay]])})

(defn index-page []
  (gen-page
   "Home"
   [:div
    [:div.jumbo
     [:h1 "Arcane Survival"]
     [:p (copy/get-copy :welcome-message)]]
    [:div.ip-and-app-button
     components/ip-box
     (components/button-link "Apply to Build" :apply)]]))

(defn not-found []
  (gen-page
   "404"
   [:div
    components/nav
    [:h1 "404: the page is not found"]]))

(defn main-application []
 (gen-page
   "Application"
   [:div [:div#app-page
          [:h1 "Greylist Application"]
          [:p (copy/get-copy :application-about)]
          components/application-form]
    components/app-success]))

(defn app-success []
  (gen-page
   "Success"
   [:h1 "Thank you for your application!"]))

(defn review []
  (gen-page
   "Application Review"
   (list [:h1 "Pending Applications"]
         (components/application-list))))

(defn tools-page []
  (gen-page
   "Tools"
   [:div
    [:h1 "Tools"]
    (components/tools)]))

(defn rules-page []
  (gen-page
   "Rules"
   [:div
    [:h1 "Rules"]
    (components/rules-list)]))

;;This isn't ready yet
;; (defn user-page [req]
;;   (let [user (get-in req [:params :username])
;;         user-hours (analytics/total-time-for-user-hours user)
;;         active-since (partial analytics/total-time-for-user-hours user)]
;;     (gen-page
;;      user
;;      [:div
;;       [:h1 user]
;;       [:div "Total hours: " user-hours]
;;       [:div "Total hours past day: " (active-since 24)]
;;       [:div "Total hours past week: " (active-since (* 24 7))]
;;       [:div "Total hours past 30 days: " (active-since (* 24 30))]])))
