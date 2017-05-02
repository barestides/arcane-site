(ns arcane-site.views.pages
  (:require [hiccup.page :as page]
            [bidi.bidi :as b]
            [arcane-site.views.copy :as copy]
            [arcane-site.views.components :as components]))

(defn gen-page [title body]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (page/html5
          [:head
           [:title (str title " &middot; Arcane Survival")]
           [:meta {:name "viewport" :content "width=device-width, initial-scale=1"}]
           (page/include-css "https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css")
           (page/include-css "/css/site.css")
           (page/include-js "https://code.jquery.com/jquery-3.2.1.min.js")
           (page/include-js "https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js")
           (page/include-js "/js/site.js")]
          [:body
           components/nav
           [:div.container
            body]])})

(defn index-page []
  (gen-page
   "Home"
   [:div.jumbotron
    [:h1 "Arcane Survival"]
    components/ip-box
    [:p (copy/get-copy :welcome-message)]
    (components/button-link "Apply to Build" :apply)]))

(defn not-found []
  (gen-page
   "404"
   [:div
    components/nav
    [:h1 "404: the page isn ot found"]]
   ))

(defn main-application []
  (gen-page
   "Application"
    [:div
     [:h1 "Greylist Application"]
     [:p (copy/get-copy :application-about)]
     components/application-form]))

(defn app-success []
  (gen-page
   "Success"
   [:h1 "Thank you for your application!"]))

(defn review []
  (gen-page
   "Application Review"
   (list [:h1 "Pending Applications"]
         (components/application-list)))
  )
