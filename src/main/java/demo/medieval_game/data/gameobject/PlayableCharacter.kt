package demo.medieval_game.data.gameobject

import demo.medieval_game.Player
import demo.medieval_game.ShaderController
import demo.medieval_game.SimpleController2D
import demo.medieval_game.data.*
import demo.medieval_game.data.static_parameters.*
import demo.medieval_game.hp.HealthBar
import demo.medieval_game.scene.MedievalGame
import engine.core.controllable.Direction
import engine.core.entity.Entity
import engine.core.game_object.DynamicGameEntity
import engine.core.geometry.Point2D
import engine.core.render.AnimatedModel2D
import engine.core.texture.Texture2D
import engine.core.update.SetOf2DParametersWithVelocity
import engine.feature.collision.boundingbox.*
import engine.feature.collision.tiled.TiledCollider
import engine.feature.collision.tiled.TiledCollisionContext
import engine.feature.interaction.BoxInteractionContext
import engine.feature.interaction.InteractionContext
import org.joml.Matrix4f

class PlayableCharacter(
    private val parameters: SetOf2DParametersWithVelocity
) : DynamicGameEntity<SetOf2DParametersWithVelocity>() {

    private var graphicalComponent: AnimatedModel2D? = null
    private var hp: HealthBar? = null
    private var boundingBox: BoundingBox? = null
    private var boxCollider: BoundingBoxCollider? = null
    private var tiledCollider: TiledCollider? = null

    private var projection = Matrix4f()
    private var currentInteractionContext: BoxInteractionContext? = null

    private var controller: SimpleController2D? = null

    override fun preSpawn(position: Point2D) {
        parameters.x = position.x
        parameters.y = position.y
    }

    fun init() {
        projection = MedievalGame.renderProjection
        boundingBox = getBoundingBox(projection)
        graphicalComponent = getAnimatedObjectComponent(projection)
        val tempSprites = TempSpritesHolder().apply { init(projection) }

        it = Player(
            drawableComponent = graphicalComponent!!,
            parameters = parameters,
            tempSpritesHolder = tempSprites
        )

        controller = SimpleController2D(
            graphicalComponent!!,
            parameters,
            absVelocityY = 10f,
            absVelocityX = 10f,
            modifier = 20f,
            isControlledByUser = true,
            onStrikingChange = { value ->
                (it as Player).isStriking = value
            },
            onChestInteraction = {
                (it as Player).isInteractingWithChest = true
            }
        )

        hp = HealthBar(characterParameters, defaultHpBarParams, projection)

        addComponent(boundingBox, characterParameters)
        addComponent(hp, characterParameters)
        addComponent(boxCollider, characterParameters)
        addComponent(tempSprites, characterParameters)
        addComponent(controller, characterParameters)
    }

    private fun getAnimatedObjectComponent(
        renderProjection: Matrix4f
    ): AnimatedModel2D {
        val frameWidth = 0.066f
        val frameHeight = 0.25f
        val path = this.javaClass.getResource("/textures/KnightAtlas.png")!!.path

        val textureArray = Texture2D.createInstance(
            path
        )

        return AnimatedModel2D(
            frameWidth,
            frameHeight,
            texture = textureArray,
            animations = characterAnimations
        ).apply {
            shader = ShaderController.createAnimationShader(renderProjection)
        }
    }

    fun <T> addToInteractionContext(interactionContext: InteractionContext<T>) {
        if (interactionContext is BoxInteractionContext) {
            currentInteractionContext = interactionContext
            currentInteractionContext?.addAgent(it as Entity, boundingBox as BoundingBox)
        }
    }

    private fun getBoundingBox(
        renderProjection: Matrix4f
    ): BoundingBox {
        return BoundingBox(
            xOffset = 10f,
            xSize = characterParameters.xSize - 20f,
            yOffset = 30f,
            ySize = characterParameters.ySize * 0.71f,
            isSizeBoundToHolder = false,
        ).apply {
            shader = ShaderController.createBoundingBoxShader(renderProjection)
        }
    }

    private fun getBoundingBoxCollider(
        collisionContext: BoundingBoxCollisionContext
    ): BoundingBoxCollider {
        return BoundingBoxCollider(
            it as Entity,
            boundingBox!!,
            characterParameters,
            collisionContext,
            emptyBehavior
        )
    }

    fun addBoundingBoxCollider(
        collisionContext: BoundingBoxCollisionContext
    ) {
        updateBoundingBox()

        if (boxCollider != null) {
            it?.removeComponent(boxCollider as Entity)
        }

        boxCollider = getBoundingBoxCollider(collisionContext)
        addComponent(boxCollider, characterParameters)

        boxCollider?.let {
            collisionContext.addCollider(it)
        }

        boundingBox?.let {
            collisionContext.addEntity(it, characterParameters)
        }
    }

    // TODO: configure layers via getTiledCollider method's parameters
    private fun getTiledCollider(
        tiledCollisionContext: TiledCollisionContext
    ): TiledCollider {
        return TiledCollider(
            it as Entity,
            characterParameters,
            tiledCollisionContext
        )
    }

    fun addTiledCollider(
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

    private fun updateBoundingBox() {
        it?.removeComponent(boundingBox!!)
        currentInteractionContext?.removeAgent(boundingBox!!)

        boundingBox = getBoundingBox(projection)
        it?.addComponent(boundingBox as Entity, characterParameters)
        currentInteractionContext?.addAgent(it as Entity, boundingBox as BoundingBox)
    }

    fun isOutOfMap(): Boolean {
        return tiledCollider?.isOutOfMap ?: false
    }

    fun getDirection() = controller?.direction ?: Direction.RIGHT

    override fun getZLevel(): Float {
        return parameters.y + parameters.ySize * 0.75f
    }

    override fun getParams(): SetOf2DParametersWithVelocity {
        return parameters.copy()
    }
}