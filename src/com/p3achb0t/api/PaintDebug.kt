package com.p3achb0t.api

import com.p3achb0t.Main
import com.p3achb0t.api.Calculations.Companion.worldToMap
import com.p3achb0t.api.user_inputs.Camera
import com.p3achb0t.api.wrappers.Bank
import com.p3achb0t.api.wrappers.ClientMode
import com.p3achb0t.api.wrappers.Dialog
import com.p3achb0t.api.wrappers.MiniMap
import com.p3achb0t.api.wrappers.tabs.Equipment
import com.p3achb0t.api.wrappers.tabs.Inventory
import com.p3achb0t.api.wrappers.tabs.Tabs
import com.p3achb0t.hook_interfaces.Cache
import com.p3achb0t.hook_interfaces.Model
import com.p3achb0t.hook_interfaces.Npc
import com.p3achb0t.hook_interfaces.ObjectComposite
import com.p3achb0t.interfaces.PaintListener
import java.awt.Color
import java.awt.Graphics
import java.awt.Point
import java.awt.Rectangle


fun drawRect(g: Graphics, rect: Rectangle) {
    g.drawRect(rect.x, rect.y, rect.width, rect.height)
}
fun debugPaint(): PaintListener {
    return object : PaintListener {
        override fun onPaint(g: Graphics) {
            try {
                if (true) {
                    g.color = Color.white
                    g.drawString("Mouse x:${Main.mouseEvent?.x} y:${Main.mouseEvent?.y}", 50, 50)
                    g.drawString("clientData.gameCycle :${Main.clientData.getGameCycle()}", 50, 60)
                    g.drawString("Game State:: ${Main.clientData.getGameState()}", 50, 70)
                    g.drawString("clientData.loginState :${Main.clientData.getLoginState()}", 50, 80)
                    g.drawString("Account status :${Main.clientData.getAccountStatus()}", 50, 90)

                    g.drawString(
                        "Camera: x:${Camera.x} y:${Camera.y} z:${Camera.z} pitch:${Camera.pitch} yaw: ${Camera.yaw} angle: ${Camera.angle}",
                        50,
                        110
                    )
                    g.drawString("OpenTab: ${Tabs.getOpenTab()?.name}", 50, 120)
                    g.drawString("Bank Status: ${Bank.isOpen()}", 50, 130)

                    try {
                        g.drawString("Spell: ${Main.clientData.getSelectedSpellName()}", 50, 140)

                        g.drawString("Animation: ${Main.clientData.getLocalPlayer().getAnimation()}", 50, 100)
                        g.drawString("Mode: ${ClientMode.getMode().name}", 50, 150)
                        g.drawString(
                            "LocalPlayer Position: (${Main.clientData.getLocalPlayer().getLocalX() / 128},${Main.clientData.getLocalPlayer().getLocalY() / 128})" +
                                    " RAW: (${Main.clientData.getLocalPlayer().getLocalX()},${Main.clientData.getLocalPlayer().getLocalY()}",
                            50,
                            160
                        )
                        g.drawString(
                            "Base(x,y): (${Main.clientData.getBaseX()},${Main.clientData.getBaseY()})",
                            50,
                            180
                        )
                        val miniMapPlayer = worldToMap(
                            Main.clientData.getLocalPlayer().getLocalX(),
                            Main.clientData.getLocalPlayer().getLocalY()
                        )
                        g.drawString(
                            "localPlayer minimap: (x,y) (${miniMapPlayer.x},${miniMapPlayer.y})" +
                                    "Including base(${Main.clientData.getLocalPlayer().getLocalX() / 128 + Main.clientData.getBaseX()}," +
                                    "${Main.clientData.getLocalPlayer().getLocalY() / 128 + Main.clientData.getBaseY()})  " +
                                    "mapAngle: ${Main.clientData.getMapAngle()}", 50, 190
                        )
                        g.drawString("Zoom: ${Main.clientData.getZoomExact()}", 50, 170)
                    } catch (e: Exception) {
                    }
                }
//                        g.drawString("cameraX :${clientData.getCameraX()}", 50, 100)
//                        g.drawString("cameraY :${clientData.getCameraY()}", 50, 110)
                Main.mouseEvent?.x?.let { Main.mouseEvent?.y?.let { it1 -> g.drawRect(it, it1, 5, 5) } }
//                        print("[")
//                        for (x in clientData.get_widgetHeights()) {
//                            print("$x,")
//                        }
//                        println("]")
//                        println(clientData.get_username() + " " + clientData.get_isWorldSelectorOpen())

                if (Main.clientData.getGameState() == 30) {
                    if (!Bank.isOpen()) {

                        try {
                            ///////Player paint//////////
                            g.color = Color.GREEN
                            val players = Main.clientData.getPlayers()
                            var count = 0
                            var point = Point(200, 50)
                            players.iterator().forEach { _player ->
                                if (_player != null && _player.getLevel() > 0) {
//                                println("${_player.getName()} ${_player.getLevel()} x:${_player.getLocalX()} y: ${_player.getLocalY()}, ")
//                                print("Queue size: ${_player.getQueueSize()}")

                                    count += 1
                                    val point = Calculations.worldToScreen(
                                        _player.getLocalX(),
                                        _player.getLocalY(),
                                        _player.getModelHeight()
                                    )
                                    if (point.x != -1 && point.y != -1 && Calculations.isOnscreen(point)) {
                                        g.color = Color.GREEN
                                        g.drawString(_player.getName().getName(), point.x, point.y)
                                    }
                                    val polygon = getActorTriangles(
                                        _player,
                                        Main.clientData.getPlayerModelCache(),
                                        _player.getComposite().getStaticModelID()
                                    )
                                    g.color = Color.YELLOW
                                    polygon.forEach {
                                        g.drawPolygon(it)
                                    }
                                    val ch = getConvexHull(
                                        _player,
                                        Main.clientData.getPlayerModelCache(),
                                        _player.getComposite().getStaticModelID()
                                    )
                                    g.color = Color.RED
                                    g.drawPolygon(ch)
                                    val tile = Calculations.getCanvasTileAreaPoly(
                                        _player.getLocalX(),
                                        _player.getLocalY()
                                    )
                                    g.color = Color.CYAN
                                    g.drawPolygon(tile)
                                    g.color = Color(0, 0, 0, 50)
                                    g.fillPolygon(tile)

                                    g.color = Color.GREEN
                                    val mapPoint = worldToMap(_player.getLocalX(), _player.getLocalY())
                                    g.fillRect(mapPoint.x, mapPoint.y, 4, 4)
                                }
                                point.y += 20
                            }
                        } catch (e: Exception) {
                            println("Error: Player pain " + e.message)
                        }

                        try {
                            ///////NPC paint//////////
                            var count = 0
                            count = 0
                            val localNpcs = Main.clientData.getLocalNPCs()
                            var npc: Npc? = null
                            localNpcs.iterator().forEach {
                                if (it != null) {
                                    npc = it

//                                print("Name: ${it.getComposite().getName()}, ID:${it.getComposite().getNpcComposite_id()} x:${it.getLocalX()} y:${it.getLocalY()},")
                                    count += 1
                                    val point =
                                        Calculations.worldToScreen(
                                            it.getLocalX(),
                                            it.getLocalY(),
                                            it.getModelHeight()
                                        )
                                    if (point.x != -1 && point.y != -1 && Calculations.isOnscreen(point)) {
                                        g.color = Color.GREEN
                                        g.drawString(
                                            "${it.getComposite().getName()} ${it.getComposite().getNpcComposite_id()} ${it.getAnimation()}",
                                            point.x,
                                            point.y
                                        )
                                    }
                                    val polygon = npc?.getComposite()?.getNpcComposite_id()?.toLong()?.let { it1 ->
                                        getActorTriangles(
                                            npc, Main.clientData.getNpcModelCache(),
                                            it1
                                        )
                                    }
                                    g.color = Color.BLUE
                                    polygon?.forEach {
                                        g.drawPolygon(it)
                                    }
                                    g.color = Color.YELLOW
                                    val mapPoint = worldToMap(it.getLocalX(), it.getLocalY())
                                    g.fillRect(mapPoint.x, mapPoint.y, 4, 4)

                                    val ch = getConvexHull(
                                        npc,
                                        Main.clientData.getNpcModelCache(),
                                        npc!!.getComposite().getNpcComposite_id().toLong()
                                    )
                                    g.color = Color.PINK
                                    g.drawPolygon(ch)

                                    val tile =
                                        Calculations.getCanvasTileAreaPoly(it.getLocalX(), it.getLocalY())
                                    g.color = Color.CYAN
                                    g.drawPolygon(tile)
                                    g.color = Color(0, 0, 0, 50)
                                    g.fillPolygon(tile)

                                }
                            }
                        } catch (e: Exception) {
                            println("Error: NPC Paint " + e.message)
                            e.stackTrace.iterator().forEach {
                                print(it)
                            }
                        }


                        try {
                            g.color = Color.red
                            drawRect(g, Calculations.chatBoxDimensions)
                            drawRect(g, Calculations.inventoryBarBottomDimensions)
                            drawRect(g, Calculations.inventoryDimensions)
                            drawRect(g, Calculations.inventoryBarTopDimensions)
                            drawRect(g, Calculations.miniMapDimensions)
                        } catch (e: Exception) {
                            println("Error: Bounds " + e.toString())
                        }

                        ///////Object paint//////////
                        if (false) {
                            val sceneData = Main.clientData.getObjectCompositeCache()
                            val region = Main.clientData.getRegion()

                            region.getTiles().iterator().forEach { plane ->
                                plane.iterator().forEach { row ->
                                    row.iterator().forEach { tile ->
                                        if (tile != null) {
                                            if (tile.getObjects().isNotEmpty()) {
                                                var count = 0
                                                tile.getObjects().iterator().forEach {
                                                    if (it != null && it.getId() > 0) {
                                                        count += 1
                                                        val tilePolygon =
                                                            Calculations.getCanvasTileAreaPoly(
                                                                it.getX(),
                                                                it.getY()
                                                            )
                                                        g.color = Color.ORANGE
                                                        g.drawPolygon(tilePolygon)
                                                        val point =
                                                            Calculations.worldToScreen(
                                                                it.getX(),
                                                                it.getY(),
                                                                tile.getPlane()
                                                            )
                                                        if (point.x != -1 && point.y != -1 && Calculations.isOnscreen(
                                                                point
                                                            )
                                                        ) {
                                                            g.color = Color.GREEN
                                                            val id = it.getId().shr(17).and(0x7fff).toInt()
                                                            val rawID = it.getId().shr(14).and(0x7fff)
//                                            println("${it.getWidgetID()},$rawID,$widgetID,${it.getRenderable().getWidgetID()}")
                                                            val objectComposite = getObjectComposite(sceneData, id)
                                                            val point2 =
                                                                Calculations.worldToScreen(
                                                                    it.getX(),
                                                                    it.getY(),
                                                                    it.getRenderable().getModelHeight()
                                                                )
                                                            g.drawString(
                                                                objectComposite?.getName() + "($id)",
                                                                point2.x,
                                                                point2.y
                                                            )
                                                        }

                                                        //Printing out the model and the hull
                                                        if (false) {
                                                            val model = it.getRenderable()
                                                            if (model is Model) {
                                                                val positionInfo =
                                                                    ObjectPositionInfo(
                                                                        it.getX(),
                                                                        it.getY(),
                                                                        it.getOrientation()
                                                                    )

                                                                val modelTriangles =
                                                                    getTrianglesFromModel(positionInfo, model)
                                                                g.color = Color.RED
                                                                modelTriangles.forEach {
                                                                    g.drawPolygon(it)
                                                                }
                                                                val hull = getConvexHullFromModel(positionInfo, model)
                                                                g.color = Color.CYAN
                                                                g.drawPolygon(hull)

                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
//TODO - ground items issue
//                    try {
//                        val groundItems = GroundItems.getAllItems()
//                        groundItems.forEach {
//                            val point1 = Calculations.worldToScreen(it.position.x, it.position.y, it.position.plane)
//                            if (point1.x != -1 && point1.y != -1 && Calculations.isOnscreen(point1)) {
//                                g.color = Color.GREEN
//                                g.drawString("(${it.id})", point1.x, point1.y - 20) // moving widgetID up 20 pixels
//                                val modelTriangles = it.getTriangles()
//                                g.color = Color.RED
//                                modelTriangles.forEach {
//                                    g.drawPolygon(it)
//                                }
//                                val hull = it.getConvexHull()
//                                g.color = Color.CYAN
//                                g.drawPolygon(hull)
//                            }
//                        }
//                    }catch(e: Exception){ println("Error: Gound items " + e.message)}
                    }

                    if (Bank.isOpen()) {
                        val items = Bank.getAll()
                        items.forEach {
                            g.color = Color.ORANGE
                            g.drawRect(it.area.x, it.area.y, it.area.width, it.area.height)

                        }
                    }


                    try {
                        // Look into menu
                        val mCount = Main.clientData.getMenuCount()
                        val heigth = Main.clientData.getMenuHeight()
                        val width = Main.clientData.getMenuWidth()
                        val mX = Main.clientData.getMenuX()
                        val mY = Main.clientData.getMenuY()
                        val mVisible = Main.clientData.getMenuVisible()
                        if (mVisible) {
                            g.color = Color.YELLOW
                            g.drawRect(mX, mY, width, heigth)
                            val baseHeight = 18
                            val lineHeight = 15
                            var yDiff = baseHeight
                            for (i in 1..mCount) {

                                g.color = Color.BLUE
                                g.drawRect(mX - 1, mY + yDiff, width, lineHeight)
                                var menuAction = Main.clientData.getMenuActions()[mCount - i]
                                menuAction = Utils.cleanColorText(menuAction)
                                var menuOption = Main.clientData.getMenuOptions()[mCount - i]
                                menuOption = Utils.cleanColorText(menuOption)
                                val action = "$menuAction $menuOption"
                                g.color = Color.GREEN

                                g.drawString(action, mX + width, mY + yDiff + (lineHeight / 2) + 7)
                                yDiff += lineHeight
                            }
                        }
                    } catch (e: Exception) {
                        println("Error: Menu " + e.message)
                    }

                    try {
                        // Look at inventory
                        if (Inventory.isOpen()) {
                            val items = Inventory.getAll()
                            if (items.size > 0) {

                                items.forEach {
                                    g.color = Color.YELLOW
                                    g.drawString("${it.id}", it.getBasePoint().x, it.getBasePoint().y)
                                    g.color = Color.GREEN
                                    g.drawString("${it.stackSize}", it.getBasePoint().x + 10, it.getBasePoint().y + 10)

                                    g.color = Color.RED
                                    g.drawRect(it.area.x, it.area.y, it.area.width, it.area.height)
                                }
                            }
                        }
                    } catch (e: Exception) {
                        println("Error: Inventory " + e.message)
                    }

                    try {
                        //Look at equipment
                        if (Equipment.isOpen()) {

                            Equipment.Companion.Slot.values().iterator().forEach { slot ->

                                val widget = Equipment.getItemAtSlot(slot)
                                if (widget != null) {
                                    g.color = Color.PINK
                                    g.drawRect(widget.area.x, widget.area.y, widget.area.width, widget.area.height)
                                    if (widget.id != -1) {
                                        g.color = Color.YELLOW
                                        g.drawString("${widget.id}", widget.getBasePoint().x, widget.getBasePoint().y)
                                        g.color = Color.GREEN
                                        g.drawString(
                                            "${widget.stackSize}",
                                            widget.getBasePoint().x + 10,
                                            widget.getBasePoint().y + 10
                                        )
                                    }


                                }
                            }

                        }
                    } catch (e: Exception) {
                        println("Error: Equipment " + e.message)
                    }
                    // Paint minimap circle
                    try {
                        val circle = MiniMap.getMapArea()
                        g.color = Color.RED
                        g.drawPolygon(circle)
                    } catch (e: Exception) {
                        println("Error: Minimap " + e.message)
                    }
                    // Paint continue
                    val dialog = Dialog.getDialogContinue()
                    if (dialog.widget != null) {
                        g.color = Color.ORANGE
                        drawRect(g, dialog.area)
                    }

                    // Paint on minimap

                    val local = Main.clientData.getLocalPlayer()
                    val point = worldToMap(local.getLocalX(), local.getLocalY())
                    if (point != Point(-1, -1)) {
                        g.color = Color.red
                        g.fillRect(point.x, point.y, 4, 4)
                    }
                }

            } catch (e: Exception) {
                println("Error:  General  $e\n ${e.stackTrace} \n ${e.localizedMessage}")
            }
        }

        fun drawCenteredCircle(g: Graphics, x: Int, y: Int, r: Int) {
            var x = x
            var y = y
            x -= r / 2
            y -= r / 2
            g.color = Color.PINK
            g.drawOval(x, y, r, r)
        }


        fun getAllObjectModels(objectModels: Cache): ArrayList<Model> {
            val modelList = ArrayList<Model>()
            objectModels.getHashTable().getBuckets().iterator().forEach { bucket ->
                if (bucket != null) {
//                    println(bucket.getWidgetID())
                    var model = bucket.getNext()
                    while (model != null && model is Model && model != bucket) {
                        modelList.add(model)
                        model = model.getNext()
                    }


                }
            }
            if (modelList.isNotEmpty()) {
                println("Models")
                modelList.forEach { println(it.getId()) }
                println("--")
            }
            return modelList
        }

        fun getObjectsModel(objectModels: Cache, renderModelID: Int): Model? {
            var desiredModel: Model? = null
            objectModels.getHashTable().getBuckets().iterator().forEach { bucket ->
                if (bucket != null) {
                    var model = bucket.getNext()
                    while (model != null && model is Model) {

                        if (model.getId() > 0) {
                            println(
                                model.getId().toString() + " " + model.getId().shr(17).and(0x7fff) + " ${model.getId().shr(
                                    16
                                )} ${model.getId().shr(15)} ${model.getId().shr(14)}" + " UUID Count: ${(model as Model).getUidCount()}"
                            )
                        }
                        if (model.getId().toInt() == renderModelID) {
                            desiredModel = model as Model
                        }
                        model = model.getNext()
                    }

                }
            }

            println("--")
            return desiredModel
        }

        private fun getObjectComposite(
            objectCache: Cache,
            gameObjectId: Int
        ): ObjectComposite? {
            var desiredGameObject1: ObjectComposite? = null
            objectCache.getHashTable().getBuckets().iterator().forEach { bucketItem ->
                if (bucketItem != null) {

                    var objectComposite = bucketItem.getNext()
                    while (objectComposite != null
                        && objectComposite is ObjectComposite
                        && objectComposite != bucketItem
                    ) {
                        if (objectComposite.getId() > 0
                            && objectComposite.getId().toInt() == gameObjectId
                        ) {
                            desiredGameObject1 = objectComposite
                            break
                        }
                        objectComposite = objectComposite.getNext()
                    }
                }
            }
            return desiredGameObject1
        }


    }
}