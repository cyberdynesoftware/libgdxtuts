(ns first-steps.core
  (:import [com.badlogic.gdx.backends.lwjgl3 Lwjgl3Application Lwjgl3ApplicationConfiguration]
           [com.badlogic.gdx.graphics.g2d BitmapFont SpriteBatch]
           [com.badlogic.gdx ApplicationAdapter])
  (:gen-class))

(def msg "Hello Word")
(def font (atom nil))
(def sprite-batch (atom nil))

(def config (doto (new Lwjgl3ApplicationConfiguration)
              (.setTitle msg)
              (.setWindowedMode 400 300)))

(def mygame (proxy [ApplicationAdapter] []
              (create []
                (reset! font (new BitmapFont))
                (reset! sprite-batch (new SpriteBatch)))
              (render []
                (.begin @sprite-batch)
                (.draw @font @sprite-batch msg (float 100) (float 100))
                (.end @sprite-batch))))

(defn -main
  "A minimal libgdx app"
  [& args]
  (new Lwjgl3Application mygame config)
  (println "Bye~"))
