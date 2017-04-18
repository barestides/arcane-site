(ns arcane-site.util
  (:require [clojure.string :as str]))

(defn string-in-coll-case-ignore?
  [string coll]
  (let [upper-string (str/upper-case string)
        upper-coll (map (partial str/upper-case) coll)]
    (boolean (some #{upper-string} upper-coll))))
