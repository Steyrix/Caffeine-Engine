package engine.feature.interaction

import engine.core.entity.Entity
import engine.feature.collision.boundingbox.BoundingBox

class BoxInteractionContext : InteractionContext<BoundingBox> {

    override val agents: MutableList<Entity> = mutableListOf()

    override val paramsMap: MutableMap<Entity, BoundingBox> = mutableMapOf()

    override val isInteracting = { target: Entity, agent: Entity -> isIntersecting(target, agent) }

    private fun isIntersecting(target: Entity, agent: Entity): Boolean {
        paramsMap[target]?.let { current ->
            paramsMap[agent]?.let {
                return current.isIntersecting(it)
            }
        }

        return false
    }
}