package com.p3achb0t.api.wrappers

import com.p3achb0t._runestar_interfaces.Component
import com.p3achb0t.api.Context
import com.p3achb0t.api.MenuOpcode
import com.p3achb0t.api.user_inputs.DoActionParams
import com.p3achb0t.api.wrappers.utils.Utils
import com.p3achb0t.api.wrappers.widgets.WidgetID
import com.p3achb0t.api.wrappers.widgets.WidgetItem
import kotlinx.coroutines.delay
import java.awt.Rectangle
import kotlin.random.Random

class Bank(val ctx: Context) {
    //DONE - open bank
    //DONE - deposit all items
    //TODO - deposit all items from a list
    companion object {
        private val BANK_OBJECTS = intArrayOf(
                782,
                2012,
                2015,
                2213,
                2196,
                4483,
                2453,
                6084,
                11758,
                12759,
                14367,
                19230,
                18491,
                24914,
                25808,
                26972,
                27663,
                29085,
                34752,
                35647,
                36786,
                4483,
                8981,
                14382,
                20607,
                21301,
                24101 // Falador bank booths
        )
    }

    suspend fun open() {

        //First look for bankers, if that doesnt work then look for bank objects
        if (!isOpen()) {
            val BankBooths = ctx.gameObjects.find("booth")
            if (BankBooths.size > 0) {
//              BankBooths.forEach{
//                  println(it.getGlobalLocation())
//              }
                if (!BankBooths[0].isOnScreen())
                    BankBooths[0].turnTo()
                if (BankBooths[0].isOnScreen())
                    BankBooths[0].clickObject(BankBooths[0])
                Utils.waitFor(3, object : Utils.Condition {
                    override suspend fun accept(): Boolean {
                        delay(100)
                        return isOpen() || isPinPanelOpen()
                    }
                })
            }else{
                println("Didnt find any bankers")
            }
        }
        //TODO - interact with bank booths is player is not here


        if(isPinPanelOpen()){
            solveBankPin("1122")//TODO - try to figure out where to get the pin from the account
        }

    }

    suspend fun openAtGe() {
        if (!isOpen()) {
            val bank = ctx.gameObjects.find(10060)
            if (!ctx.bank.isOpen() && !bank.isEmpty()) {
                bank[0].doAction2()
                Utils.waitFor(5, object : Utils.Condition {
                    override suspend fun accept(): Boolean {
                        delay(100)
                        return ctx.bank.isOpen()
                    }
                })
            }
            delay(300)
        }
    }

    suspend fun close() {
        if (isOpen()) {
            val doActionParams = DoActionParams(11, 786434, 57, 1, "", "", 0, 0)
            ctx.mouse.overrideDoActionParams = true
            ctx.mouse.doAction(doActionParams)
            delay(300)
        }
    }



    suspend fun depositAll() {
        val depositAllWidget = WidgetItem(ctx.widgets.find(12, 40), ctx = ctx)
        depositAllWidget.click()
        Utils.waitFor(3, object : Utils.Condition {
            override suspend fun accept(): Boolean {
                delay(100)
                return ctx.inventory.isEmpty()
            }
        })
    }

    fun itemVisible(itemRect: Rectangle): Boolean {
        return WidgetItem(getBankWidget(), ctx = ctx).area.intersects(itemRect)
    }


    fun getItemCount(id: Int): Int {
        var count = 0
        if (isOpen()) {
            val items = getAll()
            items.forEach {
                if (it.id == id) {
                    count = it.stackSize
                    return@forEach
                }
            }
        }
        return count
    }

    fun getItemCount(itemid: IntArray): Int {
        var count = 0
        if (isOpen()) {
            val items = getAll()
            items.forEachIndexed { index, widgetItem ->
                itemid.forEach {
                    if (widgetItem.id == it) {
                        count = widgetItem.stackSize + count
                        return@forEach
                    }
                }
            }
        }
        return count
    }

    fun containsNumberOfItems(itemid: ArrayList<Int>): Int {
        var count = 0
        if (isOpen()) {
            val items = getAll()
            items.forEachIndexed { index, widgetItem ->
                itemid.forEach {
                    if (widgetItem.id == it) {
                        count = widgetItem.stackSize + count
                        count = count + 1
                        return@forEach
                    }
                }
            }
        }
        return count
    }

    fun containsAny(itemid: List<Int>): Boolean {
        var contains = false
        if (isOpen()) {
            var items = getAll()
            items.forEachIndexed { index, widgetItem ->
                itemid.forEach {
                    if (widgetItem.id == it) {
                        contains = true
                    }
                }
            }
        }
        return contains
    }


    /**
     * added by sirscript
     */



    suspend fun doActionAttack(){

    }

    suspend fun withdrawAlldoAction(id: Int) {
        if (isOpen()) {
            if (ctx.vars.getVarp(1666) != 0) {
                val doActionParams = DoActionParams(-1, 786458,57, 1, "", "", 0, 0)
                ctx.mouse.overrideDoActionParams = true
                ctx.mouse.doAction(doActionParams)
                delay(Random.nextLong(189, 1076))
            }
            var items = getAll()
            items.forEach {
                if (it.id == id) {
                    val doActionParams = DoActionParams(it.widget!!.getChildIndex(), 786443, MenuOpcode.WIDGET_DEFAULT.id, 7, "", "", 0, 0)
                    ctx.mouse.overrideDoActionParams = true
                    ctx.mouse.doAction(doActionParams)
                    delay(Random.nextLong(189, 1076))
                }
            }
        }
    }

    suspend fun withdrawAlldoActionNoted(id: Int) {
        if (isOpen()) {
            while (ctx.vars.getVarp(115) != 1 && isOpen()) {
                val doActionParams = DoActionParams(-1, 786454,57, 1, "", "", 0, 0)
                ctx.mouse.overrideDoActionParams = true
                ctx.mouse.doAction(doActionParams)
                delay(Random.nextLong(189, 1076))
            }
            if (ctx.vars.getVarp(115) == 1) {
                var items = getAll()
                items.forEach {
                    if (it.id == id) {
                        val doActionParams = DoActionParams(it.widget!!.getChildIndex(), 786443, MenuOpcode.WIDGET_DEFAULT.id, 7, "", "", 0, 0)
                        ctx.mouse.overrideDoActionParams = true
                        ctx.mouse.doAction(doActionParams)
                        delay(Random.nextLong(189, 1076))
                    }
                }
            }
        }
    }

    /**
     * added by sirscript
     */

    suspend fun withdraw1doAction(id: Int) {
        if (isOpen()) {
            if (ctx.vars.getVarp(1666) != 0) {
                val doActionParams = DoActionParams(-1, 786458,57, 1, "", "", 0, 0)
                ctx.mouse.overrideDoActionParams = true
                ctx.mouse.doAction(doActionParams)
                delay(Random.nextLong(189, 1076))
            }
            var items = getAll()
            items.forEach {
                if (it.id == id) {
                    val doActionParams = DoActionParams(it.widget!!.getChildIndex(), 786443, MenuOpcode.WIDGET_DEFAULT.id, 1, "", "", 0, 0)
                    ctx.mouse.overrideDoActionParams = true
                    ctx.mouse.doAction(doActionParams)
                    delay(Random.nextLong(189, 1076))
                }
            }
        }
    }

    /**
     * added by sirscript
     */

    suspend fun withdrawXdoAction(id: Int, count: Int) {
        if (isOpen()) {
            if (ctx.vars.getVarp(1666) != 0) {
                val doActionParams = DoActionParams(-1, 786458,57, 1, "", "", 0, 0)
                ctx.mouse.overrideDoActionParams = true
                ctx.mouse.doAction(doActionParams)
                delay(Random.nextLong(189, 1076))
            }
            var items = getAll()
            items.forEach {
                if (it.id == id) {
                    val doActionParams = DoActionParams(it.widget!!.getChildIndex(), 786443, MenuOpcode.WIDGET_DEFAULT.id, 6, "", "", 0, 0)
                    ctx.mouse.overrideDoActionParams = true
                    ctx.mouse.doAction(doActionParams)
                    Utils.waitFor(3, object : Utils.Condition {
                        override suspend fun accept(): Boolean {
                            delay(100)
                            val chatText =
                                    ctx.widgets.find(WidgetID.CHATBOX_GROUP_ID, WidgetID.Chatbox.FULL_INPUT)
                            val text = chatText?.getText()
                            println(text + " " + chatText?.getIsHidden())
                            return text?.equals("*") ?: false
                        }
                    })
                    delay(Random.nextLong(189, 1076))
                    ctx.keyboard.sendKeys(count.toString(), sendReturn = true)
                }
            }
        }
    }

    /**
     * added by sirscript
     * sets bank withdrawmode to noted
     */

    suspend fun setnoted() {
        if (isOpen()) {
            val doActionParams =   DoActionParams(-1, 786454, MenuOpcode.WIDGET_DEFAULT.id, 1, "", "", 0, 0)
            ctx.mouse.overrideDoActionParams = true
            ctx.mouse.doAction(doActionParams)
            delay(Random.nextLong(189, 1076))
        }
    }

    /**
     * added by sirscript
     * sets bank withdrawmode to items
     */

    suspend fun setitem() {
        if (isOpen()) {
            val doActionParams =   DoActionParams(-1, 786452, MenuOpcode.WIDGET_DEFAULT.id, 1, "", "", 0, 0)
            ctx.mouse.overrideDoActionParams = true
            ctx.mouse.doAction(doActionParams)
            delay(Random.nextLong(189, 1076))
        }
    }


    /**
     * added by sirscript
     * deposits all inventory using doAction
     */

    suspend fun depositInvdoAction() {
        val doActionParams =   DoActionParams(-1, 786472, MenuOpcode.WIDGET_DEFAULT.id, 1, "", "", 0, 0)
        ctx.mouse.overrideDoActionParams = true
        ctx.mouse.doAction(doActionParams)
    }

    /**
     * added by sirscript
     * deposits all equipment using doAction
     */

    suspend fun depositEquipmentdoAction() {
        val doActionParams =   DoActionParams(-1, 786474, MenuOpcode.WIDGET_DEFAULT.id, 1, "", "", 0, 0)
        ctx.mouse.overrideDoActionParams = true
        ctx.mouse.doAction(doActionParams)
    }

    suspend fun depositallExcept(arrayList: ArrayList<Int>){

    }

    /**
     * added by sirscript
     */


    fun isOpen(): Boolean {
        return getBankWidget() != null
    }

    fun isPinPanelOpen(): Boolean{
        return getPinPanelWidget() != null
    }

    fun getPinPanelWidget(): Component?{
        return ctx.widgets.find(WidgetID.BANK_PIN_PANEL_ID,0)
    }

    fun getBankWidget(): Component? {
        return ctx.widgets.find(12,11)
    }

    fun getSize(): Int {
        if (isOpen()) {
            val widget = ctx.widgets.find(12, 4)
            if (widget?.getText() != null && widget.getText().trim().isNotEmpty()) {
                return widget.getText().trim().toInt()
            }
        }
        return 0
    }

    fun getAll(): ArrayList<WidgetItem> {
        val itemWidgets = ArrayList<WidgetItem>()
        val bank = getBankWidget()
        var itemCount = 0
        val maxItemCount = getSize()
        bank?.getChildren()?.iterator()?.forEach {
            if (itemCount > maxItemCount) return@forEach
            if (it.getItemId() > 0 && it.getItemId() != 6512) {

                itemWidgets.add(
                        WidgetItem(

                                widget = it,
                                id = it.getItemId(),
                                stackSize = it.getItemQuantity(),
                                type = WidgetItem.Type.BANK,
                                ctx = ctx
                        )
                )
                itemCount += 1
            }
        }
        return itemWidgets
    }



    private fun stillSolvingPin(): Boolean{
        val digit1 = ctx.widgets.find(WidgetID.BANK_PIN_PANEL_ID, 3)?.getText()
        val digit2 = ctx.widgets.find(WidgetID.BANK_PIN_PANEL_ID, 4)?.getText()
        val digit3 = ctx.widgets.find(WidgetID.BANK_PIN_PANEL_ID, 5)?.getText()
        val digit4 = ctx.widgets.find(WidgetID.BANK_PIN_PANEL_ID, 6)?.getText()

        return digit1 == "?" || digit2 == "?" || digit3 == "?" || digit4 == "?"
    }

    private suspend fun solveBankPin(pin: String){
        //Check to see if widget 213 is open
        //Pin
        //Look for number in the follow subchild widgets text
        //:(213,16)(1)
        //child : 16,18,20,22,24,26, 28,30,32,34
        //children in the follow can help identify with key we need to press: 3,4,5,6


        while(stillSolvingPin()){
            try {
                //Which one to look for
                val digit1 = ctx.widgets.find(WidgetID.BANK_PIN_PANEL_ID, 3)?.getText()
                val digit2 = ctx.widgets.find(WidgetID.BANK_PIN_PANEL_ID, 4)?.getText()
                val digit3 = ctx.widgets.find(WidgetID.BANK_PIN_PANEL_ID, 5)?.getText()
                val digit4 = ctx.widgets.find(WidgetID.BANK_PIN_PANEL_ID, 6)?.getText()

                when {
                    digit1 == "?" -> {
                        println("Solving digit 1")
                        findAndPressKey(pin[0])
                    }
                    digit2 == "?" -> {
                        println("Solving digit 2")
                        findAndPressKey(pin[1])
                    }
                    digit3 == "?" -> {
                        println("Solving digit 3")
                        findAndPressKey(pin[2])
                    }
                    digit4 == "?" -> {
                        println("Solving digit 4")
                        findAndPressKey(pin[3])
                    }
                }
            }catch (e:Exception){
                //Some cases where we might need to catch errors if the component was not found
            }
        }

    }

    private suspend fun findAndPressKey(digit: Char) {
        var keepSearching = true
        val firstKey = ctx.widgets.find(WidgetID.BANK_PIN_PANEL_ID, WidgetID.BankPinKeys.KEYS[0])!!.getChildren()[1]

        for (keyChildID in WidgetID.BankPinKeys.KEYS) {
            val bankPinKeyWidget = ctx.widgets.find(WidgetID.BANK_PIN_PANEL_ID, keyChildID)
            if (bankPinKeyWidget != null) {
                val children = bankPinKeyWidget.getChildren()
                //Info should be in first index
                children.iterator().forEach {
                    if (it.getText() == digit.toString() && keepSearching) {
                        WidgetItem(it, ctx = ctx).click()
                        //Wait for widget to change
                        Utils.waitFor(2, object : Utils.Condition {
                            override suspend fun accept(): Boolean {
                                val firstKeyUpdated = ctx.widgets.find(WidgetID.BANK_PIN_PANEL_ID, WidgetID.BankPinKeys.KEYS[0])!!.getChildren()[1]
                                delay(100)
                                return firstKeyUpdated.getX() == firstKey.getX() && firstKeyUpdated.getY() == firstKey.getY()
                            }
                        })
                        keepSearching = false
                        //Move mouse off the keys
                    }
                }
            }
        }
        if(keepSearching){
            //Used to move the mouse around a little bit
            WidgetItem(ctx.widgets.find(WidgetID.BANK_PIN_PANEL_ID, 0),ctx=ctx).hover()
        }
    }
}
