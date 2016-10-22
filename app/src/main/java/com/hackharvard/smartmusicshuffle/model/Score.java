package com.hackharvard.smartmusicshuffle.model;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class Score {
    @SerializedName("anger")
    private float anger;
    @SerializedName("contempt")
    private float contempt;
    @SerializedName("disgust")
    private float disgust;
    @SerializedName("fear")
    private float fear;
    @SerializedName("happiness")
    private float happiness;
    @SerializedName("neutral")
    private float neutral;
    @SerializedName("sadness")
    private float sadness;
    @SerializedName("surprise")
    private float surprise;

    public float getAnger() {
        return anger;
    }

    public void setAnger(float anger) {
        this.anger = anger;
    }

    public float getContempt() {
        return contempt;
    }

    public void setContempt(float contempt) {
        this.contempt = contempt;
    }

    public float getDisgust() {
        return disgust;
    }

    public void setDisgust(float disgust) {
        this.disgust = disgust;
    }

    public float getFear() {
        return fear;
    }

    public void setFear(float fear) {
        this.fear = fear;
    }

    public float getHappiness() {
        return happiness;
    }

    public void setHappiness(float happiness) {
        this.happiness = happiness;
    }

    public float getNeutral() {
        return neutral;
    }

    public void setNeutral(float neutral) {
        this.neutral = neutral;
    }

    public float getSadness() {
        return sadness;
    }

    public void setSadness(float sadness) {
        this.sadness = sadness;
    }

    public float getSurprise() {
        return surprise;
    }

    public void setSurprise(float surprise) {
        this.surprise = surprise;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Score score = (Score) o;
        return Float.compare(score.anger, anger) == 0 &&
                Float.compare(score.contempt, contempt) == 0 &&
                Float.compare(score.disgust, disgust) == 0 &&
                Float.compare(score.fear, fear) == 0 &&
                Float.compare(score.happiness, happiness) == 0 &&
                Float.compare(score.neutral, neutral) == 0 &&
                Float.compare(score.sadness, sadness) == 0 &&
                Float.compare(score.surprise, surprise) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(anger, contempt, disgust, fear, happiness, neutral, sadness, surprise);
    }
}
