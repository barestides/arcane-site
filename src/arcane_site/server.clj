(ns arcane-site.server
  (:require [org.httpkit.server :as httpkit]
            [ring.util.response :as resp]
            [ring.middleware.format :refer [wrap-restful-format]]
            [bidi.bidi :as bidi]
            [bidi.ring :refer (make-handler)]
            [arcane-site.routes :as routes]
            [arcane-site.pages :as pages]))

(def routes->handlerfns
   {:index pages/index-page
     :slick pages/slick-page})

(def app (-> routes/routes
             (make-handler routes->handlerfns )
             (wrap-restful-format :formats [:edn :json])))

(defonce server (atom nil))

(defn stop-server []
  (@server :timeout 100)
  (reset! server nil))

(defn start-server []
  (reset! server (httpkit/run-server #'app {:port 8080})))

(defn restart-server
  []
  (stop-server)
  (reset! server (httpkit/run-server #'app {:port 8080})))
