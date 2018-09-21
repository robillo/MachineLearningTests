package com.assistiveapps.machinelearningtests.tests.barcode_scan

import android.os.Bundle
import com.assistiveapps.machinelearningtests.R
import com.assistiveapps.machinelearningtests.tests.MLActivity

class BarcodeScanActivity : MLActivity(), BarcodeScanInterface {

    override fun setup() {

    }

    override fun processImage() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_barcode_scan)
    }
}
