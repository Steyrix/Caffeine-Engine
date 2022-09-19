package demo.labyrinth

import engine.core.entity.CompositeEntity
import engine.core.entity.Entity
import engine.core.render.render2D.AnimatedObject2D
import engine.core.scene.Scene
import engine.core.shader.Shader
import engine.core.shader.ShaderLoader
import engine.core.texture.Texture2D
import engine.core.update.SetOf2DParameters
import engine.core.window.Window
import engine.feature.animation.AnimationHolder2D
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
    private val campfireAnimations = presets.campfirePresets.animation.animations

    private var characterGraphicalComponent: AnimatedObject2D? = null
    private var characterBoundingBox: BoundingBox? = null

    private var character: CompositeEntity? = null
    override var renderProjection: Matrix4f? = null

    private var campfire: CompositeEntity? = null
    private var campfireGraphicalComponent: AnimatedObject2D? = null

    override fun init() {
        renderProjection = Matrix4f().ortho(
                0f,
                screenWidth,
                screenHeight,
                0f,
                0f,
                1f
        )

        initCharacterGraphics()

        character = Player(
                drawableComponent = characterGraphicalComponent!!
        )

        initCampfireGraphics()

        campfire = object : CompositeEntity() {}
        campfire?.addComponent(
                component = campfireGraphicalComponent as Entity,
                parameters = SetOf2DParameters(
                        x = 500f,
                        y = 500f,
                        xSize = 75f,
                        ySize = 75f,
                        rotationAngle = 0f
                )
        )
    }

    private fun initCharacterGraphics() {
        val boxVertexShaderPath = this.javaClass.getResource("/shaders/boundingBoxVertexShader.glsl")!!.path
        val boxFragmentShaderPath = this.javaClass.getResource("/shaders/boundingBoxFragmentShader.glsl")!!.path
        val characterVertexShaderPath = this.javaClass.getResource("/shaders/animVertexShader.glsl")!!.path
        val characterFragmentShaderPath = this.javaClass.getResource("/shaders/animFragmentShader.glsl")!!.path

        characterBoundingBox = BoundingBox(
                x = 100f,
                y = 100f,
                xSize = 100f,
                ySize = 100f
        ).apply {
            shader = ShaderLoader.loadFromFile(
                    vertexShaderFilePath = boxVertexShaderPath,
                    fragmentShaderFilePath = boxFragmentShaderPath
            ).also {
                it.bind()
                it.setUniform(Shader.VAR_KEY_PROJECTION, renderProjection!!)
            }
        }

        val frameSizeX = 0.1f
        val frameSizeY = 0.333f
        val mainCharacterUV = Buffer.getRectangleSectorVertices(frameSizeX, frameSizeY)

        val texturePath = this.javaClass.getResource("/textures/base_character.png")!!.path
        characterGraphicalComponent = AnimatedObject2D(
                bufferParamsCount = 2,
                dataArrays = listOf(Buffer.RECTANGLE_INDICES, mainCharacterUV),
                verticesCount = 6,
                texture = Texture2D.createInstance(texturePath),
                arrayTexture = null,
                animationHolder = AnimationHolder2D(frameSizeX, frameSizeY, characterAnimations)
        ).apply {
            boundingBox = characterBoundingBox
            x = 100f
            y = 100f
            xSize = 100f
            ySize = 100f
            shader = ShaderLoader.loadFromFile(
                    vertexShaderFilePath = characterVertexShaderPath,
                    fragmentShaderFilePath = characterFragmentShaderPath
            ).also {
                it.bind()
                it.setUniform(Shader.VAR_KEY_PROJECTION, renderProjection!!)
            }
        }
    }

    private fun initCampfireGraphics() {
        val vertexShaderPath = this.javaClass.getResource("/shaders/animVertexShader.glsl")!!.path
        val fragmentShaderPath = this.javaClass.getResource("/shaders/animFragmentShader.glsl")!!.path

        val frameSizeX = 0.2f
        val frameSizeY = 1.0f
        val uv = Buffer.getRectangleSectorVertices(frameSizeX, frameSizeY)

        val texturePath = this.javaClass.getResource("/textures/camp_fire_texture.png")!!.path
        campfireGraphicalComponent = AnimatedObject2D(
                bufferParamsCount = 2,
                dataArrays = listOf(Buffer.RECTANGLE_INDICES, uv),
                verticesCount = 6,
                texture = Texture2D.createInstance(texturePath),
                arrayTexture = null,
                animationHolder = AnimationHolder2D(frameSizeX, frameSizeY, campfireAnimations)
        ).apply {
            x = 500f
            y = 500f
            xSize = 50f
            ySize = 50f
            shader = ShaderLoader.loadFromFile(
                    vertexShaderFilePath = vertexShaderPath,
                    fragmentShaderFilePath = fragmentShaderPath
            ).also {
                it.bind()
                it.setUniform(Shader.VAR_KEY_PROJECTION, renderProjection!!)
            }
        }
    }
    override fun input(window: Window) {
        character?.input(window)
    }

    override fun update(deltaTime: Float) {
        character?.update(deltaTime)
        campfire?.update(deltaTime)
    }

    override fun render(window: Window) {
        glClear(GL_COLOR_BUFFER_BIT)
        glClearColor(0.5f, 0.5f, 0.5f, 0.5f)

        // graphicalObject?.draw()
        character?.draw()
        campfire?.draw()
    }
}