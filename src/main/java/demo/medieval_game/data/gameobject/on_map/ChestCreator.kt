package demo.medieval_game.data.gameobject.on_map

import engine.core.game_object.SingleGameEntity
import engine.core.update.SetOfStatic2DParameters
import engine.feature.interaction.BoxInteractionContext
import org.joml.Matrix4f

enum class ChestType(val path: String) {
    WOODEN("/textures/WoodenChestAtlas.png"),
    IRON("/textures/IronChestAtlas.png"),
    RUSTY_IRON("/textures/RustyIronChestAtlas.png"),
    BLUE("/textures/BlueChestAtlas.png"),
    GREEN("/textures/GreenChestAtlas.png"),
    PURPLE("/textures/PurpleChestAtlas.png")
}

object ChestCreator {

    fun create(
        renderProjection: Matrix4f,
        boxInteractionContext: BoxInteractionContext,
        type: ChestType,
        parameters: SetOfStatic2DParameters
    ): SingleGameEntity {
        val out = Chest(parameters).also {
            it.init(
                renderProjection,
                boxInteractionContext,
                type.path
            )
        }

        return out
    }
}