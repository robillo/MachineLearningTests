package com.assistiveapps.machinelearningtests.tests.image_label;

import com.google.firebase.ml.vision.label.FirebaseVisionLabel;

import java.text.DecimalFormat;
import java.util.List;

public interface ImageLabelInterface {

    void processLabelDetectionResult(List<FirebaseVisionLabel> firebaseVisionLabels);

    String returnPercentageValue(String header, DecimalFormat decimalFormat, float probability);

    void showBottomSheetResults();

    void setStatusBarColor();

}
