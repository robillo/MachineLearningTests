package com.assistiveapps.machinelearningtests.tests.barcode_scan

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.WindowManager
import com.assistiveapps.machinelearningtests.R
import com.assistiveapps.machinelearningtests.tests.MLActivity
import com.assistiveapps.machinelearningtests.tests.MLDialogFragment
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import kotlinx.android.synthetic.main.common_test_ui.*
import java.util.ArrayList

class BarcodeScanActivity : MLActivity(), BarcodeScanInterface {

    lateinit var dialogFragment: MLDialogFragment

    override fun showBottomSheetResults() {
        dialogFragment = MLDialogFragment()
        dialogFragment.setRetainInstance(true)

        dialogFragment.show(supportFragmentManager, getString(R.string.tag_bottom_sheet))
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

    override fun processBarcodeReaderResult(firebaseVisionBarcodes: MutableList<FirebaseVisionBarcode>?) {
        boundingBoxes = null
        boundingBoxes = ArrayList()

        val builder = StringBuilder()

        if (firebaseVisionBarcodes != null) {
            for (barcode in firebaseVisionBarcodes) {
                if (barcode.getBoundingBox() != null) {
                    (boundingBoxes as ArrayList<Rect>).add(barcode.getBoundingBox()!!)
                }
                builder.append(barcode.getDisplayValue()).append("\n")
            }
        }

        showPreviewInNewBitmapIfAny()

        setResultsToBottomSheet(HEADER_TEXT_SCAN, builder.toString())
    }

    fun setResultsToBottomSheet(header: String, text: String) {
        dialogFragment.setHeader(header)
        dialogFragment.setResults(text)
    }

    override fun showPreviewInNewBitmapIfAny() {
        if (bitmap != null && boundingBoxes!!.size > 0) {

            val newBitmap = bitmap!!.copy(Bitmap.Config.ARGB_8888, true)
            val canvas = Canvas(newBitmap)
            val paint = Paint(Paint.ANTI_ALIAS_FLAG)
            paint.strokeWidth = 20f
            paint.style = Paint.Style.STROKE
            paint.color = ContextCompat.getColor(this, R.color.green_shade_three)

            for (rect in boundingBoxes!!) {
                canvas.drawRect(rect, paint)
            }

            image_to_work_on.setImageBitmap(newBitmap)
        }
    }

    private var boundingBoxes: MutableList<Rect>? = null

    override fun setup() {

        HEADER_TEXT_SCAN = "BARCODE SCANNING"

        setStatusBarColor()
        setClickListeners()
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
        val detector = FirebaseVision.getInstance().visionBarcodeDetector
        detector.detectInImage(visionImage).addOnSuccessListener { firebaseVisionBarcodes ->
            processBarcodeReaderResult(firebaseVisionBarcodes)
            done_button.setEnabled(true)
        }.addOnFailureListener {
            showCoordinator(getString(R.string.please_try_again))
            done_button.setEnabled(true)
        }
        //enable button
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_barcode_scan)

        setup()
    }
}
