package engine.feature.procedural

interface NoiseParameterSet

abstract class NoiseParameter<T: Comparable<T>> {

    abstract var value: T

    // abstract fun getRange(): ClosedRange<T>
}