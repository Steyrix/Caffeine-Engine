package demo.medieval_game.interaction.event_receivers

import demo.medieval_game.data.gameobject.gui.chest.ChestGuiContainer
import demo.medieval_game.interaction.event.Loot
import engine.core.entity.CompositeEntity
import engine.core.geometry.Point2D
import engine.core.update.ParametersFactory
import engine.feature.interaction.broadcast.EventReceiver
import engine.feature.interaction.broadcast.InteractionEvent

object GuiController : CompositeEntity(), EventReceiver {

    private val chestGui: ChestGuiContainer = ChestGuiContainer(ParametersFactory.createEmptyStatic())

    var isInputBlocked: Boolean = false

    private fun showChestGui(pos: Point2D) {
        chestGui.parameters.apply {
            x = pos.x
            y = pos.y
        }
        isInputBlocked = true
        addComponent(chestGui, chestGui.parameters)
    }

    override fun proccessEvent(event: InteractionEvent) {
        when(event) {
            is Loot -> showChestGui(event.pos)
        }
    }


}