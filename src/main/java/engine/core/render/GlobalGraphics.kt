package engine.core.render

import engine.core.render.model.AnimatedModel2D
import engine.core.render.shader.ShaderLoader
import engine.core.render.shader.ShaderResource
import engine.core.texture.Texture2D
import engine.feature.animation.AtlasData

object GlobalGraphics {

    var animationShaderResource: ShaderResource = ShaderResource("", "")

    fun createAnimatedModel(
        texturePath: String,
        atlasData: AtlasData
    ): AnimatedModel2D {
        if (isEmpty(animationShaderResource)) throw IllegalStateException("Animation shader is not initialized")
        val targetShader = ShaderLoader.loadFromFile(animationShaderResource)
        val texture = Texture2D.createInstance(texturePath)

        return AnimatedModel2D(
            atlasData,
            texture,
        ).apply {
            shader = targetShader
        }
    }

    private fun isEmpty(resource: ShaderResource): Boolean {
        return resource.vertexShaderPath.isEmpty() || resource.fragmentShaderPath.isEmpty()
    }
}