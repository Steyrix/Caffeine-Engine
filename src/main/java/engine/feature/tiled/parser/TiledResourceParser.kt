package engine.feature.tiled.parser

import engine.core.texture.Texture2D
import engine.feature.tiled.data.layer.TileLayer
import engine.feature.tiled.data.TileMap
import engine.feature.tiled.data.TileSet
import engine.feature.tiled.data.layer.Layer
import engine.feature.tiled.data.layer.ObjectsLayer
import engine.feature.tiled.property.*
import org.lwjgl.opengl.GL11.*
import org.w3c.dom.Document
import org.w3c.dom.Node
import java.io.File

internal object TiledResourceParser {

    fun createTileMapFromXml(xmlFile: File): TileMap {
        val document = XmlParser.getDocument(xmlFile)
        val mapNode = document!!.getElementsByTagName(MAP)
        val mapNodeAttribs = mapNode.item(0).attributes
        val mapWidth = mapNodeAttribs.getNamedItem(MAP_WIDTH).nodeValue.toInt()
        val mapHeight = mapNodeAttribs.getNamedItem(MAP_HEIGHT).nodeValue.toInt()

        val tileSet = retrieveTileSet(document)

        return TileMap(
            layers = retrieveLayers(mapWidth, mapHeight, document, tileSet)
        )
    }

    private fun retrieveTileSet(doc: Document): TileSet {
        val mapTileSetNode = doc.getElementsByTagName(TILE_SET)
        val mapTileSetAttribs = mapTileSetNode!!.item(0).attributes
        val tileSetPath = mapTileSetAttribs.getNamedItem(SOURCE).nodeValue

        val tileSetFile = File(this.javaClass.getResource(tileSetPath)!!.path)

        val document = XmlParser.getDocument(tileSetFile)!!

        val tileSetNode = document.getElementsByTagName(TILE_SET)
        val tileSetAttribs = tileSetNode.item(0).attributes

        val tileWidth = tileSetAttribs.getNamedItem(TILE_WIDTH).nodeValue
        val tileHeight = tileSetAttribs.getNamedItem(TILE_HEIGHT).nodeValue
        val tileCount = tileSetAttribs.getNamedItem(TILE_COUNT).nodeValue
        val columnCount = tileSetAttribs.getNamedItem(COLUMN_COUNT).nodeValue

        val imageNode = document.getElementsByTagName(IMAGE)
        val sourcePath = imageNode.item(0).attributes.getNamedItem(SOURCE).nodeValue
        val texturePath = this.javaClass.getResource(sourcePath)!!.path
        val texture = Texture2D.createInstance(texturePath)

        texture.setParameter(GL_TEXTURE_MIN_FILTER, GL_NEAREST)
        texture.setParameter(GL_TEXTURE_MAG_FILTER, GL_NEAREST)
        return TileSet(tileWidth.toFloat(), tileHeight.toFloat(), texture, tileCount.toInt(), columnCount.toInt())
    }

    private fun retrieveLayers(
        widthInTiles: Int,
        heightInTiles: Int,
        doc: Document,
        tileSet: TileSet
    ): ArrayList<Layer> {
        val out = ArrayList<Layer>()
        val layers = doc.getElementsByTagName(LAYER)

        for (layerIndex in 0 until layers.length) {
            val currentLayer = layers.item(layerIndex)
            val data = retrieveData(currentLayer)
            val properties = retrieveProperties(currentLayer)
            val primitiveProperties = convertToPrimitiveProperties(properties)
            val name = currentLayer.attributes.getNamedItem(PROPERTY_NAME).nodeValue

            if (name.contains("obstacle") || name.contains("shadows")) {
                out.add(
                    ObjectsLayer(
                        name = name,
                        widthInTiles = widthInTiles,
                        heightInTiles = heightInTiles,
                        tileIdsData = data,
                        set = tileSet,
                        transparencyUniformName = "transparency",
                        isShadow = name.contains("shadows")
                    )
                )
            } else {
                out.add(
                    TileLayer(
                        name = name,
                        widthInTiles = widthInTiles,
                        heightInTiles = heightInTiles,
                        tileIdsData = data,
                        set = tileSet,
                        properties = primitiveProperties
                    )
                )
            }
        }

        return out
    }

    private fun retrieveData(node: Node): ArrayList<Int> {
        val out = ArrayList<Int>()

        val nodes = node.childNodes
        for (i in 0 until nodes.length) {
            if (nodes.item(i).nodeName == DATA) {
                out.addAll(nodes
                    .item(i)
                    .textContent
                    .replace("\n", "")
                    .replace(" ", "")
                    .split(",")
                    .map { it.toInt() - 1 })
            }
        }

        return out
    }

    private fun retrieveProperties(node: Node): ArrayList<Node> {
        val out = ArrayList<Node>()

        val nodes = node.childNodes

        for (propertyParentNodeIndex in 0 until nodes.length) {
            if (nodes.item(propertyParentNodeIndex).nodeName == PROPERTIES) {
                val properties = nodes.item(propertyParentNodeIndex).childNodes

                for (propertyChildNodeIndex in 0 until properties.length) {
                    if (properties.item(propertyChildNodeIndex).nodeName == PROPERTY)
                        out.add(properties.item(propertyChildNodeIndex))
                }

                break
            }
        }

        return out
    }

    private fun convertToPrimitiveProperties(list: ArrayList<Node>): ArrayList<Property> {
        val out = ArrayList<Property>()

        list.forEach {
            val propertyName = it.attributes.getNamedItem(PROPERTY_NAME).nodeValue
            val propertyType = it.attributes.getNamedItem(PROPERTY_TYPE).nodeValue
            val propertyValue = it.attributes.getNamedItem(PROPERTY_VALUE).nodeValue

            val propertyField = when (propertyType) {
                BOOL -> BooleanProperty(propertyValue.toBoolean(), propertyName)
                FLOAT -> FloatProperty(propertyValue.toFloat(), propertyName)
                INT -> IntProperty(propertyValue.toInt(), propertyName)
                STRING -> StringProperty(propertyValue, propertyName)
                else -> throw Exception()
            }

            out.add(propertyField)
        }

        return out
    }
}