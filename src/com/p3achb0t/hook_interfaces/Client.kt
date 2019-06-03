package com.p3achb0t.hook_interfaces

interface Client : GameShell {
    fun get_accountStatus(): Int
    fun get_baseX(): Int
    fun get_baseY(): Int
    fun get_cameraPitch(): Int
    fun get_cameraX(): Int
    fun get_cameraY(): Int
    fun get_cameraYaw(): Int
    fun get_cameraZ(): Int
    //    fun get_canvas(): Any
//    fun get_clanMembersHandler(): Any
    fun get_clickModifier(): Int

    //    fun get_collisionMaps(): Any
    fun get_crosshairColor(): Int
    fun get_currentWorld(): Int
    fun get_destinationX(): Int
    fun get_destinationY(): Int
    //    fun get_experience(): Any
//    fun get_fps(): Any
    fun get_gameCycle(): Int

    //    fun get_gameSettings(): Array<Int>
//    fun get_gameSocket(): Any
    fun get_gameState(): Int

    //    fun get_grandExchangeItems(): Any
    fun get_groundItemList(): Array<Array<Array<*>>>
//    fun get_groundItemModelCache(): Any
//    fun get_hintArrowNPCIndex(): Any
//    fun get_hintArrowPlayerIndex(): Any
//    fun get_hintArrowType(): Any
//    fun get_hintArrowX(): Any
//    fun get_hintArrowY(): Any
//    fun get_hintArrowZ(): Any
//    fun get_host(): Any
    fun get_idleTime(): Int

    fun get_isSpellSelected(): Boolean
    fun get_isWorldSelectorOpen(): Boolean
//    fun get_itemTable(): Any
//    fun get_keyboard(): Any
    fun get_lastAction(): Int
    fun get_lastActionDifference(): Int
    fun get_lastActionDifferenceMod(): Int
    fun get_lastActionTime(): Int
    fun get_lastActionTimeMod(): Int
    fun get_lastButtonClick(): Int
    fun get_lastButtonClickModA(): Int
    fun get_lastButtonClickModM(): Int
    fun get_lastClickModifier(): Int
    fun get_lastClickModifierModA(): Int
    fun get_lastClickModifierModM(): Int
    fun get_lastClickX(): Int
    fun get_lastClickY(): Int
    //    fun get_level(): Any
//    fun get_localNPCs(): Array<Npc>
//    fun get_localPlayer(): Any
    fun get_loginState(): Int
    fun get_lowestAvailableCameraPitch(): Int
    fun get_mapAngle(): Int
    //    fun get_menuActions(): Any
    fun get_menuCount(): Int
    fun get_menuHeight(): Int
    //    fun get_menuOpcodes(): Any
//    fun get_menuOptions(): Any
//    fun get_menuShiftClick(): Any
//    fun get_menuVariable(): Any
//    fun get_menuVisible(): Any
    fun get_menuWidth(): Int
    fun get_menuX(): Int
    //    fun get_menuXInteractions(): Any
    fun get_menuY(): Int

    //    fun get_menuYInteractions(): Any
//    fun get_message0(): Any
//    fun get_message1(): Any
//    fun get_message2(): Any
//    fun get_messageContainer(): Any
//    fun get_mouse(): Any
//    fun get_npcCompositeCache(): Any
//    fun get_npcModelCache(): Any
//    fun get_objectCompositeCache(): Any
//    fun get_objectModelCache(): Any
//    fun get_password(): Any
    fun get_plane(): Int
    fun get_playerIndex(): Int
    //    fun get_playerModelCache(): Any
    fun get_players(): Array<Player>
//    fun get_projectiles(): Any
//    fun get_realLevel(): Any
//    fun get_region(): Any
    fun get_selectedItemID(): Int
    fun get_selectedItemIndex(): Int
    //    fun get_selectedItemName(): Any
//    fun get_selectedSpellName(): String
    fun get_selectionState(): Int

    //    fun get_settings(): Any
//    fun get_settingsObject(): Any
//    fun get_socialHandler(): Any
//    fun get_socketWrapper(): Any
//    fun get_tileHeights(): Any
//    fun get_tileSettings(): Any
    fun get_username(): String
//    fun get_varps(): Any
fun get_widgetBoundsX(): IntArray

    fun get_widgetBoundsY(): IntArray
    fun get_widgetHeights(): IntArray
//    fun get_widgetModelCache(): Any
//    fun get_widgetNodes(): Any
//    fun get_widgetWidths(): Any
//    fun get_widgets(): Any
//    fun get_worlds(): Any
//    fun get_zoom(): Any
    fun get_zoomExact(): Int
}