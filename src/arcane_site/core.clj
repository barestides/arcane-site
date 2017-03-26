(ns arcane-site.core
  (:gen-class)
  (:require [arcane-site.server :as server]))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (server/start-server))
