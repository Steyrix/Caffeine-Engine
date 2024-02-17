package demo.medieval_game.interaction.event_receivers

import demo.medieval_game.data.gameobject.gui.chest.ChestGuiContainer
import demo.medieval_game.data.gameobject.inventory_item.InventoryItemWrapper
import demo.medieval_game.interaction.event.CloseChest
import demo.medieval_game.interaction.event.OpenChest
import demo.medieval_game.scene.MedievalGame
import engine.core.entity.CompositeEntity
import engine.core.geometry.Point2D
import engine.core.update.SetOfStatic2DParameters
import engine.feature.interaction.broadcast.EventReceiver
import engine.feature.interaction.broadcast.InteractionEvent

object GuiController : CompositeEntity(), EventReceiver {

    private val renderProjection = MedievalGame.renderProjection

    private val chestGui: ChestGuiContainer = ChestGuiContainer(
        SetOfStatic2DParameters(x = 0f, y = 0f, xSize = 400f, ySize = 496f, rotationAngle = 0f)
    )

    var isInputBlocked: Boolean = false

    init {
        chestGui.init(renderProjection)
    }

    private fun showChestGui(
        pos: Point2D,
        content: MutableList<InventoryItemWrapper>
    ) {
        val guiParams = chestGui.parameters

        guiParams.apply {
            x = pos.x
            y = pos.y
        }

        content.forEach {
            it.parameters.apply {
                x = guiParams.x + 0.13f * guiParams.xSize
                y = guiParams.y + 0.306f * guiParams.ySize
                xSize = guiParams.xSize * 0.1f
                ySize = guiParams.ySize * 0.1909f
            }
        }

        chestGui.setContent(content)

        isInputBlocked = true
        addComponent(chestGui, guiParams)
    }

    private fun hideChestGui() {
        removeComponent(chestGui)
        isInputBlocked = false
    }

    override fun proccessEvent(event: InteractionEvent) {
        when(event) {
            is OpenChest -> showChestGui(event.pos, event.content)
            is CloseChest -> hideChestGui()
        }
    }


}