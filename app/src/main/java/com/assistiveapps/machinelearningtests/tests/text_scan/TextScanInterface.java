package com.assistiveapps.machinelearningtests.tests.text_scan;

import com.google.firebase.ml.vision.text.FirebaseVisionText;

import java.util.List;

public interface TextScanInterface {

    void showBottomSheetResults();

    void setStatusBarColor();

    void processTextRecognitionResult(List<FirebaseVisionText.TextBlock> texts);

}
