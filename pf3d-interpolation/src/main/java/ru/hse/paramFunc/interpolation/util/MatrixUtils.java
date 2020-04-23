package ru.hse.paramFunc.interpolation.util;

public class MatrixUtils {

    public static double[][] sum(double[][] m1, double[][] m2) {
        double[][] result = new double[m1.length][];
        for(int row = 0; row < result.length; ++row) {
            result[row] = new double[m1[row].length];
            for(int column = 0; column < m1[row].length; ++column) {
                result[row][column] = m1[row][column] + m2[row][column];
            }
        }
        return result;
    }

    public static double[][] subtract(double[][] m1, double[][] m2) {
        double[][] result = new double[m1.length][];
        for(int row = 0; row < result.length; ++row) {
            result[row] = new double[m1[row].length];
            for(int column = 0; column < m1[row].length; ++column) {
                result[row][column] = m1[row][column] - m2[row][column];
            }
        }
        return result;
    }

    public static double[][] multiplyByNumber(double[][] m1, double a) {
        double[][] result = new double[m1.length][];
        for(int row = 0; row < result.length; ++row) {
            result[row] = new double[m1[row].length];
            for(int column = 0; column < m1[row].length; ++column) {
                result[row][column] = m1[row][column] * a;
            }
        }
        return result;
    }

    public static double[][] divideByNumber(double[][] m1, double a) {
        double[][] result = new double[m1.length][];
        for(int row = 0; row < result.length; ++row) {
            result[row] = new double[m1[row].length];
            for(int column = 0; column < m1[row].length; ++column) {
                result[row][column] = m1[row][column] / a;
            }
        }
        return result;
    }

}
