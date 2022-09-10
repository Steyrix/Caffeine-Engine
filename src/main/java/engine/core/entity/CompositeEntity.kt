package engine.core.entity

import engine.core.render.Drawable
import engine.core.update.SetOfParameters
import engine.core.update.Updatable

/*
    Ah, so, each component will have a reference to its properties.
    Therefore, update of the properties and following call of an update method
    will result in proper update of component.
 */
class CompositeEntity : Entity {

    private val components: HashMap<Entity, SetOfParameters> = hashMapOf()

    fun addComponent(
            component: Entity,
            parameters: SetOfParameters
    ) {
        components[component] = parameters
    }

    fun draw() {
        components.keys
                .filter { it is Drawable }
                .forEach { (it as Drawable).draw() }
    }

    fun update() {
        components.entries.forEach {
            (it.key as Updatable).update(it.value)
        }
    }
}