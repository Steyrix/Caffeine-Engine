package engine.core.entity

import engine.core.entity.behavior.Behavior
import engine.core.entity.behavior.LoopedBehavior
import engine.core.entity.behavior.SimpleBehavior
import engine.core.update.SetOfParameters

class BehaviouralEntity(
        private var behavior: Behavior
) : CompositeEntity() {

    fun setBehavior(behavior: Behavior) {
        this.behavior = behavior
    }

    override fun update(deltaTime: Float) {
        super.update(deltaTime)
        behave(deltaTime)
    }

    private fun behave(deltaTime: Float) {
        when (behavior) {
            is SimpleBehavior ->
                processBehavior(
                        deltaTime,
                        false
                )
            is LoopedBehavior ->
                processBehavior(
                        deltaTime,
                        true
                )
        }
    }

    private fun processBehavior(
            deltaTime: Float,
            isLoop: Boolean
    ) {

    }
}