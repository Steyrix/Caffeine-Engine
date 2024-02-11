package demo.medieval_game.data.gameobject.npc

import demo.medieval_game.ShaderController
import demo.medieval_game.data.gameobject.gui.bar.HealthBar
import engine.core.entity.CompositeEntity
import engine.core.entity.Entity
import engine.core.game_object.DynamicGameEntity
import engine.core.geometry.Point2D
import engine.core.render.AnimatedModel2D
import engine.core.texture.Texture2D
import engine.core.update.SetOf2DParametersWithVelocity
import engine.core.update.SetOfStatic2DParameters
import engine.feature.collision.boundingbox.BoundingBox
import engine.feature.collision.boundingbox.BoundingBoxCollisionContext
import engine.feature.interaction.BoxInteractionContext
import org.joml.Matrix4f

/*
    NPC class is designed to allow easy creation of npc-relate components like hp bar,
    collider, bounding box etc. It also overrides DynamicGameEntity and can be used
    to define shared pre-spawn logic.
 */
abstract class NPC<E : CompositeEntity>(
    private val parameters: SetOf2DParametersWithVelocity
) : DynamicGameEntity<SetOf2DParametersWithVelocity>() {

    override fun preSpawn(position: Point2D) {
    }

    fun init(
        renderProjection: Matrix4f,
        boundingBoxCollisionContext: BoundingBoxCollisionContext,
        boxInteractionContext: BoxInteractionContext,
        preset: Preset
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
        ).addComponent(box, parameters)

        boundingBoxCollisionContext.addEntity(box, box.getParameters())
        boxInteractionContext.addAgent(it as Entity, box)
    }

    private fun getBoundingBox(
        renderProjection: Matrix4f,
        preset: BoundingBoxPreset
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
    ): AnimatedModel2D {
        return AnimatedModel2D(
            frameWidth = preset.frameWidth,
            frameHeight = preset.frameHeight,
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

        return parameters.y + parameters.ySize
    }

    override fun getParams(): SetOf2DParametersWithVelocity {
        return parameters.copy()
    }

    protected abstract fun initEntity(
        renderProjection: Matrix4f,
        animatedModel2D: AnimatedModel2D
    ): E

    protected fun getHpBar(
        renderProjection: Matrix4f,
        onEmpty: () -> Unit
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
            renderProjection,
            texturePath = this.javaClass.getResource("/textures/gui/HealthBarAtlas.png")!!.path,
            onFilledChange = { hpAmount ->
                if (hpAmount <= 0) {
                    onEmpty.invoke()
                }
            },
            isPartOfWorldTranslation = true
        )
    }
}