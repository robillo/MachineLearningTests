package com.assistiveapps.machinelearningtests.tests

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.support.design.widget.Snackbar
import android.support.v4.app.FragmentActivity
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import android.support.v7.graphics.Palette
import android.view.View
import android.widget.TextView
import android.widget.Toast

import com.assistiveapps.machinelearningtests.R

import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date

import kotlinx.android.synthetic.main.common_test_ui.*

abstract class MLActivity : FragmentActivity(), MLInterface {

    val REQUEST_CODE_CAPTURE_IMAGE = 102
    val CHOOSER_INTENT_TITLE = "Select Image"
    val REQUEST_CODE_PICK_IMAGE = 101
    val IMAGE_CONTENT_TYPE = "image/*"
    protected var HEADER_TEXT_SCAN: String = ""
    protected var bitmap: Bitmap? = null
    private var mCurrentPhotoPath: String? = null

    override fun setClickListeners() {
        place_holder_banner.setOnClickListener {  }
        place_holder_gallery.setOnClickListener {  }
        place_holder_frame.setOnClickListener {  }
        coordinator_layout.setOnClickListener {  }
        rotate_left.setOnClickListener { setRotateLeft() }
        rotate_right.setOnClickListener { setRotateRight() }
        crop_image.setOnClickListener { setCropImage() }
        image_to_work_on.setOnClickListener {  }
        select_image_from_gallery.setOnClickListener { openGalleryForImageSelect() }
        capture_image.setOnClickListener { it ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if(checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(Array(1) {Manifest.permission.CAMERA}, 100)
                }
                else {
                    openCameraForImageSelect()
                }
            } else {
                openCameraForImageSelect()
            }
        }
        done_button.setOnClickListener { processImage() }
    }

    override fun openGalleryForImageSelect() {
        val getIntent = Intent(Intent.ACTION_GET_CONTENT)
        getIntent.type = IMAGE_CONTENT_TYPE

        val pickIntent = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickIntent.type = IMAGE_CONTENT_TYPE

        val chooserIntent = Intent.createChooser(getIntent, CHOOSER_INTENT_TITLE)
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(pickIntent))

        startActivityForResult(chooserIntent, REQUEST_CODE_PICK_IMAGE)
    }

    override fun openCameraForImageSelect() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (cameraIntent.resolveActivity(packageManager) != null) {

            val pictureFile: File?
            try {
                pictureFile = createImageFile()
            } catch (ex: IOException) {
                showCoordinator(getString(R.string.couldnt_create_error))
                return
            }

            val photoURI = FileProvider.getUriForFile(this,
                    "com.assistiveapps.machinelearningtests.GenericFileProvider",
                    pictureFile)
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            startActivityForResult(cameraIntent, REQUEST_CODE_CAPTURE_IMAGE)
        }
    }

    override fun showCoordinator(coordinatorText: String) {
        val s = Snackbar.make(coordinator_layout!!, coordinatorText, Snackbar.LENGTH_SHORT)
        val v = s.view
        v.setBackgroundColor(ContextCompat.getColor(this, R.color.red_shade_three_less_vibrant))
        val t = v.findViewById<TextView>(android.support.design.R.id.snackbar_text)
        t.setTextColor(ContextCompat.getColor(this, R.color.white))
        t.textAlignment = View.TEXT_ALIGNMENT_CENTER
        s.show()
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == RESULT_OK) {
            if (data != null) {
                val selectedImage = data.data ?: return

                try {
                    bitmap = BitmapFactory.decodeStream(
                            contentResolver.openInputStream(selectedImage)
                    )
                    image_to_work_on!!.setImageBitmap(bitmap)
                    hidePlaceholderViews()
                    setDominantColorBackground()
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                }

            } else {
                showCoordinator(getString(R.string.no_image_selected_oops))
            }
        } else if (requestCode == REQUEST_CODE_CAPTURE_IMAGE && resultCode == RESULT_OK) {
            bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath)
            val imageFile = File(mCurrentPhotoPath!!)
            if (imageFile.exists()) {
                image_to_work_on!!.setImageURI(Uri.fromFile(imageFile))
                hidePlaceholderViews()
                setDominantColorBackground()
            } else {
                showCoordinator(getString(R.string.null_photo_error))
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == 100) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCameraForImageSelect()
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_SHORT).show()
            }
            else
                showCoordinator("camera permission denied")
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        @SuppressLint("SimpleDateFormat") val timeStamp = SimpleDateFormat("yyyyMMddHHmmss").format(Date())
        val pictureFile = "SENSE_IT_ALL_$timeStamp"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(pictureFile, ".jpg", storageDir)
        mCurrentPhotoPath = image.absolutePath
        return image
    }

    fun setRotateLeft() {
        if (bitmap == null) {
            showCoordinator(getString(R.string.please_show_image))
            return
        }

        val matrix = Matrix()
        matrix.postRotate(-90f)
        val scaledBitmap = Bitmap.createScaledBitmap(bitmap!!, bitmap!!.width, bitmap!!.height, true)
        bitmap = Bitmap.createBitmap(
                scaledBitmap,
                0,
                0,
                scaledBitmap.width,
                scaledBitmap.height,
                matrix,
                true
        )

        image_to_work_on!!.setImageBitmap(bitmap)
        hidePlaceholderViews()
    }

    fun setRotateRight() {
        if (bitmap == null) {
            showCoordinator(getString(R.string.please_show_image))
            return
        }

        val matrix = Matrix()
        matrix.postRotate(90f)
        val scaledBitmap = Bitmap.createScaledBitmap(bitmap!!, bitmap!!.width, bitmap!!.height, true)
        bitmap = Bitmap.createBitmap(
                scaledBitmap,
                0,
                0,
                scaledBitmap.width,
                scaledBitmap.height,
                matrix,
                true
        )

        image_to_work_on!!.setImageBitmap(bitmap)
        hidePlaceholderViews()
    }

    fun setCropImage() {
        if (bitmap == null) showCoordinator(getString(R.string.please_show_image))
    }

    override fun hidePlaceholderViews() {
        place_holder_banner!!.visibility = View.GONE
        place_holder_gallery!!.visibility = View.GONE
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left_activity, R.anim.slide_out_right_activity)
    }

    override fun setDominantColorBackground() {
        if(bitmap != null)
            Palette.from(bitmap!!).generate {
            val vibrantSwatch = it?.vibrantSwatch
            if (vibrantSwatch != null) {
                place_holder_frame!!.setBackgroundColor(vibrantSwatch.rgb)
            }
        }
    }
}
