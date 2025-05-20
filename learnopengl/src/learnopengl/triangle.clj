(ns learnopengl.triangle
  (:import [org.lwjgl.opengl GL33]
           [org.lwjgl BufferUtils]))

(def triangle-vertices [
  -0.5 -0.5 0
  0.5 -0.5 0
  0 0.5 0])

(defn create-float-buffer
  [vertices]
  (doto (BufferUtils/createFloatBuffer (count vertices))
    (.put (float-array vertices))
    (.flip)))

(def vertex-shader-source
  "#version 330 core
  layout (location = 0) in vec3 aPos;

  void main()
  {
  gl_Position = vec4(aPos.x, aPos.y, aPos.z, 1.0);
  }")

(defn create-shader
  [shader-type source]
  (let [shader (GL33/glCreateShader shader-type)]
    (GL33/glShaderSource shader source)
    (GL33/glCompileShader shader)
    (let [success (GL33/glGetShaderi shader GL33/GL_COMPILE_STATUS)]
      (when (= success 0)
        (println "ERROR: shader compilation failed:")
        (println (GL33/glGetShaderInfoLog shader))))
    shader))

(def fragment-shader-source
  "#version 330 core
  out vec4 FragColor;

  void main()
  {
  FragColor = vec4(1.0f, 0.5f, 0.2f, 1.0f);
  }")

(defn create-shader-program
  [& shader]
  (let [program (GL33/glCreateProgram)]
    (dorun (map #(GL33/glAttachShader program %) shader))
    (GL33/glLinkProgram program)
    (let [success (GL33/glGetProgrami program GL33/GL_LINK_STATUS)]
      (when (= success 0)
        (println "ERROR: program compilation failed:")
        (println (GL33/glGetProgramInfoLog program))))
    program))

(defn get-shader-program
  []
  (let [vertex-shader (create-shader GL33/GL_VERTEX_SHADER vertex-shader-source)
        fragment-shader (create-shader GL33/GL_FRAGMENT_SHADER fragment-shader-source)
        shader-program (create-shader-program vertex-shader fragment-shader)]
    (GL33/glDeleteShader vertex-shader)
    (GL33/glDeleteShader fragment-shader)
    shader-program))

(def rectangle-vertices 
  [0.5 0.5 0
   0.5 -0.5 0
   -0.5 -0.5 0
   -0.5 0.5 0])

(def indices
  [0 1 3
   1 2 3])

(def two-triangle-vertices
  [-0.9 -0.5 0
   0 -0.5 0
   -0.45 0.5 0
   0 -0.5 0
   0.9 -0.5 0
   0.45 0.5 0])

(defn create-int-buffer
  [indices]
  (doto (BufferUtils/createIntBuffer (count indices))
    (.put (int-array indices))
    (.flip)))

(defn create-vertex-array
  []
  (let [vbo (GL33/glGenBuffers)
        vao (GL33/glGenVertexArrays)
        ebo (GL33/glGenBuffers)]
    (GL33/glBindVertexArray vao)

    (GL33/glBindBuffer GL33/GL_ARRAY_BUFFER vbo)
    (GL33/glBufferData GL33/GL_ARRAY_BUFFER (create-float-buffer two-triangle-vertices) GL33/GL_STATIC_DRAW)

    ;(GL33/glBindBuffer GL33/GL_ELEMENT_ARRAY_BUFFER ebo)
    ;(GL33/glBufferData GL33/GL_ELEMENT_ARRAY_BUFFER (create-int-buffer indices) GL33/GL_STATIC_DRAW)

    (GL33/glVertexAttribPointer 0 3 GL33/GL_FLOAT false 0 0)
    (GL33/glEnableVertexAttribArray 0)
    vao))
