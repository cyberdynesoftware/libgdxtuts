(ns learnopengl.core
  (:require [learnopengl.triangle :as triangle]
            [learnopengl.shader :as shader]
            [learnopengl.texture :as texture]
            [learnopengl.transformations :as trans])
  (:import [org.lwjgl.glfw GLFW GLFWKeyCallbackI]
           [org.lwjgl.opengl GL GL33]
           [org.lwjgl.system MemoryUtil])
  (:gen-class))

(def mix-param (atom 0.5))

(defn -main
  "learnopengl hello window"
  [& args]
  (GLFW/glfwInit)
  (GLFW/glfwWindowHint GLFW/GLFW_CONTEXT_VERSION_MAJOR 3)
  (GLFW/glfwWindowHint GLFW/GLFW_CONTEXT_VERSION_MINOR 3)
  (GLFW/glfwWindowHint GLFW/GLFW_OPENGL_PROFILE GLFW/GLFW_OPENGL_CORE_PROFILE)

  (let [window (GLFW/glfwCreateWindow (int 400) (int 300) "learnopengl" MemoryUtil/NULL MemoryUtil/NULL)]
    (GLFW/glfwMakeContextCurrent window)
    (GL/createCapabilities)

    (trans/first-steps)

    (let [vertex-shader-source (slurp "resources/shaders/vertex-shader.vs")
          fragment-shader-source (slurp "resources/shaders/fragment-shader.fs")
          shader-program (shader/get-shader-program vertex-shader-source fragment-shader-source)
          wall (texture/load-texture "resources/textures/wall.jpg")
          smiley (texture/load-texture-with-alpha "resources/textures/awesomeface.png")
          rectangle (texture/create-vertex-array texture/vertices)]
      (GL33/glUseProgram shader-program)
      (GL33/glUniform1i (GL33/glGetUniformLocation shader-program "ourTexture") 0)
      (GL33/glUniform1i (GL33/glGetUniformLocation shader-program "otherTexture") 1)

      (while (not (GLFW/glfwWindowShouldClose window))
        (when (= (GLFW/glfwGetKey window GLFW/GLFW_KEY_ESCAPE) GLFW/GLFW_PRESS)
          (GLFW/glfwSetWindowShouldClose window true))
        (when (= (GLFW/glfwGetKey window GLFW/GLFW_KEY_UP) GLFW/GLFW_PRESS)
          (swap! mix-param (fn [current args] (if (< current 1.0) (+ current args) 1.0)) 0.005))
        (when (= (GLFW/glfwGetKey window GLFW/GLFW_KEY_DOWN) GLFW/GLFW_PRESS)
          (swap! mix-param (fn [current args] (if (> current 0.0) (- current args) 0.0)) 0.005))

        (GL33/glUniform1f (GL33/glGetUniformLocation shader-program "mix_param") (float @mix-param))

        (GL33/glClearColor (float 0.2) (float 0.3) (float 0.3) (float 1))
        (GL33/glClear GL33/GL_COLOR_BUFFER_BIT)
        
        ;(GL33/glPolygonMode GL33/GL_FRONT_AND_BACK GL33/GL_LINE)

        (GL33/glUseProgram shader-program)

        ;(let [offset-location (GL33/glGetUniformLocation shader-program "offset")]
        ;  (GL33/glUniform1f offset-location (float 0.5)))

        (GL33/glActiveTexture GL33/GL_TEXTURE0)
        (GL33/glBindTexture GL33/GL_TEXTURE_2D wall)
        (GL33/glActiveTexture GL33/GL_TEXTURE1)
        (GL33/glBindTexture GL33/GL_TEXTURE_2D smiley)

        (GL33/glBindVertexArray rectangle)
        ;(GL33/glDrawArrays GL33/GL_TRIANGLES 0 3)
        (GL33/glDrawElements GL33/GL_TRIANGLES 6 GL33/GL_UNSIGNED_INT 0)

        (GLFW/glfwSwapBuffers window)
        (GLFW/glfwPollEvents))))

  (GLFW/glfwTerminate)
  (println "So long..."))
