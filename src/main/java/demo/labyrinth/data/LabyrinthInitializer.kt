package demo.labyrinth.data

import demo.labyrinth.hp.HealthBar
import demo.labyrinth.ShaderController
import demo.labyrinth.goblin.Goblin
import demo.labyrinth.data.gameobject.*
import engine.core.entity.CompositeEntity
import engine.core.entity.Entity
import engine.core.render.render2D.AnimatedObject2D
import engine.core.shader.Shader
import engine.core.shader.ShaderLoader
import engine.core.texture.Texture2D
import engine.feature.collision.boundingbox.BoundingBox
import engine.feature.collision.boundingbox.BoundingBoxCollisionContext
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
        // initCrateGraphics(renderProjection)
        GameMap.init(renderProjection, screenWidth, screenHeight)
        Character.init(renderProjection, boundingBoxCollisionContext, tiledCollisionContext)
        initCampfireGraphics(renderProjection)
        initGoblins(renderProjection)
        initPhysics(boundingBoxCollisionContext, tiledCollisionContext)
    }

    private fun initPhysics(
            bbCollisionContext: BoundingBoxCollisionContext,
            tiledCollisionContext: TiledCollisionContext
    ) {
        Character.addComponent(Character.tiledCollider, characterParameters)
        tiledCollisionContext.addEntity(GameMap.graphicalComponent as Entity, GameMap.parameters)

        Character.addComponent(Character.boxCollider, characterParameters)
        bbCollisionContext.addEntity(Character.boundingBox as Entity, characterParameters)
    }

//    private fun initCrateGraphics(renderProjection: Matrix4f) {
//        Crate.boundingBox = BoundingBox(
//                x = 400f,
//                y = 150f,
//                xSize = 70f,
//                ySize = 70f
//        ).apply {
//            shader = ShaderController.createBoundingBoxShader(renderProjection)
//        }
//
//        val texturePath = this.javaClass.getResource("/textures/obj_crate.png")!!.path
//        Crate.graphicalComponent = OpenGlObject2D(
//                texture2D = Texture2D.createInstance(texturePath),
//        ).apply {
//            shader = ShaderController.createTexturedShader(renderProjection)
//        }
//
//        Crate.it = object : CompositeEntity() {}
//
//        Crate.hp = HealthBar(crateParameters, hpBarPatameters2, renderProjection)
//
//        Crate.addComponent(Crate.graphicalComponent, crateParameters)
//        Crate.addComponent(Crate.hp, crateParameters)
//        Crate.addComponent(Crate.boundingBox, crateParameters)
//    }

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

    private fun initGoblins(renderProjection: Matrix4f) {
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
                            tileTraverser = GameMap.createTileTraverser(goblinParameters[i]),
                            playerParams = characterParameters
                    ).also {
                        it.addComponent(box, goblinParameters[i])
                    }
            )

            val hp = HealthBar(goblinParameters[i], hpBarPatameters1, renderProjection)

            Goblins.it[i].addComponent(
                    hp, goblinParameters[i]
            )
        }
    }
}