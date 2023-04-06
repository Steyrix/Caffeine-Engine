package demo.labyrinth.data.gameobject

import demo.labyrinth.ShaderController
import demo.labyrinth.data.*
import demo.labyrinth.goblin.Goblin
import demo.labyrinth.hp.HealthBar
import engine.core.entity.CompositeEntity
import engine.core.entity.Entity
import engine.core.render.render2D.AnimatedObject2D
import engine.core.texture.Texture2D
import engine.core.update.SetOf2DParametersWithVelocity
import engine.core.update.SetOfStatic2DParameters
import engine.feature.collision.boundingbox.BoundingBox
import engine.feature.collision.boundingbox.BoundingBoxCollisionContext
import engine.feature.interaction.BoxInteractionContext
import org.joml.Matrix4f

class NpcEnemy(
        private var parameters: SetOf2DParametersWithVelocity
) : GameObject {
    override var it: CompositeEntity? = null

    override fun update(deltaTime: Float) {
        it?.update(deltaTime)
    }

    override fun draw() {
        it?.draw()
    }

    fun init(
            renderProjection: Matrix4f,
            boundingBoxCollisionContext: BoundingBoxCollisionContext,
            boxInteractionContext: BoxInteractionContext
    ) {
        val box = getBoundingBox(renderProjection)
        val animatedObject = getAnimatedObject(renderProjection)

        it = Goblin(
                params = parameters,
                drawableComponent = animatedObject,
                tileTraverser = GameMap.createTileTraverser(parameters),
                playerParams = characterParameters,
                hp = getHealthBar(parameters, renderProjection)
        ).also {
            it.addComponent(box, parameters)
        }

        boundingBoxCollisionContext.addEntity(box, box.getParameters())
        boxInteractionContext.addAgent(it as Entity, box)
    }

    // TODO: move to parameters file
    private fun getBoundingBox(
            renderProjection: Matrix4f
    ): BoundingBox {
        return BoundingBox(
                xOffset = 14.5f,
                xSize = 35f,
                ySize = 64f,
                rotationAngle = 0f,
                isSizeUpdatable = false
        ).apply {
            shader = ShaderController.createBoundingBoxShader(renderProjection)
        }
    }

    private fun getAnimatedObject(
            renderProjection: Matrix4f
    ): AnimatedObject2D {
        val texturePath = this.javaClass.getResource("/textures/goblin.png")!!.path
        return AnimatedObject2D(
                frameSizeX = 0.09f,
                frameSizeY = 0.2f,
                texture = Texture2D.createInstance(texturePath),
                animations = goblinsAnimations
        ).apply {
            shader = ShaderController.createAnimationShader(renderProjection)
        }
    }

    private fun getHealthBar(
            parameters: SetOf2DParametersWithVelocity,
            renderProjection: Matrix4f
    ): HealthBar {
        return HealthBar(
                parameters,
                SetOfStatic2DParameters(
                        x = 0f,
                        y = 0f,
                        xSize = 50f,
                        ySize = 12.5f,
                        rotationAngle = 0f
                ),
                renderProjection
        )
    }
}