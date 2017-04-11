(ns arcane-site.views.copy)

;;Not sure if this is the best way to do this, but anything longer than a couple words shouldn't
;;be with the rest of the view code. This makes it easier to make adjustments in the future.

(def copy-map
  {:welcome-message
   "Arcane Survival is a semi-vanilla Minecraft server centered around 100% legit gameplay. Not a single item has been spawned in, and not a single creation was made with WorldEdit. The server is hosted in Quebec. We are and always will be free to play. Donations from players help keep the server running."

   :ip "game.arcaneminecraft.com"
   :application-about
   "You must be greylisted in order to build on the server. You may roam and chat without being
greylisted."
   :email-info
   "Email is optional. Enter it to receive updates about your application status."

   })

(defn get-copy [key]
  (get copy-map key))
