package OmGU.IMIT;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        int size = 0;
        Matrix matrix1;
        Matrix matrixH;
        Random rand = new Random();
        List<Matrix> group = new ArrayList<>();

        List<Matrix> conjugatedMatrix = new ArrayList<Matrix>();
        List<Matrix> otherMatrix = new ArrayList<>();

        while (group.size() != 168) {
            int[][] matrix2 = {{rand.nextInt(2), rand.nextInt(2), rand.nextInt(2)},
                    {rand.nextInt(2), rand.nextInt(2), rand.nextInt(2)},
                    {rand.nextInt(2), rand.nextInt(2), rand.nextInt(2)}};
            Matrix other = new Matrix(matrix2, 3);
            if (other.determinant() == 1 && !group.contains(other)) {
                group.add(other);
                otherMatrix.add(other);
            }
        }
        matrixH = otherMatrix.get(0);
        System.out.println(matrixH.toString() + "\n____________\n");
        while (size != 168) {
            matrix1 = otherMatrix.get(0);
            System.out.println(matrix1.toString());

            conjugatedMatrix.clear();

            /*for (Matrix h :
                    group) {
                Matrix newMatrix = Matrix.multiply(Matrix.multiply(h, matrix1), h.inversion());
                if (!conjugatedMatrix.contains(newMatrix)) {
                    conjugatedMatrix.add(newMatrix);
                    System.out.println(newMatrix.toString());
                }
            }*/

            for (Matrix h :
                    group) {
                Matrix newMatrix = Matrix.multiply(Matrix.multiply(Matrix.multiply(Matrix.multiply(matrixH.inversion(), h),
                        matrixH), matrix1), h.inversion());
                if (!conjugatedMatrix.contains(newMatrix)) {
                    conjugatedMatrix.add(newMatrix);
                    System.out.println(newMatrix.getOrder());
                }
            }

            size += conjugatedMatrix.size();
            System.out.println(conjugatedMatrix.size() + "\n");

            for (Matrix h :
                    conjugatedMatrix) {
                otherMatrix.remove(h);
            }
        }
    }
}
