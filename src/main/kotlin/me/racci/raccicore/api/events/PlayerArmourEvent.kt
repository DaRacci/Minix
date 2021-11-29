package me.racci.raccicore.api.events

//import org.bukkit.Material
//import org.bukkit.entity.Player
//import org.bukkit.inventory.EquipmentSlot
//import org.bukkit.inventory.ItemStack
//
//
///**
// * The armor change event **does** contain information about the event.
// *
// *
// * Unlike [PlayerArmourEquipEvent], it is called the next tick and contains previous and current armor contents.
// */
//class PlayerArmourChangeEvent(
//    player: Player,
//    val before: Array<out ItemStack?>,
//    val after: Array<out ItemStack?>,
//) : KPlayerEvent(player, true) {
//
//    var slot : EquipmentSlot? = null ; private set
//
//    /**
//     * Change
//     * 0 = Taking off armour
//     * 1 = Equipping armour
//     * 2 = Changing armour
//     */
//    var change : Int = 0 ; private set
//    var newItem : ItemStack = ItemStack(Material.AIR)
//    var oldItem : ItemStack = ItemStack(Material.AIR)
//
//    init {
//        for(i in 0..3) {
//            if(before[i] != after[i]) {
//                newItem = after[i] ?: continue
//                oldItem = before[i] ?: continue
//                if(before[i] == null) {
//                    change = 1
//                } else if(after[i] != null) {
//                    change = 2
//                }
//                slot = when(i) {
//                    0 -> {EquipmentSlot.FEET}
//                    1 -> {EquipmentSlot.LEGS}
//                    2 -> {EquipmentSlot.CHEST}
//                    else -> {EquipmentSlot.HEAD}
//                } ; break
//            }
//        }
//    }
//
//    val beforeHelmet        get() = before[3]
//    val beforeChestplate    get() = before[2]
//    val beforeLeggings      get() = before[1]
//    val beforeBoots         get() = before[0]
//
//    var helmet
//        get() = after[3]
//        set(value) {player.inventory.helmet = value}
//    var chestplate
//        get() = after[2]
//        set(value) {player.inventory.chestplate = value}
//    var leggings
//        get() = after[1]
//        set(value) {player.inventory.leggings = value}
//    var boots
//        get() = after[0]
//        set(value) {player.inventory.boots = value}
//
//}
//
///**
// * The armor equip event <b>does not contain information about the event.</b>
// * <p>
// * It is purely a trigger called whenever a player changes armor, you have to run
// * your own checks.
// * <p>
// * The event is called before the player's inventory actually updates,
// * so you can check a tick later to see the new contents.
// *
// * @see PlayerArmourChangeEvent
// */
//class PlayerArmourEquipEvent(
//    player: Player
//) : KPlayerEvent(player) {
//
//
//
//}