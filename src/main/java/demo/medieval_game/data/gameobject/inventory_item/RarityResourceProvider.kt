package demo.medieval_game.data.gameobject.inventory_item

fun ItemRarity.getResourcePath(): String {
    return when(this) {
        ItemRarity.COMMON -> this.javaClass.getResource("/textures/gui/chest/DoubleCellCommon.png")!!.path
        else -> this.javaClass.getResource("/textures/gui/chest/DoubleCellCommon.png")!!.path
    }
}