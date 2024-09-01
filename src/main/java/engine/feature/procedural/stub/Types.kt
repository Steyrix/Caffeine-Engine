package engine.feature.procedural.stub

import engine.feature.procedural.MapElementType
import engine.feature.procedural.MinMax
import engine.feature.procedural.NoiseParameterType

class StubParameter : NoiseParameterType

class StubType : MapElementType {
    override val rangeMap: Map<NoiseParameterType, MinMax> =
        mapOf(StubParameter() to Pair(Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY))
}