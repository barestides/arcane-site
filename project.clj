(defproject arcane-site "0.1.0-SNAPSHOT"
  :description "Website for Arcane Survival minecraft server"
  :resource-paths ["resources/"]
  :url "arcaneminecraft.com"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [http-kit "2.2.0"]
                 [bidi "2.0.16"]
                 [ring-middleware-format "0.7.2"]
                 [hiccup "1.0.5"]
                 [bouncer "1.0.1"]
                 [cheshire "5.7.0"]

                 [environ "1.1.0"]
                 [mysql/mysql-connector-java "5.1.6"]
                 [org.clojure/java.jdbc "0.7.0-alpha3"]
                 ;;Need to decide if we want to / need to use honeysql or not
                 [honeysql "0.8.2"]]
  :plugins [[lein-ring "0.11.0"]
            [lein-environ "1.1.0"]]
  :ring {:handler arcane-site.server/app}

  :main ^:skip-aot arcane-site.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
