package demo.medieval_game.data.gameobject.npc

import engine.core.update.SetOfStatic2DParametersWithOffset
import engine.feature.animation.Animation

data class BoundingBoxPreset(
    val initialParams: SetOfStatic2DParametersWithOffset,
    val isSizeBoundToHolder: Boolean
)

data class AnimationPreset(
    val atlasTexturePath: String,
    val frameSizeX: Float,
    val frameSizeY: Float,
    val animations: List<Animation>
)

data class Preset(
    val box: BoundingBoxPreset,
    val animation: AnimationPreset
)
