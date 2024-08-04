package engine.feature.procedural

interface ProceduralEnum {

    val parameters: List<NoiseParameter<Float>>

    fun checkIfMatch(): Boolean
}