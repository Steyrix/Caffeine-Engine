package demo.text

import engine.core.scene.Scene
import engine.core.shader.Shader
import engine.core.shader.ShaderLoader
import engine.core.window.Window
import engine.feature.geometry.Point2D
import engine.feature.text.TextRenderer
import engine.feature.text.data.TextDataUtils
import org.joml.Matrix4f
import org.lwjgl.opengl.GL33C.*
import java.awt.Dimension

class TextDemo(
        override val screenWidth: Float,
        override val screenHeight: Float
) : Scene {

    override var renderProjection: Matrix4f? = null
    var textRenderer: TextRenderer? = null
    val textToDisplay: String = "This is a text sample"

    override fun init() {
        renderProjection = Matrix4f().ortho(
                0f,
                screenWidth,
                screenHeight,
                0f,
                0f,
                1f
        )

        val vertexShaderPath = this.javaClass.getResource("/shaders/textShaders/textRenderVertexShader.glsl")!!.path
        val fragmentShaderPath = this.javaClass.getResource("/shaders/textShaders/textRenderFragmentShader.glsl")!!.path

        val textureAtlasPath = this.javaClass.getResource("/textures/simpleFontAtlas.png")!!.path

        textRenderer = TextRenderer.getInstance(
                charSize = Dimension(64, 64),
                textureFilePath = textureAtlasPath,
                characters = TextDataUtils.symbolSetSimple.toMutableList(),
                initialShader = ShaderLoader.loadFromFile(
                        vertexShaderFilePath = vertexShaderPath,
                        fragmentShaderFilePath = fragmentShaderPath
                ).also {
                    it.bind()
                    it.setUniform(Shader.VAR_KEY_PROJECTION, renderProjection!!)
                }
        )
    }

    override fun input(window: Window) {
        //
    }

    override fun update(deltaTime: Float) {
        //
    }

    override fun render(window: Window) {
        glClear(GL_COLOR_BUFFER_BIT)
        glClearColor(0f, 0f, 0f, 1f)

        textRenderer?.drawText(
                text = textToDisplay,
                fontSize = Dimension(25, 25),
                pos = Point2D(0f, 0f)
        )

        textRenderer?.drawText(
                text = textToDisplay,
                fontSize = Dimension(40, 50),
                pos = Point2D(0f, 30f)
        )
    }
}