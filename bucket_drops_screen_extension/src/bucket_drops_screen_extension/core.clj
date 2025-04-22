(ns bucket-drops-screen-extension.core
  (:require [bucket-drops-screen-extension.game :as game])
  (:import [com.badlogic.gdx.backends.lwjgl3 Lwjgl3Application Lwjgl3ApplicationConfiguration])
  (:gen-class))

(def config (doto (new Lwjgl3ApplicationConfiguration)
              (.setTitle "Drop")
              (.setWindowedMode 800 500)))

(defn -main
  "An extended game tutorial for libgdx"
  [& args]
  (new Lwjgl3Application game/mygame config)
  (println "Bye~"))
