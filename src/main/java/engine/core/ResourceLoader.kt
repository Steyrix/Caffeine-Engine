package engine.core

object ResourceLoader {

    fun get(path: String): String {
        return this.javaClass.getResource(path)!!.path
    }
}