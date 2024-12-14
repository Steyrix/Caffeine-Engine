package engine.core.render.model

import engine.core.entity.Entity
import engine.core.render.interfaces.Drawable
import engine.core.render.shader.Shader
import engine.core.texture.ArrayTexture2D
import engine.core.texture.Texture2D
import engine.feature.matrix.MatrixComputer
import engine.core.render.util.DefaultBufferData
import engine.core.update.SetOfParameters
import org.joml.Matrix4f
import org.joml.Vector3f
import org.lwjgl.opengl.GL33C.*

open class Model(
    private val mesh: Mesh,
    private var texture: Texture2D? = null,
    private var arrayTexture: ArrayTexture2D? = null,
    var isPartOfWorldTranslation: Boolean = true,
    var isStencilBufferEnabled: Boolean = false,
    var stencilBufferFunction: () -> Unit = {}
) : Drawable<SetOfParameters>, Entity {

    constructor(
        texture: Texture2D,
        uv: FloatArray = DefaultBufferData.getRectangleSectorVertices(1.0f, 1.0f),
        isStencilBufferEnabled: Boolean = false,
        stencilBufferFunction: () -> Unit = {}
    ) : this(
        mesh = Mesh(
            dataArrays = listOf(
                DefaultBufferData.RECTANGLE_INDICES,
                uv
            ),
            verticesCount = 6
        ),
        texture = texture,
        arrayTexture = null
    )

    constructor(
        arrayTexture: ArrayTexture2D,
        isStencilBufferEnabled: Boolean = false,
        stencilBufferFunction: () -> Unit = {}
    ) : this(
        mesh = Mesh(
            dataArrays = DefaultBufferData.getRectangleSectorBuffers(),
            verticesCount = 6
        ),
        texture = null,
        arrayTexture = arrayTexture
    )

    constructor(
        dataArrays: List<FloatArray>,
        verticesCount: Int,
        texture: Texture2D? = null,
        arrayTexture: ArrayTexture2D? = null,
        isStencilBufferEnabled: Boolean = false,
        stencilBufferFunction: () -> Unit = {}
    ) : this(
        mesh = Mesh(
            dataArrays = dataArrays,
            verticesCount = verticesCount
        ),
        texture = texture,
        arrayTexture = arrayTexture
    )

    private var textureUniformName: String = ""

    override var shader: Shader? = null

    var stencilShader: Shader? = null

    var x: Float = 0f
    var y: Float = 0f
    var xSize: Float = 0f
    var ySize: Float = 0f
    var rotationAngle: Float = 0f
    override var zLevel: Float = 0f

    var drawMode = GL_TRIANGLES

    var preDrawFunc: (() -> Unit)? = null
    var postDrawFunc: (() -> Unit)? = null

    override fun draw() {
        preDrawFunc?.invoke()
        shader?.let {
            mesh.prepare()
            it.bind()

            writeToStencilBuffer()

            val model = MatrixComputer.getResultMatrix(x, y, xSize, ySize, rotationAngle, isPartOfWorldTranslation)

            defineTextureState()
            it.setUniform(Shader.VAR_KEY_MODEL, model)
            it.validate()

            glDrawArrays(drawMode, 0, mesh.verticesCount)

            applyStencil(model)
        }
        postDrawFunc?.invoke()
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

    private fun bindTextureToStencil() {
        glActiveTexture(GL_TEXTURE0)
        texture!!.bind()
        stencilShader!!.bind()
        stencilShader!!.setUniform(textureUniformName, 0)
    }

    private fun bindArrayTexture() {
        // glActiveTexture(GL_TEXTURE0)
        arrayTexture!!.bind()
        shader!!.bind()
        shader!!.setUniform(textureUniformName, 0)
    }

    private fun writeToStencilBuffer() {
        if (!isStencilBufferEnabled) return
        stencilShader?.let {
            glStencilFunc(GL_ALWAYS, 1, 0xFF)
            glStencilMask(0xFF)
        }
    }

    private fun applyStencil(srcModel: Matrix4f) {
        if (!isStencilBufferEnabled) return
        stencilShader?.let { sShader ->
            sShader.bind()
            bindTextureToStencil()
            stencilBufferFunction.invoke()
            glStencilMask(0x00)
            var scaledModel = srcModel.scaleXY(1.02f, 1.02f)
            scaledModel = scaledModel.translate(Vector3f(-0.01f, -0.01f, 0f))
            sShader.setUniform(Shader.VAR_KEY_MODEL, scaledModel)
            sShader.validate()
            glDrawArrays(drawMode, 0, mesh.verticesCount)
            glStencilMask(0xFF)
            glStencilFunc(GL_ALWAYS, 0, 0xFF)
        }
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