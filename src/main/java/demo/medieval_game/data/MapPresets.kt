package demo.medieval_game.data

import engine.core.loop.AccumulatedTimeEvent
import engine.core.render.render2D.Drawable2D
import engine.feature.matrix.MatrixState
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
                                campfireParameters.x + MatrixState.worldTranslation.x,
                                campfireParameters.y + MatrixState.worldTranslation.y
                        )
                )
            }
        }
)

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