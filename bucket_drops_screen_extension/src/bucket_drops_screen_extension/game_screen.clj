(ns bucket-drops-screen-extension.game-screen
  (:require [bucket-drops-screen-extension.globals :as globals])
  (:import [com.badlogic.gdx ScreenAdapter Gdx Files Audio Input$Keys Input$Buttons]
           [com.badlogic.gdx.graphics Texture Color]
           [com.badlogic.gdx.graphics.g2d SpriteBatch Sprite]
           [com.badlogic.gdx.utils.viewport Viewport FitViewport]
           [com.badlogic.gdx.utils ScreenUtils Array]
           [com.badlogic.gdx.math Vector2 MathUtils]
           [com.badlogic.gdx.audio Sound Music])
  (:gen-class))


(def drop-timer (atom (float 0)))
(def drop-sprites (atom []))
(def bucket-sprite (atom nil))

(defn move-bucket
  [direction]
  (let [speed (float 0.25)
        delta (.getDeltaTime Gdx/graphics)]
    (.translateX @bucket-sprite (* speed delta direction))))

(defn input []
  (when (.isKeyPressed Gdx/input Input$Keys/RIGHT)
    (move-bucket 1))
  (when (.isKeyPressed Gdx/input Input$Keys/LEFT)
    (move-bucket -1))
  (when (.isButtonJustPressed Gdx/input Input$Buttons/RIGHT)
    (let [pos (new Vector2 (.getX Gdx/input) (.getY Gdx/input))]
      (.unproject @globals/viewport pos)
      (.setCenterX @bucket-sprite (.x pos)))))

(defn create-droplet []
  (doto (new Sprite (.get globals/assets "resources/drop.png" Texture))
    (.setSize (float 1) (float 1))
    (.setX (MathUtils/random (float 0) (float (- (.getWorldWidth @globals/viewport) 1))))
    (.setY (.getWorldHeight @globals/viewport))))

(defn one-second-passed
  [delta]
  (when (> (swap! drop-timer #(float (+ % delta))) 1)
    (swap! drop-timer #(float (- % 1)))
    true))

(defn update-drops
  [delta]
  (doseq [it @drop-sprites]
    (.translateY it (float (* -2 delta)))
    (when (.overlaps (.getBoundingRectangle it) (.getBoundingRectangle @bucket-sprite))
      (swap! drop-sprites #(remove #{it} %))
      (.play (.get globals/assets "resources/drop.mp3" Sound))))
  (let [visible-drops (filter #(> (.getY %) (* (.getHeight %) -1)) @drop-sprites)]
    (when (< (count visible-drops) (count @drop-sprites))
      (reset! drop-sprites visible-drops))))

(defn logic
  [delta]
  (.setX @bucket-sprite (MathUtils/clamp (.getX @bucket-sprite)
                                        (float 0)
                                        (float (- (.getWorldWidth @globals/viewport)
                                                  (.getWidth @bucket-sprite)))))
  (update-drops delta)
  (when (one-second-passed delta)
    (swap! drop-sprites #(conj % (create-droplet)))))

(defn draw
  []
  (ScreenUtils/clear Color/BLACK)
  (.apply @globals/viewport)
  (.setProjectionMatrix @globals/sprite-batch (.. @globals/viewport getCamera combined))
  (.begin @globals/sprite-batch)
  (.draw @globals/sprite-batch
         (.get globals/assets "resources/background.png" Texture)
         (float 0)
         (float 0)
         (.getWorldWidth @globals/viewport)
         (.getWorldHeight @globals/viewport))
  (.draw @bucket-sprite @globals/sprite-batch)
  (doseq [it @drop-sprites]
    (.draw it @globals/sprite-batch))
  (.end @globals/sprite-batch))

(defn mygame
  []
  (reset! bucket-sprite (doto (new Sprite (.get globals/assets "resources/bucket.png" Texture))
                          (.setSize 1 1)))
  (doto (.get globals/assets "resources/music.mp3" Music)
    (.setLooping true)
    (.setVolume (float 0.5))
    (.play))
  (proxy [ScreenAdapter] []
    (render [delta]
      (input)
      (logic delta)
      (draw))
    (resize [width height]
      (.update @globals/viewport width height true))))
