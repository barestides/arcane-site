(ns arcane-site.server
  (:require
   [clojure.pprint :as pprint]
   [org.httpkit.server :as httpkit]
   [ring.util.response :as resp]
   [ring.middleware.format :refer [wrap-restful-format]]
   [ring.middleware.resource :as resource]
   [ring.middleware.params :as params]
   [ring.middleware.keyword-params :as kp]
   [ring.middleware.json :refer [wrap-json-body]]
   [bidi.bidi :as bidi]
   [bidi.ring :refer (make-handler) :as br]
   [arcane-site.authentication :as auth]
   [arcane-site.handlers :as handlers]
   [arcane-site.routes :as routes]
   [arcane-site.views.pages :as pages]))

(defonce server-state (atom {:nonces #{}}))
(defonce server (atom nil))

(def routes->handlerfns
  ;;Static pages don't need the request info
 (let [page (fn [page-handler]
               (fn [_] resp/response (page-handler)))]
   {:index (fn [_] (pages/index-page))
    :apply (fn [_] (pages/main-application))
    :app-success (fn [_] (pages/app-success))
    :tools (fn [_] (pages/tools-page))
    :rules (fn [_] (pages/rules-page))
    :review (fn [req] (handlers/review-page req server-state))
    :submit-app handlers/submit-app
    :review-app handlers/review-app}))

(def app (-> routes/routes
             (make-handler routes->handlerfns)
             (resource/wrap-resource "public")
             (wrap-restful-format :formats [:json :edn])
             kp/wrap-keyword-params
             params/wrap-params
             wrap-json-body))

(defn stop-server []
  (@server :timeout 100)
  (reset! server nil))

(defn start-server []
  (reset! server-state {:nonces #{}})
  (reset! server (httpkit/run-server #'app {:port 8080})))

(defn restart-server
  []
  (stop-server)
  (reset! server-state {:nonces #{}})
  (reset! server (httpkit/run-server #'app {:port 8080})))
