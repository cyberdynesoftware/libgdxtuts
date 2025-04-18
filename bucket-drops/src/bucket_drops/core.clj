(ns bucket-drops.core
  (:import [com.badlogic.gdx.backends.lwjgl3 Lwjgl3Application Lwjgl3ApplicationConfiguration]
           [com.badlogic.gdx ApplicationAdapter Gdx Files Audio Input$Keys]
           [com.badlogic.gdx.graphics Texture Color]
           [com.badlogic.gdx.graphics.g2d SpriteBatch Sprite]
           [com.badlogic.gdx.utils.viewport Viewport FitViewport]
           [com.badlogic.gdx.utils ScreenUtils Array]
           [com.badlogic.gdx.math Vector2 MathUtils]
           [com.badlogic.gdx.assets AssetManager]
           [com.badlogic.gdx.audio Sound Music])
  (:gen-class))

(def config (doto (new Lwjgl3ApplicationConfiguration)
              (.setTitle "Drop")
              (.setWindowedMode 800 500)
              (.setWindowIcon (into-array ["resources/bucket.png"]))))

(def resources (atom nil))

(def drop-timer (atom (float 0)))

(def drop-sprites (atom []))

(def assets (new AssetManager))

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

(defn create-droplet [texture]
  (doto (new Sprite texture)
    (.setSize (float 1) (float 1))
    (.setX (MathUtils/random (float 0) (float (- (.getWorldWidth (:viewport @resources)) 1))))
    (.setY (.getWorldHeight (:viewport @resources)))))

(defn logic []
  (let [bucket-sprite (:bucket-sprite @resources)
        viewport (:viewport @resources)
        delta (.getDeltaTime Gdx/graphics)]
    (.setX bucket-sprite (MathUtils/clamp (.getX bucket-sprite)
                                          (float 0)
                                          (float (- (.getWorldWidth viewport) (.getWidth bucket-sprite)))))
    (doseq [it @drop-sprites]
      (.translateY it (float (* -2 delta)))
      (when (.overlaps (.getBoundingRectangle it) (.getBoundingRectangle bucket-sprite))
        (swap! drop-sprites #(remove #{it} %))
        (.play (:sound @resources))))
    (when (> (swap! drop-timer #(float (+ % delta))) 1)
      (reset! drop-timer (float 0))
      (swap! drop-sprites #(conj % (create-droplet (:drop @resources))))))
  (let [visible-drops (filter #(> (.getY %) (* (.getHeight %) -1)) @drop-sprites)]
    (when (< (count visible-drops) (count @drop-sprites))
      (reset! drop-sprites visible-drops))))

(defn draw []
  (let [sprite-batch (:sprite-batch @resources)
        viewport (:viewport @resources)]
    (ScreenUtils/clear Color/BLACK)
    (.apply viewport)
    (.setProjectionMatrix sprite-batch (.. viewport getCamera combined))
    (.begin sprite-batch)
    (.draw sprite-batch (:background @resources) (float 0) (float 0) (.getWorldWidth viewport) (.getWorldHeight viewport))
    (.draw (:bucket-sprite @resources) sprite-batch)
    (doseq [it @drop-sprites]
      (.draw it sprite-batch))
    (.end sprite-batch)))

(def mygame (proxy [ApplicationAdapter] []
              (create []
                (doto assets
                  (.load "resources/bucket.png" Texture)
                  (.load "resources/background.png" Texture)
                  (.load "resources/drop.png" Texture)
                  (.load "resources/drop.mp3" Sound)
                  (.load "resources/music.mp3" Music)
                  (.finishLoading))
                (let [bucket-sprite (new Sprite (.get assets "resources/bucket.png" Texture))]
                  (.setSize bucket-sprite 1 1)
                  (reset! resources
                          {:bucket-sprite bucket-sprite
                           :background (.get assets "resources/background.png" Texture)
                           :drop (.get assets "resources/drop.png" Texture)
                           :sound (.get assets "resources/drop.mp3" Sound)
                           :sprite-batch (new SpriteBatch)
                           :viewport (new FitViewport 8 5)}))
                (doto (.get assets "resources/music.mp3" Music)
                  (.setLooping true)
                  (.setVolume (float 0.5))
                  (.play)))
             (resize [width height]
               (Viewport/.update (:viewport @resources) width height true))
             (render []
               (.update assets)
               (input)
               (logic)
               (draw))))

(defn -main
  "A simple game tutorial for libgdx"
  [& args]
  (new Lwjgl3Application mygame config)
  (println "Bye~"))
