package demo.medieval_game.interaction.event_receivers

import demo.medieval_game.data.gameobject.gui.chest.ChestGuiContainer
import demo.medieval_game.data.gameobject.gui.inventory.InventoryGuiContainer
import demo.medieval_game.data.gameobject.inventory_item.InventoryItemWrapper
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

    private val inventoryGui: InventoryGuiContainer = InventoryGuiContainer(
        SetOfStatic2DParameters(x = 0f, y = 0f, xSize = 530f, ySize = 635f, rotationAngle = 0f)
    )

    var isInputBlocked: Boolean = false

    init {
        chestGui.init(renderProjection)
        inventoryGui.init(renderProjection)
    }

    private fun showInventoryGui(
        content: MutableList<InventoryItemWrapper>
    ) {
        val guiParams = inventoryGui.parameters
        val newPosition = Point2D(450f, 0f)

        inventoryGui.updatePosition(newPosition)
        // inventoryGui.setContent
        addComponent(inventoryGui, guiParams)
        // setOnClicks
    }

    private fun showChestGui(
        content: MutableList<InventoryItemWrapper>
    ) {
        val guiParams = chestGui.parameters
        val newPosition = Point2D(0f, 225f)

        chestGui.updatePosition(newPosition)

        chestGui.setContent(content)

        // TODO: move out state setting
        isInputBlocked = true
        addComponent(chestGui, guiParams)
        chestGui.setOnCloseClick { hideChestGui() }
    }

    private fun hideChestGui() {
        removeComponent(chestGui)
        isInputBlocked = false
    }

    override fun proccessEvent(event: InteractionEvent) {
        when(event) {
            is OpenChest -> {
                showInventoryGui(mutableListOf())
                showChestGui(event.content)
            }
        }
    }


}