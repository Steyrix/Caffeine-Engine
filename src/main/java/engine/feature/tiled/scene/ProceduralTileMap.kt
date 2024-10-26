package engine.feature.tiled.scene

import engine.core.entity.CompositeEntity
import engine.core.entity.Entity
import engine.core.game_object.SingleGameEntity
import engine.core.geometry.Point2D
import engine.core.loop.GameLoopTimeEvent
import engine.core.update.SetOfParameters
import engine.core.update.SetOfStatic2DParameters
import engine.feature.collision.CollisionContext
import engine.feature.procedural.generators.ProceduralGenerator
import engine.feature.tiled.data.TileMap
import org.joml.Matrix4f

class ProceduralTileMap private constructor(
    private val mapPresets: ProceduralMapPreset?,
    private val proceduralGenerator: ProceduralGenerator?,
    private val seed: Long = 0,
) : SingleGameEntity(), TileMapController {

    private constructor(builder: Builder) : this(builder.presets, builder.generator, builder.seed)

    class Builder {
        private var presets: ProceduralMapPreset? = null
        private var generator: ProceduralGenerator? = null
        private var seed: Long = 0

        fun build() = ProceduralTileMap(this)
        fun presets(value: ProceduralMapPreset) {
            this.apply {
                presets = value
            }
        }

        fun generator(value: ProceduralGenerator) {
            this.apply {
                generator = value
            }
        }

        fun seed(value: Long) {
            this.apply {
                seed = value
            }
        }
    }

    override var parameters: SetOfStatic2DParameters =
        SetOfStatic2DParameters(
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
            parameters = SetOfStatic2DParameters(
                x = 0f,
                y = 0f,
                xSize = width,
                ySize = height,
                rotationAngle = 0f
            )

            mapComponent =
                proceduralGenerator!!.generateMap(seed).apply {
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

        if (proceduralGenerator == null) {
            throw IllegalStateException("generator cannot be null")
        }
    }
}