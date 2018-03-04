(ns arcane-site.util
  (:require [clojure.string :as str]
            [clojure.walk :as walk]
            [clojure.pprint :as pprint]
            [clj-ssh.ssh :as ssh]
            [cheshire.core :as chesh]
            [org.httpkit.client :as http]
            [ring.util.codec :as ring-codec]
            [environ.core :as environ]))
(defn spy
  [x & [f]]
  (pprint/pprint (if f (f x) x))
  x)

(defn string-in-coll-case-ignore?
  [string coll]
  (let [upper-string (str/upper-case string)
        upper-coll (map (partial str/upper-case) coll)]
    (boolean (some #{upper-string} upper-coll))))

;;good req url
"https://forum.arcaneminecraft.com/admin/users/list/staff.json?api_key=15345155b26561fd9d51e9454b757e988c1e116df405f53035a498c52693a26b&api_username=jugglingman"
;; "https://forum.arcaneminecraft.com/admin/users/list/staff.json?api_key=15345155b26561fd9d5e19454b757e988c1e116df405f53035a498c52693a26b&api_username=jugglingman"
;;    "https://forum.arcaneminecraft.com/admin/users/list/staff.json?api_key=15345155b26561fd9d5e9454b757e988c1e116df405f53035a498c52693a26b&api_username=jugglingman"
(defn get-staff-usernames []
  (let [req-url (str "https://forum.arcaneminecraft.com/admin/users/list/staff.json?api_key="
                     (environ/env :forum-api-key)
                     "&api_username="
                     (environ/env :forum-api-username))
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

(defn remove-newlines [s]
  (apply str (filter (fn [c] (not= c \newline)) s)))

;;https://stackoverflow.com/questions/6591604/how-to-parse-url-parameters-in-clojure
(defn parse-query-string
  "Parse parameters from a string into a map."
  [param-string]
  (walk/keywordize-keys (ring-codec/form-decode param-string)))
