(ns learnopengl.triangle
  (:import [org.lwjgl.opengl GL33]
           [org.lwjgl BufferUtils]))

(def vertices [
  -0.5 -0.5 0
  0.5 -0.5 0
  0 0.5 0])

;(def vertex-buffer (doto (BufferUtils/createFloatBuffer (count vertices))
;                     (dorun (map #(.put (float %)) vertices))))

(defn create-vbo
  []
  (let [vbo (GL33/glGenBuffers)]
    (GL33/glBindBuffer GL33/GL_ARRAY_BUFFER vbo)
    (GL33/glBufferData GL33/GL_ARRAY_BUFFER vertex-buffer GL33/GL_STATIC_DRAW)
    vbo))

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

(defn activate-shader-program
  []
  (let [vertex-shader (create-shader GL33/GL_VERTEX_SHADER vertex-shader-source)
        fragment-shader (create-shader GL33/GL_FRAGMENT_SHADER fragment-shader-source)
        shader-program (create-shader-program vertex-shader fragment-shader)]
    (GL33/glUseProgram shader-program)
    (GL33/glDeleteShader vertex-shader)
    (GL33/glDeleteShader fragment-shader)))
