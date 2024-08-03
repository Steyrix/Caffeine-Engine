package engine.feature.procedural

interface NoiseParameterSet

class NoiseParameter<T : Comparable<T>>(
    var value: T
) {

    // abstract fun getRange(): ClosedRange<T>
}