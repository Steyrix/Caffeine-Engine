package demo.medieval_game.data.gameobject.npc.goblin

import demo.medieval_game.data.HUMANOID_SIZE_Y
import demo.medieval_game.data.gameobject.npc.NpcBoxPreset
import demo.medieval_game.data.gameobject.npc.NpcPreset
import demo.medieval_game.data.HUMANOID_BOX_OFFSET
import demo.medieval_game.data.HUMANOID_BOX_SIZE
import demo.medieval_game.data.gameobject.npc.AnimationPreset
import demo.medieval_game.data.goblinsAnimations
import engine.core.update.SetOfStatic2DParametersWithOffset

object GoblinPreset {

    fun get(): NpcPreset {
        return NpcPreset(
            NpcBoxPreset(
                SetOfStatic2DParametersWithOffset(
                    x = 0f,
                    y = 0f,
                    xSize = HUMANOID_BOX_SIZE,
                    xOffset = HUMANOID_BOX_OFFSET,
                    ySize = HUMANOID_SIZE_Y,
                    yOffset = 0f,
                    rotationAngle = 0f
                ),
                isSizeBoundToHolder = false
            ),
            AnimationPreset(
                this.javaClass.getResource("/textures/GoblinAtlas_ver1.png")!!.path,
                animations = goblinsAnimations.map { it.copy() },
                frameSizeX = 0.09f,
                frameSizeY = 0.2f
            )
        )
    }
}