(ns bucket-drops.core
  (:import [com.badlogic.gdx.backends.lwjgl3 Lwjgl3Application Lwjgl3ApplicationConfiguration]
           [com.badlogic.gdx ApplicationAdapter Gdx Files Audio]
           [com.badlogic.gdx.graphics Texture Color]
           [com.badlogic.gdx.graphics.g2d SpriteBatch]
           [com.badlogic.gdx.utils.viewport Viewport FitViewport]
           [com.badlogic.gdx.utils ScreenUtils])
  (:gen-class))

(def config (doto (new Lwjgl3ApplicationConfiguration)
              (.setTitle "Drop")
              (.setWindowedMode 800 500)
              (.setWindowIcon (into-array ["resources/bucket.png"]))))

(def resources (atom nil))

(defn input []
  )

(defn logic []
  )

(defn draw []
  (ScreenUtils/clear Color/BLACK)
  (.apply (:viewport @resources))
  (.setProjectionMatrix (:sprite-batch @resources) (.combined (.getCamera (:viewport @resources))))
  (println (type (:bucket @resources)))
  (.begin (:sprite-batch @resources))
  (.draw (:sprite-batch @resources) (:bucket @resources) 0.0 0.0 1.0 1.0)
  (.end (:sprite-batch @resources)))

(def mygame (proxy [ApplicationAdapter] []
             (create [] (reset! resources
                                {:bucket (new Texture "resources/bucket.png")
                                 :background (new Texture "resources/background.png")
                                 :drop (new Texture "resources/drop.png")
                                 :sound (.newSound Gdx/audio (.internal Gdx/files "resources/drop.mp3"))
                                 :music (.newMusic Gdx/audio (.internal Gdx/files "resources/music.mp3"))
                                 :sprite-batch (new SpriteBatch)
                                 :viewport (new FitViewport 8 5)}))
             (resize [width height] (Viewport/.update (:viewport @resources) width height true))
             (render [] (input) (logic) (draw))))

(defn -main
  "A simple game tutorial for libgdx"
  [& args]
  (new Lwjgl3Application mygame config)
  (println "Bye~"))
