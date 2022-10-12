package engine.feature.collision

interface Collider {
    fun reactToCollision()

    fun isColliding(): Boolean
}