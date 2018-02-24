(ns arcane-site.routes
  (:require [bidi.ring :as ring]))


(def routes ["/" [;;Pages
                  ["" :index]
                  ["apply" :apply]
                  ["review" :review]
                  ["success" :app-success]
                  ["donate" :donate]
                  ["forum" :forum]
                  ["tools" :tools]
                  ["rules" :rules]

                  ;;Actually does things
                  ["submit-app" {:post :submit-app}]
                  ["review-app" {:post :review-app}]]])
