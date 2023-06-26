package engine.core.render

import engine.core.entity.Entity
import engine.core.shader.Shader
import engine.core.texture.ArrayTexture2D
import engine.core.texture.Texture2D
import engine.feature.matrix.MatrixComputer
import engine.core.render.util.DefaultBufferData
import engine.core.update.SetOfParameters
import org.lwjgl.opengl.GL33C.*

open class Model(
        protected val mesh: Mesh,
        var texture: Texture2D? = null,
        var arrayTexture: ArrayTexture2D? = null
) : Drawable<SetOfParameters>, Entity {

    constructor(
            texture2D: Texture2D,
            uv: FloatArray = DefaultBufferData.getRectangleSectorVertices(1.0f, 1.0f)
    ) : this(
            mesh = Mesh(
                    dataArrays = listOf(
                            DefaultBufferData.RECTANGLE_INDICES,
                            uv
                    ),
                    verticesCount = 6
            ),
            texture = texture2D,
            arrayTexture = null
    )

    constructor(arrayTexture2D: ArrayTexture2D) : this(
            mesh = Mesh(
                    dataArrays = listOf(
                            DefaultBufferData.RECTANGLE_INDICES,
                            DefaultBufferData.getRectangleSectorVertices(1.0f, 1.0f)
                    ),
                    verticesCount = 6
            ),
            texture = null,
            arrayTexture = arrayTexture2D
    )

    private var textureUniformName: String = ""

    override var shader: Shader? = null

    var x: Float = 0f
    var y: Float = 0f
    var xSize: Float = 0f
    var ySize: Float = 0f
    var rotationAngle: Float = 0f

    var drawMode = GL_TRIANGLES

    override fun draw() {
        shader?.let {
            mesh.prepare()
            it.bind()

            val model = MatrixComputer.getResultMatrix(x, y, xSize, ySize, rotationAngle)

            defineTextureState()
            it.setUniform(Shader.VAR_KEY_MODEL, model)
            it.validate()

            glDrawArrays(drawMode, 0, mesh.verticesCount)
        }
    }

    fun dispose() {
        mesh.dispose()
    }

    private fun defineTextureState() {
        if (texture != null) {
            textureUniformName = Shader.VAR_KEY_TEXTURE_SAMPLE
            bindTexture()
        } else if (arrayTexture != null) {
            textureUniformName = Shader.VAR_KEY_TEXTURE_ARRAY
            bindArrayTexture()
        }
    }

    private fun bindTexture() {
        glActiveTexture(GL_TEXTURE0)
        texture!!.bind()
        shader!!.bind()
        shader!!.setUniform(textureUniformName, 0)
    }

    private fun bindArrayTexture() {
        // glActiveTexture(GL_TEXTURE0)
        arrayTexture!!.bind()
        shader!!.bind()
        shader!!.setUniform(textureUniformName, 0)
    }

    fun isTextured(): Boolean {
        return texture != null || arrayTexture != null
    }

    override fun updateParameters(parameters: SetOfParameters) {
        x = parameters.x
        y = parameters.y
        xSize = parameters.xSize
        ySize = parameters.ySize
        rotationAngle = parameters.rotationAngle
    }

    // todo: get rid of proxying
    fun updateMesh(
            bufferIndex: Int,
            offset: Long,
            data: FloatArray
    ) {
        mesh.updateBuffer(bufferIndex, offset, data)
    }
}