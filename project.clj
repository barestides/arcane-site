(defproject arcane-site "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [http-kit "2.2.0"]
                 [bidi "2.0.16"]
                 [ring-middleware-format "0.7.2"]
                 [hiccup "1.0.5"]]

  :main ^:skip-aot arcane-site.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
