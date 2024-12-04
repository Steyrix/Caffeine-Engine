package engine.feature.interaction

import engine.core.entity.Entity
import engine.feature.collision.bounding_box.BoundingBox
import engine.feature.interaction.broadcast.EventReceiver

class BoxInteractionContext : InteractionContext<BoundingBox> {

    override val agents: MutableList<Entity> = mutableListOf()

    override val paramsMap: MutableMap<Entity, BoundingBox> = mutableMapOf()

    override val isInteracting = { target: Entity, agent: Entity -> isIntersecting(target, agent) }

    override val listeners: MutableList<EventReceiver> = mutableListOf()

    private fun isIntersecting(target: Entity, agent: Entity): Boolean {
        if (target == agent) return false

        paramsMap[target]?.let { current ->
            paramsMap[agent]?.let {
                return current.isIntersecting(it)
            }
        }

        return false
    }
}