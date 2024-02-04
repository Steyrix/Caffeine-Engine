package demo.medieval_game.data.gameobject.npc.goblin

import demo.medieval_game.data.static_parameters.HUMANOID_SIZE_Y
import demo.medieval_game.data.gameobject.npc.BoundingBoxPreset
import demo.medieval_game.data.gameobject.npc.Preset
import demo.medieval_game.data.static_parameters.HUMANOID_BOX_OFFSET_X
import demo.medieval_game.data.static_parameters.HUMANOID_BOX_WIDTH
import demo.medieval_game.data.gameobject.npc.AnimationPreset
import demo.medieval_game.data.goblinsAnimations
import engine.core.update.SetOfStatic2DParametersWithOffset

object GoblinPreset {

    fun get(): Preset {
        return Preset(
            BoundingBoxPreset(
                SetOfStatic2DParametersWithOffset(
                    x = 0f,
                    y = 0f,
                    xSize = HUMANOID_BOX_WIDTH - 30f,
                    xOffset = HUMANOID_BOX_OFFSET_X,
                    ySize = HUMANOID_SIZE_Y - 20f,
                    yOffset = 0f,
                    rotationAngle = 0f
                ),
                isSizeBoundToHolder = false
            ),
            AnimationPreset(
                this.javaClass.getResource("/textures/GoblinAtlas.png")!!.path,
                animations = goblinsAnimations.map { it.copy() },
                frameWidth = 0.09f,
                frameHeight = 0.2f
            )
        )
    }
}