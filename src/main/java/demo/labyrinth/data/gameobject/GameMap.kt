package demo.labyrinth.data.gameobject

import demo.labyrinth.data.*
import engine.core.entity.CompositeEntity
import engine.core.loop.AccumulatedTimeEvent
import engine.core.shader.Shader
import engine.core.shader.ShaderLoader
import engine.core.update.SetOf2DParametersWithVelocity
import engine.core.update.SetOfStatic2DParameters
import engine.feature.tiled.TileMap
import engine.feature.tiled.parser.TiledResourceParser
import engine.feature.tiled.traversing.TileGraph
import engine.feature.tiled.traversing.TileTraverser
import org.joml.Matrix4f
import org.joml.Vector2f
import java.io.File

object GameMap : GameObject {
    override var it: CompositeEntity? = null
    var graphicalComponent: TileMap? = null
        set(value) {
            value?.let {
                tileGraph = value.getGraph(
                        listOf("walking_layer", "walkable_objects_layer"),
                        listOf("obstacles_layer", "obstacles_bg_layer", "unwalking_layer")
                )

                field = value
            }
        }

    var parameters: SetOfStatic2DParameters = SetOfStatic2DParameters(
            0f, 0f, 0f, 0f, 0f
    )
    var tileGraph: TileGraph? = null

    private val lightBlinking = AccumulatedTimeEvent(
            timeLimit = lightBlinkingTimeLimit,
            action = {
                if (current + 1 >= lightIntensityCaps.size) {
                    current = 0
                } else current++
                graphicalComponent?.shader?.let {
                    it.bind()
                    it.setUniform("lightIntensityCap", lightIntensityCaps[current])
                    it.setUniform("lightSourceCoords", Vector2f(campfireParameters.x, campfireParameters.y))
                }
            }
    )

    fun init(
            renderProjection: Matrix4f,
            screenWidth: Float,
            screenHeight: Float
    ) {
        parameters = getMapParameters(screenWidth, screenHeight)
        graphicalComponent = getGraphicalComponent(renderProjection, screenWidth, screenHeight)

        parameters.xSize = screenWidth
        parameters.ySize = screenHeight

        it = object  : CompositeEntity() {}
        GameMap.addComponent(graphicalComponent, parameters)
    }

    private fun getGraphicalComponent(
            renderProjection: Matrix4f,
            screenWidth: Float,
            screenHeight: Float
    ): TileMap {
        val vertexShaderPath = this.javaClass.getResource("/shaders/lightingShaders/lightingVertexShader.glsl")!!.path
        val fragmentShaderPath = this.javaClass.getResource("/shaders/lightingShaders/lightingFragmentShader.glsl")!!.path

        graphicalComponent = TiledResourceParser.createTileMapFromXml(
                File(this.javaClass.getResource("/tiled/port_map.xml")!!.path)
        )
        graphicalComponent?.shader = ShaderLoader.loadFromFile(
                vertexShaderFilePath = vertexShaderPath,
                fragmentShaderFilePath = fragmentShaderPath
        ).also {
            it.bind()
            it.setUniform(Shader.VAR_KEY_PROJECTION, renderProjection)

            it.setUniform("screenSize", Vector2f(screenWidth, screenHeight))
            it.setUniform("lightSourceSize", Vector2f(campfireParameters.xSize, campfireParameters.ySize))
            it.setUniform("lightSourceCoords", Vector2f(campfireParameters.x, campfireParameters.y))
            it.setUniform("lightIntensityCap", lightIntensityCap)
        }

        return graphicalComponent as TileMap
    }

    override fun update(deltaTime: Float) {
        super.update(deltaTime)
        lightBlinking.schedule(deltaTime)
    }

    fun createTileTraverser(params: SetOf2DParametersWithVelocity): TileTraverser {
        return TileTraverser(
                tileGraph!!,
                graphicalComponent!!,
                params
        )
    }
}