(ns bucket-drops.core
  (:import [com.badlogic.gdx.backends.lwjgl3 Lwjgl3Application Lwjgl3ApplicationConfiguration]
           [com.badlogic.gdx ApplicationAdapter Gdx Files Audio Input$Keys]
           [com.badlogic.gdx.graphics Texture Color]
           [com.badlogic.gdx.graphics.g2d SpriteBatch Sprite]
           [com.badlogic.gdx.utils.viewport Viewport FitViewport]
           [com.badlogic.gdx.utils ScreenUtils]
           [com.badlogic.gdx.math Vector2])
  (:gen-class))

(def config (doto (new Lwjgl3ApplicationConfiguration)
              (.setTitle "Drop")
              (.setWindowedMode 800 500)
              (.setWindowIcon (into-array ["resources/bucket.png"]))))

(def resources (atom nil))

(defn input []
  (let [speed (float 0.25)
        delta (.getDeltaTime Gdx/graphics)]
    (when (.isKeyPressed Gdx/input Input$Keys/RIGHT)
      (.translateX (:bucket-sprite @resources) (* speed delta)))
    (when (.isKeyPressed Gdx/input Input$Keys/LEFT)
      (.translateX (:bucket-sprite @resources) (* speed delta -1))))
  (let [touch-pos (new Vector2)]
    (when (.isTouched Gdx/input)
      (.set touch-pos (.getX Gdx/input) (.getY Gdx/input))
      (.unproject (:viewport @resources) touch-pos)
      (.setCenterX (:bucket-sprite @resources) (.x touch-pos)))))

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
    (.draw (:bucket-sprite @resources) sprite-batch)
    (.end sprite-batch)))

(def mygame (proxy [ApplicationAdapter] []
              (create []
                (let [bucket-texture (new Texture "resources/bucket.png")
                      bucket-sprite (new Sprite bucket-texture)]
                  (.setSize bucket-sprite 1 1)
                  (reset! resources
                          {:bucket-sprite bucket-sprite
                           :background (new Texture "resources/background.png")
                           :drop (new Texture "resources/drop.png")
                           :sound (.newSound Gdx/audio (.internal Gdx/files "resources/drop.mp3"))
                           :music (.newMusic Gdx/audio (.internal Gdx/files "resources/music.mp3"))
                           :sprite-batch (new SpriteBatch)
                           :viewport (new FitViewport 8 5)
                           :touch-pos (new Vector2)})))
             (resize [width height]
               (Viewport/.update (:viewport @resources) width height true))
             (render []
               (input)
               (logic)
               (draw))))

(defn -main
  "A simple game tutorial for libgdx"
  [& args]
  (new Lwjgl3Application mygame config)
  (println "Bye~"))
