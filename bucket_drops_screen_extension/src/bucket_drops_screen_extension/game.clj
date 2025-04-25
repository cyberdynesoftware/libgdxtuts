(ns bucket-drops-screen-extension.game
  (:require [bucket-drops-screen-extension.main-menu :as menu]
            [bucket-drops-screen-extension.globals :as globals]
            [bucket-drops-screen-extension.game-screen :as game-screen])
  (:import [com.badlogic.gdx Game Gdx]
           [com.badlogic.gdx.graphics.g2d BitmapFont SpriteBatch]
           [com.badlogic.gdx.utils.viewport FitViewport])
  (:gen-class))

(def game-started (atom false))

(def mygame (proxy [Game] []
              (create []
                (reset! globals/sprite-batch (new SpriteBatch))
                (let [viewport (new FitViewport 8 5)]
                  (reset! globals/viewport viewport)
                  (reset! globals/font (doto (new BitmapFont)
                                         (.setUseIntegerPositions false)
                                         (.. (getData) (setScale (float (/ (.getWorldHeight viewport)
                                                                           (.getHeight Gdx/graphics))))))))
                (globals/load-assets)
                (.setScreen this (menu/screen)))
              (render []
                (when (and (not @game-started)
                           @menu/start-game)
                  (reset! game-started true)
                  (.setScreen this (game-screen/mygame))
                  (println "Start game"))
                (proxy-super render))
              (dispose []
                (.dispose @globals/sprite-batch)
                (.dispose @globals/font))))
