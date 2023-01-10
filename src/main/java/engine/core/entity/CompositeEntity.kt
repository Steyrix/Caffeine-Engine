package engine.core.entity

import engine.core.controllable.Controllable
import engine.core.render.Drawable
import engine.core.update.SetOfParameters
import engine.core.update.Updatable
import engine.core.update.update2D.Parameterized
import engine.core.window.Window

/*
    Ah, so, each component will have a reference to its properties.
    Therefore, update of the properties and following call of an update method
    will result in proper update of component.
 */
open class CompositeEntity : Entity, Updatable {

    protected val parametersMap: HashMap<SetOfParameters, MutableList<Entity>> = hashMapOf()

    fun addComponent(
            component: Entity,
            parameters: SetOfParameters
    ) {
        parametersMap
                .getOrPut(parameters) { mutableListOf() }
                .add(component)

        component.onAdd()
    }

    fun draw() {
        parametersMap.entries.forEach {
            it.value.forEach { entity ->
                (entity as? Drawable)?.draw()
            }
        }
    }

    override fun update(deltaTime: Float) {
        parametersMap.entries.forEach {
            it.value.forEach { entity ->
                (entity as? Updatable)?.update(deltaTime)
                (entity as? Parameterized)?.updateParameters(it.key)
            }
        }
    }

    fun input(window: Window) {
        parametersMap.entries.forEach {
            it.value.forEach { entity ->
                (entity as? Controllable)?.input(window)
            }
        }
    }
}