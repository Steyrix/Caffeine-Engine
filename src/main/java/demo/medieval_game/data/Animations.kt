package demo.medieval_game.data

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
    const val STRIKE_U = "STRIKE_U"
    const val STRIKE_D = "STRIKE_D"

    const val GOBLIN_WALK_R = "GOB_WALK_R"
    const val GOBLIN_WALK_L = "GOB_WALK_L"
    const val GOBLIN_WALK_U = "GOB_WALK_U"
    const val GOBLIN_WALK_D = "GOB_WALK_D"
    const val GOBLIN_IDLE_R = "GOB_IDLE_R"
    const val GOBLIN_IDLE_L = "GOB_IDLE_L"
    const val GOBLIN_IDLE_U = "GOB_IDLE_U"
    const val GOBLIN_IDLE_D = "GOB_IDLE_D"
    const val GOBLIN_STRIKE_R = "GOB_STRIKE_R"
    const val GOBLIN_STRIKE_L = "GOB_STRIKE_L"
    const val GOBLIN_STRIKE_U = "GOB_STRIKE_U"
    const val GOBLIN_STRIKE_D = "GOB_STRIKE_D"
    const val GOBLIN_DEFEAT = "GOB_DEFEAT"

    const val BURN = "BURN"
    const val HIT = "HIT"
}

val characterAnimations = mutableListOf<Animation>(
        SequenceAtlasAnimation(
                name = AnimationKey.IDLE_R,
                frames = FrameParametersGenerator.generate(Pair(0, 0), Pair(3, 3), 0.066f, 0.25f),
                timeLimit = 1f
        ),
        SequenceAtlasAnimation(
                name = AnimationKey.IDLE_L,
                frames = FrameParametersGenerator.generate(Pair(0, 0), Pair(1, 1), 0.066f, 0.25f),
                timeLimit = 1f
        ),
        SequenceAtlasAnimation(
                name = AnimationKey.IDLE_U,
                frames = FrameParametersGenerator.generate(Pair(0, 0), Pair(0, 0), 0.066f, 0.25f),
                timeLimit = 1f
        ),
        SequenceAtlasAnimation(
                name = AnimationKey.IDLE_D,
                frames = FrameParametersGenerator.generate(Pair(0, 0), Pair(2, 2), 0.066f, 0.25f),
                timeLimit = 1f
        ),
        SequenceAtlasAnimation(
                name = AnimationKey.WALK_R,
                frames = FrameParametersGenerator.generate(Pair(1, 8), Pair(3, 3), 0.066f, 0.25f),
                timeLimit = 0.05f
        ),
        SequenceAtlasAnimation(
                name = AnimationKey.WALK_L,
                frames = FrameParametersGenerator.generate(Pair(8, 1), Pair(1, 1), 0.066f, 0.25f),
                timeLimit = 0.05f
        ),
        SequenceAtlasAnimation(
                name = AnimationKey.WALK_U,
                frames = FrameParametersGenerator.generate(Pair(1, 8), Pair(0, 0), 0.066f, 0.25f),
                timeLimit = 0.05f
        ),
        SequenceAtlasAnimation(
                name = AnimationKey.WALK_D,
                frames = FrameParametersGenerator.generate(Pair(1, 8), Pair(2, 2), 0.066f, 0.25f),
                timeLimit = 0.05f
        ),
        SequenceAtlasAnimation(
                name = AnimationKey.STRIKE_U,
                frames = FrameParametersGenerator.generate(Pair(14, 10), Pair(0, 0), 0.066f, 0.25f),
                timeLimit = 0.1f
        ),
        SequenceAtlasAnimation(
                name = AnimationKey.STRIKE_L,
                frames = FrameParametersGenerator.generate(Pair(10, 14), Pair(1, 1), 0.066f, 0.25f),
                timeLimit = 0.1f
        ),
        SequenceAtlasAnimation(
                name = AnimationKey.STRIKE_D,
                frames = FrameParametersGenerator.generate(Pair(14, 10), Pair(2, 2), 0.066f, 0.25f),
                timeLimit = 0.1f
        ),
        SequenceAtlasAnimation(
                name = AnimationKey.STRIKE_R,
                frames = FrameParametersGenerator.generate(Pair(10, 14), Pair(3, 3), 0.066f, 0.25f),
                timeLimit = 0.1f
        ),
)

val goblinsAnimations = mutableListOf<Animation>(
        SequenceAtlasAnimation(
                name = AnimationKey.GOBLIN_IDLE_R,
                frames = FrameParametersGenerator.generate(Pair(6, 6), Pair(1, 1), 0.09f, 0.2f),
                timeLimit = 1f
        ),
        SequenceAtlasAnimation(
                name = AnimationKey.GOBLIN_IDLE_L,
                frames = FrameParametersGenerator.generate(Pair(6, 6), Pair(3, 3), 0.09f, 0.2f),
                timeLimit = 1f
        ),
        SequenceAtlasAnimation(
                name = AnimationKey.GOBLIN_IDLE_U,
                frames = FrameParametersGenerator.generate(Pair(6, 6), Pair(2, 2), 0.09f, 0.2f),
                timeLimit = 1f
        ),
        SequenceAtlasAnimation(
                name = AnimationKey.GOBLIN_IDLE_D,
                frames = FrameParametersGenerator.generate(Pair(6, 6), Pair(0, 0), 0.09f, 0.2f),
                timeLimit = 1f
        ),
        SequenceAtlasAnimation(
                name = AnimationKey.GOBLIN_WALK_R,
                frames = FrameParametersGenerator.generate(Pair(0, 5), Pair(1, 1), 0.09f, 0.2f),
                timeLimit = 0.25f
        ),
        SequenceAtlasAnimation(
                name = AnimationKey.GOBLIN_WALK_L,
                frames = FrameParametersGenerator.generate(Pair(0, 5), Pair(3, 3), 0.09f, 0.2f),
                timeLimit = 0.25f
        ),
        SequenceAtlasAnimation(
                name = AnimationKey.GOBLIN_WALK_U,
                frames = FrameParametersGenerator.generate(Pair(0, 5), Pair(2, 2), 0.09f, 0.2f),
                timeLimit = 0.25f
        ),
        SequenceAtlasAnimation(
                name = AnimationKey.GOBLIN_WALK_D,
                frames = FrameParametersGenerator.generate(Pair(0, 5), Pair(0, 0), 0.09f, 0.2f),
                timeLimit = 0.25f
        ),
        SequenceAtlasAnimation(
                name = AnimationKey.GOBLIN_DEFEAT,
                isReplayable = false,
                frames = FrameParametersGenerator.generate(Pair(0, 4), Pair(4, 4), 0.09f, 0.2f),
                timeLimit = 0.25f
        ),
        SequenceAtlasAnimation(
                name = AnimationKey.GOBLIN_STRIKE_D,
                frames = FrameParametersGenerator.generate(Pair(7, 10), Pair(0, 0), 0.09f, 0.2f),
                timeLimit = 0.1f
        ),
        SequenceAtlasAnimation(
                name = AnimationKey.GOBLIN_STRIKE_R,
                frames = FrameParametersGenerator.generate(Pair(7, 10), Pair(1, 1), 0.09f, 0.2f),
                timeLimit = 0.1f
        ),
        SequenceAtlasAnimation(
                name = AnimationKey.GOBLIN_STRIKE_U,
                frames = FrameParametersGenerator.generate(Pair(7, 10), Pair(2, 2), 0.09f, 0.2f),
                timeLimit = 0.1f
        ),
        SequenceAtlasAnimation(
                name = AnimationKey.GOBLIN_STRIKE_L,
                frames = FrameParametersGenerator.generate(Pair(7, 10), Pair(3, 3), 0.09f, 0.2f),
                timeLimit = 0.1f
        ),
)

val campfireAnimations = mutableListOf<Animation>(
        SequenceAtlasAnimation(
                name = AnimationKey.BURN,
                frames = FrameParametersGenerator.generate(Pair(0, 3), Pair(0, 0), 0.2f, 1.0f),
                timeLimit = 0.1f
        )
)

val hitAnimation = mutableListOf<Animation>(
        SequenceAtlasAnimation(
                name = AnimationKey.HIT,
                frames = FrameParametersGenerator.generate(Pair(0, 3), Pair(0, 3), 0.25f, 0.25f),
                timeLimit = 0.05f
        )
)