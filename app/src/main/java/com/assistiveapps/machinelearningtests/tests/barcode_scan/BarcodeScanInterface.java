package com.assistiveapps.machinelearningtests.tests.barcode_scan;

import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode;

import java.util.List;

public interface BarcodeScanInterface {

    void showBottomSheetResults();

    void setStatusBarColor();

    void processBarcodeReaderResult(List<FirebaseVisionBarcode> firebaseVisionBarcodes);

    void showPreviewInNewBitmapIfAny();

}
