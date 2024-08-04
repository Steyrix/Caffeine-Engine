package engine.feature.procedural

typealias MinMax = Pair<Float, Float>
typealias NoiseValue = NoiseParameter<Float>

abstract class ProceduralEnum(
    private val rangeMap: Map<NoiseValue, MinMax>
) {
    fun checkIfMatch(): Boolean {
        rangeMap.entries.forEach {
            val condition = it.key.value >= it.value.first && it.key.value <= it.value.second
            if (!condition) {
                return false
            }
        }

        return true
    }
}