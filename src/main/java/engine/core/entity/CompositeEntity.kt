package engine.core.entity

import engine.core.render.Drawable
import engine.core.update.SetOfParameters
import engine.core.update.Updatable

class CompositeEntity(
        private val components: MutableList<Entity>
) : Entity {

    fun draw() {
        components
                .filter { it is Drawable }
                .forEach { (it as Drawable).draw() }
    }

    // TODO: magic logic - rethink it, should it be some kind of map?
    fun update(parameters: MutableList<SetOfParameters>) {
        var parametersIndex = 0

        components.filter { it is Updatable }.forEach {
            (it as Updatable).update(parameters[parametersIndex++])
        }
    }
}