package com.assistiveapps.machinelearningtests.main.rv;

public class OptionsData {

    private String option_name;
    private int option_drawable;

    public OptionsData(String option_name, int option_drawable) {
        this.option_name = option_name;
        this.option_drawable = option_drawable;
    }

    public String getOption_name() {
        return option_name;
    }

    public void setOption_name(String option_name) {
        this.option_name = option_name;
    }

    public int getOption_drawable() {
        return option_drawable;
    }

    public void setOption_drawable(int option_drawable) {
        this.option_drawable = option_drawable;
    }
}
