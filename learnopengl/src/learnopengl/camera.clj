(ns learnopengl.camera
  (:import [org.joml Matrix4f Vector3f]
           [org.lwjgl.glfw GLFWCursorPosCallbackI GLFWScrollCallbackI]))

(def speed (float 2.5))

(def view (new Matrix4f))

(def position (new Vector3f (float 0) (float 0) (float 3)))

(def front (new Vector3f (float 0) (float 0) (float -1)))
(def front-temp (new Vector3f))

(def up (new Vector3f (float 0) (float 1) (float 0)))

(defn camera
  [delta]
  (.set front-temp front)
  (.setLookAt view position (.add front-temp position) up))

(defn WS
  [sign delta]
  (.set front-temp front)
  (.setComponent front-temp 1 (float 0))
  (.add position (.mul (.normalize front-temp) (float (* sign delta speed)))))

(defn AD
  [sign delta]
  (.set front-temp front)
  (.setComponent front-temp 1 (float 0))
  (.add position (.mul (.normalize (.cross front-temp up)) (float (* sign delta speed)))))

(def last-x (atom 200))
(def last-y (atom 150))
(def yaw (atom -90))
(def pitch (atom 0))
(def first-call (atom true))

(defn constrain-pitch
  [pitch]
  (if (> pitch 89)
    89
    (if (< pitch -89)
      -89
      pitch)))

(def cursor-callback
  (reify GLFWCursorPosCallbackI
    (invoke [_ _ x y]
      (when @first-call
        (reset! last-x x)
        (reset! last-y y)
        (reset! first-call false))
      (let [sensitivity 0.1
            offset-x (* (- x @last-x) sensitivity)
            offset-y (* (- y @last-y) sensitivity -1)]
        (reset! last-x x)
        (reset! last-y y)
        (swap! yaw + offset-x)
        (swap! pitch + offset-y)
        (swap! pitch constrain-pitch)
        (.normalize
          (.set front
                (float (* (Math/cos (Math/toRadians @yaw))
                          (Math/cos (Math/toRadians @pitch))))
                (float (Math/sin (Math/toRadians @pitch)))
                (float (* (Math/sin (Math/toRadians @yaw))
                          (Math/cos (Math/toRadians @pitch))))))))))

(def perspective-matrix (new Matrix4f))

(def fov (atom 45))

(def scroll-callback
  (reify GLFWScrollCallbackI
    (invoke [_ _ _ y]
      (swap! fov - (* y 0.1))
      (swap! fov #(if (< % 1)
                    1
                    (if (> % 45)
                      45
                      %))))))

(defn perspective
  []
  (.setPerspective perspective-matrix (float @fov) (float (/ 4 3)) (float 0.1) (float 100)))
