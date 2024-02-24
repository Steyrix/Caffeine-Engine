package demo.medieval_game.data.gameobject.character

import engine.feature.animation.Animation
import engine.feature.animation.FrameParametersGenerator
import engine.feature.animation.SequenceAtlasAnimation

object CharacterAnimations {
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
    const val ALT_STRIKE_R = "ALT_STRIKE_R"
    const val ALT_STRIKE_L = "ALT_STRIKE_L"
    const val ALT_STRIKE_U = "ALT_STRIKE_U"
    const val ALT_STRIKE_D = "ALT_STRIKE_D"
}

val characterWithSwordAnimation = mutableListOf<Animation>(
    SequenceAtlasAnimation(
        name = CharacterAnimations.IDLE_R,
        frames = FrameParametersGenerator.generate(Pair(0, 0), Pair(4, 4), 0.1111f, 0.0714f),
        timeLimit = 1f
    ),
    SequenceAtlasAnimation(
        name = CharacterAnimations.IDLE_L,
        frames = FrameParametersGenerator.generate(Pair(0, 0), Pair(0, 0), 0.1111f, 0.0714f),
        timeLimit = 1f
    ),
    SequenceAtlasAnimation(
        name = CharacterAnimations.IDLE_U,
        frames = FrameParametersGenerator.generate(Pair(0, 0), Pair(11, 11), 0.1111f, 0.0714f),
        timeLimit = 1f
    ),
    SequenceAtlasAnimation(
        name = CharacterAnimations.IDLE_D,
        frames = FrameParametersGenerator.generate(Pair(0, 0), Pair(8, 8), 0.1111f, 0.0714f),
        timeLimit = 1f
    ),
    SequenceAtlasAnimation(
        name = CharacterAnimations.WALK_R,
        frames = FrameParametersGenerator.generate(Pair(1, 8), Pair(4, 4), 0.1111f, 0.0714f),
        timeLimit = 0.05f
    ),
    SequenceAtlasAnimation(
        name = CharacterAnimations.WALK_L,
        frames = FrameParametersGenerator.generate(Pair(8, 1), Pair(0, 0), 0.1111f, 0.0714f),
        timeLimit = 0.05f
    ),
    SequenceAtlasAnimation(
        name = CharacterAnimations.WALK_U,
        frames = FrameParametersGenerator.generate(Pair(1, 8), Pair(11, 11), 0.1111f, 0.0714f),
        timeLimit = 0.05f
    ),
    SequenceAtlasAnimation(
        name = CharacterAnimations.WALK_D,
        frames = FrameParametersGenerator.generate(Pair(1, 8), Pair(8, 8), 0.1111f, 0.0714f),
        timeLimit = 0.05f
    ),
    SequenceAtlasAnimation(
        name = CharacterAnimations.STRIKE_U,
        frames = FrameParametersGenerator.generate(Pair(0, 5), Pair(12, 12), 0.1111f, 0.0714f),
        timeLimit = 0.1f
    ),
    SequenceAtlasAnimation(
        name = CharacterAnimations.ALT_STRIKE_U,
        frames = FrameParametersGenerator.generate(Pair(0, 5), Pair(13, 13), 0.1111f, 0.0714f),
        timeLimit = 0.1f
    ),
    SequenceAtlasAnimation(
        name = CharacterAnimations.STRIKE_L,
        frames = FrameParametersGenerator.generate(Pair(0, 5), Pair(1, 1), 0.1111f, 0.0714f),
        timeLimit = 0.1f
    ),
    SequenceAtlasAnimation(
        name = CharacterAnimations.ALT_STRIKE_L,
        frames = FrameParametersGenerator.generate(Pair(0, 5), Pair(2, 2), 0.1111f, 0.0714f),
        timeLimit = 0.1f
    ),
    SequenceAtlasAnimation(
        name = CharacterAnimations.STRIKE_D,
        frames = FrameParametersGenerator.generate(Pair(0, 5), Pair(9, 9), 0.1111f, 0.0714f),
        timeLimit = 0.1f
    ),
    SequenceAtlasAnimation(
        name = CharacterAnimations.ALT_STRIKE_D,
        frames = FrameParametersGenerator.generate(Pair(0, 5), Pair(10, 10), 0.1111f, 0.0714f),
        timeLimit = 0.1f
    ),
    SequenceAtlasAnimation(
        name = CharacterAnimations.STRIKE_R,
        frames = FrameParametersGenerator.generate(Pair(0, 5), Pair(5, 5), 0.1111f, 0.0714f),
        timeLimit = 0.1f
    ),
    SequenceAtlasAnimation(
        name = CharacterAnimations.ALT_STRIKE_R,
        frames = FrameParametersGenerator.generate(Pair(0, 5), Pair(6, 6), 0.1111f, 0.0714f),
        timeLimit = 0.1f
    ),
)

val characterAnimations = mutableListOf<Animation>(
    SequenceAtlasAnimation(
        name = CharacterAnimations.IDLE_R,
        frames = FrameParametersGenerator.generate(Pair(0, 0), Pair(3, 3), 0.066f, 0.25f),
        timeLimit = 1f
    ),
    SequenceAtlasAnimation(
        name = CharacterAnimations.IDLE_L,
        frames = FrameParametersGenerator.generate(Pair(0, 0), Pair(1, 1), 0.066f, 0.25f),
        timeLimit = 1f
    ),
    SequenceAtlasAnimation(
        name = CharacterAnimations.IDLE_U,
        frames = FrameParametersGenerator.generate(Pair(0, 0), Pair(0, 0), 0.066f, 0.25f),
        timeLimit = 1f
    ),
    SequenceAtlasAnimation(
        name = CharacterAnimations.IDLE_D,
        frames = FrameParametersGenerator.generate(Pair(0, 0), Pair(2, 2), 0.066f, 0.25f),
        timeLimit = 1f
    ),
    SequenceAtlasAnimation(
        name = CharacterAnimations.WALK_R,
        frames = FrameParametersGenerator.generate(Pair(1, 8), Pair(3, 3), 0.066f, 0.25f),
        timeLimit = 0.05f
    ),
    SequenceAtlasAnimation(
        name = CharacterAnimations.WALK_L,
        frames = FrameParametersGenerator.generate(Pair(8, 1), Pair(1, 1), 0.066f, 0.25f),
        timeLimit = 0.05f
    ),
    SequenceAtlasAnimation(
        name = CharacterAnimations.WALK_U,
        frames = FrameParametersGenerator.generate(Pair(1, 8), Pair(0, 0), 0.066f, 0.25f),
        timeLimit = 0.05f
    ),
    SequenceAtlasAnimation(
        name = CharacterAnimations.WALK_D,
        frames = FrameParametersGenerator.generate(Pair(1, 8), Pair(2, 2), 0.066f, 0.25f),
        timeLimit = 0.05f
    ),
    SequenceAtlasAnimation(
        name = CharacterAnimations.STRIKE_U,
        frames = FrameParametersGenerator.generate(Pair(14, 10), Pair(0, 0), 0.066f, 0.25f),
        timeLimit = 0.1f
    ),
    SequenceAtlasAnimation(
        name = CharacterAnimations.STRIKE_L,
        frames = FrameParametersGenerator.generate(Pair(10, 14), Pair(1, 1), 0.066f, 0.25f),
        timeLimit = 0.1f
    ),
    SequenceAtlasAnimation(
        name = CharacterAnimations.STRIKE_D,
        frames = FrameParametersGenerator.generate(Pair(14, 10), Pair(2, 2), 0.066f, 0.25f),
        timeLimit = 0.1f
    ),
    SequenceAtlasAnimation(
        name = CharacterAnimations.STRIKE_R,
        frames = FrameParametersGenerator.generate(Pair(10, 14), Pair(3, 3), 0.066f, 0.25f),
        timeLimit = 0.1f
    ),
)
