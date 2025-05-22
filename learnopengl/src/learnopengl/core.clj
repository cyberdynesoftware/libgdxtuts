(ns learnopengl.core
  (:require [learnopengl.triangle :as triangle]
            [learnopengl.shader :as shader])
  (:import [org.lwjgl.glfw GLFW GLFWKeyCallbackI]
           [org.lwjgl.opengl GL GL33]
           [org.lwjgl.system MemoryUtil])
  (:gen-class))

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

    (let [vertex-shader-source (slurp "resources/shaders/vertex-shader.vs")
          fragment-shader-source (slurp "resources/shaders/fragment-shader.fs")
          shader-program (shader/get-shader-program vertex-shader-source fragment-shader-source)
          triangle (triangle/create-vertex-array triangle/triangle-vertices)]

      (while (not (GLFW/glfwWindowShouldClose window))
        (when (= (GLFW/glfwGetKey window GLFW/GLFW_KEY_ESCAPE) GLFW/GLFW_PRESS)
          (GLFW/glfwSetWindowShouldClose window true))

        (GL33/glClearColor (float 0.2) (float 0.3) (float 0.3) (float 1))
        (GL33/glClear GL33/GL_COLOR_BUFFER_BIT)
        
        ;(GL33/glPolygonMode GL33/GL_FRONT_AND_BACK GL33/GL_LINE)

        (GL33/glUseProgram shader-program)

        (let [delta (GLFW/glfwGetTime)
              green-tint (+ (/ (Math/sin delta) 2) 0.5)
              vertex-color-location (GL33/glGetUniformLocation shader-program "ourColor")]
          (GL33/glUniform4f vertex-color-location (float 0) (float green-tint) (float 0) (float 1)))

        (GL33/glBindVertexArray triangle)
        (GL33/glDrawArrays GL33/GL_TRIANGLES 0 3)
        ;(GL33/glDrawElements GL33/GL_TRIANGLES 6 GL33/GL_UNSIGNED_INT 0)

        (GLFW/glfwSwapBuffers window)
        (GLFW/glfwPollEvents))))

  (GLFW/glfwTerminate)
  (println "So long..."))
