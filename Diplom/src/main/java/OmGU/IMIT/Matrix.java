package OmGU.IMIT;

import java.util.Arrays;
import java.util.Objects;

public class Matrix {
    private int[][] matrix;
    private int size;

    Matrix(int[][] matrix, int resize) {
        this.size = resize;
        this.matrix = matrix;
    }

    Matrix(Matrix oldMatrix) {
        size = oldMatrix.getSize();
        matrix  = new int[oldMatrix.getSize()][oldMatrix.getSize()];
        for(int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {
                matrix[i][j] = oldMatrix.getElement(i,j);
            }
        }
    }

    int determinant() {
        return (matrix[0][0] * matrix[1][1] * matrix[2][2] + matrix[0][1] * matrix[1][0] * matrix[2][2] +
                matrix[0][0] * matrix[2][1] * matrix[1][2] + matrix[0][2] * matrix[1][1] * matrix[2][0] +
                matrix[0][1] * matrix[1][2] * matrix[2][0] + matrix[0][2] * matrix[2][1] * matrix[1][0]) % 2;

    }

    static Matrix multiply(Matrix matrix1, Matrix matrix2) {
        int[][] newMatrix = new int[3][3];
        for (int i = 0; i < matrix1.getSize(); i++) {
            for (int j = 0; j < matrix2.getSize(); j++) {
                newMatrix[i][j] = ((matrix1.getElement(i, 0) * matrix2.getElement(0, j)) +
                        (matrix1.getElement(i, 1) * matrix2.getElement(1, j)) +
                        (matrix1.getElement(i, 2) * matrix2.getElement(2, j))) % 2;
            }
        }
        return new Matrix(newMatrix, 3);
    }

    Matrix inversion() {
        int[][] E = new int[size][size];
        Matrix newMatrix = new Matrix(this);

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                E[i][j] = 0;

                if (i == j) {
                    E[i][j] = 1;
                }
            }
        }
        int index = 0;
        int element;
        for (int k = 0; k < size; k++) {

            for (int j = 0; j < size; j++) {
                if (newMatrix.getElement(j,k) == 1) {
                    index = j;
                }
            }
            if (k != index && newMatrix.getElement(k,k) != 1) {
                for (int j = 0; j < size; j++) {
                    element = newMatrix.getElement(k,j);
                    newMatrix.setElement(k,j, newMatrix.getElement(index,j));
                    newMatrix.setElement(index,j, element);
                    element = E[k][j];
                    E[k][j] = E[index][j];
                    E[index][j] = element;
                }
            }
            for (int i = 0; i < size; i++) {
                if (i != k && newMatrix.getElement(i,k) == 1) {
                    for (int j = 0; j < size; j++) {
                        newMatrix.setElement(i,j,(newMatrix.getElement(i,j) + newMatrix.getElement(k,j)) % 2);
                        E[i][j] = (E[i][j] + E[k][j]) % 2;
                    }
                }
            }


        }

        return new Matrix(E, 3);
    }

    Matrix transpose() {
        int[][] trans = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                trans[j][i] = matrix[i][j];
            }
        }
        return new Matrix(trans, 3);
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder("");
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                str.append(matrix[i][j]).append(" ");
            }
            str.append("\n");
        }
        return str.toString();
    }

    @Override
    public boolean equals(Object o) {
        Matrix matrix1 = (Matrix) o;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (matrix[i][j] != matrix1.getElement(i, j)) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(size);
        result = 31 * result + Arrays.hashCode(matrix);
        return result;
    }

    private int getSize() {
        return size;
    }

    private int getElement(int i, int j) {
        return matrix[i][j];
    }

    private void setElement(int i, int j, int element) {
        matrix[i][j] = element;
    }
}
