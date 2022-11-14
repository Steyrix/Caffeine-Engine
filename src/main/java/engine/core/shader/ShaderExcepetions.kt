package engine.core.shader

import java.lang.IllegalArgumentException

class ShaderNotCreatedException : IllegalStateException("Could not create shader program")

class UniformNotFoundException(
        uniformName: String
) : IllegalArgumentException("Could not find uniform with name $uniformName")

class IllegalShaderTypeException(
        shaderType: Int
) : IllegalStateException("Could not create shader with type $shaderType")

class ShaderCompilationFailedException(
        log: String
) : IllegalStateException("Could not compile shader: $log")

class ShaderLinkFailedException(
        log: String
) : IllegalStateException("Could not link shader: $log")

class ShaderValidationFailedException(
        log: String
) : IllegalStateException("Could not validate shader: $log")