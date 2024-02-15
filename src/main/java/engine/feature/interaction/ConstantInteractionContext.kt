package engine.feature.interaction

import engine.core.entity.Entity
import engine.feature.interaction.broadcast.EventReceiver

class ConstantInteractionContext : InteractionContext<Unit> {

    override val agents: MutableList<Entity> = mutableListOf()

    override val paramsMap: MutableMap<Entity, Unit> = mutableMapOf()

    override val isInteracting = { _: Entity, _: Entity -> true }

    override val listeners: MutableList<EventReceiver> = mutableListOf()
}