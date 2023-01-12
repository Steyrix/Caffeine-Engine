package demo.labyrinth.hp

import demo.labyrinth.ShaderController
import engine.core.entity.CompositeEntity
import engine.core.render.render2D.AnimatedObject2D
import engine.core.render.render2D.OpenGlObject2D
import engine.core.texture.Texture2D
import engine.core.update.SetOf2DParametersWithVelocity
import engine.core.update.SetOfStatic2DParameters
import engine.feature.animation.Animation
import engine.feature.animation.FrameParameters
import engine.feature.animation.SequenceAtlasAnimation
import engine.feature.util.Buffer
import org.joml.Matrix4f

class HealthBar(
        private val characterParams: SetOf2DParametersWithVelocity,
        private val barParams: SetOfStatic2DParameters,
        private val projection: Matrix4f
) : CompositeEntity() {

    private val fullBar: OpenGlObject2D
    private val emptyBar: AnimatedObject2D

    private val barAnims = mutableListOf<Animation>(
            SequenceAtlasAnimation(
                    name = "FULL",
                    frames = listOf(
                            FrameParameters(
                                xOffset = 0f,
                                yOffset = 0f,
                                frameNumberX = 0,
                                frameNumberY = 0
                            ),
                            FrameParameters(
                                    xOffset = 0.5f,
                                    yOffset = 0.5f,
                                    frameNumberX = 0,
                                    frameNumberY = 0
                            )
                    ),
                    timeLimit = 5f
            )
    )

    init {
        val textureFilePath = this.javaClass.getResource("/textures/healthbar_atlas.png")!!.path
        fullBar = OpenGlObject2D(
                texture2D = Texture2D.createInstance(textureFilePath),
                uv = Buffer.getRectangleSectorVertices(
                        sectorWidth = 1f,
                        sectorHeight = 0.5f
                )
        ).apply {
            shader = ShaderController.createTexturedShader(projection)
        }

        emptyBar = AnimatedObject2D(
                frameSizeX = 1f,
                frameSizeY = 0.5f,
                texture = Texture2D.createInstance(textureFilePath),
                animations = barAnims
        ).apply {
            shader = ShaderController.createAnimationShader(projection)
        }

        addComponent(fullBar, barParams)
        addComponent(emptyBar, barParams)
    }
    override fun update(deltaTime: Float) {
        super.update(deltaTime)
        with(characterParams) {
            barParams.x = this.x - ((barParams.xSize - this.xSize) / 2)
            barParams.y = this.y - 2 - barParams.ySize
        }
    }
}