package engine.core.window

import org.lwjgl.glfw.Callbacks
import org.lwjgl.glfw.Callbacks.glfwFreeCallbacks
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.system.MemoryStack
import org.lwjgl.system.MemoryUtil
import java.nio.IntBuffer

private const val DEFAULT_WIDTH = 100
private const val DEFAULT_HEIGHT = 100
private const val DEFAULT_TITLE = "Caffeine Engine"

private const val INIT_ERR_MSG = "Unable to initialize GLFW"
private const val WINDOW_CREATE_ERR_MSG = "Failed to create the GLFW window"

class Window(
        var width: Int = DEFAULT_WIDTH,
        var height: Int = DEFAULT_HEIGHT,
        var isVsyncEnabled: Boolean = false,
        title: String = DEFAULT_TITLE
) {

    private val window: Long
    private var isResized: Boolean = false

    init {
        GLFWErrorCallback.createPrint(System.err).set()

        if (!glfwInit()) {
            throw IllegalStateException(INIT_ERR_MSG)
        }

        glfwDefaultWindowHints() // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE) // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE) // the window will be resizable

        window = glfwCreateWindow(width, height, title, MemoryUtil.NULL, MemoryUtil.NULL)

        if (window == MemoryUtil.NULL) {
            throw RuntimeException(WINDOW_CREATE_ERR_MSG)
        }

        glfwSetKeyCallback(window) { window: Long, key: Int, scancode: Int, action: Int, mods: Int ->
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
                glfwSetWindowShouldClose(window, true)
            } // We will detect this in the rendering loop
        }

        glfwSetFramebufferSizeCallback(window) { window, newWidth, newHeight ->
            width = newWidth
            height = newHeight
            isResized = true
        }

        // Get the thread stack and push a new frame
        MemoryStack.stackPush().use { stack ->
            val pWidth: IntBuffer = stack.mallocInt(1) // int*
            val pHeight: IntBuffer = stack.mallocInt(1) // int*

            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(window, pWidth, pHeight)

            // Get the resolution of the primary monitor
            val vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor())

            // Center the window
            glfwSetWindowPos(
                    window,
                    (vidmode!!.width() - pWidth[0]) / 2,
                    (vidmode!!.height() - pHeight[0]) / 2
            )
        }

        // Make the OpenGL context current
        glfwMakeContextCurrent(window)

        // Enable v-sync
        if (isVsyncEnabled) {
            glfwSwapInterval(1)
        }

        // Make the window visible
        glfwShowWindow(window)
    }

    fun dispose() {
        glfwFreeCallbacks(window)
        glfwDestroyWindow(window)
    }

    fun isKeyPressed(keyCode: Int): Boolean  {
        return glfwGetKey(window, keyCode) == GLFW_PRESS
    }

    fun shouldClose(): Boolean {
        return glfwWindowShouldClose(window)
    }

    fun update() {
        glfwSwapBuffers(window)
        glfwPollEvents()
    }
}