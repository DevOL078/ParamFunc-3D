package ru.hse.paramfunc.domain;


import java.util.Objects;

public class FunctionPoint {

    private int t;

    private float systemX;
    private float systemY;
    private float systemZ;

    private float originalX;
    private float originalY;
    private float originalZ;

    public FunctionPoint() {
    }

    public FunctionPoint(int t, float originalX, float originalY, float originalZ) {
        this.t = t;
        this.originalX = originalX;
        this.originalY = originalY;
        this.originalZ = originalZ;
    }

    public int getT() {
        return t;
    }

    public void setT(int t) {
        this.t = t;
    }

    public float getSystemX() {
        return systemX;
    }

    public void setSystemX(float systemX) {
        this.systemX = systemX;
    }

    public float getSystemY() {
        return systemY;
    }

    public void setSystemY(float systemY) {
        this.systemY = systemY;
    }

    public float getSystemZ() {
        return systemZ;
    }

    public void setSystemZ(float systemZ) {
        this.systemZ = systemZ;
    }

    public float getOriginalX() {
        return originalX;
    }

    public void setOriginalX(float originalX) {
        this.originalX = originalX;
    }

    public float getOriginalY() {
        return originalY;
    }

    public void setOriginalY(float originalY) {
        this.originalY = originalY;
    }

    public float getOriginalZ() {
        return originalZ;
    }

    public void setOriginalZ(float originalZ) {
        this.originalZ = originalZ;
    }

    @Override
    public String toString() {
        return "FunctionPoint{" +
                "t=" + t +
                ", systemX=" + systemX +
                ", systemY=" + systemY +
                ", systemZ=" + systemZ +
                ", originalX=" + originalX +
                ", originalY=" + originalY +
                ", originalZ=" + originalZ +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FunctionPoint that = (FunctionPoint) o;
        return t == that.t &&
                Float.compare(that.systemX, systemX) == 0 &&
                Float.compare(that.systemY, systemY) == 0 &&
                Float.compare(that.systemZ, systemZ) == 0 &&
                Float.compare(that.originalX, originalX) == 0 &&
                Float.compare(that.originalY, originalY) == 0 &&
                Float.compare(that.originalZ, originalZ) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(t, systemX, systemY, systemZ, originalX, originalY, originalZ);
    }

    public static FunctionPointBuilder builder() {
        return new FunctionPointBuilder();
    }

    public static class FunctionPointBuilder {

        private FunctionPoint functionPoint;

        public FunctionPointBuilder() {
            this.functionPoint = new FunctionPoint();
        }

        public FunctionPointBuilder t(int t) {
            this.functionPoint.setT(t);
            return this;
        }

        public FunctionPointBuilder systemX(float systemX) {
            this.functionPoint.setSystemX(systemX);
            return this;
        }

        public FunctionPointBuilder systemY(float systemY) {
            this.functionPoint.setSystemY(systemY);
            return this;
        }

        public FunctionPointBuilder systemZ(float systemZ) {
            this.functionPoint.setSystemZ(systemZ);
            return this;
        }

        public FunctionPointBuilder originalX(float originalX) {
            this.functionPoint.setOriginalX(originalX);
            return this;
        }

        public FunctionPointBuilder originalY(float originalY) {
            this.functionPoint.setOriginalY(originalY);
            return this;
        }

        public FunctionPointBuilder originalZ(float originalZ) {
            this.functionPoint.setOriginalZ(originalZ);
            return this;
        }

        public FunctionPoint build() {
            return this.functionPoint;
        }


    }

}
