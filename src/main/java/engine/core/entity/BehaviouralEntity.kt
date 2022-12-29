package engine.core.entity

import engine.core.entity.behavior.Behavior
import engine.core.update.SetOfParameters

class BehaviouralEntity(
        private var behavior: Behavior,
        private var parameters: SetOfParameters
) : CompositeEntity() {

    fun setBehavior(behavior: Behavior) {
        this.behavior = behavior
    }

    override fun update(deltaTime: Float) {
        super.update(deltaTime)
        behave(deltaTime)
    }

    private fun behave(deltaTime: Float) {
        behavior.execute(
                deltaTime,
                listOf(parameters)
        )
    }
}