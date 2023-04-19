package demo.labyrinth.data

import demo.labyrinth.ShaderController
import demo.labyrinth.data.gameobject.*
import engine.core.entity.CompositeEntity
import engine.core.render.render2D.AnimatedObject2D
import engine.core.texture.Texture2D
import engine.feature.collision.boundingbox.BoundingBoxCollisionContext
import engine.feature.collision.tiled.TiledCollisionContext
import engine.feature.interaction.BoxInteractionContext
import engine.feature.matrix.MatrixComputer
import org.joml.Matrix4f

object LabyrinthInitializer {

    fun initAll(
            renderProjection: Matrix4f,
            screenWidth: Float,
            screenHeight: Float,
            boundingBoxCollisionContext: BoundingBoxCollisionContext,
            tiledCollisionContext: TiledCollisionContext,
            boxInteractionContext: BoxInteractionContext
    ) {
        GameMap.init(renderProjection, screenWidth, screenHeight)
        Character.init(
                renderProjection,
                boundingBoxCollisionContext,
                tiledCollisionContext,
                boxInteractionContext
        )
        initCampfireGraphics(renderProjection)
        initGoblins(
                renderProjection,
                boundingBoxCollisionContext,
                boxInteractionContext
        )
        TempSprites.init(renderProjection)

        MatrixComputer.worldTranslation.x += screenWidth / 2 - characterParameters.x - characterParameters.xSize / 2
        MatrixComputer.worldTranslation.y += screenHeight / 2 - characterParameters.y - characterParameters.ySize / 2
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

    // TODO: reduce
    private fun initGoblins(
            renderProjection: Matrix4f,
            boundingBoxCollisionContext: BoundingBoxCollisionContext,
            boxInteractionContext: BoxInteractionContext
    ) {
        val enemy1 = NpcEnemy(goblinParams1)
                .also { npc ->
                    npc.init(
                            renderProjection,
                            boundingBoxCollisionContext,
                            boxInteractionContext
                    )
                }
        val enemy2 = NpcEnemy(goblinParams2)
                .also { npc ->
                    npc.init(
                            renderProjection,
                            boundingBoxCollisionContext,
                            boxInteractionContext
                    )
                }
        NPCs.it.addAll(listOf(enemy1, enemy2))
    }
}