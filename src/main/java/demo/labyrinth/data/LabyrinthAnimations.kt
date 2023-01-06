package demo.labyrinth.data

import engine.feature.animation.BasicAtlasAnimation

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
        BasicAtlasAnimation(
                AnimationKey.IDLE_R,
                1, 0, 1, 1, 0, 0, 1f
        ),
        BasicAtlasAnimation(
                AnimationKey.WALK_R,
                2, 0, 6, 1, 1, 0, 0.2f
        ).apply {
            setFirstPosX(1)
            setLastPosX(6)
        },
        BasicAtlasAnimation(
                AnimationKey.JUMP_R,
                3, 0, 3, 1, 7, 1, 0.5f
        ).apply {
            setFirstPosX(7)
            setLastPosX(9)
        },

        BasicAtlasAnimation(
                AnimationKey.IDLE_L,
                4, 0, 1, 1, 9, 3, 1f
        ),
        BasicAtlasAnimation(
                AnimationKey.WALK_L,
                5, 0, 6, 1, 8, 3, 0.2f
        ).apply {
            setFirstPosX(8)
            setLastPosX(3)
        },
        BasicAtlasAnimation(
                AnimationKey.JUMP_L,
                6, 0, 3, 1, 0, 4, 0.5f
        ).apply {
            setFirstPosX(0)
            setLastPosX(2)
        }
)

val campfireAnimations =
        mutableListOf(
                BasicAtlasAnimation(
                        AnimationKey.BURN,
                        100, 0, 4, 1, 0, 0, 0.1f
                )
        )
