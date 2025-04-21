(ns bucket-drops-screen-extension.game
  (:import [com.badlogic.gdx Game Gdx]
           [com.badlogic.gdx.graphics.g2d BitmapFont SpriteBatch]
           [com.badlogic.gdx.utils.viewport FitViewport])
  (:gen-class))

(def res (atom nil))

(def mygame (proxy [Game] []
              (create []
                (let [viewport (new FitViewport 8 5)]
                  (reset! res 
                          {:sprite-batch (new SpriteBatch)
                           :viewport viewport
                           :font (doto (new BitmapFont)
                                   (.setUseIntegerPositions false)
                                   (.. (getData) (setScale (float (/ (.getWorldHeight viewport)
                                                                (.getHeight Gdx/graphics))))))})))
              (dispose []
                (.dispose (:sprite-batch @res))
                (.dispose (:font @res)))))
