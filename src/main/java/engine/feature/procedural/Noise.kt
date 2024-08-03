package engine.feature.procedural

interface NoiseParameterSet

interface NoiseParameter<T: Comparable<T>> {

    var value: T

    fun getRange(): ClosedRange<T>
}