package engine.core.update

interface Parameterized<P : SetOfParameters> {
    fun updateParameters(parameters: P)
}