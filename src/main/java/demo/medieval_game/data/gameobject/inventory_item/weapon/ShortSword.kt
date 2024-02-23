package demo.medieval_game.data.gameobject.inventory_item.weapon


import demo.medieval_game.data.gameobject.inventory_item.ItemRarity
import engine.core.render.Model
import engine.core.texture.Texture2D
import engine.feature.animation.Animation

abstract class ShortSword(
    texturePath: String,
    override val baseDamage: Float,
    override val rarity: ItemRarity,
    override val afterEquipAnimation: List<Animation>,
) : Weapon() {

    override val onUseAnimations = emptyList<Animation>()

    final override val drawableComponent: Model

    init {
        drawableComponent = Model(
            texture = Texture2D.createInstance(texturePath)
        )
    }
}