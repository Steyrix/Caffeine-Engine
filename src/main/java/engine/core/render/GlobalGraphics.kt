package engine.core.render

import engine.core.render.shader.Shader
import engine.core.render.shader.ShaderLoader
import engine.core.render.shader.ShaderResource

object GlobalGraphics {

    var animationShaderResource: ShaderResource = ShaderResource("", "")

    val defaultAnimationShader: Shader
    get() {
        if (isEmpty(animationShaderResource)) throw IllegalStateException("Animation shader is not initialized")

        return ShaderLoader.loadFromFile(
            animationShaderResource.vertexShaderPath,
            animationShaderResource.fragmentShaderPath
        )
    }

    private fun isEmpty(resource: ShaderResource): Boolean {
        return resource.vertexShaderPath.isEmpty() || resource.fragmentShaderPath.isEmpty()
    }
}