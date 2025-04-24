(ns bucket-drops-screen-extension.main-menu
  (:require [bucket-drops-screen-extension.globals :as globals])
  (:import [com.badlogic.gdx Gdx ScreenAdapter Input$Buttons]
           [com.badlogic.gdx.graphics Color]
           [com.badlogic.gdx.utils ScreenUtils])
  (:gen-class))

(def start-game (atom false))

(defn screen
  []
  (proxy [ScreenAdapter] []
    (render [delta]
      (let [assets-ready (.update globals/assets)]
        (ScreenUtils/clear Color/BLACK)
        (.apply (deref globals/viewport))
        (let [matrix (.. @globals/viewport getCamera combined)]
          (.setProjectionMatrix @globals/sprite-batch matrix))
        (.begin @globals/sprite-batch)
        (.draw @globals/font @globals/sprite-batch "Drop" (float 1) (float 4))
        (when assets-ready
          (.draw @globals/font @globals/sprite-batch "Click to play" (float 1) (float 3)))
        (.end @globals/sprite-batch)
        (when (and assets-ready 
                   (.isButtonJustPressed Gdx/input Input$Buttons/LEFT))
          (reset! start-game true))))
    (resize [x y]
      (.update @globals/viewport x y true))))
