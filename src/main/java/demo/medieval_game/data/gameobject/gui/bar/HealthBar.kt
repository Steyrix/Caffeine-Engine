package demo.medieval_game.data.gameobject.gui.bar

import demo.medieval_game.interaction.PlayerAttackInteraction
import engine.core.update.SetOfParameters
import engine.core.update.SetOfStatic2DParameters
import engine.feature.interaction.Interaction
import org.joml.Matrix4f

class HealthBar(
    objParams: SetOfParameters,
    barParams: SetOfStatic2DParameters,
    projection: Matrix4f,
    isBoundToParams: Boolean = true,
    texturePath: String,
    onFilledChange: (Float) -> Unit = {},
    isPartOfWorldTranslation: Boolean = true
) : ResourceBar(
    objParams,
    barParams,
    projection,
    onFilledChange,
    isBoundToParams,
    texturePath,
    isPartOfWorldTranslation
) {

    override fun consumeInteraction(interaction: Interaction) {
        when (interaction) {
            is PlayerAttackInteraction -> {
                filled -= interaction.damage
                onFilledChange(filled)
            }
        }
    }
}