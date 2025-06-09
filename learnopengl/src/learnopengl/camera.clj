(ns learnopengl.camera
  (:import [org.joml Matrix4f Vector3f]))

(def radius 10)

(def view (new Matrix4f))

(def position (new Vector3f))

(def target (new Vector3f))

(def up (new Vector3f (float 0) (float 1) (float 0)))

(defn camera
  [delta]
  (let [cam-x (float (* (Math/sin delta) radius))
        cam-z (float (* (Math/cos delta) radius))]
    (.setLookAt view
                (.set position cam-x (float 0) cam-z)
                target
                up)))
