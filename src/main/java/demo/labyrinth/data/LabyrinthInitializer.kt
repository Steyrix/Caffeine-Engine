package demo.labyrinth.data

import demo.labyrinth.hp.HealthBar
import demo.labyrinth.Player
import demo.labyrinth.ShaderController
import demo.labyrinth.goblin.Goblin
import engine.core.entity.CompositeEntity
import engine.core.entity.Entity
import engine.core.render.render2D.AnimatedObject2D
import engine.core.render.render2D.OpenGlObject2D
import engine.core.shader.Shader
import engine.core.shader.ShaderLoader
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
        initCrateGraphics(renderProjection)
        initCharacterGraphics(renderProjection, boundingBoxCollisionContext, tiledCollisionContext)
        initCampfireGraphics(renderProjection)
        initTileMapGraphics(renderProjection, screenWidth, screenHeight)
        initGoblins(renderProjection, boundingBoxCollisionContext)
        initPhysics(boundingBoxCollisionContext, tiledCollisionContext)
    }

    private fun initPhysics(
            bbCollisionContext: BoundingBoxCollisionContext,
            tiledCollisionContext: TiledCollisionContext
    ) {

        Character.addComponent(Character.boxCollider, characterParameters)
        Character.addComponent(Character.tiledCollider, characterParameters)

        for (i in 0..1) {
            Goblins.it[i].addComponent(
                    Goblins.boxColliders[i],
                    goblinParameters[i]
            )

            bbCollisionContext.addEntity(Goblins.boundingBoxes[i] as Entity)
        }

        bbCollisionContext.addEntity(Character.boundingBox as Entity)
        tiledCollisionContext.addEntity(GameMap.graphicalComponent as Entity)
    }

    private fun initTileMapGraphics(
            renderProjection: Matrix4f,
            screenWidth: Float,
            screenHeight: Float
    ) {
        GameMap.parameters = getMapParameters(screenWidth, screenHeight)

        val vertexShaderPath = this.javaClass.getResource("/shaders/lightingShaders/lightingVertexShader.glsl")!!.path
        val fragmentShaderPath = this.javaClass.getResource("/shaders/lightingShaders/lightingFragmentShader.glsl")!!.path

        GameMap.graphicalComponent = TiledResourceParser.createTileMapFromXml(
                File(this.javaClass.getResource("/tiled/port_map.xml")!!.path)
        )
        GameMap.graphicalComponent?.shader = ShaderLoader.loadFromFile(
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

        GameMap.parameters.xSize = screenWidth / GameMap.graphicalComponent!!.relativeMapWidth
        GameMap.parameters.ySize = screenHeight / GameMap.graphicalComponent!!.relativeMapHeight

        GameMap.it = object  : CompositeEntity() {}
        GameMap.addComponent(GameMap.graphicalComponent, GameMap.parameters)
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

        val frameSizeX = 0.111f
        val frameSizeY = 0.25f
        val texturePathFirst = this.javaClass.getResource("/textures/character_front_walk.png")!!.path

        val textureArray = Texture2D.createInstance(
                texturePathFirst
        )

        Character.graphicalComponent = AnimatedObject2D(
                frameSizeX,
                frameSizeY,
                texture = textureArray,
                animations = characterAnimations
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
        Character.hp = HealthBar(characterParameters, hpBarPatameters1, renderProjection)

        Character.boxCollider =
                BoundingBoxCollider(
                        Character.it as Entity,
                        Character.boundingBox!!,
                        characterParameters,
                        bbCollisionContext
                )

        Character.tiledCollider =
                TiledCollider(
                        Character.it as Entity,
                        characterParameters,
                        listOf("walking_layer", "walkable_objects_layer"),
                        tiledCollisionContext)

        Character.addComponent(Character.boundingBox, characterParameters)
        Character.addComponent(Character.hp, characterParameters)
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

        Crate.hp = HealthBar(crateParameters, hpBarPatameters2, renderProjection)

        Crate.addComponent(Crate.graphicalComponent, crateParameters)
        Crate.addComponent(Crate.hp, crateParameters)
        Crate.addComponent(Crate.boundingBox, crateParameters)
    }

    private fun initCampfireGraphics(renderProjection: Matrix4f) {
        val frameSizeX = 0.2f
        val frameSizeY = 1.0f

        val texturePath = this.javaClass.getResource("/textures/camp_fire_texture.png")!!.path
        Campfire.graphicalComponent = AnimatedObject2D(
                frameSizeX = frameSizeX,
                frameSizeY = frameSizeY,
                texture = Texture2D.createInstance(texturePath),
                animations = campfireAnimations
        ).apply {
            shader = ShaderController.createAnimationShader(renderProjection)
        }

        Campfire.it = object : CompositeEntity() {}
        Campfire.addComponent(Campfire.graphicalComponent, campfireParameters)
    }

    private fun initGoblins(
            renderProjection: Matrix4f,
            bbCollisionContext: BoundingBoxCollisionContext
    ) {
        val frameSizeX = 0.09f
        val frameSizeY = 0.2f
        val texturePath = this.javaClass.getResource("/textures/goblin.png")!!.path

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

            Goblins.boundingBoxes.add(box)

            val animatedObject = AnimatedObject2D(
                    frameSizeX = frameSizeX,
                    frameSizeY = frameSizeY,
                    texture = Texture2D.createInstance(texturePath),
                    animations = goblinsAnimations
            ).apply {
                shader = ShaderController.createAnimationShader(renderProjection)
            }

            Goblins.it.add(
                    Goblin(
                            params = goblinParameters[i],
                            drawableComponent = animatedObject,
                            tileTraverser = createTileTraverser(goblinParameters[i]),
                            playerParams = characterParameters
                    ).also {
                        it.addComponent(box, goblinParameters[i])
                    }
            )

            Goblins.boxColliders.add(
                    BoundingBoxCollider(
                            Goblins.it[i] as Entity,
                            box,
                            goblinParameters[i],
                            bbCollisionContext)
            )
        }
    }
//    private fun characterOnCollision(box: BoundingBox) {
//        if (box == Crate.boundingBox) {
//            val player = Character.it as Player
//        }
//    }
}