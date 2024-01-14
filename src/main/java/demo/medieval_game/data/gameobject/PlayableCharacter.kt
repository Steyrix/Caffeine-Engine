package demo.medieval_game.data.gameobject

import demo.medieval_game.Player
import demo.medieval_game.ShaderController
import demo.medieval_game.data.*
import demo.medieval_game.hp.HealthBar
import demo.medieval_game.scene.MedievalGame
import engine.core.entity.Entity
import engine.core.game_object.DynamicGameEntity
import engine.core.render.AnimatedModel2D
import engine.core.texture.Texture2D
import engine.core.update.SetOf2DParametersWithVelocity
import engine.feature.collision.boundingbox.*
import engine.feature.collision.tiled.TiledCollider
import engine.feature.collision.tiled.TiledCollisionContext
import engine.feature.interaction.BoxInteractionContext
import org.joml.Matrix4f

class PlayableCharacter(
    params: SetOf2DParametersWithVelocity
) : DynamicGameEntity<SetOf2DParametersWithVelocity>(params) {

    private var graphicalComponent: AnimatedModel2D? = null
    private var hp: HealthBar? = null
    private var boundingBox: BoundingBox? = null
    private var boxCollider: BoundingBoxCollider? = null
    private var tiledCollider: TiledCollider? = null

    private var projection = Matrix4f()
    private var currentInteractionContext: BoxInteractionContext? = null

    override fun preSpawn(setOfParameters: SetOf2DParametersWithVelocity) {
        // TODO: implement
    }

    fun init(
        bbCollisionContext: BoundingBoxCollisionContext,
        tiledCollisionContext: TiledCollisionContext?,
        boxInteractionContext: BoxInteractionContext
    ) {
        projection = MedievalGame.renderProjection
        boundingBox = getBoundingBox(projection)
        graphicalComponent = getAnimatedObjectComponent(projection)
        val tempSprites = TempSpritesHolder().apply { init(projection) }

        it = Player(
            drawableComponent = graphicalComponent!!,
            params = characterParameters,
            tempSpritesHolder = tempSprites
        )

        hp = HealthBar(characterParameters, hpBarParameters1, projection)

        boxCollider = getBoundingBoxCollider(bbCollisionContext)

        tiledCollisionContext?.let {
            tiledCollider = getTiledCollider(tiledCollisionContext)
            addComponent(tiledCollider, characterParameters)
        }

        addComponent(boundingBox, characterParameters)
        addComponent(hp, characterParameters)
        addComponent(boxCollider, characterParameters)
        addComponent(tempSprites, characterParameters)

        bbCollisionContext.addEntity(boundingBox as Entity, characterParameters)

        currentInteractionContext = boxInteractionContext
        boxInteractionContext.addAgent(it as Entity, boundingBox as BoundingBox)
    }

    private fun getBoundingBox(
        renderProjection: Matrix4f
    ): BoundingBox {
        return BoundingBox(
            xOffset = HUMANOID_BOX_OFFSET,
            xSize = HUMANOID_BOX_SIZE,
            ySize = parameters.ySize,
            isSizeBoundToHolder = false
        ).apply {
            shader = ShaderController.createBoundingBoxShader(renderProjection)
        }
    }

    private fun getAnimatedObjectComponent(
        renderProjection: Matrix4f
    ): AnimatedModel2D {
        val frameSizeX = 0.066f
        val frameSizeY = 0.25f
        val texturePathFirst = this.javaClass.getResource("/textures/KnightAtlas_ver8.png")!!.path

        val textureArray = Texture2D.createInstance(
            texturePathFirst
        )

        return AnimatedModel2D(
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

    // TODO: configure layers via getTiledCollider method's parameters
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
        return parameters.y
    }

    override fun getParams(): SetOf2DParametersWithVelocity {
        return parameters.copy()
    }
}