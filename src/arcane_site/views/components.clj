(ns arcane-site.views.components
  (:require [hiccup.page :as page]
            [bidi.bidi :as b]
            [arcane-site.util :as util]
            [arcane-site.views.copy :as copy]
            [arcane-site.db :as db]
            [arcane-site.routes :as routes]))

;;Navbar at top of page
(def nav
  [:nav.navbar.navbar-default
   [:div.container-fluid
    (into [:ul.nav.navbar-nav]
          (mapv (fn [[title link]] [:li [:a {:href link} title]])
                [["Home" (b/path-for routes/routes :index)]
                 ["Forum" "www.forum.arcaneminecraft.com"]
                 ["Tools" (b/path-for routes/routes :tools)]
                 ["Dynmap" "www.game.arcaneminecraft.com/dynmap"]
                 ["Rules" (b/path-for routes/routes :rules)]]))]])

;;;
;;; Landing
;;;

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

;;;
;;; Application
;;;

;;The greylist application form for users to fill out. This looks messy, and we could have some
;;abstractions to clean it up, but I don't think that there will be many more forms, so that's
;;not useful right now.
(def application-form
  [:form.form-horizontal.app-form
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
     [:input#staff-ign.form-control {:type :text :name "staff-ign" :required true
                                     ;; jinkies temp for testing.. remove
                                     :value "jugglingman"}]]]
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
      [:label [:input {:type :checkbox :value "" :required true}
               (copy/get-copy :application-checkbox)]]]]]
   [:div.row
    [:div#form-errors.hidden.bg-danger.col-sm-offset-2.col-sm-4
     [:p "There are errors with your application:"]
     [:ul]]]
   [:div.row
    [:input.btn.btn-default.col-sm-offset-2 {:type :submit :value "Submit"}]]])

(def app-success
  [:div#app-success.hidden
   [:h1 "Thank you for applying to Arcane Survival!"]
   [:p "Expect a response within 2-3 days at the latest."]])

;;;
;;; Review Page
;;;

(defn application [app-map]
  (let [{:keys [date username age email bio referral id]} app-map]
    [:div.application
     [:div.app-top
      [:div.col-sm-2 [:b username]]
      [:div.col-sm-offset-10 date]
      [:div.col-sm-12.text-muted email]]
     [:div.app-main.col-sm-12
      [:div [:b "Age "] age]
      [:div [:b "Bio "] bio]
      [:div [:b "Referral "] referral]]

     [:form.app-review {:action (b/path-for routes/routes :review-app)
                        :method "post"}
      [:div.form-group
       [:label.control-label {:for "comments"} "Comments for Applicant"]
       [:textarea#comments.form-control {:rows 3 :name "comments"}]]
      ;;we don't really need to send username
      [:input {:type :hidden :value username :name "username"}]
      [:input {:type :hidden :value id :name "id"}]
      [:div.form-group
       [:label.control-label {:for "staff-name"} "Reviewer Username"]
       [:select.form-control {:name "staff-name"}
        (map #(conj [:option %]) (util/get-staff-usernames))]]
      [:input.review-btn.col-lg-2.btn.btn-success {:type :submit :value "Accept" :style
                                               "margin-right: 5px" :name "accept"}]
      [:input.review-btn.col-lg-2.btn.btn-danger {:type :submit :value "Deny" :name "deny"}]]]))

(defn application-list []
  [:div.col-sm-8
   (let [apps (db/get-pending-apps)]
     (if (empty? apps)
       [:h3 "No pending apps"]
       (map application (db/get-pending-apps))))])

;;;
;;; Rules
;;;

(defn rule
  [head description]
  [:li
   [:div.rule
    [:h3 head]
    [:p description]]])

(defn rules-list []
  (let [rule-headings {:respect "Respect"
                       :cheating "Cheating"
                       :stealing "Stealing, Griefing, and PvP"
                       :building "Building"
                       :chat "Chat"}]
    [:div.content
     [:div
      (copy/get-copy :rules-discretion1)
      [:br]
      (copy/get-copy :rules-discretion2)]
     [:ol (map (fn [[rule-key head]]
                      (rule head (copy/get-copy rule-key)))
               rule-headings)]
     (copy/get-copy :rules-consequences)]))
