(ns arcane-site.server
  (:require
   [clojure.pprint :as pprint]
   [clojure.walk :as walk]
   [org.httpkit.server :as httpkit]
   [ring.util.response :as resp]
   [ring.middleware.format :refer [wrap-restful-format]]
   [ring.middleware.resource :as resource]
   [ring.middleware.params :as params]
   [ring.middleware.keyword-params :as kp]
   [bidi.bidi :as bidi]
   [bidi.ring :refer (make-handler) :as br]
   [arcane-site.handlers :as handlers]
   [arcane-site.routes :as routes]
   [arcane-site.views.pages :as pages]))

(def routes->handlerfns
  ;;Static pages don't need the request info
 (let [page (fn [page-handler]
               (fn [_] resp/response (page-handler)))]
   {:index (fn [_] (pages/index-page))
    :apply (fn [_] (pages/main-application))
    :submit-app handlers/submit-app}))

(def app (-> routes/routes
             (make-handler routes->handlerfns )
             (resource/wrap-resource "public")
             (wrap-restful-format :formats [:edn :json])
             kp/wrap-keyword-params
             params/wrap-params))

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
