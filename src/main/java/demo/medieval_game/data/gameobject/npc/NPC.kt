package demo.medieval_game.data.gameobject.npc

import demo.medieval_game.ShaderController
import engine.core.entity.CompositeEntity
import engine.core.entity.Entity
import engine.core.game_object.DynamicGameObject
import engine.core.render.render2D.AnimatedObject2D
import engine.core.texture.Texture2D
import engine.core.update.SetOf2DParametersWithVelocity
import engine.feature.collision.boundingbox.BoundingBox
import engine.feature.collision.boundingbox.BoundingBoxCollisionContext
import engine.feature.interaction.BoxInteractionContext
import org.joml.Matrix4f

abstract class NPC<E : CompositeEntity>(
        params: SetOf2DParametersWithVelocity
) : DynamicGameObject<SetOf2DParametersWithVelocity>(params) {

    override fun preSpawn(setOfParameters: SetOf2DParametersWithVelocity) {
    }

    fun init(
            renderProjection: Matrix4f,
            boundingBoxCollisionContext: BoundingBoxCollisionContext,
            boxInteractionContext: BoxInteractionContext,
            preset: NpcPreset
    ) {
        val box = getBoundingBox(
                renderProjection,
                preset.box
        )

        val animatedObject = getAnimatedObject(
                renderProjection,
                preset.animation
        )

        it = initEntity(
                renderProjection,
                animatedObject
        )

        boundingBoxCollisionContext.addEntity(box, box.getParameters())
        boxInteractionContext.addAgent(it as Entity, box)
    }

    private fun getBoundingBox(
            renderProjection: Matrix4f,
            preset: NpcBoxPreset
    ): BoundingBox {
        return BoundingBox(
                preset.initialParams,
                preset.isSizeBoundToHolder
        ).apply {
            shader = ShaderController.createBoundingBoxShader(renderProjection)
        }
    }

    private fun getAnimatedObject(
            renderProjection: Matrix4f,
            preset: AnimationPreset
    ): AnimatedObject2D {
        return AnimatedObject2D(
                frameSizeX = preset.frameSizeX,
                frameSizeY = preset.frameSizeY,
                texture = Texture2D.createInstance(preset.atlasTexturePath),
                animations = preset.animations
        ).apply {
            shader = ShaderController.createAnimationShader(renderProjection)
        }
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

    abstract fun initEntity(
            renderProjection: Matrix4f,
            animatedObject2D: AnimatedObject2D
    ): E
}