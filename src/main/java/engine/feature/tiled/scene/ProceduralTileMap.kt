package engine.feature.tiled.scene

import engine.core.entity.CompositeEntity
import engine.core.entity.Entity
import engine.core.game_object.SingleGameEntity
import engine.core.geometry.Point2D
import engine.core.loop.GameLoopTimeEvent
import engine.core.update.SetOfParameters
import engine.core.update.SetOfStaticParameters
import engine.feature.collision.CollisionContext
import engine.feature.procedural.generators.ProceduralGenerator
import engine.feature.tiled.data.TileMap
import engine.feature.tiled.data.TileSet
import org.joml.Matrix4f

class ProceduralTileMap private constructor(
    private val mapPresets: ProceduralMapPreset?,
    private val widthInTiles: Int,
    private val heightInTiles: Int,
    private val biomeMap: Map<String, TileSet>?,
    private val numSeeds: Int,
    private val maxPolygonDisplacement: Int,
    private val seed: Int = 0,
) : SingleGameEntity(), TileMapController {

    private constructor(builder: Builder) : this(
        builder.presets,
        builder.widthInTiles,
        builder.heightInTiles,
        builder.biomeMap,
        builder.numSeeds,
        builder.seed
    )

    class Builder {
        var presets: ProceduralMapPreset? = null
        var widthInTiles: Int = 0
        var heightInTiles: Int = 0
        var biomeMap: Map<String, TileSet>? = null
        var numSeeds: Int = 0
        var maxPolygonDisplacement: Int = 0
        var seed: Int = 0
        private var renderProjection: Matrix4f? = null
        private var collisionContexts: List<CollisionContext<*>> = emptyList()

        fun build() = ProceduralTileMap(this).apply {
            init(renderProjection!!, collisionContexts)
        }

        fun presets(value: ProceduralMapPreset) =
            this.apply {
                presets = value
            }

        fun widthInTiles(value: Int) =
            this.apply {
                widthInTiles = value
            }

        fun heightInTiles(value: Int) =
            this.apply {
                heightInTiles = value
            }

        fun biomeMap(value: Map<String, TileSet>) =
            this.apply {
                biomeMap = value
            }

        fun numSeeds(value: Int) =
            this.apply {
                numSeeds = value
            }

        fun maxPolygonDisplacement(value: Int) =
            this.apply {
                maxPolygonDisplacement = value
            }

        fun seed(value: Int) =
            this.apply {
                seed = value
            }

        fun renderProjection(value: Matrix4f) =
            this.apply {
                renderProjection = value
            }

        fun collisionContexts(value: List<CollisionContext<*>>) =
            this.apply {
                collisionContexts = value
            }
    }

    override var parameters: SetOfStaticParameters =
        SetOfStaticParameters(
            0f, 0f, 0f, 0f, 0f
        )

    override val worldSize: Point2D
        get() {
            val w = mapComponent?.getWorldWidth() ?: 0f
            val h = mapComponent?.getWorldHeight() ?: 0f
            return Point2D(w, h)
        }

    override val walkableLayers = mutableListOf<String>()
    override val obstacleLayers = mutableListOf<String>()

    override var mapComponent: TileMap? = null
        set(value) {
            value?.let { map ->
                walkableLayers.addAll(map.layers
                    .filter { layer -> layer.name.contains("walkable") }
                    .map { it.name }
                )
                obstacleLayers.addAll(map.layers
                    .filter { layer -> !layer.name.contains("walkable") }
                    .map { it.name }
                )
                map.generateGraph(
                    walkableLayers,
                    obstacleLayers
                )

                field = map
            }
        }

    override val eventSet: MutableSet<GameLoopTimeEvent> = mutableSetOf()

    override var isDebugMeshEnabled: Boolean = false
        set(value) {
            mapComponent?.isDebugMeshEnabled = value
            field = value
        }

    init {
        it = CompositeEntity()
    }

    override fun init(
        renderProjection: Matrix4f,
        collisionContexts: List<CollisionContext<*>>
    ) {
        checkParameters()

        mapPresets?.run {
            parameters = SetOfStaticParameters(
                x = 0f,
                y = 0f,
                xSize = width,
                ySize = height,
                rotationAngle = 0f
            )

            mapComponent =
                ProceduralGenerator.generateMap(
                    widthInTiles,
                    heightInTiles,
                    numSeeds,
                    biomeMap!!,
                    maxPolygonDisplacement,
                    seed
                ).apply {
                    shaders = TileMapGraphicsProvider.getShaders(mapPresets, renderProjection)
                }

            addComponent(mapComponent, parameters)

            collisionContexts.forEach {
                it.addEntity(mapComponent as Entity, parameters)
            }

            mapComponent?.updateParameters(parameters)
            updateEvents.forEach {
                val event = it.invoke(mapComponent!!)
                eventSet.add(event)
            }

            isSpawned = true
        }
    }

    override fun update(
        deltaTime: Float
    ) {
        super.update(deltaTime)
        eventSet.forEach { it.schedule(deltaTime) }
    }

    fun adjustParameters(
        sizeToMapRelation: Float,
        params: List<SetOfParameters>
    ) {
        params.forEach {
            it.xSize = (mapComponent?.getTileWidth() ?: 0f) * 3
            it.ySize = (mapComponent?.getTileHeight() ?: 0f) * 3
        }
    }

    override fun getZLevel(): Float {
        return Float.NEGATIVE_INFINITY
    }

    private fun checkParameters() {
        if (mapPresets == null) {
            throw IllegalStateException("presets cannot be null")
        }
    }
}