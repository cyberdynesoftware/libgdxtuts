(ns lwjgl-intro.core
  (:import [org.lwjgl.glfw GLFW])
  (:gen-class))

(defn -main
  "LWJGL introduction"
  [& args]
  (GLFW/glfwInit)
  (GLFW/glfwWindowHint GLFW/GLFW_CONTEXT_VERSION_MAJOR 3)
  (GLFW/glfwWindowHint GLFW/GLFW_CONTEXT_VERSION_MINOR 3)
  (GLFW/glfwWindowHint GLFW/GLFW_OPENGL_PROFILE GLFW/GLFW_OPENGL_CORE_PROFILE)

  (let [window (GLFW/glfwCreateWindow (int 400) (int 300) "LWJGL" nil nil)]
    (GLFW/glfwMakeContextCurrent window))

  (println "So long..."))
