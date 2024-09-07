package engine.feature.procedural.`object`

import engine.feature.procedural.MapElementType

interface GeneratedObjectType {

    val baseMapElementType: MapElementType
    val variants: List<Any>
}

fun getObject(type: MapElementType): GeneratedObjectType {
    TODO()
}
