package engine.feature.tiled

import engine.core.render.render2D.Drawable2D
import engine.core.render.render2D.OpenGlObject2D
import engine.core.shader.Shader
import engine.core.update.SetOfParameters
import engine.feature.tiled.property.Property
import java.util.Collections

class TileLayer(
        val name: String,
        val widthInTiles: Int,
        val heightInTiles: Int,
        val tileIdsData: List<Int>,
        internal val set: TileSet,
        private val properties: ArrayList<Property>
): Drawable2D {

    override var shader: Shader? = null
        set(value) {
            field = value
            graphicalComponent.shader = value
        }
    override val innerDrawableComponents: MutableList<Drawable2D> = mutableListOf()

    private var initialWidth = 0f
    private var initialHeight = 0f
    private val graphicalComponent: OpenGlObject2D

    private val graph: MutableMap<Int, MutableList<Int>>
        get() = Collections.unmodifiableMap(field)

    init {
        initialWidth = (widthInTiles * set.tileWidthPx)
        initialHeight = (heightInTiles * set.tileHeightPx)
        graphicalComponent = TileLayerInitializer.genGraphicalComponent(this)
        graph = TileLayerInitializer.generateTileGraph(this)
    }

    override fun updateParameters(parameters: SetOfParameters) {
        graphicalComponent.updateParameters(parameters)
    }

    override fun draw() {
        graphicalComponent.draw()
    }

    fun getTileNumberByIndex(index: Int): Int {
        return if (index <= tileIdsData.size - 1) tileIdsData[index]
        else -1
    }
}