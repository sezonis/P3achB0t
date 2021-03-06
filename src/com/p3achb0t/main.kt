package com.p3achb0t

import com.formdev.flatlaf.FlatDarkLaf
import com.p3achb0t.client.ui.GameWindow
import com.p3achb0t.client.ui.setup
import com.p3achb0t.scripts_debug.paint_debug.PaintDebug
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.awt.Font
import javax.swing.UIManager
import javax.swing.UIManager.setLookAndFeel
import javax.swing.plaf.FontUIResource


object Main {

    var validationKey = PaintDebug.key
    @JvmStatic
    fun main(args: Array<String>) {

        var getNextKey = false
        args.iterator().forEach {
            if(getNextKey){
                validationKey = PaintDebug.key
            }
            if(it == "-key"){
                getNextKey = true
            }
            println(it)
        }
        println("validation key: $validationKey")

        setup()
        setLookAndFeel(FlatDarkLaf())
        for ((key) in UIManager.getDefaults()) {
            val value = UIManager.get(key)
            if (value != null && value is FontUIResource) {
                val fr = value
                val f = FontUIResource(Font("Courier Bold", Font.BOLD, 12))
                UIManager.put(key, f)
            }
        }
        val g = GameWindow()
        CoroutineScope(Dispatchers.Default).launch { g.run() }
    }
}
