package ru.hse.paramFunc.interpolation.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MatrixUtilsTest {

    @Test
    void sum() {
        double[][] m1 = new double[][] {
                {1, 2, 3},
                {1, 2, 3},
                {1, 2, 3}
        };
        double[][] m2 = new double[][] {
                {1, 1, 1},
                {1, 1, 1},
                {0, 0, 0}
        };
        double[][] check = new double[][] {
                {2, 3, 4},
                {2, 3, 4},
                {1, 2, 3}
        };
        double[][] result = MatrixUtils.sum(m1, m2);
        for(int i = 0; i < check.length; ++i) {
            for(int j = 0; j < check[i].length; ++j) {
                assertEquals(check[i][j], result[i][j]);
            }
        }
    }

    @Test
    void subtract() {
        double[][] m1 = new double[][] {
                {1, 2, 3},
                {1, 2, 3},
                {1, 2, 3}
        };
        double[][] m2 = new double[][] {
                {1, 1, 1},
                {1, 1, 1},
                {0, 0, 0}
        };
        double[][] check = new double[][] {
                {0, 1, 2},
                {0, 1, 2},
                {1, 2, 3}
        };
        double[][] result = MatrixUtils.subtract(m1, m2);
        for(int i = 0; i < check.length; ++i) {
            for(int j = 0; j < check[i].length; ++j) {
                assertEquals(check[i][j], result[i][j]);
            }
        }
    }

    @Test
    void multiplyByNumber() {
        double[][] m1 = new double[][] {
                {1, 2, 3},
                {1, 2, 3},
                {1, 2, 3}
        };
        double a = 2;
        double[][] check = new double[][] {
                {2, 4, 6},
                {2, 4, 6},
                {2, 4, 6}
        };
        double[][] result = MatrixUtils.multiplyByNumber(m1, a);
        for(int i = 0; i < check.length; ++i) {
            for(int j = 0; j < check[i].length; ++j) {
                assertEquals(check[i][j], result[i][j]);
            }
        }
    }

    @Test
    void divideByNumber() {
        double[][] m1 = new double[][] {
                {2, 4, 6},
                {2, 4, 6},
                {2, 4, 6}
        };
        double a = 2;
        double[][] check = new double[][] {
                {1, 2, 3},
                {1, 2, 3},
                {1, 2, 3}
        };
        double[][] result = MatrixUtils.divideByNumber(m1, a);
        for(int i = 0; i < check.length; ++i) {
            for(int j = 0; j < check[i].length; ++j) {
                assertEquals(check[i][j], result[i][j]);
            }
        }
    }


}