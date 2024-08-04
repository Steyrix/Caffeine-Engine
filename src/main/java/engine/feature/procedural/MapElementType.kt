package engine.feature.procedural

typealias MinMax = Pair<Float, Float>

interface MapElementType {
    val rangeMap: Map<NoiseParameterType, MinMax>

    fun checkIfMatch(parameters: List<NoiseParameter>): Boolean {
        parameters.forEach { param ->
            rangeMap[param.type]?.let {
                val condition = param.value >= it.first && param.value <= it.second
                if (!condition) return false
            } ?: return false
        }

        return true
    }
}