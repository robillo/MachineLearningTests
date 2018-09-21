package com.assistiveapps.machinelearningtests.tests.text_scan

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import com.assistiveapps.machinelearningtests.R
import com.assistiveapps.machinelearningtests.tests.MLActivity
import com.assistiveapps.machinelearningtests.tests.MLDialogFragment
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.text.FirebaseVisionText
import kotlinx.android.synthetic.main.common_test_ui.*

class TextScanActivity : MLActivity(), TextScanInterface {

    lateinit var dialogFragment: MLDialogFragment

    override fun setup() {

        HEADER_TEXT_SCAN = "TEXT SCANNING"

        setStatusBarColor()
        setClickListeners()
    }

    fun setResultsToBottomSheet(header: String, text: String) {
        dialogFragment.setHeader(header)
        dialogFragment.setResults(text)
    }

    override fun setStatusBarColor() {
        val window = window ?: return
        val view = window.decorView ?: return
        var flags = view.systemUiVisibility
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) flags = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        view.systemUiVisibility = flags
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    }

    override fun processTextRecognitionResult(texts: MutableList<FirebaseVisionText.TextBlock>?) {
        val builder = StringBuilder()
        if (texts != null) {
            for (t in texts) {
                builder.append(t.getText()).append("\n")
            }
        }
        setResultsToBottomSheet(HEADER_TEXT_SCAN, builder.toString())
    }

    override fun showBottomSheetResults() {
        dialogFragment = MLDialogFragment()
        dialogFragment.setRetainInstance(true)

        dialogFragment.show(supportFragmentManager, getString(R.string.tag_bottom_sheet))
    }

    override fun processImage() {
        //disable button till image is processed
        done_button.setEnabled(false)
        //process image
        if (bitmap == null) {
            showCoordinator(getString(R.string.please_show_image))
            done_button.setEnabled(true)
            return
        }

        showBottomSheetResults()

        val visionImage = FirebaseVisionImage.fromBitmap(bitmap!!)
        val detector = FirebaseVision.getInstance().onDeviceTextRecognizer
        detector.processImage(visionImage).addOnSuccessListener { firebaseVisionText ->
            processTextRecognitionResult(firebaseVisionText.textBlocks)
            done_button.setEnabled(true)
        }.addOnFailureListener {
            showCoordinator(getString(R.string.please_try_again))
            done_button.setEnabled(true)
        }
        //enable button
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text_scan)

        setup()
    }
}
