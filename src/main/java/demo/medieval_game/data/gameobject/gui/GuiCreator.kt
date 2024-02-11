package demo.medieval_game.data.gameobject.gui

import demo.medieval_game.data.gameobject.gui.bar.HealthBar
import demo.medieval_game.data.gameobject.gui.bar.ManaBar
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

        val healthBar = createHealthBar(containerParams, hpBarParams, renderProjection)
        val manaBar = createManaBar(containerParams, manaBarParams, renderProjection)
        val smallCells = createCells(containerParams, renderProjection)
        val guiContainer = GuiContainer(containerParams)

        guiContainer.init(renderProjection)
        guiContainer.addComponent(healthBar, hpBarParams)
        guiContainer.addComponent(manaBar, manaBarParams)
        smallCells.forEach {
            guiContainer.addComponent(it, it.parameters)
        }

        matrixState.nonTranslatedParams.addAll(
            listOf(containerParams, hpBarParams, manaBarParams)
        )
        matrixState.nonTranslatedParams.addAll(
            smallCells.map { it.parameters }
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
        hpBarParams: SetOfStatic2DParameters,
        renderProjection: Matrix4f
    ) = HealthBar(
        objParams = containerParams,
        barParams = hpBarParams,
        onFilledChange = {},
        projection = renderProjection,
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
        manaBarParams: SetOfStatic2DParameters,
        renderProjection: Matrix4f
    ) = ManaBar(
        objParams = containerParams,
        barParams = manaBarParams,
        onFilledChange = {},
        projection = renderProjection,
        texturePath = this.javaClass.getResource("/textures/gui/ManaBarAtlas.png")!!.path,
        isBoundToParams = false
    )

    private fun createCells(
        containerParams: SetOfStatic2DParameters,
        renderProjection: Matrix4f
    ): List<HotBarCellSmall> {
        val cellsCount = 10
        val out = mutableListOf<HotBarCellSmall>()

        var prevX = containerParams.x + containerParams.xSize * 0.305f

        while (out.size < cellsCount) {

            val params = SetOfStatic2DParameters(
                x = prevX,
                y = containerParams.y + (containerParams.ySize * 0.439f),
                xSize = containerParams.xSize * 0.039f,
                ySize = containerParams.ySize * 0.233f,
                rotationAngle = 0f
            )

            prevX += params.xSize + containerParams.xSize * 0.002f

            out.add(
                HotBarCellSmall(
                    renderProjection = renderProjection,
                    parameters = params,
                    containerParameters = containerParams,
                    texturePath = this.javaClass.getResource("/textures/gui/HotBarCellSmall.png")!!.path
                )
            )
        }

        return out.toList()
    }
}