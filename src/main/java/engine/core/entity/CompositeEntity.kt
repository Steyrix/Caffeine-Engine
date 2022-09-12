package engine.core.entity

import engine.core.controllable.Controllable
import engine.core.render.Drawable
import engine.core.update.SetOfParameters
import engine.core.update.Updatable
import engine.core.update.update2D.Parameterized2D
import engine.core.window.Window

/*
    Ah, so, each component will have a reference to its properties.
    Therefore, update of the properties and following call of an update method
    will result in proper update of component.
 */
open class CompositeEntity : Entity, Updatable {

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

    override fun update(deltaTime: Float) {
        components.entries.forEach {
            if (it.key is Updatable) {
                (it.key as Updatable).update(deltaTime)
            }

            // TODO switch to global interface
            if (it.key is Parameterized2D) {
                (it.key as Parameterized2D).updateParameters(it.value)
            }
        }
    }

    fun input(window: Window) {
        components.keys
                .filter { it is Controllable }
                .forEach { (it as Controllable).input(window) }
    }
}