package engine.feature.interaction

import engine.core.entity.Entity

interface InteractionContext<T> {
    val isInteracting: (target: Entity, agent: Entity) -> Boolean

    val agents: MutableList<Entity>

    val paramsMap: MutableMap<Entity, T>

    fun addAgent(entity: Entity, value: T) {
        agents.add(entity)
        paramsMap[entity] = value
    }

    fun removeAgent(entity: Entity) {
        agents.remove(entity)
        paramsMap.remove(entity)
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