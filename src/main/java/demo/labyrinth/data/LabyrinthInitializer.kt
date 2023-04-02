package demo.labyrinth.data

import demo.labyrinth.ShaderController
import demo.labyrinth.data.gameobject.*
import engine.core.entity.CompositeEntity
import engine.core.entity.Entity
import engine.core.render.render2D.AnimatedObject2D
import engine.core.texture.Texture2D
import engine.feature.collision.boundingbox.BoundingBoxCollisionContext
import engine.feature.collision.tiled.TiledCollisionContext
import org.joml.Matrix4f

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
        val enemy1 = NpcEnemy(goblinParams1).also { npc -> npc.init(renderProjection) }
        val enemy2 = NpcEnemy(goblinParams2).also { npc -> npc.init(renderProjection) }
        NPCs.it.addAll(listOf(enemy1, enemy2))
    }
}