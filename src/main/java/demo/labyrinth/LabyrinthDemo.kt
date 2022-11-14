package demo.labyrinth

import demo.labyrinth.Character.boundingBox
import demo.labyrinth.Character.graphicalComponent
import engine.core.entity.CompositeEntity
import engine.core.entity.Entity
import engine.core.render.render2D.AnimatedObject2D
import engine.core.render.render2D.OpenGlObject2D
import engine.core.scene.Scene
import engine.core.shader.Shader
import engine.core.shader.ShaderLoader
import engine.core.texture.Texture2D
import engine.core.update.SetOf2DParametersWithVelocity
import engine.core.window.Window
import engine.feature.collision.boundingbox.BoundingBox
import engine.feature.collision.boundingbox.BoundingBoxCollider
import engine.feature.collision.boundingbox.BoundingBoxCollisionContext
import engine.feature.collision.tiled.TiledCollider
import engine.feature.collision.tiled.TiledCollisionContext
import engine.feature.tiled.parser.TiledResourceParser
import org.joml.Matrix4f
import org.joml.Vector2f
import org.lwjgl.opengl.GL33C.*
import java.io.File

class LabyrinthDemo(
        override val screenWidth: Float,
        override val screenHeight: Float
) : Scene {

    private val bbCollisionContext = BoundingBoxCollisionContext()
    private val tiledCollisionContext = TiledCollisionContext()

    private val presets = LabyrinthPresets()
    private val characterAnimations = presets.characterPresets.animation.animations
    private val campfireAnimations = presets.campfirePresets.animation.animations

    override var renderProjection: Matrix4f? = null

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
        Character.it = Player(
                drawableComponent = graphicalComponent!!,
                params = characterParameters
        )

        initCrateGraphics()
        Crate.it = object : CompositeEntity() {}
        Crate.it?.addComponent(
                component = Crate.graphicalComponent as Entity,
                parameters = crateParameters
        )

        initCampfireGraphics()
        Campfire.it = object : CompositeEntity() {}
        Campfire.it?.addComponent(
                component = Campfire.graphicalComponent as Entity,
                parameters = campfireParameters
        )

        Map.parameters = getMapParameters(screenWidth, screenHeight)
        initTileMapGraphics()
        Map.it = object  : CompositeEntity() {}
        Map.it?.addComponent(
                component = Map.graphicalComponent as Entity,
                parameters = Map.parameters
        )

        initSkeletons()
        Skeletons.graphicalComponents.forEachIndexed { i, it ->
            Skeletons.it.add(
                    CompositeEntity().also { entity ->
                        entity.addComponent(
                                component = it,
                                parameters = Skeletons.parameters[i]
                        )
                    }
            )
        }

        initPhysics()
    }

    private fun initPhysics() {
        Character.it?.addComponent(
                Character.boxCollider as Entity,
                parameters = characterParameters
        )

        Character.it?.addComponent(
                Character.tiledCollider as Entity,
                parameters = characterParameters
        )

        bbCollisionContext.addEntity(Crate.boundingBox as Entity)
        tiledCollisionContext.addEntity(Map.graphicalComponent as Entity)
    }

    private fun initTileMapGraphics() {
        val vertexShaderPath = this.javaClass.getResource("/shaders/lightingVertexShader.glsl")!!.path
        val fragmentShaderPath = this.javaClass.getResource("/shaders/lightingFragmentShader.glsl")!!.path

        Map.graphicalComponent = TiledResourceParser.createTileMapFromXml(
                File(this.javaClass.getResource("/tiled/cave_level.xml")!!.path)
        )
        Map.graphicalComponent?.shader = ShaderLoader.loadFromFile(
                vertexShaderFilePath = vertexShaderPath,
                fragmentShaderFilePath = fragmentShaderPath
        ).also {
            it.bind()
            it.setUniform(Shader.VAR_KEY_PROJECTION, renderProjection!!)

            it.setUniform("screenSize", Vector2f(screenWidth, screenHeight))
            it.setUniform("lightSourceSize", Vector2f(campfireParameters.xSize, campfireParameters.ySize))
            it.setUniform("lightSourceCoords", Vector2f(campfireParameters.x, campfireParameters.y))
            it.setUniform("lightIntensityCap", lightIntensityCap)
        }

        Map.parameters.xSize = screenWidth / Map.graphicalComponent!!.relativeWidth
        Map.parameters.ySize = screenHeight / Map.graphicalComponent!!.relativeHeight
    }

    private fun initCharacterGraphics() {

        boundingBox = BoundingBox(
                x = 100f,
                y = 100f,
                xSize = 60f,
                ySize = 60f
        ).apply {
            shader = ShaderController.createBoundingBoxShader(renderProjection!!)
        }

        Character.boxCollider =
                BoundingBoxCollider(boundingBox!!, characterParameters, bbCollisionContext)
        Character.tiledCollider =
                TiledCollider(characterParameters, "Walking Layer", tiledCollisionContext)

        val frameSizeX = 0.1f
        val frameSizeY = 0.333f

        val texturePath = this.javaClass.getResource("/textures/base_character.png")!!.path
        graphicalComponent = AnimatedObject2D(
                frameSizeX,
                frameSizeY,
                texture = Texture2D.createInstance(texturePath),
                animations = characterAnimations
        ).apply {
            boundingBox = Character.boundingBox
            x = 100f
            y = 100f
            xSize = 60f
            ySize = 60f
            shader = ShaderController.createAnimationShader(renderProjection!!)
        }
    }

    private fun initCrateGraphics() {
        Crate.boundingBox = BoundingBox(
                x = 400f,
                y = 150f,
                xSize = 70f,
                ySize = 70f
        ).apply {
            shader = ShaderController.createBoundingBoxShader(renderProjection!!)
        }

        val texturePath = this.javaClass.getResource("/textures/obj_crate.png")!!.path
        Crate.graphicalComponent = OpenGlObject2D(
                texture2D = Texture2D.createInstance(texturePath),
        ).apply {
            boundingBox = Crate.boundingBox
            shader = ShaderController.createTexturedShader(renderProjection!!)
        }
    }

    private fun initCampfireGraphics() {
        val frameSizeX = 0.2f
        val frameSizeY = 1.0f

        val texturePath = this.javaClass.getResource("/textures/camp_fire_texture.png")!!.path
        Campfire.graphicalComponent = AnimatedObject2D(
                frameSizeX = frameSizeX,
                frameSizeY = frameSizeY,
                texture = Texture2D.createInstance(texturePath),
                animations = campfireAnimations
        ).apply {
            shader = ShaderController.createAnimationShader(renderProjection!!)
        }
    }

    private fun initSkeletons() {
        val frameSizeX = 0.1f
        val frameSizeY = 0.1f
        val texturePath = this.javaClass.getResource("/textures/base_skeleton.png")!!.path

        for (i in 0 .. 1) {
            Skeletons.parameters.add(
                    SetOf2DParametersWithVelocity(
                            x = 150f + (100 * i),
                            y = 120f,
                            xSize = 50f,
                            ySize = 50f,
                            rotationAngle = 0f,
                            velocityX = 0f,
                            velocityY = 0f
                    )
            )
        }

        for (i in 0..1) {
            val box = BoundingBox(
                    x = 150f + (100 * i),
                    y = 120f,
                    xSize = 50f,
                    ySize = 50f,
                    rotationAngle = 0f
            ).apply {
                shader = ShaderController.createBoundingBoxShader(renderProjection!!)
            }

            val skeletonObject = AnimatedObject2D(
                    frameSizeX = frameSizeX,
                    frameSizeY = frameSizeY,
                    texture = Texture2D.createInstance(texturePath),
                    animations = characterAnimations
            ).apply {
                boundingBox = box
                shader = ShaderController.createAnimationShader(renderProjection!!)
            }

            Skeletons.graphicalComponents.add(skeletonObject)
        }
    }

    override fun input(window: Window) {
        Character.it?.input(window)
    }

    override fun update(deltaTime: Float) {
        Character.update(deltaTime)
        Skeletons.update(deltaTime)
        Crate.it?.update(deltaTime)
        bbCollisionContext.update()
        tiledCollisionContext.update()

        Campfire.update(deltaTime)
        Map.update(deltaTime)

        accumulated += deltaTime
        if (accumulated >= timeLimit) {
            accumulated = 0f
            if (current + 1 >= lightIntensityCaps.size) {
                current = 0
            } else current++
            Map.graphicalComponent?.shader?.let {
                it.bind()
                it.setUniform("lightIntensityCap", lightIntensityCaps[current])
            }
        }
    }

    override fun render(window: Window) {
        glClear(GL_COLOR_BUFFER_BIT)
        glClearColor(0f, 0.5f, 0f, 0.5f)

        Map.draw()
        Crate.draw()
        Campfire.draw()
        Character.draw()
        Skeletons.draw()
    }
}