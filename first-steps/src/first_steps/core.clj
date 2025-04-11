(ns first-steps.core
  (:import [com.badlogic.gdx.backends.lwjgl3 Lwjgl3Application Lwjgl3ApplicationConfiguration]
           [com.badlogic.gdx ApplicationAdapter])
  (:gen-class))

(def config (new Lwjgl3ApplicationConfiguration))

(def mygame (proxy [ApplicationAdapter] [] ))

(defn -main
  "Running a libgdx app"
  [& args]
  (new Lwjgl3Application mygame config)
  (println "Bye~"))
