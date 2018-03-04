(ns arcane-site.core
  (:gen-class)
  (:require [arcane-site.server :as server]))

(defn -main
  [& args]
  (server/start-server args))
