package com.assistiveapps.machinelearningtests.tests.image_label

import android.os.Bundle
import com.assistiveapps.machinelearningtests.R
import com.assistiveapps.machinelearningtests.tests.MLActivity

class ImageLabelActivity : MLActivity(), ImageLabelInterface {

    override fun setup() {

    }

    override fun processImage() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_label)
    }
}
