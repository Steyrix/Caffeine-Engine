package demo.medieval_game.data.starting_level

import demo.medieval_game.data.*
import demo.medieval_game.data.static_parameters.campfireParameters
import demo.medieval_game.matrix.MedievalGameMatrixState
import engine.core.loop.AccumulatedTimeEvent
import engine.core.render.Drawable
import engine.feature.tiled.scene.TileMapPreset
import org.joml.Vector2f

fun getLightBlinkingEvent(graphicalComponent: Drawable<*>) = AccumulatedTimeEvent(
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
        mapSourcePath = "/tiled/general_map.xml",
        vertexShaderPath = "/shaders/lightingShaders/lightingVertexShader.glsl",
        fragmentShaderPath = "/shaders/lightingShaders/lightingFragmentShader.glsl",
        objectFragmentShaderPath = "/shaders/mapShaders/layerObjectsFragmentShader.glsl",
        objectVertexShaderPath = "/shaders/mapShaders/layerObjectsVertexShader.glsl",
        shaderUniforms = mapOf(
            "screenSize" to Vector2f(screenWidth, screenHeight),
            "lightSourceSize" to Vector2f(campfireParameters.xSize, campfireParameters.ySize),
            "lightSourceCoords" to Vector2f(campfireParameters.x, campfireParameters.y),
            "lightIntensityCap" to lightIntensityCap
        ),
        updateEvents = listOf { drawable: Drawable<*> -> getLightBlinkingEvent(drawable) },
        walkingLayers = listOf("walkable_layer", "transparent_obstacles_layer"),
        obstacleLayers = listOf("obstacles_layer", "unwalkable_bg_layer"),
    )

fun getNexusMapPreset(): TileMapPreset =
    TileMapPreset(
        width = 800f,
        height = 800f,
        mapSourcePath = "/tiled/nexus_map.xml",
        vertexShaderPath = "/shaders/texturedShaders/texturedVertexShader.glsl",
        fragmentShaderPath = "/shaders/texturedShaders/texturedFragmentShader.glsl",
        objectFragmentShaderPath = "/shaders/mapShaders/layerObjectsFragmentShader.glsl",
        objectVertexShaderPath = "/shaders/mapShaders/layerObjectsVertexShader.glsl",
        shaderUniforms = mapOf(),
        updateEvents = listOf(),
        walkingLayers = listOf("walking_layer"),
        obstacleLayers = listOf("obstacles_layer", "obstacles_layer_2")
    )