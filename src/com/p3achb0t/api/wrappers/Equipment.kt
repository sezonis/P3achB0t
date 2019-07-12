package com.p3achb0t.api.wrappers

import com.p3achb0t.api.Utils
import com.p3achb0t.api.wrappers.widgets.WidgetID
import com.p3achb0t.api.wrappers.widgets.WidgetItem
import com.p3achb0t.api.wrappers.widgets.Widgets
import kotlinx.coroutines.delay

class Equipment {


    //Info from each item comes from child widget in index 1

    companion object {
        const val NODE_ID = 94

        enum class Slot(val widgetID: Int, val cacheIndex: Int = -1) {
            Head(WidgetID.Equipment.HELMET, 0),
            Cape(WidgetID.Equipment.CAPE, 1),
            Neck(WidgetID.Equipment.AMULET, 2),
            Weapon(WidgetID.Equipment.WEAPON, 3),
            Body(WidgetID.Equipment.BODY, 4),
            Shield(WidgetID.Equipment.SHIELD, 5),
            Legs(WidgetID.Equipment.LEGS, 7),
            Gloves(WidgetID.Equipment.GLOVES, 9),
            Boots(WidgetID.Equipment.BOOTS, 10),
            Ring(WidgetID.Equipment.RING, 12),
            Quiver(WidgetID.Equipment.AMMO, 13),
            EquiptmentStats(17),
            PriceChecker(19),
            ItemsKeptOnDeath(21),
            CallFollower(23),
        }

        fun isOpen(): Boolean {
            return Tabs.getOpenTab() == Tabs.Tab_Types.Equiptment
        }

        suspend fun open(waitForActionToComplete: Boolean = true) {
            println("Opening Equiptment tab")
            Tabs.openTab(Tabs.Tab_Types.Equiptment)
            //Wait for tab to be open
            if (waitForActionToComplete)
                Utils.waitFor(2, object : Utils.Condition {
                    override suspend fun accept(): Boolean {
                        delay(100)
                        return Tabs.getOpenTab() == Tabs.Tab_Types.Equiptment
                    }
                })
            if (!isOpen()) open()
        }

        suspend fun unEquiptItem(slot: Slot, waitForActionToComplete: Boolean = true) {
            val item = getItemAtSlot(slot)
            println("Removing item from ${slot.name} ${item?.area}")
            item?.interact("Remove")
            // Wait till item gets removed
            if (waitForActionToComplete)
                Utils.waitFor(2, object : Utils.Condition {
                    override suspend fun accept(): Boolean {
                        delay(100)
                        return !isEquipmentSlotEquipted(slot)
                    }
                })

        }

        fun isEquipmentSlotEquipted(slot: Slot): Boolean {
            try {
                val item = Items.getItemInfo(NODE_ID, slot.cacheIndex)
                if (item.id > -1)
                    return true
            } catch (e: Exception) {
                return false
            }
            return false
        }

        fun getItemAtSlot(slot: Slot): WidgetItem? {
            return try {
                val widget = Widgets.find(WidgetID.EQUIPMENT_GROUP_ID, slot.widgetID)
                val item = Items.getItemInfo(NODE_ID, slot.cacheIndex)
                WidgetItem(widget, id = item.id, stackSize = item.stackSize)
            } catch (e: Exception) {
                null
            }
        }
    }
}