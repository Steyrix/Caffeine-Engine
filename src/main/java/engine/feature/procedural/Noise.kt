package engine.feature.procedural

interface NoiseParameterSet

interface NoiseParameter<T: Comparable<T>> {

    fun getValue(): T

    fun getRange(): ClosedRange<T>
}