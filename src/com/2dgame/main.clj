(ns com.2dgame.main
  (:import
   (org.lwjgl.glfw GLFW)
   (org.lwjgl.opengl GL GL11)))

(defonce globals (atom {:errorCallback nil
                        :keyCallback   nil
                        :window        nil
                        :width         0
                        :height        0
                        :title         "none"
                        :angle         0.0
                        :last-time     0
                        ;; geom ids
                        :vao-id        0
                        :vbo-id        0
                        :vboc-id       0
                        :vboi-id       0
                        :indices-count 0
                        ;; shader program ids
                        :vs-id         0
                        :fs-id         0
                        :p-id          0
                        :angle-loc     0}))

(defn- init-window
  [width height title]
  (when (not (GLFW/glfwInit))
    (throw (IllegalStateException. "Unable to initialize GLFW")))

  (GLFW/glfwDefaultWindowHints)
  (GLFW/glfwWindowHint GLFW/GLFW_VISIBLE               GLFW/GLFW_FALSE)
  (GLFW/glfwWindowHint GLFW/GLFW_RESIZABLE             GLFW/GLFW_TRUE)
  (GLFW/glfwWindowHint GLFW/GLFW_OPENGL_PROFILE        GLFW/GLFW_OPENGL_CORE_PROFILE)
  (GLFW/glfwWindowHint GLFW/GLFW_OPENGL_FORWARD_COMPAT GL11/GL_TRUE)
  (GLFW/glfwWindowHint GLFW/GLFW_CONTEXT_VERSION_MAJOR 3)
  (GLFW/glfwWindowHint GLFW/GLFW_CONTEXT_VERSION_MINOR 2)

  (swap! globals assoc
         :window (GLFW/glfwCreateWindow width height title 0 0))

  (GLFW/glfwMakeContextCurrent (:window @globals))
  (GLFW/glfwSwapInterval 1)
  (GLFW/glfwShowWindow (:window @globals)))

(defn- init-gl
  []
  (GL/createCapabilities)
  (println "OpenGL version:" (GL11/glGetString GL11/GL_VERSION))
  (GL11/glClearColor 0.5 0.3 0.8 1.0)
  (GL11/glViewport 0 0 800 600))

(defn- main-loop
  []
  (while (not (GLFW/glfwWindowShouldClose (:window @globals)))
    (GL11/glClear GL11/GL_COLOR_BUFFER_BIT)
    (GLFW/glfwSwapBuffers (:window @globals))
    (GLFW/glfwPollEvents)))

(defn- destroy-gl [])

(defn main
  [opts]
  (try
    (init-window 800 600 "2D Game")
    (init-gl)
    (main-loop)
    (destroy-gl)
    (GLFW/glfwDestroyWindow (:window @globals))
    (finally
      (GLFW/glfwTerminate))))
