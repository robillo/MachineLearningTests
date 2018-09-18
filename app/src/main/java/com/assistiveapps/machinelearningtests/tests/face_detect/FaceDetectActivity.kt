package com.assistiveapps.machinelearningtests.tests.face_detect

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.assistiveapps.machinelearningtests.R

class FaceDetectActivity : AppCompatActivity(), FaceDetectInterface {

    override fun setup() {
        setStatusBarColor()

    }

    override fun setStatusBarColor() {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_face_detect)

        setup()
    }
}
