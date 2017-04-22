(ns arcane-site.routes
  (:require [bidi.ring :as ring]))


(def routes ["/" [;;Pages
                  ["" :index]
                  ["apply" :apply]
                  ["success" :app-success]
                  ["donate" :donate]
                  ["forum" :forum]
                  ["tools" :tools]
                  ["dynmap" :dynmap]
                  ["rules" :rules]

                  ;;Actually does things
                  ["submit-app" {:post :submit-app}]


                  ]])
