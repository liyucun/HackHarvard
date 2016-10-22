package com.hackharvard.smartmusicshuffle.model;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class FaceRectangle {
    @SerializedName("left")
    private int left;
    @SerializedName("right")
    private int right;
    @SerializedName("width")
    private int width;
    @SerializedName("height")
    private int height;

    public int getLeft() {
        return left;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public int getRight() {
        return right;
    }

    public void setRight(int right) {
        this.right = right;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FaceRectangle that = (FaceRectangle) o;
        return left == that.left &&
                right == that.right &&
                width == that.width &&
                height == that.height;
    }

    @Override
    public int hashCode() {
        return Objects.hash(left, right, width, height);
    }
}
