(ns bucket-drops-screen-extension.globals
  (:import [com.badlogic.gdx Game Gdx]
           [com.badlogic.gdx.graphics.g2d BitmapFont SpriteBatch]
           [com.badlogic.gdx.assets AssetManager]
           [com.badlogic.gdx.graphics Texture]
           [com.badlogic.gdx.audio Sound Music]
           [com.badlogic.gdx.utils.viewport FitViewport])
  (:gen-class))

(def sprite-batch (atom nil))
(def viewport (atom nil))
(def font (atom nil))

(def assets (new AssetManager))

(defn load-assets
  []
  (doto assets
    (.load "resources/bucket.png" Texture)
    (.load "resources/background.png" Texture)
    (.load "resources/drop.png" Texture)
    (.load "resources/drop.mp3" Sound)
    (.load "resources/music.mp3" Music)))
