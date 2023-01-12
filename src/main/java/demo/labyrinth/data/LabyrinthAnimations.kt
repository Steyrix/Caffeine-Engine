package demo.labyrinth.data

import engine.feature.animation.Animation
import engine.feature.animation.FrameParametersGenerator
import engine.feature.animation.SequenceAtlasAnimation

object AnimationKey {
    const val WALK_R = "WALK_R"
    const val WALK_L = "WALK_L"
    const val IDLE_R = "IDLE_R"
    const val IDLE_L = "IDLE_L"
    const val JUMP_R = "JUMP_R"
    const val JUMP_L = "JUMP_L"

    const val WALK_R_SKEL = "WALK_R_S"
    const val WALK_L_SKEL = "WALK_L_S"
    const val IDLE_R_SKEL = "IDLE_R_S"
    const val IDLE_L_SKEL = "IDLE_L_S"
    const val JUMP_R_SKEL = "JUMP_R_S"
    const val JUMP_L_SKEL = "JUMP_L_S"

    const val BURN = "BURN"
}

val characterAnimations = mutableListOf<Animation>(
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

val skeletonAnimations = mutableListOf<Animation>(
        SequenceAtlasAnimation(
                AnimationKey.IDLE_R_SKEL,
                FrameParametersGenerator.generate(Pair(0,0), Pair(0,0), 0.1f, 0.166f),
                timeLimit = 1f
        ),
        SequenceAtlasAnimation(
                AnimationKey.IDLE_L_SKEL,
                FrameParametersGenerator.generate(Pair(9,9), Pair(6,6), 0.1f, 0.166f),
                timeLimit = 1f
        ),
        SequenceAtlasAnimation(
                AnimationKey.WALK_R_SKEL,
                FrameParametersGenerator.generate(Pair(1,6), Pair(0,0), 0.1f, 0.166f),
                timeLimit = 0.2f
        ),
        SequenceAtlasAnimation(
                AnimationKey.WALK_L_SKEL,
                FrameParametersGenerator.generate(Pair(8,3), Pair(6,6), 0.1f, 0.166f),
                timeLimit = 0.2f
        ),
        SequenceAtlasAnimation(
                AnimationKey.JUMP_R_SKEL,
                FrameParametersGenerator.generate(Pair(7,9), Pair(1,1), 0.1f, 0.166f),
                timeLimit = 0.5f
        ),
        SequenceAtlasAnimation(
                AnimationKey.JUMP_L_SKEL,
                FrameParametersGenerator.generate(Pair(0,2), Pair(7,7), 0.1f, 0.166f),
                timeLimit = 0.5f
        )
)

val campfireAnimationss = mutableListOf<Animation>(
        SequenceAtlasAnimation(
                AnimationKey.BURN,
                FrameParametersGenerator.generate(Pair(0,3), Pair(0,0), 0.2f, 1.0f),
                timeLimit = 0.1f
        )
)