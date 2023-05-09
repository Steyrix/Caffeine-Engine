package engine.feature.tiled.scene

import demo.medieval_game.data.recalculateParameters
import engine.core.entity.CompositeEntity
import engine.core.entity.Entity
import engine.core.render.render2D.Drawable2D
import engine.core.scene.GameObject
import engine.core.shader.Shader
import engine.core.shader.ShaderLoader
import engine.core.update.SetOf2DParametersWithVelocity
import engine.core.update.SetOfParameters
import engine.core.update.SetOfStatic2DParameters
import engine.feature.collision.CollisionContext
import engine.feature.geometry.Point2D
import engine.feature.tiled.data.TileMap
import engine.feature.tiled.parser.TiledResourceParser
import engine.feature.tiled.traversing.TileGraph
import engine.feature.tiled.traversing.TileTraverser
import org.joml.Matrix4f
import java.io.File

class TileMapObject(
        private val mapPresets: TileMapPreset
) : GameObject {

    override var it: CompositeEntity? = CompositeEntity()

    var parameters: SetOfStatic2DParameters =
            SetOfStatic2DParameters(
                    0f, 0f, 0f, 0f, 0f
            )

    private var graph: TileGraph? = null

    val worldSize: Point2D
        get() {
            val w = graphicalComponent?.getWorldWidth() ?: 0f
            val h = graphicalComponent?.getTileHeight() ?: 0f
            return Point2D(w, h)
        }

    private var graphicalComponent: TileMap? = null
        set(value) {
            value?.let {
                graph = value.getGraph(
                        mapPresets.walkingLayers,
                        mapPresets.obstacleLayers
                )

                field = value
            }
        }

    fun init(
            renderProjection: Matrix4f,
            collisionContexts: List<CollisionContext>
    ) {
        parameters = SetOfStatic2DParameters(
                x = 0f,
                y = 0f,
                xSize = mapPresets.width,
                ySize = mapPresets.height,
                rotationAngle = 0f
        )

        graphicalComponent = getGraphicalComponent(renderProjection)
        addComponent(graphicalComponent, parameters!!)

        collisionContexts.forEach {
            it.addEntity(graphicalComponent as Entity, parameters)
        }

        graphicalComponent?.updateParameters(parameters)
        recalculateParameters(parameters.xSize, parameters.ySize)
    }

    private fun getGraphicalComponent(
            renderProjection: Matrix4f
    ): TileMap {
        val vertexShaderPath = this.javaClass.getResource(mapPresets.vertexShaderPath)?.path
                ?: throw IllegalStateException()

        val fragmentShaderPath = this.javaClass.getResource(mapPresets.fragmentShaderPath)?.path
                ?: throw IllegalStateException()

        val sourcePath = this.javaClass.getResource(mapPresets.mapSourcePath)?.path
                ?: throw IllegalStateException()

        val graphicalComponent = TiledResourceParser.createTileMapFromXml(
                File(sourcePath)
        )

        graphicalComponent.shader = ShaderLoader.loadFromFile(
                vertexShaderFilePath = vertexShaderPath,
                fragmentShaderFilePath = fragmentShaderPath
        ).also { shader ->
            shader.bind()
            shader.setUniform(Shader.VAR_KEY_PROJECTION, renderProjection)

            mapPresets.shaderUniforms.forEach {
                shader.setUniform(it.key, it.value)
            }
        }

        return graphicalComponent
    }

    override fun update(
            deltaTime: Float
    ) {
        super.update(deltaTime)
        mapPresets.updateEvents.forEach {
            it.invoke(graphicalComponent as Drawable2D).schedule(deltaTime)
        }
    }

    fun createTraverser(
            targetParams: SetOf2DParametersWithVelocity
    ): TileTraverser {
        return TileTraverser(
                graph!!,
                graphicalComponent!!,
                targetParams
        )
    }

    fun adjustParameters(
            sizeToMapRelation: Float,
            params: List<SetOfParameters>
    ) {
        params.forEach {
            it.xSize = sizeToMapRelation * parameters.xSize
            it.ySize = sizeToMapRelation * parameters.ySize
        }
    }

    override fun getZLevel(): Float {
        return Float.MIN_VALUE
    }
}