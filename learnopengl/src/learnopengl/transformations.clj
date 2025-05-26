(ns learnopengl.transformations
  (:import [org.joml Vector3f Matrix4f]))

(defn first-steps
  []
  (println "hallo")
  (let [vec3 (new Vector3f (float 1) (float 0) (float 0))
        mat4 (new Matrix4f)]
    (.translate mat4 (float 1) (float 1) (float 0))
    (let [t (.transformPosition mat4 vec3)]
      (println (.x t) (.y t) (.z t)))))
