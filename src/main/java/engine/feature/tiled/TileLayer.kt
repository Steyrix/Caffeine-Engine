package engine.feature.tiled

import engine.core.render.render2D.Drawable2D
import engine.core.render.render2D.OpenGlObject2D
import engine.core.shader.Shader
import engine.core.update.SetOfParameters
import engine.feature.tiled.property.Property
import engine.feature.tiled.traversing.TileGraphGenerator

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

    private val graphicalComponent: OpenGlObject2D = TileLayerInitializer.genGraphicalComponent(this)

    val graph: Map<Int, List<Int>> = TileGraphGenerator.generateTileGraph(this)

    override fun updateParameters(parameters: SetOfParameters) {
        graphicalComponent.updateParameters(parameters)
    }

    override fun draw() {
        graphicalComponent.draw()
    }

    fun getTileValueByIndex(index: Int): Int {
        return if (index <= tileIdsData.size - 1) tileIdsData[index]
        else -1
    }
}