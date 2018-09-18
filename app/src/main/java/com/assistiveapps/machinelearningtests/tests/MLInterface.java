package com.assistiveapps.machinelearningtests.tests;

import android.view.View;

public interface MLInterface {

    void setup();

    void openGalleryForImageSelect();

    void openCameraForImageSelect();

    void showCoordinator(String coordinatorText);

    void processImage();

    void hidePlaceholderViews();

    void setDominantColorBackground();

    void setClickListeners();

}
