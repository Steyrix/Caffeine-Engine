package demo.labyrinth.data

import engine.feature.animation.BasicAnimation

object AnimationKey {
    const val WALK_R = "WALK_R"
    const val WALK_L = "WALK_L"
    const val IDLE_R = "IDLE_R"
    const val IDLE_L = "IDLE_L"
    const val JUMP_R = "JUMP_R"
    const val JUMP_L = "JUMP_L"
    const val BURN = "BURN"
}

val characterAnimations = mutableListOf(
        BasicAnimation(
                AnimationKey.IDLE_R,
                1, 0, 1, 1, 0, 0, 1f
        ),
        BasicAnimation(
                AnimationKey.WALK_R,
                2, 0, 6, 1, 1, 0, 0.2f
        ).apply {
            setFirstPosX(1)
            setLastPosX(6)
        },
        BasicAnimation(
                AnimationKey.JUMP_R,
                3, 0, 3, 1, 7, 1, 0.5f
        ).apply {
            setFirstPosX(7)
            setLastPosX(9)
        },

        BasicAnimation(
                AnimationKey.IDLE_L,
                4, 1, 1, 1, 8, 0, 1f
        ),
        BasicAnimation(
                AnimationKey.WALK_L,
                5, 1, 6, 1, 7, 0, 0.2f
        ).apply {
            setFirstPosX(7)
            setLastPosX(3)
        },
        BasicAnimation(
                AnimationKey.JUMP_L,
                6, 1, 3, 1, 0, 1, 0.5f
        ).apply {
            setFirstPosX(0)
            setLastPosX(2)
        }
)

val campfireAnimations =
        mutableListOf(
                BasicAnimation(
                        AnimationKey.BURN,
                        100, 0, 4, 1, 0, 0, 0.1f
                )
        )
