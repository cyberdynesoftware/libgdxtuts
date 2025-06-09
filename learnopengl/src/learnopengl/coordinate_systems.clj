(ns learnopengl.coordinate-systems
  (:require [learnopengl.triangle :as triangle])
  (:import [org.joml Matrix4f Vector3f]
           [org.lwjgl.opengl GL33]))

(defn ortho
  [mat4 x y]
  (.ortho mat4 (float -2) (float 2) (float 1.5) (float -1.5) (float -1) (float 1)))

(defn view
  [mat4]
  (.translate mat4 (new Vector3f (float 0) (float 0) (float -3))))

(defn perspective
  [mat4]
  (.perspective mat4 (org.joml.Math/toRadians (float 75)) (float (/ 4 3)) (float 0.1) (float 100)))

(defn rotate-model
  [mat4]
  (.rotate mat4 (org.joml.Math/toRadians (float -55)) (new Vector3f (float 1) (float 0) (float 0))))

(defn rotate-model-2
  [mat4 delta]
  (.rotate mat4 (float delta) (new Vector3f (float 0.5) (float 1) (float 0))))


(def vertices [
    -0.5, -0.5, -0.5,  0.0, 0.0,
     0.5, -0.5, -0.5,  1.0, 0.0,
     0.5,  0.5, -0.5,  1.0, 1.0,
     0.5,  0.5, -0.5,  1.0, 1.0,
    -0.5,  0.5, -0.5,  0.0, 1.0,
    -0.5, -0.5, -0.5,  0.0, 0.0,

    -0.5, -0.5,  0.5,  0.0, 0.0,
     0.5, -0.5,  0.5,  1.0, 0.0,
     0.5,  0.5,  0.5,  1.0, 1.0,
     0.5,  0.5,  0.5,  1.0, 1.0,
    -0.5,  0.5,  0.5,  0.0, 1.0,
    -0.5, -0.5,  0.5,  0.0, 0.0,

    -0.5,  0.5,  0.5,  1.0, 0.0,
    -0.5,  0.5, -0.5,  1.0, 1.0,
    -0.5, -0.5, -0.5,  0.0, 1.0,
    -0.5, -0.5, -0.5,  0.0, 1.0,
    -0.5, -0.5,  0.5,  0.0, 0.0,
    -0.5,  0.5,  0.5,  1.0, 0.0,

     0.5,  0.5,  0.5,  1.0, 0.0,
     0.5,  0.5, -0.5,  1.0, 1.0,
     0.5, -0.5, -0.5,  0.0, 1.0,
     0.5, -0.5, -0.5,  0.0, 1.0,
     0.5, -0.5,  0.5,  0.0, 0.0,
     0.5,  0.5,  0.5,  1.0, 0.0,

    -0.5, -0.5, -0.5,  0.0, 1.0,
     0.5, -0.5, -0.5,  1.0, 1.0,
     0.5, -0.5,  0.5,  1.0, 0.0,
     0.5, -0.5,  0.5,  1.0, 0.0,
    -0.5, -0.5,  0.5,  0.0, 0.0,
    -0.5, -0.5, -0.5,  0.0, 1.0,

    -0.5,  0.5, -0.5,  0.0, 1.0,
     0.5,  0.5, -0.5,  1.0, 1.0,
     0.5,  0.5,  0.5,  1.0, 0.0,
     0.5,  0.5,  0.5,  1.0, 0.0,
    -0.5,  0.5,  0.5,  0.0, 0.0,
    -0.5,  0.5, -0.5,  0.0, 1.0])

(defn create-vertex-array
  [vertices]
  (let [vbo (GL33/glGenBuffers)
        vao (GL33/glGenVertexArrays)]
    (GL33/glBindVertexArray vao)

    (GL33/glBindBuffer GL33/GL_ARRAY_BUFFER vbo)
    (GL33/glBufferData GL33/GL_ARRAY_BUFFER (triangle/create-float-buffer vertices) GL33/GL_STATIC_DRAW)

    (GL33/glVertexAttribPointer 0 3 GL33/GL_FLOAT false 20 0)
    (GL33/glEnableVertexAttribArray 0)

    (GL33/glVertexAttribPointer 1 2 GL33/GL_FLOAT false 20 12)
    (GL33/glEnableVertexAttribArray 1)
    vao))

(def cube-positions [(new Vector3f (float 0) (float 0) (float 0))
                     (new Vector3f (float 2) (float 5) (float -15))
                     (new Vector3f (float -1.5) (float -2.2) (float -2.5))
                     (new Vector3f (float -3.8) (float -2) (float -12.3))
                     (new Vector3f (float 2.4) (float -0.4) (float -3.5))
                     (new Vector3f (float -1.7) (float 3) (float -7.5))
                     (new Vector3f (float 1.3) (float -2) (float -2.5))
                     (new Vector3f (float 1.5) (float 2) (float -2.5))
                     (new Vector3f (float 1.5) (float 0.2) (float -1.5))
                     (new Vector3f (float -1.3) (float 1) (float -1.5))])
