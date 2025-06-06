(ns lwjgl-intro.core
  (:import [org.lwjgl.glfw GLFW GLFWKeyCallbackI]
           [org.lwjgl.opengl GL GL11]
           [org.lwjgl.system MemoryUtil])
  (:gen-class))

(def cb (reify GLFWKeyCallbackI
          (invoke [_ window keycode _ action _]
            (when (and (= keycode GLFW/GLFW_KEY_ESCAPE)
                       (= action GLFW/GLFW_PRESS))
              (GLFW/glfwSetWindowShouldClose window true)))))

(defn -main
  "LWJGL introduction"
  [& args]
  (GLFW/glfwInit)
  (GLFW/glfwDefaultWindowHints)
  (GLFW/glfwWindowHint GLFW/GLFW_VISIBLE GLFW/GLFW_FALSE)
  (GLFW/glfwWindowHint GLFW/GLFW_RESIZABLE GLFW/GLFW_TRUE)

  ;(GLFW/glfwWindowHint GLFW/GLFW_CONTEXT_VERSION_MAJOR 3)
  ;(GLFW/glfwWindowHint GLFW/GLFW_CONTEXT_VERSION_MINOR 3)
  ;(GLFW/glfwWindowHint GLFW/GLFW_OPENGL_PROFILE GLFW/GLFW_OPENGL_CORE_PROFILE)

  (let [window (GLFW/glfwCreateWindow (int 400) (int 300) "LWJGL" MemoryUtil/NULL MemoryUtil/NULL)]
    (GLFW/glfwMakeContextCurrent window)
    (GLFW/glfwShowWindow window)

    (GL/createCapabilities)
    (GL11/glClearColor (float 0) (float 0) (float 1) (float 0))
    (GLFW/glfwSetKeyCallback window cb) 

    (while (not (GLFW/glfwWindowShouldClose window))
    ;  (when (= (GLFW/glfwGetKey window GLFW/GLFW_KEY_ESCAPE) GLFW/GLFW_PRESS)
    ;          (GLFW/glfwSetWindowShouldClose window true))

      (GL11/glClear GL11/GL_COLOR_BUFFER_BIT)
      (GLFW/glfwSwapBuffers window)
      (GLFW/glfwPollEvents)))

  (GLFW/glfwTerminate)
  (println "So long..."))
