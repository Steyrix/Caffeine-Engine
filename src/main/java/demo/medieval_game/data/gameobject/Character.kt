package demo.medieval_game.data.gameobject

import demo.medieval_game.Player
import demo.medieval_game.ShaderController
import demo.medieval_game.data.*
import demo.medieval_game.hp.HealthBar
import engine.core.entity.CompositeEntity
import engine.core.entity.Entity
import engine.core.render.render2D.AnimatedObject2D
import engine.core.scene.GameObject
import engine.core.texture.Texture2D
import engine.feature.collision.boundingbox.*
import engine.feature.collision.tiled.TiledCollider
import engine.feature.collision.tiled.TiledCollisionContext
import engine.feature.interaction.BoxInteractionContext
import org.joml.Matrix4f

class Character : GameObject {

    override var it: CompositeEntity? = null

    private var graphicalComponent: AnimatedObject2D? = null
    private var hp: HealthBar? = null
    private var boundingBox: BoundingBox? = null
    private var boxCollider: BoundingBoxCollider? = null
    private var tiledCollider: TiledCollider? = null

    fun init(
            renderProjection: Matrix4f,
            bbCollisionContext: BoundingBoxCollisionContext,
            tiledCollisionContext: TiledCollisionContext,
            boxInteractionContext: BoxInteractionContext,
            tempSpritesHolder: TempSpritesHolder
    ) {
        boundingBox = getBoundingBox(renderProjection)
        graphicalComponent = getAnimatedObjectComponent(renderProjection)

        it = Player(
                drawableComponent = graphicalComponent!!,
                params = characterParameters,
                tempSpritesHolder = tempSpritesHolder
        )

        hp = HealthBar(characterParameters, hpBarPatameters1, renderProjection)

        boxCollider = getBoundingBoxCollider(bbCollisionContext)
        tiledCollider = getTiledCollider(tiledCollisionContext)

        addComponent(boundingBox, characterParameters)
        addComponent(hp, characterParameters)

        addComponent(tiledCollider, characterParameters)
        addComponent(boxCollider, characterParameters)
        bbCollisionContext.addEntity(boundingBox as Entity, characterParameters)

        boxInteractionContext.addAgent(it as Entity, boundingBox as BoundingBox)
    }

    private fun getBoundingBox(
            renderProjection: Matrix4f
    ): BoundingBox {
        return BoundingBox(
                xOffset = getHumanoidBoxOffset(),
                xSize = getHumanoidBoxSize(),
                ySize = characterParameters.ySize,
                isSizeUpdatable = false
        ).apply {
            shader = ShaderController.createBoundingBoxShader(renderProjection)
        }
    }

    private fun getAnimatedObjectComponent(
            renderProjection: Matrix4f
    ): AnimatedObject2D {
        val frameSizeX = 0.066f
        val frameSizeY = 0.25f
        val texturePathFirst = this.javaClass.getResource("/textures/character_atlas.png")!!.path

        val textureArray = Texture2D.createInstance(
                texturePathFirst
        )

        return AnimatedObject2D(
                frameSizeX,
                frameSizeY,
                texture = textureArray,
                animations = characterAnimations
        ).apply {
            x = 100f
            y = 100f
            xSize = 60f
            ySize = 60f
            shader = ShaderController.createAnimationShader(renderProjection)
        }
    }

    private fun getBoundingBoxCollider(
            bbCollisionContext: BoundingBoxCollisionContext
    ): BoundingBoxCollider {
        return BoundingBoxCollider(
                it as Entity,
                boundingBox!!,
                characterParameters,
                bbCollisionContext,
                emptyBehavior
        )
    }

    private fun getTiledCollider(
            tiledCollisionContext: TiledCollisionContext
    ): TiledCollider {
        return TiledCollider(
                it as Entity,
                characterParameters,
                listOf("walking_layer", "walkable_objects_layer"),
                tiledCollisionContext
        )
    }

    override fun getZLevel(): Float {
        return characterParameters.y
    }
}
