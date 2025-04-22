(ns bucket-drops-screen-extension.globals
  (:import [com.badlogic.gdx Game Gdx]
           [com.badlogic.gdx.graphics.g2d BitmapFont SpriteBatch]
           [com.badlogic.gdx.utils.viewport FitViewport])
  (:gen-class))

(def sprite-batch (atom nil))
(def viewport (atom nil))
(def font (atom nil))
