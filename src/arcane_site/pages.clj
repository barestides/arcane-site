(ns arcane-site.pages
  (:require [hiccup.core :as hiccup]
            [arcane-site.routes :as routes]))

(defn index-page [_]
  {:status  200
   :headers {"Content-Type" "text/html"}
   :body    "This is the index page"})

(defn slick-page [_]
  {:status  200
   :headers {"Content-Type" "text/html"}
   :body    "This is the slick page"})
