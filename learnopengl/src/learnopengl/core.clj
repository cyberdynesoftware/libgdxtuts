(ns learnopengl.core
  (:require [learnopengl.triangle :as triangle]
            [learnopengl.shader :as shader]
            [learnopengl.texture :as texture]
            [learnopengl.transformations :as trans]
            [learnopengl.coordinate-systems :as coordsys]
            [learnopengl.camera :as cam])
  (:import [org.lwjgl.glfw GLFW GLFWKeyCallbackI]
           [org.lwjgl.opengl GL GL33]
           [org.lwjgl.system MemoryUtil]
           [org.lwjgl.system MemoryStack]
           [org.joml Matrix4f Vector3f])
  (:gen-class))

(def mix-param (atom 0.5))

(def last-frame (atom 0))

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
    (GL33/glEnable GL33/GL_DEPTH_TEST)
    (GLFW/glfwSetInputMode window GLFW/GLFW_CURSOR GLFW/GLFW_CURSOR_DISABLED)
    (GLFW/glfwSetCursorPosCallback window cam/cursor-callback)
    (GLFW/glfwSetScrollCallback window cam/scroll-callback)

    (let [vertex-shader-source (slurp "resources/shaders/vertex-shader.vs")
          fragment-shader-source (slurp "resources/shaders/fragment-shader.fs")
          shader-program (shader/get-shader-program vertex-shader-source fragment-shader-source)
          wall (texture/load-texture "resources/textures/wall.jpg")
          smiley (texture/load-texture-with-alpha "resources/textures/awesomeface.png")
          cube (coordsys/create-vertex-array coordsys/vertices)
          mat4 (new Matrix4f)
          pivot (new Vector3f (float 1) (float 0.3) (float 0.5))]
      (GL33/glUseProgram shader-program)
      (GL33/glUniform1i (GL33/glGetUniformLocation shader-program "ourTexture") 0)
      (GL33/glUniform1i (GL33/glGetUniformLocation shader-program "otherTexture") 1)

      (while (not (GLFW/glfwWindowShouldClose window))
        (let [now (GLFW/glfwGetTime)
              delta (- now @last-frame)]
          (reset! last-frame now)
          (when (= (GLFW/glfwGetKey window GLFW/GLFW_KEY_ESCAPE) GLFW/GLFW_PRESS)
            (GLFW/glfwSetWindowShouldClose window true))
          (when (= (GLFW/glfwGetKey window GLFW/GLFW_KEY_UP) GLFW/GLFW_PRESS)
            (swap! mix-param (fn [current args] (if (< current 1.0) (+ current args) 1.0)) 0.005))
          (when (= (GLFW/glfwGetKey window GLFW/GLFW_KEY_DOWN) GLFW/GLFW_PRESS)
            (swap! mix-param (fn [current args] (if (> current 0.0) (- current args) 0.0)) 0.005))
          (when (= (GLFW/glfwGetKey window GLFW/GLFW_KEY_W) GLFW/GLFW_PRESS)
            (cam/WS 1 delta))
          (when (= (GLFW/glfwGetKey window GLFW/GLFW_KEY_S) GLFW/GLFW_PRESS)
            (cam/WS -1 delta))
          (when (= (GLFW/glfwGetKey window GLFW/GLFW_KEY_A) GLFW/GLFW_PRESS)
            (cam/AD -1 delta))
          (when (= (GLFW/glfwGetKey window GLFW/GLFW_KEY_D) GLFW/GLFW_PRESS)
            (cam/AD 1 delta))

          (GL33/glUniform1f (GL33/glGetUniformLocation shader-program "mix_param") (float @mix-param))

          (GL33/glClearColor (float 0.2) (float 0.3) (float 0.3) (float 1))
          (GL33/glClear (bit-or GL33/GL_COLOR_BUFFER_BIT GL33/GL_DEPTH_BUFFER_BIT))

          (GL33/glActiveTexture GL33/GL_TEXTURE0)
          (GL33/glBindTexture GL33/GL_TEXTURE_2D wall)
          (GL33/glActiveTexture GL33/GL_TEXTURE1)
          (GL33/glBindTexture GL33/GL_TEXTURE_2D smiley)
          (GL33/glUseProgram shader-program)

          (GL33/glBindVertexArray cube)

          (doseq [[position angle] (partition 2 (interleave coordsys/cube-positions (range 20 400 20)))]
            (.translation mat4 position)
            (if (some #{angle} [20 80 140 200])
              (.rotate mat4 (float (GLFW/glfwGetTime)) pivot)
              (.rotate mat4 (org.joml.Math/toRadians (float angle)) pivot))
            (shader/load-matrix shader-program "model" mat4)
            (GL33/glDrawArrays GL33/GL_TRIANGLES 0 36))

          (shader/load-matrix shader-program "view" (cam/camera (GLFW/glfwGetTime)))
          (shader/load-matrix shader-program "projection" (cam/perspective))

          (GLFW/glfwSwapBuffers window)
          (GLFW/glfwPollEvents)))))

  (GLFW/glfwTerminate)
  (println "So long..."))
