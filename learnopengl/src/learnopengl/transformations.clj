(ns learnopengl.transformations
  (:import [org.joml Vector3f Matrix4f]
           [org.lwjgl.system MemoryStack]))

(def memory-stack (MemoryStack/stackPush))

(defn first-steps
  []
  (let [vec3 (new Vector3f (float 1) (float 0) (float 0))
        mat4 (new Matrix4f)]
    (.translate mat4 (float 1) (float 1) (float 0))
    (let [t (.transformPosition mat4 vec3)]
      (println (.x t) (.y t) (.z t)))))

(defn scale-n-rotate
  [mat4]
  (.rotation mat4 (org.joml.Math/toRadians (float 90)) (new Vector3f (float 0) (float 0) (float 1)))
  (.scale mat4 (new Vector3f (float 0.5) (float 0.5) (float 0.5)))
  (.get mat4 (.mallocFloat memory-stack 16)))

(defn translate-n-rotate
  [mat4 angle]
  (.rotation mat4 (float angle) (new Vector3f (float 0) (float 0) (float 1)))
  (.translate mat4 (new Vector3f (float 0.5) (float -0.5) (float 0)))
  (.get mat4 (.mallocFloat memory-stack 16)))
