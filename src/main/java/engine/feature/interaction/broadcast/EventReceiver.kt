package engine.feature.interaction.broadcast

interface EventReceiver {

    fun proccessEvent(event: InteractionEvent)
}