package engine.feature.interaction

import engine.core.entity.Entity

interface InteractionContext {
    val isInteracting: (target: Entity, agent: Entity) -> Boolean

    val agents: MutableList<Entity>

    fun addAgent(entity: Entity) {
        agents.add(entity)
    }

    fun removeAgent(entity: Entity) {
        agents.remove(entity)
    }

    fun update(deltaTime: Float) {
        agents.forEach { current ->
            agents.forEach { agent ->
                checkForInteraction(current, agent)
            }
        }
    }

    private fun checkForInteraction(target: Entity, agent: Entity) {
        if (agent != target) {
            if (isInteracting(target, agent)) {
                val interaction = target.getInteraction()
                interaction?.let { agent.consumeInteraction(it) }
            }
        }
    }
}