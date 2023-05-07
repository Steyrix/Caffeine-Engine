package demo.medieval_game.data.starting_level

import demo.medieval_game.MedievalGameMatrixState
import demo.medieval_game.data.campfireParameters
import engine.core.loop.AccumulatedTimeEvent
import engine.core.render.render2D.Drawable2D
import engine.feature.tiled.scene.TileMapPreset
import org.joml.Vector2f

fun getLightBlinkingEvent(graphicalComponent: Drawable2D) = AccumulatedTimeEvent(
        timeLimit = lightBlinkingTimeLimit,
        action = {
            if (current + 1 >= lightIntensityCaps.size) {
                current = 0
            } else current++
            graphicalComponent.shader?.let {
                it.bind()
                it.setUniform(
                        "lightIntensityCap",
                        lightIntensityCaps[current]
                )
                it.setUniform(
                        "lightSourceCoords",
                        Vector2f(
                                campfireParameters.x + MedievalGameMatrixState.worldTranslation.x,
                                campfireParameters.y + MedievalGameMatrixState.worldTranslation.y
                        )
                )
            }
        }
)

// TODO: store in map
fun getStartingMapPreset(screenWidth: Float, screenHeight: Float): TileMapPreset =
        TileMapPreset(
                width = 800f,
                height = 800f,
                mapSourcePath = "/tiled/port_map.xml",
                vertexShaderPath = "/shaders/lightingShaders/lightingVertexShader.glsl",
                fragmentShaderPath = "/shaders/lightingShaders/lightingFragmentShader.glsl",
                shaderUniforms = mapOf(
                        "screenSize" to Vector2f(screenWidth, screenHeight),
                        "lightSourceSize" to Vector2f(campfireParameters.xSize, campfireParameters.ySize),
                        "lightSourceCoords" to Vector2f(campfireParameters.x, campfireParameters.y),
                        "lightIntensityCap" to lightIntensityCap
                ),
                updateEvents = listOf { drawable: Drawable2D -> getLightBlinkingEvent(drawable) },
                walkingLayers = listOf("walking_layer", "walkable_objects_layer"),
                obstacleLayers = listOf("obstacles_layer")
        )

fun getNexusMapPreset(screenWidth: Float, screenHeight: Float): TileMapPreset =
        TileMapPreset(
                width = 800f,
                height = 800f,
                mapSourcePath = "/tiled/nexus_map.xml",
                vertexShaderPath = "/shaders/lightingShaders/texturedVertexShader.glsl",
                fragmentShaderPath = "/shaders/lightingShaders/texturedFragmentShader.glsl",
                shaderUniforms = mapOf(
                        "screenSize" to Vector2f(screenWidth, screenHeight),
                ),
                updateEvents = listOf(),
                walkingLayers = listOf("walking_layer"),
                obstacleLayers = listOf("obstacles_layer", "obstacles_layer_2")
        )