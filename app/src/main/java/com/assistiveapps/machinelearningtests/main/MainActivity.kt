package com.assistiveapps.machinelearningtests.main

import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.view.WindowManager
import com.assistiveapps.machinelearningtests.R
import com.assistiveapps.machinelearningtests.main.rv.OptionsAdapter
import com.assistiveapps.machinelearningtests.main.rv.OptionsData
import com.assistiveapps.machinelearningtests.tests.barcode_scan.BarcodeScanActivity
import com.assistiveapps.machinelearningtests.tests.face_detect.FaceDetectActivity
import com.assistiveapps.machinelearningtests.tests.image_label.ImageLabelActivity
import com.assistiveapps.machinelearningtests.tests.text_scan.TextScanActivity
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), MainInterface {

    var color: Int = 0
    val list: MutableList<OptionsData> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        changeStatusBarColor()
        initialize()
        setClickListeners()
    }

    override fun setClickListeners() {

    }

    override fun initialize() {
        list.add(OptionsData("IMAGE LABELLING", ""))
        list.add(OptionsData("FACE DETECTION", ""))
        list.add(OptionsData("BARCODE SCANNING", ""))
        list.add(OptionsData("TEXT SCANNING", ""))

        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = OptionsAdapter(list)
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
    }

    override fun changeStatusBarColor() {
        val window = window ?: return
        val view = window.decorView ?: return
//        var flags = view.systemUiVisibility
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) flags = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
//        view.systemUiVisibility = flags
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.disp_color_1))
        }
    }
}
