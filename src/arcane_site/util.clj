(ns arcane-site.util
  (:require [clojure.string :as str]
            [clj-ssh.ssh :as ssh]
            [cheshire.core :as chesh]
            [org.httpkit.client :as http]
            [environ.core :as environ]))

(defn string-in-coll-case-ignore?
  [string coll]
  (let [upper-string (str/upper-case string)
        upper-coll (map (partial str/upper-case) coll)]
    (boolean (some #{upper-string} upper-coll))))

(defn get-staff-usernames []
  (let [req-url (str "https://forum.arcaneminecraft.com/admin/users/list/staff.json?api_key="
                     (environ/env :forum-api-key))
        resp (http/get req-url)
        ;;convert json->edn and extract usernames
        valid-usernames (map :username (chesh/parse-string (:body @resp) true))]
    (remove (partial = "system") valid-usernames)))

(defn execute-game-command [command]
  (let [agent (ssh/ssh-agent {})
        screen-command (str "screen -S mc -p 0 -X stuff '" command " \\015'")]
    (let [session (ssh/session agent (environ/env :game-server-host)
                               {:strict-host-key-checking :no
                                ;;because apparently environ ignores types...
                                :port (Integer. (environ/env :game-server-port))
                                :username (environ/env :game-server-user)
                                :password (environ/env :game-server-password)})]
      (ssh/with-connection session
        (let [result (ssh/ssh session {:cmd screen-command})])))))

;;from https://stackoverflow.com/questions/10062967/clojures-equivalent-to-pythons-encodehex-and-decodehex
(defn hexify [s]
  (format "%x" (new java.math.BigInteger (.getBytes s))))

(defn hexify2 [s]
  (apply str
    (map #(format "%02x" (int %)) s)))

(defn hexify3
  "Convert byte sequence to hex string"
  [coll]
  (let [hex [\0 \1 \2 \3 \4 \5 \6 \7 \8 \9 \a \b \c \d \e \f]]
      (letfn [(hexify-byte [b]
        (let [v (bit-and b 0xFF)]
          [(hex (bit-shift-right v 4)) (hex (bit-and v 0x0F))]))]
        (apply str (mapcat hexify-byte coll)))))

(defn hexify-str [s]
  (hexify3 (.getBytes s)))
