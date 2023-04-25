package engine.core.scene.context

import engine.core.scene.GameObject
import engine.core.update.SetOfParameters

interface Bundle {

    companion object {
        fun build(presetObjects: MutableMap<GameObject, SetOfParameters>): Bundle {
            return object : Bundle {
                override val objects = presetObjects
            }
        }
    }

    val objects: MutableMap<GameObject, SetOfParameters>
}