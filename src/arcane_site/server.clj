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
   [environ.core :as environ]
   [arcane-site.authentication :as auth]
   [arcane-site.database :as database]
   [arcane-site.handlers :as handlers]
   [arcane-site.routes :as routes]
   [arcane-site.views.pages :as pages]))

(defonce server-state (atom {:nonces #{}
                             :db {}}))
(defonce server (atom nil))

(def routes->handlerfns
  ;;Static pages don't need the request info
 (let [page (fn [page-handler]
              (fn [_] resp/response (page-handler)))
       with-state (fn [handler] (partial handler server-state))]
   {:index (fn [_] (pages/index-page))
    :apply (fn [_] (pages/main-application))
    :app-success (fn [_] (pages/app-success))
    :tools (fn [_] (pages/tools-page))
    :rules (fn [_] (pages/rules-page))
    :review (fn [req] (handlers/review-page req server-state))
    :submit-app (with-state handlers/submit-app)
    :review-app (with-state handlers/review-app)}))

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

(defn start-server
  [& args]
  (let [db {:dbtype "mysql"
            :dbname (environ/env :database-name)
            :host "localhost"
            :user (environ/env :database-user)
            :password (environ/env :database-password)}]
    (when (contains? (set args) '("--create-db"))
      (database/apply-db-schema db))
    (reset! server-state {:nonces #{}
                          :db db})
    (reset! server (httpkit/run-server #'app {:port 8082}))))

(defn restart-server
  []
  (stop-server)
  (start-server))
