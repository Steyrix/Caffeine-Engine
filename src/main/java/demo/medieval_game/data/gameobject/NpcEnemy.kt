package demo.medieval_game.data.gameobject

import demo.medieval_game.ShaderController
import demo.medieval_game.data.*
import demo.medieval_game.goblin.Goblin
import demo.medieval_game.hp.HealthBar
import engine.core.entity.Entity
import engine.core.render.render2D.AnimatedObject2D
import engine.core.game_object.DynamicGameObject
import engine.core.texture.Texture2D
import engine.core.update.SetOf2DParametersWithVelocity
import engine.core.update.SetOfStatic2DParameters
import engine.feature.collision.boundingbox.BoundingBox
import engine.feature.collision.boundingbox.BoundingBoxCollisionContext
import engine.feature.interaction.BoxInteractionContext
import engine.feature.tiled.traversing.TileTraverser
import org.joml.Matrix4f

class NpcEnemy(
        params: SetOf2DParametersWithVelocity
) : DynamicGameObject<SetOf2DParametersWithVelocity>(params) {

    override fun preSpawn(setOfParameters: SetOf2DParametersWithVelocity) {
    }

    fun init(
            renderProjection: Matrix4f,
            boundingBoxCollisionContext: BoundingBoxCollisionContext,
            boxInteractionContext: BoxInteractionContext,
            tileTraverser: TileTraverser,
            tempSpritesHolder: TempSpritesHolder
    ) {
        val box = getBoundingBox(renderProjection)
        val animatedObject = getAnimatedObject(renderProjection)

        it = Goblin(
                params = parameters,
                drawableComponent = animatedObject,
                tileTraverser = tileTraverser,
                playerParams = characterParameters,
                hp = getHealthBar(parameters, renderProjection),
                tempSpritesHolder = tempSpritesHolder
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
                xOffset = getHumanoidBoxOffset(),
                xSize = getHumanoidBoxSize(),
                ySize = HUMANOID_SIZE_X,
                rotationAngle = 0f,
                isSizeBoundToHolder = false
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
                animations = goblinsAnimations.map { it.copy() }
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

    override fun getZLevel(): Float {
        if (it?.isDisposed == true) {
            return 0.1f
        }

        return parameters.y
    }

    override fun getParams(): SetOf2DParametersWithVelocity {
        return parameters.copy()
    }
}