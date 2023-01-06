package demo.labyrinth.data

import engine.feature.animation.BasicAtlasAnimation
import engine.feature.animation.FrameParametersGenerator
import engine.feature.animation.SequenceAtlasAnimation

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

val characterAnimations2 = mutableListOf(
        SequenceAtlasAnimation(
                AnimationKey.IDLE_R,
                FrameParametersGenerator.generate(Pair(0,0), Pair(0,0), 0.1f, 0.166f),
                timeLimit = 1f
        ),
        SequenceAtlasAnimation(
                AnimationKey.IDLE_L,
                FrameParametersGenerator.generate(Pair(9,9), Pair(3,3), 0.1f, 0.166f),
                timeLimit = 1f
        ),
        SequenceAtlasAnimation(
                AnimationKey.WALK_R,
                FrameParametersGenerator.generate(Pair(1,6), Pair(0,0), 0.1f, 0.166f),
                timeLimit = 0.2f
        ),
        SequenceAtlasAnimation(
                AnimationKey.WALK_L,
                FrameParametersGenerator.generate(Pair(8,3), Pair(3,3), 0.1f, 0.166f),
                timeLimit = 0.2f
        ),
        SequenceAtlasAnimation(
                AnimationKey.JUMP_R,
                FrameParametersGenerator.generate(Pair(7,9), Pair(1,1), 0.1f, 0.166f),
                timeLimit = 0.5f
        ),
        SequenceAtlasAnimation(
                AnimationKey.JUMP_L,
                FrameParametersGenerator.generate(Pair(0,2), Pair(4,4), 0.1f, 0.166f),
                timeLimit = 0.5f
        ),

)


val campfireAnimations =
        mutableListOf(
                BasicAtlasAnimation(
                        AnimationKey.BURN,
                        100, 0, 4, 1, 0, 0, 0.1f
                )
        )

val campfireAnimations2 = mutableListOf(
        SequenceAtlasAnimation(
                AnimationKey.BURN,
                FrameParametersGenerator.generate(Pair(0,3), Pair(0,0), 0.2f, 1.0f),
                timeLimit = 0.1f
        )
)