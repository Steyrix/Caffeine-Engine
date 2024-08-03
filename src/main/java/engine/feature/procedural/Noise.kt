package engine.feature.procedural

interface NoiseParameterSet

interface ProceduralEnum

interface NoiseParameter<T: Comparable<T>> {

    fun getValue(): T

    fun getRange(): ClosedRange<T>
}