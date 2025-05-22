(ns learnopengl.shader
  (:import [org.lwjgl.opengl GL33]))

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

(defn create-shader-program
  [& shader]
  (let [program (GL33/glCreateProgram)]
    (dorun (map #(GL33/glAttachShader program %) shader))
    (GL33/glLinkProgram program)
    (let [success (GL33/glGetProgrami program GL33/GL_LINK_STATUS)]
      (when (= success 0)
        (println "ERROR: shader program linking failed:")
        (println (GL33/glGetProgramInfoLog program))))
    program))

(defn get-shader-program
  [vertex-shader-source
   fragment-shader-source]
  (let [vertex-shader (create-shader GL33/GL_VERTEX_SHADER vertex-shader-source)
        fragment-shader (create-shader GL33/GL_FRAGMENT_SHADER fragment-shader-source)
        shader-program (create-shader-program vertex-shader fragment-shader)]
    (GL33/glDeleteShader vertex-shader)
    (GL33/glDeleteShader fragment-shader)
    shader-program))
