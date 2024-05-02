package engine.feature.tiled.scene

import engine.core.shader.Shader
import engine.core.update.SetOfStatic2DParameters
import engine.feature.tiled.data.TileMap
import engine.feature.tiled.data.lighting.LightMap
import engine.feature.tiled.data.lighting.LightSource
import org.joml.Matrix4f

abstract class LightMapHolder {

    abstract val lightMapProjection: Matrix4f
    abstract var lightMapScreenWidth: Float
    abstract var lightMapScreenHeight: Float
    abstract var holderMap: TileMap?

    protected val lightSources: MutableList<LightSource> = mutableListOf()

    protected var lightMap: LightMap? = null

    protected var lightMapShader: Shader? = null

    protected var lightmapPrecision: Float = 10f

    fun addLightSource(lightSource: LightSource) {
        lightSources.add(lightSource)
        if (lightSource.isLit) {
            lightMap = generateLightMap()
        }
    }

    fun removeLightSource(lightSource: LightSource) {
        lightSources.remove(lightSource)
        if (lightSource.isLit) {
            lightMap = generateLightMap()
        }
    }

    fun setLightSourceLit(lightSource: LightSource, value: Boolean) {
        if (value != lightSource.isLit) {
            lightSource.isLit = value
            lightMap = generateLightMap()
        }
    }

    fun updateLightMapShader(shader: Shader) {
        lightMap?.setShader(shader)
        lightMapShader = shader
    }

    protected fun generateLightMap(): LightMap {
        val litLightSources = lightSources.filter { it.isLit }
        return LightMap(
            precision = lightmapPrecision,
            projection = lightMapProjection,
            parameters = SetOfStatic2DParameters(
                x = 0f,
                y = 0f,
                xSize = lightMapScreenWidth * (lightMapScreenWidth / lightmapPrecision),
                ySize = lightMapScreenHeight * (lightMapScreenHeight / lightmapPrecision),
                rotationAngle = 0f
            ),
            tileMap = holderMap ?: throw IllegalStateException(),
            lightSources = litLightSources,
            screenSizeX = lightMapScreenWidth,
            screenSizeY = lightMapScreenHeight,
        ).apply {
            lightMapShader?.let {
                this.setShader(it)
            }
        }
    }
}