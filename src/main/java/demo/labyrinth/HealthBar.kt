package demo.labyrinth

import engine.core.entity.CompositeEntity
import engine.core.update.SetOf2DParametersWithVelocity
import engine.core.update.SetOfStatic2DParameters

class HealthBar(
        private val characterParams: SetOf2DParametersWithVelocity,
        private val barParams: SetOfStatic2DParameters
) : CompositeEntity() {

    override fun update(deltaTime: Float) {
        super.update(deltaTime)
        with(characterParams) {
            barParams.x = this.x - ((barParams.xSize - this.xSize) / 2)
            barParams.y = this.y - 2 - barParams.ySize
        }
    }
}