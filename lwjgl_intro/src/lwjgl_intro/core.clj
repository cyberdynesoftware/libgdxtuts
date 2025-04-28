(ns lwjgl-intro.core
  (:import [org.lwjgl.glfw GLFW GLFWKeyCallbackI]
           [org.lwjgl.opengl GL GL33]
           [org.lwjgl.system MemoryUtil])
  (:gen-class))

(def cb (proxy [java.lang.Object GLFWKeyCallbackI] []
          (invoke [window keycode _ action _]
            (when (and (= keycode GLFW/GLFW_KEY_ESCAPE)
                       (= action GLFW/GLFW_PRESS))
              (GLFW/glfwSetWindowShouldClose window true)))))

(defn -main
  "LWJGL introduction"
  [& args]
  (GLFW/glfwInit)
  (GLFW/glfwWindowHint GLFW/GLFW_CONTEXT_VERSION_MAJOR 3)
  (GLFW/glfwWindowHint GLFW/GLFW_CONTEXT_VERSION_MINOR 3)
  (GLFW/glfwWindowHint GLFW/GLFW_OPENGL_PROFILE GLFW/GLFW_OPENGL_CORE_PROFILE)

  (let [window (GLFW/glfwCreateWindow (int 400) (int 300) "LWJGL" MemoryUtil/NULL MemoryUtil/NULL)]
    (GLFW/glfwMakeContextCurrent window)
    (GL/createCapabilities)
    (GL33/glClearColor (float 0) (float 0) (float 1) (float 0))
    ;(GLFW/glfwSetKeyCallback window cb) 

    (while (not (GLFW/glfwWindowShouldClose window))
      (when (= (GLFW/glfwGetKey window GLFW/GLFW_KEY_ESCAPE) GLFW/GLFW_PRESS)
              (GLFW/glfwSetWindowShouldClose window true))

      (GL33/glClear GL33/GL_COLOR_BUFFER_BIT)
      (GLFW/glfwSwapBuffers window)
      (GLFW/glfwPollEvents)))

  (GLFW/glfwTerminate)
  (println "So long..."))
