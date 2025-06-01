(ns learnopengl.coordinate-systems
  (:import [org.joml Matrix4f Vector3f]))

(defn ortho-proj-mat
  [mat4 x y]
  (.ortho mat4 (float 0) (float x) (float 0) (float y) (float 0.1) (float 100)))

(defn view
  [mat4]
  (.translate mat4 (new Vector3f (float 0) (float 0) (float -3))))

(defn perspective
  [mat4]
  (.perspective mat4 (org.joml.Math/toRadians (float 45)) (float (/ 4 3)) (float 0.1) (float 100)))

(defn rotate-model
  [mat4]
  (.rotate mat4 (org.joml.Math/toRadians (float -55)) (new Vector3f (float 1) (float 0) (float 0))))
