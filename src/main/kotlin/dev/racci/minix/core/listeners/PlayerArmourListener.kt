@file:Suppress("SpellCheckingInspection")

package dev.racci.minix.core.listeners
//
// class ArmourListener : KotlinListener {
//
//    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
//    fun inventoryClick(event: InventoryClickEvent) {
//
//        var shift = false
//        var numberKey = false
//
//        if(event.isCancelled || event.action == InventoryAction.NOTHING) return
//        if(event.clickedInventory?.type != InventoryType.PLAYER) return
//        if(event.slotType != InventoryType.SlotType.ARMOR && event.slotType != InventoryType.SlotType.QUICKBAR && event.slotType != InventoryType.SlotType.CONTAINER) return
//        if(event.inventory.type != InventoryType.CRAFTING && event.inventory.type != InventoryType.PLAYER) return
//        if(event.whoClicked !is Player) return
//        if(event.click == ClickType.SHIFT_LEFT || event.click == ClickType.SHIFT_RIGHT) shift = true
//        if(event.click == ClickType.NUMBER_KEY) numberKey = true
//
//        var newArmourType = ArmourType.matchType(if(shift) event.currentItem else event.cursor)
//        if(!shift && newArmourType != null && event.rawSlot != newArmourType.slot) return
//
//        if(shift) {
//            newArmourType = ArmourType.matchType(event.currentItem) ?: return
//            var equipping = true
//            if(event.rawSlot == newArmourType.slot) equipping = false
//            if (newArmourType == ArmourType.HELMET
//                && equipping == isAirOrNull(event.whoClicked.inventory.helmet)
//                || newArmourType == ArmourType.CHESTPLATE
//                && equipping == isAirOrNull(event.whoClicked.inventory.chestplate)
//                || newArmourType == ArmourType.LEGGINGS
//                && equipping == isAirOrNull(event.whoClicked.inventory.leggings)
//                || newArmourType == ArmourType.BOOTS
//                && equipping == isAirOrNull(event.whoClicked.inventory.boots)
//            ) {
//                pm.callEvent(PlayerArmourEquipEvent(event.whoClicked as Player))
//            }
//        } else {
//            if (numberKey) {
//                if (event.clickedInventory!!.type == InventoryType.PLAYER) {
//                    val hotbarItem = event.clickedInventory!!.getItem(event.hotbarButton)
//                    newArmourType = if (!isAirOrNull(hotbarItem)) {
//                        ArmourType.matchType(hotbarItem)
//                    } else {
//                        ArmourType.matchType(if (!isAirOrNull(event.currentItem)) event.currentItem else event.cursor)
//                    }
//                }
//            } else {
//                if (isAirOrNull(event.cursor) && !isAirOrNull(event.currentItem)) {
//                    newArmourType = ArmourType.matchType(event.currentItem)
//                }
//            }
//            if (newArmourType != null && event.rawSlot == newArmourType.slot) {
//                pm.callEvent(PlayerArmourEquipEvent(event.whoClicked as Player))
//            }
//        }
//    }
//
//    @EventHandler(priority = EventPriority.HIGHEST)
//    fun onPlayerInteract(event: PlayerInteractEvent) {
//
//        if(event.useItemInHand() == Event.Result.DENY || event.action == Action.PHYSICAL) return
//        if(event.action != Action.RIGHT_CLICK_AIR || event.action != Action.RIGHT_CLICK_BLOCK) {
//            val newArmourType = ArmourType.matchType(event.item) ?: return
//            if (newArmourType == ArmourType.HELMET
//                && isAirOrNull(event.player.inventory.helmet)
//                || newArmourType == ArmourType.CHESTPLATE
//                && isAirOrNull(event.player.inventory.chestplate)
//                || newArmourType == ArmourType.LEGGINGS
//                && isAirOrNull(event.player.inventory.leggings)
//                || newArmourType == ArmourType.BOOTS
//                && isAirOrNull(event.player.inventory.boots)
//            ) {
//                pm.callEvent(PlayerArmourEquipEvent(event.player))
//            }
//        }
//    }
//
//    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
//    fun inventoryDrag(event: InventoryDragEvent) {
//        val type = ArmourType.matchType(event.oldCursor)
//        if (event.rawSlots.isEmpty()) {
//            return
//        }
//        if (type != null && type.slot == event.rawSlots.stream().findFirst().orElse(0)) {
//            val armorEquipEvent = PlayerArmourEquipEvent(
//                (event.whoClicked as Player)
//            )
//            Bukkit.getPluginManager().callEvent(armorEquipEvent)
//        }
//    }
//
//    @EventHandler
//    fun playerJoinEvent(event: PlayerJoinEvent) {
//        val armorEquipEvent = PlayerArmourEquipEvent(event.player)
//        Bukkit.getPluginManager().callEvent(armorEquipEvent)
//    }
//
//    @EventHandler
//    fun playerRespawnEvent(event: PlayerRespawnEvent) {
//        val armorEquipEvent = PlayerArmourEquipEvent(event.player)
//        Bukkit.getPluginManager().callEvent(armorEquipEvent)
//    }
//
//    @EventHandler
//    fun itemBreakEvent(event: PlayerItemBreakEvent) {
//        val type = ArmourType.matchType(event.brokenItem)
//        if (type != null) {
//            val p = event.player
//            val armorEquipEvent = PlayerArmourEquipEvent(p)
//            Bukkit.getPluginManager().callEvent(armorEquipEvent)
//        }
//    }
//
//    @EventHandler
//    fun playerDeathEvent(event: PlayerDeathEvent) {
//        val p = event.entity
//        if (event.keepInventory) {
//            return
//        }
//        for (i in p.inventory.armorContents) {
//            if (!isAirOrNull(i)) {
//                Bukkit.getPluginManager().callEvent(PlayerArmourEquipEvent(p))
//            }
//        }
//    }
//
//    companion object {
//        fun isAirOrNull(item: ItemStack?): Boolean {
//            return item == null || item.type == Material.AIR
//        }
//    }
// }
//
// class ArmorChangeEventListeners : KotlinListener {
//
//    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
//    fun onArmourChange(event: PlayerArmourEquipEvent) {
//        val player = event.player
//        val before = player.inventory.armorContents
//        skeduleAsync(racciCore) {
//            delay(20)
//            PlayerArmourChangeEvent(
//                    player,
//                    before,
//                    player.inventory.armorContents,
//                ).callEvent()
//        }
//    }
//
//    @EventHandler
//    fun dispenseArmourEvent(event: BlockDispenseArmorEvent) = skeduleAsync(racciCore) {
//        val type = ArmourType.matchType(event.item)
//        if (type != null && event.targetEntity is Player) {
//            pm.callEvent(PlayerArmourEquipEvent(event.targetEntity as Player))
//        }
//    }
// }
//
// enum class ArmourType(val slot: Int) {
//    HELMET(5), CHESTPLATE(6), LEGGINGS(7), BOOTS(8);
//
//    companion object {
//        fun matchType(hItemStack: ItemStack?): ArmourType? {
//            if (ArmourListener.isAirOrNull(hItemStack)) {
//                return null
//            }
//            hItemStack ?: return null
//            val type = hItemStack.type.name
//            return if (type.endsWith("_HELMET") || type.endsWith("_SKULL") || type.endsWith("PLAYER_HEAD")) {
//                HELMET
//            } else if (type.endsWith("_CHESTPLATE") || type.endsWith("ELYTRA")) {
//                CHESTPLATE
//            } else if (type.endsWith("_LEGGINGS")) {
//                LEGGINGS
//            } else if (type.endsWith("_BOOTS")) {
//                BOOTS
//            } else {
//                null
//            }
//        }
//    }
// }
