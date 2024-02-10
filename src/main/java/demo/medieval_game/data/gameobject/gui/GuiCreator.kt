package demo.medieval_game.data.gameobject.gui

import demo.medieval_game.data.gameobject.gui.bar.HealthBar
import demo.medieval_game.data.gameobject.gui.bar.ManaBar
import demo.medieval_game.scene.MedievalGame
import engine.core.update.SetOfStatic2DParameters
import engine.feature.matrix.MatrixState
import org.joml.Matrix4f

object GuiCreator {

    fun createGuiEntity(
        screenWidth: Float,
        screenHeight: Float,
        matrixState: MatrixState,
        renderProjection: Matrix4f
    ): GuiContainer {

        val containerParams = createParametersForContainer(screenWidth, screenHeight)
        val hpBarParams = createParametersForHpBar(containerParams)
        val manaBarParams = createParametersForManaBar(containerParams)

        val healthBar = createHealthBar(containerParams, hpBarParams)
        val manaBar = createManaBar(containerParams, manaBarParams)
        val guiContainer = GuiContainer(containerParams)

        guiContainer.init(renderProjection)
        guiContainer.addComponent(healthBar, hpBarParams)
        guiContainer.addComponent(manaBar, manaBarParams)
        matrixState.nonTranslatedParams.addAll(
            listOf(containerParams, hpBarParams, manaBarParams)
        )

        return guiContainer
    }

    private fun createParametersForContainer(
        screenWidth: Float,
        screenHeight: Float
    ) = SetOfStatic2DParameters(
        screenWidth / 4,
        screenHeight - ((screenWidth / 2) * 0.191f),
        screenWidth / 2,
        ((screenWidth / 2) * 0.191f),
        0f
    )

    private fun createParametersForHpBar(
        containerParams: SetOfStatic2DParameters
    ) = SetOfStatic2DParameters(
        x = containerParams.x + containerParams.xSize * 0.295f,
        y = containerParams.y + containerParams.ySize * 0.234f,
        xSize = containerParams.xSize * 0.202f,
        ySize = containerParams.ySize * 0.205f,
        rotationAngle = 0f
    )

    private fun createHealthBar(
        containerParams: SetOfStatic2DParameters,
        hpBarParams: SetOfStatic2DParameters
    ) = HealthBar(
        objParams = containerParams,
        barParams = hpBarParams,
        onFilledChange = {},
        projection = MedievalGame.renderProjection,
        texturePath = this.javaClass.getResource("/textures/gui/HealthBarAtlas.png")!!.path,
        isBoundToParams = false
    )

    private fun createParametersForManaBar(
        containerParams: SetOfStatic2DParameters
    ) = SetOfStatic2DParameters(
        x = containerParams.x + containerParams.xSize * 0.509f,
        y = containerParams.y + containerParams.ySize * 0.234f,
        xSize = containerParams.xSize * 0.202f,
        ySize = containerParams.ySize * 0.205f,
        rotationAngle = 0f
    )

    private fun createManaBar(
        containerParams: SetOfStatic2DParameters,
        manaBarParams: SetOfStatic2DParameters
    ) = ManaBar(
        objParams = containerParams,
        barParams = manaBarParams,
        onFilledChange = {},
        projection = MedievalGame.renderProjection,
        texturePath = this.javaClass.getResource("/textures/gui/ManaBarAtlas.png")!!.path,
        isBoundToParams = false
    )
}