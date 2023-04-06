package engine.core.entity

import engine.core.controllable.Controllable
import engine.core.render.Drawable
import engine.core.update.SetOfParameters
import engine.core.update.Updatable
import engine.core.update.update2D.Parameterized
import engine.core.window.Window

/*
    Each set of parameters which is held by the composite entity can be modified
    by several inner entities. So whenever set of parameters changes, according
    entities will be updated.
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
                (entity as? CompositeEntity)?.draw()
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

    override fun consumeInteraction(interaction: Interaction) {
        parametersMap.entries.forEach {
            it.value.forEach {entity ->
                entity.consumeInteraction(interaction)
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