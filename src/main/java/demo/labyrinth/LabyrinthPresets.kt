package demo.labyrinth

import engine.feature.animation.BasicAnimation

object AnimationKey {
        const val WALK = "WALK"
        const val IDLE = "IDLE"
        const val JUMP = "JUMP"
}
data class LabyrinthPresets(
        val characterPresets: CharacterPresets = CharacterPresets()
)

data class CharacterPresets(
        val animation: AnimationsPresets = AnimationsPresets()
)

data class AnimationsPresets(
        val animations: MutableList<BasicAnimation> = mutableListOf(
                BasicAnimation(
                        AnimationKey.IDLE,
                        1, 0, 1, 1, 0, 0, 1f
                ),
                BasicAnimation(
                        AnimationKey.WALK,
                        2, 0, 6, 1, 1, 0, 0.2f
                ).apply {
                        setFirstPosX(1)
                        setLastPosX(6)
                },
                BasicAnimation(
                        AnimationKey.JUMP,
                        3, 0, 3, 1, 7, 1, 0.5f
                ).apply {
                        setFirstPosX(7)
                        setLastPosX(9)
                }
        )
)