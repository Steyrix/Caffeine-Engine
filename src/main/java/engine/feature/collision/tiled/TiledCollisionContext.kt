package engine.feature.collision.tiled

import engine.core.entity.Entity
import engine.core.update.SetOfParameters
import engine.feature.collision.CollisionContext
import engine.feature.tiled.data.TileMap

class TiledCollisionContext(
    private val nonCollisionLayers: MutableList<String> = mutableListOf(),
    private val objectLayers: MutableList<String> = mutableListOf(),
    val tileMap: TileMap
) : CollisionContext<TiledCollider> {

    override val colliders: MutableSet<TiledCollider> = mutableSetOf()

    override val entities: MutableSet<Entity> = mutableSetOf()

    override val entitiesParams: MutableMap<Entity, SetOfParameters> = mutableMapOf()

    override val toRemove: MutableSet<Entity> = mutableSetOf()

    override val collisions: MutableMap<TiledCollider, MutableList<Entity>> = mutableMapOf()

    private val tilesWithIncreasedCost: MutableSet<Int> = mutableSetOf()

    override fun addCollider(collider: TiledCollider) {
        super.addCollider(collider)

        val toAddNonCollisionLayers = nonCollisionLayers.filter {
            !collider.nonCollisionLayers.contains(it)
        }

        val toAddObjectLayers = objectLayers.filter {
            !collider.objectLayers.contains(it)
        }

        if(collider.shouldBlockTile) {
            tileMap.graph?.remove(collider.currentOccupiedTile)
        }

        collider.nonCollisionLayers.addAll(toAddNonCollisionLayers)
        collider.objectLayers.addAll(toAddObjectLayers)
    }

    override fun update() {
        val occupiedTiles = colliders.map { it.currentOccupiedTile }

        updateCosts(occupiedTiles)

        tileMap.graph?.let {
            occupiedTiles.forEach { tileIndex ->
                if (!tilesWithIncreasedCost.contains(tileIndex)) {
                    it.increaseCost(tileIndex)
                    tilesWithIncreasedCost.add(tileIndex)
                }
            }
        }

        colliders.forEach { collider ->
            collider.tilesOccupiedByOtherEntities = occupiedTiles.filter {
                it != collider.currentOccupiedTile
            }
        }
        super.update()
    }

    private fun updateCosts(occupiedTiles: List<Int>) {
        val iterator = tilesWithIncreasedCost.iterator()
        while (iterator.hasNext()) {
            val tile = iterator.next()
            if (!occupiedTiles.contains(tile)) {
                tileMap.graph?.let {
                    it.decreaseCost(tile)
                }
                iterator.remove()
            }
        }
    }
}