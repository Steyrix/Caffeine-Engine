package engine.feature.interaction

import engine.core.entity.Entity

interface InteractionContext<T> {

    val isInteracting: (target: Entity, agent: Entity) -> Boolean

    val agents: MutableList<Entity>

    val paramsMap: MutableMap<Entity, T>

    val currentlyInteractingAgents: MutableMap<Entity, MutableSet<Entity>>

    fun addAgent(entity: Entity, value: T) {
        agents.add(entity)
        paramsMap[entity] = value
    }

    fun removeAgent(entity: Entity) {
        agents.remove(entity)
        paramsMap.remove(entity)
    }

    fun update() {
        val consumersList = mutableListOf<Entity>()

        agents.forEach { current ->
            agents.forEach { agent ->
                if (isInteracting(current, agent)) {
                    consumersList.add(agent)
                    currentlyInteractingAgents.getOrPut(current) { mutableSetOf() }.add(agent)
                } else {
                    currentlyInteractingAgents.getOrDefault(current, mutableSetOf()).remove(agent)
                }
            }

            current.getInteractions().forEach { interaction ->
                consumersList.forEach {
                    it.consumeInteraction(interaction)
                    it.onInteractionAvailable(current)
                }
            }
            consumersList.clear()
        }
    }
}