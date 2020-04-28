package com.timmytime.predictor.lambda.enumerator;

import java.util.UUID;

public enum MachineLearningByCountryTypes {
    PREDICT_GOALS("goals"),
    PREDICT_RESULTS("results");
    private String displayName;
    private String type;
    private UUID completedReceipt;

    MachineLearningByCountryTypes(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public UUID getCompletedReceipt() {
        return completedReceipt;
    }

    public void setCompletedReceipt(UUID completedReceipt) {
        this.completedReceipt = completedReceipt;
    }
}
