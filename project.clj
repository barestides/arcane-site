(defproject arcane-site "0.1.0"
  :description "Website for Arcane Survival minecraft server"
  :resource-paths ["resources/"]
  :url "arcaneminecraft.com"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [http-kit "2.3.0"]
                 [bidi "2.0.16"]
                 [ring-middleware-format "0.7.2"]
                 [ring/ring-json "0.4.0"]
                 [hiccup "1.0.5"]
                 [bouncer "1.0.1"]
                 [cheshire "5.7.0"]
                 [com.draines/postal "2.0.2"]
                 [clj-ssh "0.5.14"]
                 [environ "1.1.0"]
                 [clj-http "3.7.0"]
                 [pandect "0.6.1"]
                 [crypto-random "1.2.0"]
                 [mysql/mysql-connector-java "5.1.6"]
                 [base64-clj "0.1.1"]
                 [org.clojure/java.jdbc "0.7.0-alpha3"]
                 [com.taoensso/timbre "4.10.0"]]

  :plugins [[lein-ring "0.11.0"]
            [lein-environ "1.1.0"]]
  :ring {:handler arcane-site.server/app}

  :main ^:skip-aot arcane-site.core
  :target-path "target/%s"
  :repl-options {:init-ns arcane-site.server}
  :profiles {:uberjar {:aot :all}})
