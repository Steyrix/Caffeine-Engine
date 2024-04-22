package engine.core

import engine.core.texture.Texture2D

object ResourceLoader {

    fun get(path: String): String {
        return this.javaClass.getResource(path)!!.path
    }

    fun loadTexture(path: String): Texture2D {
        return Texture2D.createInstance(get(path))
    }
}