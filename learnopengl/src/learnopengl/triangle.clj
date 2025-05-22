(ns learnopengl.triangle
  (:import [org.lwjgl.opengl GL33]
           [org.lwjgl BufferUtils]))

(def triangle-vertices [
  -0.5 -0.5 0
  0.5 -0.5 0
  0 0.5 0])

(def triangle-vertices-with-color 
  ; vertices   color
  [-0.5 -0.5 0 1 0 0
   0.5 -0.5 0  0 1 0
   0 0.5 0     0 0 1])

(def rectangle-vertices 
  [0.5 0.5 0
   0.5 -0.5 0
   -0.5 -0.5 0
   -0.5 0.5 0])

(def indices
  [0 1 3
   1 2 3])

(def first-triangle-vertices
  [-0.9 -0.5 0
   0 -0.5 0
   -0.45 0.5 0])

(def second-triangle-vertices
  [0 -0.5 0
   0.9 -0.5 0
   0.45 0.5 0])

(defn create-int-buffer
  [indices]
  (doto (BufferUtils/createIntBuffer (count indices))
    (.put (int-array indices))
    (.flip)))

(defn create-float-buffer
  [vertices]
  (doto (BufferUtils/createFloatBuffer (count vertices))
    (.put (float-array vertices))
    (.flip)))

(defn create-vertex-array
  [vertices]
  (let [vbo (GL33/glGenBuffers)
        vao (GL33/glGenVertexArrays)
        ebo (GL33/glGenBuffers)]
    (GL33/glBindVertexArray vao)

    (GL33/glBindBuffer GL33/GL_ARRAY_BUFFER vbo)
    (GL33/glBufferData GL33/GL_ARRAY_BUFFER (create-float-buffer vertices) GL33/GL_STATIC_DRAW)

    ;(GL33/glBindBuffer GL33/GL_ELEMENT_ARRAY_BUFFER ebo)
    ;(GL33/glBufferData GL33/GL_ELEMENT_ARRAY_BUFFER (create-int-buffer indices) GL33/GL_STATIC_DRAW)

    (GL33/glVertexAttribPointer 0 3 GL33/GL_FLOAT false 24 0)
    (GL33/glEnableVertexAttribArray 0)

    (GL33/glVertexAttribPointer 1 3 GL33/GL_FLOAT false 24 12)
    (GL33/glEnableVertexAttribArray 1)
    vao))
