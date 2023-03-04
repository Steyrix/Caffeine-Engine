package demo.labyrinth.data

import engine.feature.animation.Animation
import engine.feature.animation.FrameParametersGenerator
import engine.feature.animation.SequenceAtlasAnimation

object AnimationKey {
    const val WALK_R = "WALK_R"
    const val WALK_L = "WALK_L"
    const val WALK_U = "WALK_U"
    const val WALK_D = "WALK_D"
    const val IDLE_R = "IDLE_R"
    const val IDLE_L = "IDLE_L"
    const val IDLE_U = "IDLE_U"
    const val IDLE_D = "IDLE_D"
    const val STRIKE_R = "STRIKE_R"
    const val STRIKE_L = "STRIKE_L"

    const val GOBLIN_WALK_R = "WALK_R_S"
    const val GOBLIN_WALK_L = "WALK_L_S"
    const val GOBLIN_WALK_U = "WALK_U_S"
    const val GOBLIN_WALK_D = "WALK_D_S"
    const val GOBLIN_IDLE_R = "IDLE_R_S"
    const val GOBLIN_IDLE_L = "IDLE_L_S"
    const val GOBLIN_IDLE_U = "IDLE_U_S"
    const val GOBLIN_IDLE_D = "IDLE_D_S"

    const val BURN = "BURN"
}

val characterAnimations = mutableListOf<Animation>(
        SequenceAtlasAnimation(
                AnimationKey.IDLE_R,
                FrameParametersGenerator.generate(Pair(0,0), Pair(3,3), 0.111f, 0.25f),
                timeLimit = 1f
        ),
        SequenceAtlasAnimation(
                AnimationKey.IDLE_L,
                FrameParametersGenerator.generate(Pair(0,0), Pair(1,1), 0.111f, 0.25f),
                timeLimit = 1f
        ),
        SequenceAtlasAnimation(
                AnimationKey.IDLE_U,
                FrameParametersGenerator.generate(Pair(0,0), Pair(0,0), 0.111f, 0.25f),
                timeLimit = 1f
        ),
        SequenceAtlasAnimation(
                AnimationKey.IDLE_D,
                FrameParametersGenerator.generate(Pair(0,0), Pair(2,2), 0.111f, 0.25f),
                timeLimit = 1f
        ),
        SequenceAtlasAnimation(
                AnimationKey.WALK_R,
                FrameParametersGenerator.generate(Pair(1,8), Pair(3,3), 0.111f, 0.25f),
                timeLimit = 0.05f
        ),
        SequenceAtlasAnimation(
                AnimationKey.WALK_L,
                FrameParametersGenerator.generate(Pair(8,1), Pair(1,1), 0.111f, 0.25f),
                timeLimit = 0.05f
        ),
        SequenceAtlasAnimation(
                AnimationKey.WALK_U,
                FrameParametersGenerator.generate(Pair(1,8), Pair(0,0), 0.111f, 0.25f),
                timeLimit = 0.05f
        ),
        SequenceAtlasAnimation(
                AnimationKey.WALK_D,
                FrameParametersGenerator.generate(Pair(1,8), Pair(2,2), 0.111f, 0.25f),
                timeLimit = 0.05f
        ),
)

val goblinsAnimations = mutableListOf<Animation>(
        SequenceAtlasAnimation(
                AnimationKey.GOBLIN_IDLE_R,
                FrameParametersGenerator.generate(Pair(6,6), Pair(1,1), 0.09f, 0.2f),
                timeLimit = 1f
        ),
        SequenceAtlasAnimation(
                AnimationKey.GOBLIN_IDLE_L,
                FrameParametersGenerator.generate(Pair(6,6), Pair(3,3), 0.09f, 0.2f),
                timeLimit = 1f
        ),
        SequenceAtlasAnimation(
                AnimationKey.GOBLIN_IDLE_U,
                FrameParametersGenerator.generate(Pair(6,6), Pair(2,2), 0.09f, 0.2f),
                timeLimit = 1f
        ),
        SequenceAtlasAnimation(
                AnimationKey.GOBLIN_IDLE_D,
                FrameParametersGenerator.generate(Pair(6,6), Pair(0,0), 0.09f, 0.2f),
                timeLimit = 1f
        ),
        SequenceAtlasAnimation(
                AnimationKey.GOBLIN_WALK_R,
                FrameParametersGenerator.generate(Pair(0,5), Pair(1,1), 0.09f, 0.2f),
                timeLimit = 0.05f
        ),
        SequenceAtlasAnimation(
                AnimationKey.GOBLIN_WALK_L,
                FrameParametersGenerator.generate(Pair(0,5), Pair(3,3), 0.09f, 0.2f),
                timeLimit = 0.05f
        ),
        SequenceAtlasAnimation(
                AnimationKey.GOBLIN_WALK_U,
                FrameParametersGenerator.generate(Pair(0,5), Pair(2,2), 0.09f, 0.2f),
                timeLimit = 0.05f
        ),
        SequenceAtlasAnimation(
                AnimationKey.GOBLIN_WALK_D,
                FrameParametersGenerator.generate(Pair(0,5), Pair(0,0), 0.09f, 0.2f),
                timeLimit = 0.05f
        ),
)

val campfireAnimationss = mutableListOf<Animation>(
        SequenceAtlasAnimation(
                AnimationKey.BURN,
                FrameParametersGenerator.generate(Pair(0,3), Pair(0,0), 0.2f, 1.0f),
                timeLimit = 0.1f
        )
)