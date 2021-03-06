package com.p3achb0t.api.wrappers

import com.p3achb0t.api.Context
import com.p3achb0t.api.private_api.hopWorld
import com.p3achb0t.api.private_api.selectionOptiondoAction
import com.p3achb0t.api.user_inputs.DoActionParams
import com.p3achb0t.api.wrappers.utils.Utils
import kotlinx.coroutines.delay
import kotlin.random.Random

class WorldHop(val ctx: Context) {
    companion object {
        enum class World(val region: Int) {
            UK(1),
            USA(0),
            AUS(3),
            GER(7),

        }

    }
    val isLoggedIn: Boolean get() { return GameState.currentState(ctx) == GameState.LOGGED_IN}

    val freeWorldNoTotalRequirement = intArrayOf(301, 308, 316, 326, 335, 379, 380, 382, 383, 384, 393, 394, 397, 398, 399, 417, 418, 425, 426, 430, 431,
            433, 434, 435, 436, 437, 438, 439, 440, 451, 452, 453, 454, 455, 456, 457, 458, 459, 469, 470, 471, 472, 473, 474, 475, 476, 497, 498, 499, 500, 591, 502, 503, 504)

    suspend fun switchRandF2pWorld() {
        hopWorld(freeWorldNoTotalRequirement.random())
    }


    fun getCurrent(): Int{
        var World = 0
        try {
                World = ctx.client.getWorldId()
        } catch (e: Exception) {
            println("World Exception")
        }
        return World
    }

    suspend fun hopRandomP2P(){
        try {
            val Worlds = ctx.client.getWorlds()
            val MembersWorlds = ArrayList<Int>()
            Worlds.forEach {
                if (it.getProperties() == 1) {
                    MembersWorlds.add(it.getId())
                }

            }
            hopWorld(MembersWorlds.random())
        }  catch (e: Exception) {
            println("World Exception")
        }
    }

    fun getMembershipDays(): Int{
        return ctx.vars.getVarbit(1780)
    }

    suspend fun hopRandomP2P(region: World){
        try {
            val Worlds = ctx.client.getWorlds()
            val MembersWorlds = ArrayList<Int>()
            Worlds.forEach {
                if (it.getProperties() == 1 && it.getLocation() == region.region) {
                    MembersWorlds.add(it.getId())
                }

            }
            hopWorld(MembersWorlds.random())
        }  catch (e: Exception) {
            println("World Exception")
        }
    }

    suspend fun hopRandomF2p(reverse: Boolean = false){
        val curWorld = ctx.client.getWorldId()

        val mainWorldArray = freeWorldNoTotalRequirement
        val randWorld = mainWorldArray.random()
        hopWorld(randWorld)
    }

    suspend fun logout(){
        this.ctx.mouse.doAction(DoActionParams(-1, 11927560, 57, 1, "Logout", "", 0, 0))
        delay(400)

        Utils.waitFor(10,object : Utils.Condition {
            override suspend fun accept(): Boolean {
                delay(50)
                return GameState.currentState(ctx) == GameState.LOGIN_SCREEN
            }
        })
        if(GameState.currentState(ctx) == GameState.LOGGED_IN){
            logout()
        }
    }


    suspend fun hopWorld(worldNum: Int) {
        try {
            //Open logout menu
            //argument0:-1, argument1:10747934, argument2:57, argument3:1, action:Logout, targetName:, mouseX:781, mouseY:10, argument8:0
            this.ctx.mouse.doAction(DoActionParams(-1, 10747934, 57, 1, "Logout", "", 0, 0))
        } catch (e: Exception) {
            println("World Exception")
        }
        delay(1000)
        try {
            //Open world hopper
//        argument0:-1, argument1:11927555, argument2:57, argument3:1, action:World Switcher, targetName:, mouseX:670, mouseY:433, argument8:0
            this.ctx.mouse.doAction(DoActionParams(-1, 11927555, 57, 1, "World Switcher", "", 0, 0))
        }catch (e: Exception) {
            println("World Exception")
        }
        delay(1000)
        try {
            //Pick world
            //argument0:326, argument1:4522000, argument2:57, argument3:1, action:Switch, targetName:<col=ff9040>326</col>, mouseX:620, mouseY:427, argument8:0
            this.ctx.mouse.doAction(DoActionParams(worldNum, 4522000, 57, 1, "Switch", "", 0, 0))
        }catch (e: Exception) {
            println("World Exception")
        }
        delay(1000)
        try {
            if(ctx.dialog.isContinueAvailable()){
                //argument0:-1, argument1:15007746, argument2:30, argument3:0, action:Continue, targetName:, mouseX:264, mouseY:549, argument8:1565406540
                this.ctx.mouse.doAction(DoActionParams(-1, 15007746, 30, 0, "Switch", "", 0, 0))
                delay(2000)
            }
        }catch (e: Exception) {
            println("World Exception")
        }
        delay(1000)
        try {
            //click yes
            if (ctx.dialog.isDialogOptionsOpen()) {
                ctx.dialog.selectionOptiondoAction("Yes")
                delay(Random.nextLong(1333, 1777))
            }
        }catch (e: Exception) {
            println("World Exception")
        }
        delay(2000)
        try {
            //click yes
            if (ctx.dialog.isDialogOptionsOpen()) {
                ctx.dialog.selectionOptiondoAction("Yes")
                delay(Random.nextLong(1333, 1777))
            }
        }catch (e: Exception) {
            println("World Exception")
        }
        Utils.waitFor(10, object : Utils.Condition {
            override suspend fun accept(): Boolean {
                delay(50)
                return ctx.client.getLoginState() == 13
            }
        })

    }

    fun isMember(): Boolean {
        return ctx.client.getIsMembersWorld()
    }
}