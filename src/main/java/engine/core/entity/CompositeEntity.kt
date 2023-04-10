package engine.core.entity

import engine.core.controllable.Controllable
import engine.core.render.Drawable
import engine.core.update.SetOfParameters
import engine.core.update.Updatable
import engine.core.update.update2D.Parameterized
import engine.core.window.Window
import engine.feature.interaction.Interaction

/*
    Each set of parameters which is held by the composite entity can be modified
    by several inner entities. So whenever set of parameters changes, according
    entities will be updated.
 */
open class CompositeEntity : Entity, Updatable {

    protected val parametersMap: HashMap<Entity, SetOfParameters> = hashMapOf()

    var isDisposed = false

    fun addComponent(
            component: Entity,
            parameters: SetOfParameters
    ) {
        parametersMap[component] = parameters
        component.onAdd()
    }

    fun removeComponent(
            entity: Entity
    ) {
        parametersMap.remove(entity)
    }

    fun draw() {
        parametersMap.keys.forEach { entity ->
            (entity as? Drawable)?.draw()
            (entity as? CompositeEntity)?.draw()
        }
    }

    override fun update(deltaTime: Float) {
        parametersMap.entries.forEach {
            (it.key as? Updatable)?.update(deltaTime)
            (it.key as? Parameterized)?.updateParameters(it.value)
        }
    }

    override fun consumeInteraction(interaction: Interaction) {
        parametersMap.keys.forEach { entity ->
            entity.consumeInteraction(interaction)
        }
    }

    fun input(window: Window) {
        parametersMap.keys.forEach { entity ->
            (entity as? Controllable)?.input(window)
        }
    }
}