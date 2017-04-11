(ns arcane-site.views.components
  (:require [hiccup.page :as page]
            [bidi.bidi :as b]
            [arcane-site.views.copy :as copy]
            [arcane-site.routes :as routes]))

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
   [:input#ip.form-control {:type :text :value (copy/copy :ip)}]
   [:span.input-group-btn
    [:button.btn.btn-default {:data-clipboard-target "#ip"}
     [:span.glyphicon.glyphicon-copy]]]])

(defn button-link
  "A link that looks like a button but behaves as a link (goes to a different page when clicked)"
  [content link]
  [:a.btn.btn-primary.btn-lg.col-md-2 {:href (b/path-for routes/routes link)}
   content])

(def application-form
  [:form.form-horizontal
   {:action (b/path-for routes/routes :submit-app)
    :method :post}
   [:div.form-group
    [:label.col-sm-2.control-label {:for "ign"} "Minecraft Username"]
    [:div.col-sm-3
     [:input#ign.form-control {:type :text :name "ign"}]]]
   [:div.form-group
    [:label.col-sm-2.control-label {:for "bio"} "A little about yourself"]
    [:div.col-sm-5
     [:textarea#bio.form-control {:rows 5 :name "bio"}]]]
   [:div.form-group
    [:label.col-sm-2.control-label {:for "age"} "Age"]
    [:div.col-sm-1
     [:input#age.form-control {:type :number :name "age"}]]]
   [:div.form-group
    [:label.col-sm-2.control-label {:for "referral"} "Referral"]
    [:div.col-sm-5
     [:input#referral.form-control {:type :text :name "referral"}]]]
   [:div.form-group
    [:label.col-sm-2.control-label {:for "admin-ign"} "Any admin's username"]
    [:div.col-sm-3
     [:input#admin-ign.form-control {:type :text :name "admin-ign"}]]]
   [:div.form-group
    [:label.col-sm-2.control-label {:for "email"} "Email"]
    [:div.col-sm-3
     [:input#email.form-control {:type :email :name "email" :aria-describedby "emailInfo"}]]]
   [:span#emailInfo.help-block.col-sm-offset-2 (copy/get-copy :email-info)]
   [:div.form-group
    [:div.col-sm-offset-2.col-sm-10
     [:div.checkbox
      [:label [:input {:type :checkbox :value ""}
               "By checking this, you acknowledge that this is an application to build on the server, "
               [:em [:b "not"]] " to become a staff member."]]]]]
   [:input.btn.btn-default.col-sm-offset-2 {:type :submit :value "Submit"}]])
