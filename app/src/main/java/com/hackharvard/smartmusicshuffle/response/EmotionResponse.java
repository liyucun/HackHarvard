package com.hackharvard.smartmusicshuffle.response;

import com.google.gson.annotations.SerializedName;
import com.hackharvard.smartmusicshuffle.model.FaceRectangle;
import com.hackharvard.smartmusicshuffle.model.Score;

import java.util.Objects;

public class EmotionResponse {
    @SerializedName("faceRectangle")
    private FaceRectangle faceRectangle;
    @SerializedName("scores")
    private Score score;

    public FaceRectangle getFaceRectangle() {
        return faceRectangle;
    }

    public void setFaceRectangle(FaceRectangle faceRectangle) {
        this.faceRectangle = faceRectangle;
    }

    public Score getScore() {
        return score;
    }

    public void setScore(Score score) {
        this.score = score;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmotionResponse that = (EmotionResponse) o;
        return Objects.equals(faceRectangle, that.faceRectangle) &&
                Objects.equals(score, that.score);
    }

    @Override
    public int hashCode() {
        return Objects.hash(faceRectangle, score);
    }
}
