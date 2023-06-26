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

    protected val entitiesMap: HashMap<Entity, SetOfParameters> = hashMapOf()

    var isDisposed = false

    fun addComponent(
            component: Entity,
            parameters: SetOfParameters
    ) : CompositeEntity {
        entitiesMap[component] = parameters
        component.onAdd()

        return this
    }

    fun removeComponent(
            entity: Entity
    ) {
        if (entitiesMap.contains(entity)) {
            entitiesMap.remove(entity)
        }
    }

    fun draw() {
        entitiesMap.keys.forEach { entity ->
            (entity as? Drawable<SetOfParameters>)?.draw()
            (entity as? CompositeEntity)?.draw()
        }
    }

    override fun update(deltaTime: Float) {
        entitiesMap.entries.forEach {
            (it.key as? Updatable)?.update(deltaTime)
            (it.key as? Parameterized<SetOfParameters>)?.updateParameters(it.value)
        }
    }

    override fun consumeInteraction(interaction: Interaction) {
        entitiesMap.keys.forEach { entity ->
            entity.consumeInteraction(interaction)
        }
    }

    fun input(window: Window) {
        entitiesMap.keys.forEach { entity ->
            (entity as? Controllable)?.input(window)
        }
    }
}