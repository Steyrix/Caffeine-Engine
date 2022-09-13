package demo.labyrinth

import engine.feature.animation.BasicAnimation

data class LabyrinthPresets(
        val characterPresets: CharacterPresets = CharacterPresets()
)

data class CharacterPresets(
        val animation: AnimationsPresets = AnimationsPresets()
)

data class AnimationsPresets(
        val animations: MutableList<BasicAnimation> = mutableListOf(
                BasicAnimation("IDLE", 1, 0, 1, 1, 100f),
                BasicAnimation("WALK", 2, 0, 6, 1, 100f),
                BasicAnimation("JUMP", 3, 0, 3, 1, 200f)
        )
)