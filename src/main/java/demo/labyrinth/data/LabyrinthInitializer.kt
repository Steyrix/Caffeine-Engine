package demo.labyrinth.data

import demo.labyrinth.Player
import demo.labyrinth.ShaderController
import demo.labyrinth.skeleton.Skeleton
import engine.core.entity.CompositeEntity
import engine.core.entity.Entity
import engine.core.render.render2D.AnimatedObject2D
import engine.core.render.render2D.OpenGlObject2D
import engine.core.shader.Shader
import engine.core.shader.ShaderLoader
import engine.core.texture.ArrayTexture2D
import engine.core.texture.Texture2D
import engine.feature.collision.boundingbox.BoundingBox
import engine.feature.collision.boundingbox.BoundingBoxCollider
import engine.feature.collision.boundingbox.BoundingBoxCollisionContext
import engine.feature.collision.tiled.TiledCollider
import engine.feature.collision.tiled.TiledCollisionContext
import engine.feature.tiled.parser.TiledResourceParser
import org.joml.Matrix4f
import org.joml.Vector2f
import java.io.File

object LabyrinthInitializer {

    fun initAll(
            renderProjection: Matrix4f,
            screenWidth: Float,
            screenHeight: Float,
            boundingBoxCollisionContext: BoundingBoxCollisionContext,
            tiledCollisionContext: TiledCollisionContext
    ) {
        initCharacterGraphics(renderProjection, boundingBoxCollisionContext, tiledCollisionContext)
        initCrateGraphics(renderProjection)
        initCampfireGraphics(renderProjection)
        initTileMapGraphics(renderProjection, screenWidth, screenHeight)
        initSkeletons(renderProjection)
        initPhysics(boundingBoxCollisionContext, tiledCollisionContext)
    }

    private fun initPhysics(
            bbCollisionContext: BoundingBoxCollisionContext,
            tiledCollisionContext: TiledCollisionContext
    ) {
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

    private fun initTileMapGraphics(
            renderProjection: Matrix4f,
            screenWidth: Float,
            screenHeight: Float
    ) {
        Map.parameters = getMapParameters(screenWidth, screenHeight)

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

        Map.it = object  : CompositeEntity() {}
        Map.it?.addComponent(
                component = Map.graphicalComponent as Entity,
                parameters = Map.parameters
        )
    }

    private fun initCharacterGraphics(
            renderProjection: Matrix4f,
            bbCollisionContext: BoundingBoxCollisionContext,
            tiledCollisionContext: TiledCollisionContext
    ) {

        Character.boundingBox = BoundingBox(
                x = 100f,
                y = 100f,
                xSize = 60f,
                ySize = 60f
        ).apply {
            shader = ShaderController.createBoundingBoxShader(renderProjection)
        }

        Character.boxCollider =
                BoundingBoxCollider(Character.boundingBox!!, characterParameters, bbCollisionContext)
        Character.tiledCollider =
                TiledCollider(characterParameters, "Walking Layer", tiledCollisionContext)

        val frameSizeX = 0.1f
        val frameSizeY = 0.166f
        val texturePathFirst = this.javaClass.getResource("/textures/base_character.png")!!.path

        val textureArray = Texture2D.createInstance(
                texturePathFirst
        )

        Character.graphicalComponent = AnimatedObject2D(
                frameSizeX,
                frameSizeY,
                texture = textureArray,
                animations = characterAnimations2
        ).apply {
            x = 100f
            y = 100f
            xSize = 60f
            ySize = 60f
            shader = ShaderController.createAnimationShader(renderProjection!!)
        }

        Character.it = Player(
                drawableComponent = Character.graphicalComponent!!,
                params = characterParameters
        )

        Character.boundingBox?.let { box ->
            Character.it?.addComponent(box, characterParameters)
        }
    }

    private fun initCrateGraphics(renderProjection: Matrix4f) {
        Crate.boundingBox = BoundingBox(
                x = 400f,
                y = 150f,
                xSize = 70f,
                ySize = 70f
        ).apply {
            shader = ShaderController.createBoundingBoxShader(renderProjection)
        }

        val texturePath = this.javaClass.getResource("/textures/obj_crate.png")!!.path
        Crate.graphicalComponent = OpenGlObject2D(
                texture2D = Texture2D.createInstance(texturePath),
        ).apply {
            shader = ShaderController.createTexturedShader(renderProjection)
        }

        Crate.it = object : CompositeEntity() {}

        Crate.it?.addComponent(
                component = Crate.graphicalComponent as Entity,
                parameters = crateParameters
        )

        Crate.boundingBox?.let { box ->
            Crate.it?.addComponent(box, crateParameters)
        }
    }

    private fun initCampfireGraphics(renderProjection: Matrix4f) {
        val frameSizeX = 0.2f
        val frameSizeY = 1.0f

        val texturePath = this.javaClass.getResource("/textures/camp_fire_texture.png")!!.path
        Campfire.graphicalComponent = AnimatedObject2D(
                frameSizeX = frameSizeX,
                frameSizeY = frameSizeY,
                texture = Texture2D.createInstance(texturePath),
                animations = campfireAnimations2
        ).apply {
            shader = ShaderController.createAnimationShader(renderProjection)
        }

        Campfire.it = object : CompositeEntity() {}
        Campfire.it?.addComponent(
                component = Campfire.graphicalComponent as Entity,
                parameters = campfireParameters
        )
    }

    private fun initSkeletons(renderProjection: Matrix4f) {
        val frameSizeX = 0.1f
        val frameSizeY = 0.1f
        val texturePath = this.javaClass.getResource("/textures/base_skeleton.png")!!.path

        for (i in 0..1) {
            val box = BoundingBox(
                    x = 150f + (100 * i),
                    y = 120f,
                    xSize = 50f,
                    ySize = 50f,
                    rotationAngle = 0f
            ).apply {
                shader = ShaderController.createBoundingBoxShader(renderProjection)
            }

            val skeletonObject = AnimatedObject2D(
                    frameSizeX = frameSizeX,
                    frameSizeY = frameSizeY,
                    texture = Texture2D.createInstance(texturePath),
                    animations = characterAnimations2
            ).apply {
                shader = ShaderController.createAnimationShader(renderProjection)
            }

            Skeletons.it.add(
                    Skeleton(
                            behavior = skeletonBehaviors[i],
                            params = skeletonParameters[i],
                            drawableComponent = skeletonObject
                    )
            )
        }
    }
}