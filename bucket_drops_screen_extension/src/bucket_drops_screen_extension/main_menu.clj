(ns bucket-drops-screen-extension.main-menu
  (:require [bucket-drops-screen-extension.globals :as globals])
  (:import [com.badlogic.gdx Gdx ScreenAdapter]
           [com.badlogic.gdx.graphics Color]
           [com.badlogic.gdx.utils ScreenUtils])
  (:gen-class))

(defn screen
  []
  (proxy [ScreenAdapter] []
    (render [delta]
      (ScreenUtils/clear Color/BLACK)
      (.apply (deref globals/viewport))
      (let [matrix (.. @globals/viewport getCamera combined)]
        (.setProjectionMatrix @globals/sprite-batch matrix))
      (.begin @globals/sprite-batch)
      (.draw @globals/font @globals/sprite-batch "test" (float 1) (float 1))
      (.end @globals/sprite-batch))
    (resize [x y]
      (.update @globals/viewport x y true))))
