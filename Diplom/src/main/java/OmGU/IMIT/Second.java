package OmGU.IMIT;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Second {
    public static void main(String[] args) {
        int[][] unitArray = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
        Matrix matrix1;
        Matrix matrix2;
        Matrix unitMatrix = new Matrix(unitArray, 3);
        Random rand = new Random();
        List<Matrix> group = new ArrayList<>();
        boolean addElem = true;
        boolean find = false;

        List<Matrix> secondOrderMatrix = new ArrayList<Matrix>();
        List<Matrix> thirdOrderMatrix = new ArrayList<Matrix>();
        List<Matrix> otherMatrix = new ArrayList<Matrix>();
        List<Matrix> diffMatrix = new ArrayList<Matrix>();

        while (group.size() != 168) {
            int[][] matrix = {{rand.nextInt(2), rand.nextInt(2), rand.nextInt(2)},
                    {rand.nextInt(2), rand.nextInt(2), rand.nextInt(2)},
                    {rand.nextInt(2), rand.nextInt(2), rand.nextInt(2)}};
            Matrix other = new Matrix(matrix, 3);
            if (other.determinant() == 1 && !group.contains(other)) {
                group.add(other);
            }
        }
        for (Matrix elem :
                group) {
            if (unitMatrix.equals(Matrix.multiply(elem, elem))) {
                secondOrderMatrix.add(elem);
            } else if (unitMatrix.equals(Matrix.multiply(Matrix.multiply(elem, elem), elem))) {
                thirdOrderMatrix.add(elem);
            }
        }
        for (Matrix secondElem :
                secondOrderMatrix) {
            for (Matrix thirdElem :
                    thirdOrderMatrix) {
                otherMatrix.clear();
                otherMatrix.add(secondElem);
                otherMatrix.add(thirdElem);
                otherMatrix.add(Matrix.multiply(thirdElem, thirdElem));
                otherMatrix.add(Matrix.multiply(secondElem, secondElem));
                while (otherMatrix.size() < 168 && addElem) {
                    addElem = false;
                    for (Matrix mainElem :
                            otherMatrix) {
                        matrix1 = Matrix.multiply(mainElem, secondElem);
                        matrix2 = Matrix.multiply(mainElem, thirdElem);
                        if (!otherMatrix.contains(matrix1) && !diffMatrix.contains(matrix1)) {
                            diffMatrix.add(matrix1);
                            addElem = true;
                        }
                        if (!otherMatrix.contains(matrix2) && !diffMatrix.contains(matrix2)) {
                            diffMatrix.add(matrix2);
                            addElem = true;
                        }
                    }
                    otherMatrix.addAll(diffMatrix);
                    diffMatrix.clear();
                }
                if (otherMatrix.size() == 168) {
                    matrix1 = secondElem;
                    matrix2 = thirdElem;
                    System.out.println(matrix1.toString());
                    System.out.println(matrix2.toString());
                }
                System.out.println(otherMatrix.size() + "\n");
                addElem = true;

            }
        }

    }
}
