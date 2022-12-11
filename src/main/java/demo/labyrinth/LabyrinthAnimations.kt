package demo.labyrinth

import engine.feature.animation.BasicAnimation

object AnimationKey {
    const val WALK = "WALK"
    const val IDLE = "IDLE"
    const val JUMP = "JUMP"
    const val BURN = "BURN"
}

val characterAnimations = mutableListOf(
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

val campfireAnimations =
        mutableListOf(
                BasicAnimation(
                        AnimationKey.BURN,
                        4, 0, 4, 1, 0, 0, 0.1f
                )
        )
