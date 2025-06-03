(ns learnopengl.texture
  (:require [learnopengl.triangle :as triangle])
  (:import [org.lwjgl.opengl GL33]
           [org.lwjgl.stb STBImage]
           [org.lwjgl BufferUtils]))

(defn load-texture
  [path]
  (STBImage/stbi_set_flip_vertically_on_load true)
  (let [width (BufferUtils/createIntBuffer 1)
        height (BufferUtils/createIntBuffer 1)
        channels (BufferUtils/createIntBuffer 1)
        image-data (STBImage/stbi_load path width height channels 0)
        texture (GL33/glGenTextures)]
    (GL33/glBindTexture GL33/GL_TEXTURE_2D texture)

    (GL33/glTexParameteri GL33/GL_TEXTURE_2D GL33/GL_TEXTURE_WRAP_S GL33/GL_REPEAT)
    (GL33/glTexParameteri GL33/GL_TEXTURE_2D GL33/GL_TEXTURE_WRAP_T GL33/GL_REPEAT)

    (GL33/glTexParameteri GL33/GL_TEXTURE_2D GL33/GL_TEXTURE_MIN_FILTER GL33/GL_LINEAR)
    (GL33/glTexParameteri GL33/GL_TEXTURE_2D GL33/GL_TEXTURE_MAG_FILTER GL33/GL_LINEAR)

    (GL33/glTexImage2D GL33/GL_TEXTURE_2D 0 GL33/GL_RGB (.get width) (.get height) 0 GL33/GL_RGB GL33/GL_UNSIGNED_BYTE image-data)
    (GL33/glGenerateMipmap GL33/GL_TEXTURE_2D)

    (STBImage/stbi_image_free image-data)
    texture))

(defn load-texture-with-alpha
  [path]
  (STBImage/stbi_set_flip_vertically_on_load true)
  (let [width (BufferUtils/createIntBuffer 1)
        height (BufferUtils/createIntBuffer 1)
        channels (BufferUtils/createIntBuffer 1)
        image-data (STBImage/stbi_load path width height channels 0)
        texture (GL33/glGenTextures)]
    (GL33/glBindTexture GL33/GL_TEXTURE_2D texture)

    (GL33/glTexParameteri GL33/GL_TEXTURE_2D GL33/GL_TEXTURE_WRAP_S GL33/GL_REPEAT)
    (GL33/glTexParameteri GL33/GL_TEXTURE_2D GL33/GL_TEXTURE_WRAP_T GL33/GL_REPEAT)

    (GL33/glTexParameteri GL33/GL_TEXTURE_2D GL33/GL_TEXTURE_MIN_FILTER GL33/GL_LINEAR)
    (GL33/glTexParameteri GL33/GL_TEXTURE_2D GL33/GL_TEXTURE_MAG_FILTER GL33/GL_LINEAR)

    (GL33/glTexImage2D GL33/GL_TEXTURE_2D 0 GL33/GL_RGB (.get width) (.get height) 0 GL33/GL_RGBA GL33/GL_UNSIGNED_BYTE image-data)
    (GL33/glGenerateMipmap GL33/GL_TEXTURE_2D)

    (STBImage/stbi_image_free image-data)
    texture))

(def vertices
  ; // positions          // colors           // texture coords
  [
   0.5,  0.5, 0.0,   1.0, 0.0, 0.0,   1.0, 1.0,   ;// top right
   0.5, -0.5, 0.0,   0.0, 1.0, 0.0,   1.0, 0.0,   ;// bottom right
   -0.5, -0.5, 0.0,  0.0, 0.0, 1.0,   0.0, 0.0,  ; // bottom left
   -0.5,  0.5, 0.0,  1.0, 1.0, 0.0,   0.0, 1.0   ; // top left 
   ])

(defn create-vertex-array
  [vertices]
  (let [vbo (GL33/glGenBuffers)
        vao (GL33/glGenVertexArrays)
        ebo (GL33/glGenBuffers)]
    (GL33/glBindVertexArray vao)

    (GL33/glBindBuffer GL33/GL_ARRAY_BUFFER vbo)
    (GL33/glBufferData GL33/GL_ARRAY_BUFFER (triangle/create-float-buffer vertices) GL33/GL_STATIC_DRAW)

    (GL33/glBindBuffer GL33/GL_ELEMENT_ARRAY_BUFFER ebo)
    (GL33/glBufferData GL33/GL_ELEMENT_ARRAY_BUFFER (triangle/create-int-buffer triangle/indices) GL33/GL_STATIC_DRAW)

    (GL33/glVertexAttribPointer 0 3 GL33/GL_FLOAT false 32 0)
    (GL33/glEnableVertexAttribArray 0)

    (GL33/glVertexAttribPointer 1 3 GL33/GL_FLOAT false 32 12)
    (GL33/glEnableVertexAttribArray 1)

    (GL33/glVertexAttribPointer 2 2 GL33/GL_FLOAT false 32 24)
    (GL33/glEnableVertexAttribArray 2)
    vao))
