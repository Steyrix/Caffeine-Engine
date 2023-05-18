package demo.medieval_game.data.gameobject

import demo.medieval_game.Player
import demo.medieval_game.ShaderController
import demo.medieval_game.data.*
import demo.medieval_game.hp.HealthBar
import engine.core.entity.CompositeEntity
import engine.core.entity.Entity
import engine.core.render.render2D.AnimatedObject2D
import engine.core.scene.game_object.GameObject
import engine.core.scene.game_object.SingleGameObject
import engine.core.texture.Texture2D
import engine.feature.collision.boundingbox.*
import engine.feature.collision.tiled.TiledCollider
import engine.feature.collision.tiled.TiledCollisionContext
import engine.feature.interaction.BoxInteractionContext
import org.joml.Matrix4f

class PlayableCharacter : SingleGameObject() {

    private var graphicalComponent: AnimatedObject2D? = null
    private var hp: HealthBar? = null
    private var boundingBox: BoundingBox? = null
    private var boxCollider: BoundingBoxCollider? = null
    private var tiledCollider: TiledCollider? = null

    private var projection = Matrix4f()
    private var currentInteractionContext: BoxInteractionContext? = null

    fun init(
            renderProjection: Matrix4f,
            bbCollisionContext: BoundingBoxCollisionContext,
            tiledCollisionContext: TiledCollisionContext?,
            boxInteractionContext: BoxInteractionContext,
            tempSpritesHolder: TempSpritesHolder
    ) {
        projection = renderProjection
        boundingBox = getBoundingBox(renderProjection)
        graphicalComponent = getAnimatedObjectComponent(renderProjection)

        it = Player(
                drawableComponent = graphicalComponent!!,
                params = characterParameters,
                tempSpritesHolder = tempSpritesHolder
        )

        hp = HealthBar(characterParameters, hpBarPatameters1, renderProjection)

        boxCollider = getBoundingBoxCollider(bbCollisionContext)

        tiledCollisionContext?.let {
            tiledCollider = getTiledCollider(tiledCollisionContext)
            addComponent(tiledCollider, characterParameters)
        }

        addComponent(boundingBox, characterParameters)
        addComponent(hp, characterParameters)
        addComponent(boxCollider, characterParameters)

        bbCollisionContext.addEntity(boundingBox as Entity, characterParameters)

        currentInteractionContext = boxInteractionContext
        boxInteractionContext.addAgent(it as Entity, boundingBox as BoundingBox)
    }

    private fun getBoundingBox(
            renderProjection: Matrix4f
    ): BoundingBox {
        return BoundingBox(
                xOffset = getHumanoidBoxOffset(),
                xSize = getHumanoidBoxSize(),
                ySize = characterParameters.ySize,
                isSizeBoundToHolder = false
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

    // TODO update obstacles bg
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

    fun updateCollisionContext(
            collisionContext: TiledCollisionContext
    ) {
        if (tiledCollider != null) {
            it?.removeComponent(tiledCollider as Entity)
        }

        tiledCollider = getTiledCollider(collisionContext)

        addComponent(tiledCollider, characterParameters)

        tiledCollider?.let {
            collisionContext.addCollider(it)
        }
    }

    fun updateBoundingBox() {
        it?.removeComponent(boundingBox!!)
        currentInteractionContext?.removeAgent(boundingBox!!)

        boundingBox = getBoundingBox(projection)
        it?.addComponent(boundingBox as Entity, characterParameters)
        currentInteractionContext?.addAgent(it as Entity, boundingBox as BoundingBox)
    }

    fun isOutOfMap(): Boolean {
        return tiledCollider?.isOutOfMap ?: false
    }

    fun getDirection() = (it as Player).getDirection()

    override fun getZLevel(): Float {
        return characterParameters.y
    }
}
