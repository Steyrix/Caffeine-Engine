package demo.labyrinth

import engine.core.render.render2D.OpenGlObject2D
import engine.core.scene.Scene
import engine.core.shader.Shader
import engine.core.shader.ShaderLoader
import engine.core.texture.Texture2D
import engine.core.window.Window
import engine.feature.collision.boundingbox.BoundingBox
import engine.feature.util.Buffer
import org.joml.Matrix4f
import org.lwjgl.opengl.GL33C.*

class LabyrinthDemo(
        val screenWidth: Float,
        val screenHeight: Float
) : Scene {

    private val presets = LabyrinthPresets()
    private val characterAnimations = presets.characterPresets.animation.animations

    private var mainCharacter: OpenGlObject2D? = null
    private var characterBoundingBox: BoundingBox? = null
    private var renderProjection: Matrix4f? = null

    override fun init() {
        renderProjection = Matrix4f().ortho(
                0f,
                screenWidth,
                screenHeight,
                0f,
                0f,
                1f
        )

        val boxVertexShaderPath = this.javaClass.getResource("/shaders/boundingBoxVertexShader.glsl")!!.path
        val boxFragmentShaderPath = this.javaClass.getResource("/shaders/boundingBoxFragmentShader.glsl")!!.path
        characterBoundingBox = BoundingBox(
                x = 100f,
                y = 100f,
                xSize = 100f,
                ySize = 100f
        ).apply {
            shader = ShaderLoader.loadFromFile(
                    vertexShaderFilePath = boxVertexShaderPath,
                    fragmentShaderFilePath = boxFragmentShaderPath
            )

            shader!!.bind()
            shader!!.setUniform(Shader.VAR_KEY_PROJECTION, renderProjection!!)
        }

        val frameSizeX = 0.1f
        val frameSizeY = 0.333f
        val mainCharacterUV = Buffer.getRectangleSectorVertices(frameSizeX, frameSizeY)

        val texturePath = this.javaClass.getResource("/textures/base_character.png")!!.path
        mainCharacter = OpenGlObject2D(
                bufferParamsCount = 2,
                dataArrays = listOf(Buffer.RECTANGLE_INDICES, mainCharacterUV),
                verticesCount = 6,
                texture = Texture2D.createInstance(texturePath),
                arrayTexture = null
        )

        val vertexShaderPath = this.javaClass.getResource("/shaders/animVertexShader.glsl")!!.path
        val fragmentShaderPath = this.javaClass.getResource("/shaders/animFragmentShader.glsl")!!.path
        mainCharacter?.let { openGlObject ->
            openGlObject.boundingBox = characterBoundingBox
            openGlObject.x = 100f
            openGlObject.y = 100f
            openGlObject.xSize = 100f
            openGlObject.ySize = 100f
            openGlObject.shader = ShaderLoader.loadFromFile(
                    vertexShaderFilePath = vertexShaderPath,
                    fragmentShaderFilePath = fragmentShaderPath
            )
            openGlObject.shader!!.bind()
            openGlObject.shader!!.setUniform(Shader.VAR_KEY_PROJECTION, renderProjection!!)
        }
    }

    override fun input(window: Window) {
       //
    }

    override fun update(deltaTime: Float) {
        //
    }

    override fun render(window: Window) {
        glClear(GL_COLOR_BUFFER_BIT)
        glClearColor(0.5f, 0.5f, 0.5f, 0.5f)

        mainCharacter?.draw2D()
    }
}