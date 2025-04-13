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
  (let [sprite-batch (:sprite-batch @resources)
        viewport (:viewport @resources)]
    (ScreenUtils/clear Color/BLACK)
    (.apply viewport)
    (.setProjectionMatrix sprite-batch (.. viewport getCamera combined))
    (.begin sprite-batch)
    (.draw sprite-batch (:background @resources) (float 0) (float 0) (.getWorldWidth viewport) (.getWorldHeight viewport))
    (.draw sprite-batch (:bucket @resources) (float 0) (float 0) (float 1) (float 1))
    (.end sprite-batch)))

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
