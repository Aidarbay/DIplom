package OmGU.IMIT;

public class Matrix {
    private int[][] elements;
    private int size;

    Matrix(int[][] elements) {
        this.size = elements.length;
        this.elements = elements;
    }

    private Matrix(Matrix oldMatrix) {
        size = oldMatrix.getSize();
        elements = new int[oldMatrix.getSize()][oldMatrix.getSize()];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                elements[i][j] = oldMatrix.getElement(i, j);
            }
        }
    }

    int getDeterminant() {
        Matrix minor;
        int result = 0;
        if (size == 1) {
            return elements[0][0];
        } else {
            for (int i = 0; i < size; i++) {
                int[][] minorElements = new int[size - 1][size - 1];
                for (int j = 1; j < size; j++) {
                    for (int k = 0; k < size; k++) {
                        if (k < i) {
                            minorElements[j - 1][k] = elements[j][k];
                        } else if (k > i) {
                            minorElements[j - 1][k - 1] = elements[j][k];
                        }
                    }
                }
                minor = new Matrix(minorElements);
                result = result + (int) Math.pow(-1, i) * elements[0][i] * minor.getDeterminant();
            }
            return result;
        }
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
        return new Matrix(newMatrix);
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
                if (newMatrix.getElement(j, k) == 1) {
                    index = j;
                }
            }
            if (k != index && newMatrix.getElement(k, k) != 1) {
                for (int j = 0; j < size; j++) {
                    element = newMatrix.getElement(k, j);
                    newMatrix.setElement(k, j, newMatrix.getElement(index, j));
                    newMatrix.setElement(index, j, element);
                    element = E[k][j];
                    E[k][j] = E[index][j];
                    E[index][j] = element;
                }
            }
            for (int i = 0; i < size; i++) {
                if (i != k && newMatrix.getElement(i, k) == 1) {
                    for (int j = 0; j < size; j++) {
                        newMatrix.setElement(i, j, (newMatrix.getElement(i, j) + newMatrix.getElement(k, j)) % 2);
                        E[i][j] = (E[i][j] + E[k][j]) % 2;
                    }
                }
            }


        }

        return new Matrix(E);
    }

    Matrix transpose() {
        int[][] trans = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                trans[j][i] = elements[i][j];
            }
        }
        return new Matrix(trans);
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder("");
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                str.append(elements[i][j]).append(" ");
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
                if (elements[i][j] != matrix1.getElement(i, j)) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = 0;
        int[] arr = {elements[0][0], elements[0][1], elements[0][2], elements[1][0], elements[1][1], elements[1][2],
                elements[2][0],
                elements[2][1], elements[2][2]};
        for (int i = 0; i < size * 3; i++) {
            if (arr[i] == 1) {
                result += Math.pow(2, i);
            }
        }
        return result;
    }

    int getOrder() {
        int order = 1;
        int[][] unitArray = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
        Matrix unitMatrix = new Matrix(unitArray);
        Matrix newMatrix = this;
        while (!newMatrix.equals(unitMatrix)) {
            newMatrix = multiply(newMatrix, this);
            order++;
        }
        return order;
    }

    private int getSize() {
        return size;
    }

    private int getElement(int i, int j) {
        return elements[i][j];
    }

    private void setElement(int i, int j, int element) {
        elements[i][j] = element;
    }
}
