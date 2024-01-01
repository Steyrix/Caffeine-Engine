package engine.feature.interaction

import demo.medieval_game.data.gameobject.npc.goblin.Goblin
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

    fun update() {
        agents.forEach { current ->
            agents.forEach { agent ->
                checkForInteraction(current, agent)
            }
        }
    }

    // TODO: add consumers to list, each should consume interaction, then list should be cleared
    private fun checkForInteraction(target: Entity, agent: Entity) {
        if (agent != target) {
            if (isInteracting(target, agent)) {
                val interactions = target.getInteractions()
                interactions.forEach {
                    agent.consumeInteraction(it)
                }
            }
        }
    }
}