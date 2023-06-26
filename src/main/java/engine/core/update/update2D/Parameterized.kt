package engine.core.update.update2D

import engine.core.update.SetOfParameters

interface Parameterized<P : SetOfParameters> {
    fun updateParameters(parameters: P)
}