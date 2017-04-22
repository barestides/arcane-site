(ns arcane-site.views.components
  (:require [hiccup.page :as page]
            [bidi.bidi :as b]
            [arcane-site.views.copy :as copy]
            [arcane-site.routes :as routes]))

;;Navbar at top of page
(def nav
  [:nav.navbar.navbar-default
   [:div.container-fluid
    [:div.navbar-header
     [:a.navbar-brand {:href "/"} "Home"]]
    (into [:ul.nav.navbar-nav] (mapv (fn [[title link]] [:li [:a {:href
                                                                  (b/path-for routes/routes link)}
                                                              title]])
                                     [["Forum" :forum]
                                      ["Tools" :tools]
                                      ["Dynmap" :dynmap]
                                      ["Rules" :rules]]))]])

;;Box that includes the server IP in an input (maybe should be regular text) field along with a button
;;to copy the ip to the clipboard using this clipboard lib.
(def ip-box
  [:div#ip-box.input-group.col-sm-3
   (page/include-js "https://cdn.jsdelivr.net/clipboard.js/1.6.0/clipboard.min.js")
   [:script
    " var clipboard = new Clipboard('.btn');
    clipboard.on('success', function(e) {
        console.log(e);
    });
    clipboard.on('error', function(e) {
        console.log(e);
    });"]
   [:input#ip.form-control {:type :text :value (copy/get-copy :ip)}]
   [:span.input-group-btn
    [:button.btn.btn-default {:data-clipboard-target "#ip"}
     [:span.glyphicon.glyphicon-copy]]]])

;;This might not be a useful fn. There's so many classes and variations for a button
(defn button-link
  "A link that looks like a button but behaves as a link (goes to a different page when clicked)"
  [content link]
  [:a.btn.btn-primary.btn-lg.col-md-2 {:href (b/path-for routes/routes link)}
   content])

;;The greylist application form for users to fill out. This looks messy, and we could have some
;;abstractions to clean it up, but I don't think that there will be many more forms, so that's
;;not useful right now.
(def application-form
  [:form.form-horizontal
   {:action (b/path-for routes/routes :submit-app)
    :method :post}
   [:div.form-group
    [:label.col-sm-2.control-label {:for "username"} "Minecraft Username"]
    [:div.col-sm-3
     [:input#username.form-control {:type :text :name "username" :required true} ]]]
   [:div.form-group
    [:label.col-sm-2.control-label {:for "bio"} "A little about yourself"]
    [:div.col-sm-5
     [:textarea#bio.form-control {:rows 5 :name "bio" :required true}]]]
   [:div.form-group
    [:label.col-sm-2.control-label {:for "age"} "Age"]
    [:div.col-sm-1
     [:input#age.form-control {:type :number :name "age" :required true}]]]
   [:div.form-group
    [:label.col-sm-2.control-label {:for "referral"} "Referral"]
    [:div.col-sm-5
     [:input#referral.form-control {:type :text :name "referral" :required true}]]]
   [:div.form-group
    [:label.col-sm-2.control-label {:for "staff-ign"} "Any staff's username"]
    [:div.col-sm-3
     [:input#staff-ign.form-control {:type :text :name "staff-ign" :required true}]]]
   [:div.form-group
    [:div.checkbox.col-sm-offset-2.col-sm-10
     [:label [:input#email-toggle {:type :checkbox :value "" :name "email-toggle"}
              (copy/get-copy :email-check)]]]]
   [:div#email-box.form-group
    {:style "display: none;"}
    [:label.col-sm-2.control-label {:for "email"} "Email"]
    [:div.col-sm-3
     [:input#email.form-control {:type :email :name "email" }]]]
   [:div.form-group
    [:div.col-sm-offset-2.col-sm-10
     [:div.checkbox
      [:label [:input {:type :checkbox :value ""} (copy/get-copy :application-checkbox)]]]]]
   [:input.btn.btn-default.col-sm-offset-2 {:type :submit :value "Submit"}]])
