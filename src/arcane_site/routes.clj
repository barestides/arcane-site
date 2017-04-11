(ns arcane-site.routes
  (:require [bidi.ring :as ring]))


(def routes ["/" [;;Pages
                  ["" :index]
                  ["apply" :apply]
                  ["donate" :donate]
                  ["forum" :forum]
                  ["tools" :tools]
                  ["dynmap" :dynmap]
                  ["rules" :rules]
                  ;;Actually does things
                  ["submit-app" {:post :submit-app}]

                  ]])
