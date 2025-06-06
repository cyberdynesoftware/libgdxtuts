(ns learnopengl.transformations
  (:import [org.joml Vector3f Matrix4f]
           [org.lwjgl.system MemoryStack]))

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
  (.get mat4 (.mallocFloat (MemoryStack/stackPush) 16)))

(defn translate-n-rotate
  [mat4 angle]
  (.translation mat4 (new Vector3f (float 0.5) (float -0.5) (float 0)))
  (.rotate mat4 (float angle) (new Vector3f (float 0) (float 0) (float 1)))
  (.get mat4 (.mallocFloat (MemoryStack/stackPush) 16)))

(defn translate-n-scale
  [mat4 angle]
  (.translation mat4 (new Vector3f (float -0.5) (float 0.5) (float 0)))
  (let [scale (Math/sin angle)]
    (.scale mat4 (new Vector3f scale scale (float 1))))
  (.get mat4 (.mallocFloat (MemoryStack/stackPush) 16)))
