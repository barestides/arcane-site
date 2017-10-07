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
         [:em [:b "not"]] " to become a staff member.")
   :respect "You must respect everyone. Causing any kind of harm or abuse to another person is not allowed. This includes bullying, harassment, and much more. Racist, harsh, or otherwise rude language towards players is not tolerated. Swearing is allowed within reason."
   :cheating "Client modifications or texture packs that give an unfair advantage (e.g. x-ray) are not allowed. Any kind of exploit that places you at an unfair advantage over other players is strictly against the rules.
However, you are allowed to use OptiFine and Mini-Map mods."
   :stealing "You may not steal from other players or grief their builds. You may not kill anyone without their permission. When you kill someone in a PvP fight, you must give-back the dropped items (unless specified). In other words, you may not loot the items from another player without permission. You can toggle PvP using /pvp. Remember to turn it off when you don't want to fight!"
   :building "You can mark your territory using a sign. You may not build on land clearly claimed by another player. Even if no marked sign is present, you may not build too close to another player without their permission. Ask staff if you are unsure."
   :chat "Do not fill the chat with memes, CAPS-LOCK, or the same message over and over."
   :rules-discretion1 [:em "Many of these rules are entirely dependent on context - server staff will decide what is ok and what is not."]
   :rules-discretion2
   [:em "Warnings will be issued before bans. If any rule is unclear, please ask a mod or admin."]
   :rules-consequences [:em "Players who do not follow these rules will receive temporary or permanent mutes or bans. "]})

(defn get-copy [key]
  (get copy-map key))
