package engine.core.texture

// TODO: implement
class ArrayTexture2D(
        private var id: Int,
        val layersCount: Int
) {

    companion object {
        fun createInstance(
                sources: List<String>,
                layersCount: Int
        ): ArrayTexture2D {
            return ArrayTexture2D(
                    TextureLoader.loadArrayTexture2D(sources, layersCount),
                    layersCount
            )
        }
    }

}