package engine.core.texture

interface Texture {
    val id: Int

    fun bind()

    fun unbind()
}