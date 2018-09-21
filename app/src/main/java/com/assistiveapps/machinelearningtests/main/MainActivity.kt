package com.assistiveapps.machinelearningtests.main

import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import com.assistiveapps.machinelearningtests.R
import com.assistiveapps.machinelearningtests.tests.barcode_scan.BarcodeScanActivity
import com.assistiveapps.machinelearningtests.tests.face_detect.FaceDetectActivity
import com.assistiveapps.machinelearningtests.tests.image_label.ImageLabelActivity
import com.assistiveapps.machinelearningtests.tests.text_scan.TextScanActivity
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), MainInterface {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        changeStatusBarColor()
        initialize()
        setClickListeners()
    }

    override fun setClickListeners() {
        barcode_scanning_card.setOnClickListener {
            startActivity(Intent(this, BarcodeScanActivity::class.java))
        }
        face_detection_card.setOnClickListener {
            startActivity(Intent(this, FaceDetectActivity::class.java))
        }
        image_label_generator_card.setOnClickListener {
            startActivity(Intent(this, ImageLabelActivity::class.java))
        }
        text_scanning_card.setOnClickListener {
            startActivity(Intent(this, TextScanActivity::class.java))
        }
    }

    override fun initialize() {

    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
    }

    override fun changeStatusBarColor() {
        val window = window ?: return
        val view = window.decorView ?: return
        var flags = view.systemUiVisibility
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) flags = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        view.systemUiVisibility = flags
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    }
}
