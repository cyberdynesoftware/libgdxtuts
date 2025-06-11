(ns learnopengl.camera
  (:import [org.joml Matrix4f Vector3f]))

(def speed (float 2.5))

(def view (new Matrix4f))

(def position (new Vector3f (float 0) (float 0) (float 3)))

(def front (new Vector3f (float 0) (float 0) (float -1)))

(def up (new Vector3f (float 0) (float 1) (float 0)))

(defn camera
  [delta]
  (.setLookAt view position (.add front position) up)
  (.set front (float 0) (float 0) (float -1))
  view)

(defn WS
  [sign delta]
  (.add position (.mul front (float (* sign delta speed))))
  (.set front (float 0) (float 0) (float -1)))

(defn AD
  [sign delta]
  (.add position (.mul (.normalize (.cross front up)) (float (* sign delta speed))))
  (.set front (float 0) (float 0) (float -1)))
