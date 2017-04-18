(ns arcane-site.views.copy)

;;Not sure if this is the best way to do this, but anything longer than a couple words shouldn't
;;be with the rest of the view code. This makes it easier to make adjustments in the future.

;;There should only be typograhical (bold, emphasis, etc) html tags in here, no divs / paragraphs /
;;headings.

(def copy-map
  {:welcome-message
   "Arcane Survival is a semi-vanilla Minecraft server centered around 100% legit gameplay. Not a single item has been spawned in, and not a single creation was made with WorldEdit. The server is hosted in Quebec. We are and always will be free to play. Donations from players help keep the server running."
   :ip "game.arcaneminecraft.com"
   :application-about
   "You must be greylisted in order to build on the server. You may roam and chat without being
greylisted."
   :email-check
   "Send me updates on my application!"
   :application-checkbox
   (list "By checking this, you acknowledge that this is an application to build on the server, "
         [:em [:b "not"]] " to become a staff member.")})

(defn get-copy [key]
  (get copy-map key))
