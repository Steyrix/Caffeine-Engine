package engine.feature.tiled.scene

import engine.core.entity.CompositeEntity
import engine.core.entity.Entity
import engine.core.game_object.SingleGameEntity
import engine.core.render.Drawable
import engine.core.shader.Shader
import engine.core.shader.ShaderLoader
import engine.core.update.SetOf2DParametersWithVelocity
import engine.core.update.SetOfParameters
import engine.core.update.SetOfStatic2DParameters
import engine.feature.collision.CollisionContext
import engine.core.geometry.Point2D
import engine.feature.tiled.data.TileMap
import engine.feature.tiled.data.`object`.MapObjectEntity
import engine.feature.tiled.data.`object`.MapObjectRetriever
import engine.feature.tiled.parser.TiledResourceParser
import engine.feature.tiled.traversing.TileGraph
import engine.feature.tiled.traversing.TileTraverser
import org.joml.Matrix4f
import java.io.File

class TileMapEntity(
    private val mapPresets: TileMapPreset
) : SingleGameEntity() {

    var parameters: SetOfStatic2DParameters =
        SetOfStatic2DParameters(
            0f, 0f, 0f, 0f, 0f
        )

    private var graph: TileGraph? = null

    val worldSize: Point2D
        get() {
            val w = graphicalComponent?.getWorldWidth() ?: 0f
            val h = graphicalComponent?.getWorldHeight() ?: 0f
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

    init {
        it = CompositeEntity()
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
        addComponent(graphicalComponent, parameters)

        collisionContexts.forEach {
            it.addEntity(graphicalComponent as Entity, parameters)
        }

        graphicalComponent?.updateParameters(parameters)
    }

    private fun getGraphicalComponent(
        renderProjection: Matrix4f
    ): TileMap {
        val vertexShaderPath = this.javaClass.getResource(mapPresets.vertexShaderPath)?.path
            ?: throw IllegalStateException()

        val fragmentShaderPath = this.javaClass.getResource(mapPresets.fragmentShaderPath)?.path
            ?: throw IllegalStateException()

        val vertexObjectShaderPath = this.javaClass.getResource(mapPresets.objectVertexShaderPath)?.path
            ?: throw IllegalStateException()

        val fragmentObjectShaderPath = this.javaClass.getResource(mapPresets.objectFragmentShaderPath)?.path
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

        graphicalComponent.objectShaderCreator = {
            ShaderLoader.loadFromFile(
                vertexShaderFilePath = vertexObjectShaderPath,
                fragmentShaderFilePath = fragmentObjectShaderPath
            ).also { shader ->
                shader.bind()
                shader.setUniform(Shader.VAR_KEY_PROJECTION, renderProjection)
            }
        }

        // TODO: cover with debug flag
        val debugVertexShaderPath =
            this.javaClass.getResource("/shaders/boundingBoxShaders/boundingBoxVertexShader.glsl")?.path
                ?: throw IllegalStateException()

        val debugFragmentShaderPath =
            this.javaClass.getResource("/shaders/boundingBoxShaders/boundingBoxFragmentShader.glsl")?.path
                ?: throw IllegalStateException()

        graphicalComponent.debugShader = ShaderLoader.loadFromFile(
            vertexShaderFilePath = debugVertexShaderPath,
            fragmentShaderFilePath = debugFragmentShaderPath
        ).also { shader ->
            shader.bind()
            shader.setUniform(Shader.VAR_KEY_PROJECTION, renderProjection)
        }

        return graphicalComponent
    }

    override fun update(
        deltaTime: Float
    ) {
        super.update(deltaTime)
        mapPresets.updateEvents.forEach {
            it.invoke(graphicalComponent as Drawable<*>).schedule(deltaTime)
        }
    }

    fun createTraverser(
        holderParams: SetOf2DParametersWithVelocity,
        targetParams: SetOf2DParametersWithVelocity
    ): TileTraverser {
        return TileTraverser(
            graph!!,
            graphicalComponent!!,
            holderParams,
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

    fun retrieveObjectEntities(): List<MapObjectEntity> {
        graphicalComponent?.let {
            return MapObjectRetriever.getObjectsAsEntities(it, parameters)
        }

        return emptyList()
    }

    override fun getZLevel(): Float {
        return Float.NEGATIVE_INFINITY
    }
}