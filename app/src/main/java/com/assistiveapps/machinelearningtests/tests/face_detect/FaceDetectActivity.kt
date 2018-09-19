package com.assistiveapps.machinelearningtests.tests.face_detect

import android.annotation.SuppressLint
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
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.face.FirebaseVisionFace
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions
import kotlinx.android.synthetic.main.common_test_ui.*
import java.text.DecimalFormat
import java.util.ArrayList

class FaceDetectActivity : MLActivity(), FaceDetectInterface {

    lateinit var dialogFragment: MLDialogFragment

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
        val options = FirebaseVisionFaceDetectorOptions.Builder()
                .setTrackingEnabled(false)
                .setModeType(FirebaseVisionFaceDetectorOptions.ACCURATE_MODE)
                .setLandmarkType(FirebaseVisionFaceDetectorOptions.ALL_LANDMARKS)
                .setClassificationType(FirebaseVisionFaceDetectorOptions.ALL_CLASSIFICATIONS)
                .setMinFaceSize(0.05f).build()
        val detector = FirebaseVision.getInstance().getVisionFaceDetector(options)
        detector.detectInImage(visionImage).addOnSuccessListener {
            processFaceDetectorFaces(it)
            done_button.setEnabled(true)
        }.addOnFailureListener {
            showCoordinator(getString(R.string.please_try_again))
            done_button.setEnabled(true)
        }
        //enable button
    }

    fun processFaceDetectorFaces(firebaseVisionFaces: List<FirebaseVisionFace>) {

        val boundingBoxes = ArrayList<Rect>()

        val builder = StringBuilder()

        var faceCount = 1

        val decimalFormat = DecimalFormat("#.##")

        for (face in firebaseVisionFaces) {
            if (face.boundingBox != null) {
                boundingBoxes.add(face.boundingBox)
            }

            builder.append("<hr/>").append("<h1>").append("<font color=\'black\'>").append("Face ")
                    .append(faceCount).append("</font></h1>")
                    .append(returnPercentageValue(
                            "Smile",
                            decimalFormat,
                            face.smilingProbability)
                    )
                    .append(returnPercentageValue(
                            "Left eye open",
                            decimalFormat,
                            face.leftEyeOpenProbability)
                    )
                    .append(returnPercentageValue(
                            "Right eye open",
                            decimalFormat,
                            face.rightEyeOpenProbability)
                    )
                    .append("<br>")

            faceCount++
        }

        showPreviewInNewBitmapIfAny(boundingBoxes)

        setResultsToBottomSheet(HEADER_TEXT_SCAN, builder.toString())
    }

    fun setResultsToBottomSheet(header: String, text: String) {
        dialogFragment.setHeader(header)
        dialogFragment.setResults(text)
    }

    fun showPreviewInNewBitmapIfAny(boundingBoxes: List<Rect>) {
        if (bitmap != null && boundingBoxes.size > 0) {
            val newBitmap = bitmap!!.copy(Bitmap.Config.ARGB_8888, true)
            val canvas = Canvas(newBitmap)
            val paint = Paint(Paint.ANTI_ALIAS_FLAG)
            paint.strokeWidth = 20f
            paint.style = Paint.Style.STROKE
            paint.color = ContextCompat.getColor(this, R.color.green_shade_three)

            for (rect in boundingBoxes) {
                canvas.drawRect(rect, paint)
            }

            image_to_work_on.setImageBitmap(newBitmap)

            for (rect in boundingBoxes) {
                canvas.clipRect(rect)
            }
        }
    }


    @SuppressLint("ResourceType")
    fun returnPercentageValue(header: String, decimalFormat: DecimalFormat, prob: Float): String? {
        var probability: Float = prob
        if (probability < 0)
            probability = 0.00f
        else {
            try {
                probability = java.lang.Float.valueOf(decimalFormat.format((probability * 100).toDouble()))
            } catch (e: NumberFormatException) {
                probability = 0f
            }
        }

        val builder = StringBuilder()
        val colorHex: String

        if (probability > 90) {
            colorHex = resources.getString(R.color.ninety_plus)
        } else if (probability > 80) {
            colorHex = String.format("#%06x", ContextCompat.getColor(this, R.color.eighty_plus) and 0xffffff)
        } else if (probability > 70) {
            colorHex = String.format("#%06x", ContextCompat.getColor(this, R.color.seventy_plus) and 0xffffff)
        } else if (probability > 60) {
            colorHex = String.format("#%06x", ContextCompat.getColor(this, R.color.sixty_plus) and 0xffffff)
        } else if (probability > 50) {
            colorHex = String.format("#%06x", ContextCompat.getColor(this, R.color.fifty_plus) and 0xffffff)
        } else if (probability > 40) {
            colorHex = String.format("#%06x", ContextCompat.getColor(this, R.color.forty_plus) and 0xffffff)
        } else if (probability > 30) {
            colorHex = String.format("#%06x", ContextCompat.getColor(this, R.color.thirty_plus) and 0xffffff)
        } else if (probability > 20) {
            colorHex = String.format("#%06x", ContextCompat.getColor(this, R.color.twenty_plus) and 0xffffff)
        } else if (probability > 10) {
            colorHex = String.format("#%06x", ContextCompat.getColor(this, R.color.ten_plus) and 0xffffff)
        } else if (probability >= 0) {
            colorHex = String.format("#%06x", ContextCompat.getColor(this, R.color.zero_plus) and 0xffffff)
        } else {
            colorHex = String.format("#%06x", ContextCompat.getColor(this, R.color.zero_plus) and 0xffffff)
        }

        builder.append("<h5>")
                .append(header).append(" <font color=\'")
                .append("#")
                .append(colorHex.substring(3))
                .append("\'>").append(probability)
                .append("%</font> sure</h4>\n")

        return builder.toString()
    }

    override fun showBottomSheetResults() {
        dialogFragment = MLDialogFragment()
        dialogFragment.setRetainInstance(true)

        dialogFragment.show(supportFragmentManager, getString(R.string.tag_bottom_sheet))
    }

    override fun setup() {

        HEADER_TEXT_SCAN = "FACE DETECTION"

        setStatusBarColor()

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_face_detect)

        setup()
    }
}
