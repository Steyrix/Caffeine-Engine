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
    private val toRemove = mutableSetOf<Entity>()

    var isDisposed = false

    fun addComponent(
        component: Entity,
        parameters: SetOfParameters
    ): CompositeEntity {
        entitiesMap[component] = parameters
        component.onAdd()

        return this
    }

    fun removeComponent(
        entity: Entity
    ) {
        if (entitiesMap.contains(entity) && !toRemove.contains(entity)) {
            toRemove.add(entity)
        }
    }

    fun draw() {
        this.getDrawablesFlatList().forEach {
            (it as? Drawable<*>)?.draw()
        }
    }

    private fun getDrawablesFlatList(): List<Entity> {
        val preSortedList = mutableListOf<Entity>()

        entitiesMap.keys.forEach {
            if (it is CompositeEntity) {
                preSortedList.addAll(it.getDrawablesFlatList())
            } else if (it is Drawable<*>) {
                preSortedList.add(it)
            }
        }

        return preSortedList.sortedBy { (it as? Drawable<*>)?.zLevel }
    }

    override fun update(deltaTime: Float) {
        val iterator = entitiesMap.entries.iterator()
        while (iterator.hasNext()) {
            val item = iterator.next()
            if (toRemove.contains(item.key)) {
                iterator.remove()
                toRemove.remove(item.key)
            } else {
                (item.key as? Updatable)?.update(deltaTime)
                (item.key as? Parameterized<SetOfParameters>)?.updateParameters(item.value)
            }
        }
    }

    override fun consumeInteraction(interaction: Interaction) {
        entitiesMap.keys.forEach { entity ->
            entity.consumeInteraction(interaction)
        }
    }

    override fun onInteractionAvailable(producer: Entity) {
        entitiesMap.keys.forEach { entity ->
            entity.onInteractionAvailable(producer)
        }
    }

    override fun onInteractionUnavailable(producer: Entity) {
        entitiesMap.keys.forEach { entity ->
            entity.onInteractionUnavailable(producer)
        }
    }

    open fun input(window: Window) {
        entitiesMap.keys.forEach { entity ->
            (entity as? Controllable)?.input(window)
        }
    }

    fun contains(entity: Entity): Boolean {
        return entitiesMap.containsKey(entity)
    }
}