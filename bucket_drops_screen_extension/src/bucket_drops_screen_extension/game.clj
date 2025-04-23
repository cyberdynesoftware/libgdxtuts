(ns bucket-drops-screen-extension.game
  (:require [bucket-drops-screen-extension.main-menu :as menu]
            [bucket-drops-screen-extension.globals :as globals])
  (:import [com.badlogic.gdx Game Gdx]
           [com.badlogic.gdx.graphics.g2d BitmapFont SpriteBatch]
           [com.badlogic.gdx.utils.viewport FitViewport])
  (:gen-class))

(def mygame (proxy [Game] []
              (create []
                (reset! globals/sprite-batch (new SpriteBatch))
                (let [viewport (new FitViewport 8 5)]
                  (reset! globals/viewport viewport)
                  (reset! globals/font (doto (new BitmapFont)
                                         (.setUseIntegerPositions false)
                                         (.. (getData) (setScale (float (/ (.getWorldHeight viewport)
                                                                           (.getHeight Gdx/graphics))))))))
                (.setScreen this (menu/screen)))
              (dispose []
                (.dispose @globals/sprite-batch)
                (.dispose @globals/font))))
