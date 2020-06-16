package OmGU.IMIT;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Main {
    public static void main(String[] args) {
        //int size = 0;
        //Matrix matrix1;
        int[][] matrix = {{2, 2, 1, 0}, {1, 1, 1, 1}, {0, 1, 1, 1}, {1, 1, 1, 1}};
        Matrix matrixH = new Matrix(matrix);
        System.out.println(matrixH.toString() + "\n____________\n");
        System.out.println(matrixH.getDeterminant());
        /*Random rand = new Random();
        List<Matrix> group = new ArrayList<>();
        List<Matrix> otherMatrix = new ArrayList<>();

        List<Matrix> conjugatedMatrix = new ArrayList<Matrix>();

        while (group.size() != 168) {
            int[][] matrix2 = {{rand.nextInt(2), rand.nextInt(2), rand.nextInt(2)},
                    {rand.nextInt(2), rand.nextInt(2), rand.nextInt(2)},
                    {rand.nextInt(2), rand.nextInt(2), rand.nextInt(2)}};
            Matrix other = new Matrix(matrix2);
            if (other.getDeterminant() == 1 && !group.contains(other)) {
                group.add(other);
            }
        }
        System.out.println(matrixH.toString() + "\n____________\n");
        while (size != 168) {
            otherMatrix.addAll(group);
            matrix1 = otherMatrix.get(0);
            System.out.println(matrix1.toString());

            conjugatedMatrix.clear();

            for (Matrix h :
                    group) {
                Matrix newMatrix = Matrix.multiply(Matrix.multiply(Matrix.multiply(Matrix.multiply(matrixH, h),
                        matrixH.inversion()), matrix1), h.inversion());
                if (!conjugatedMatrix.contains(newMatrix)) {
                    conjugatedMatrix.add(newMatrix);
                    System.out.println("порядок обычной матрицы: " + newMatrix.getOrder());
                    System.out.println("порядок умноженной матрицы: " + Matrix.multiply(newMatrix, matrixH.inversion()).getOrder());
                }
            }

            size += conjugatedMatrix.size();
            System.out.println(conjugatedMatrix.size() + "\n");

            for (Matrix h :
                    conjugatedMatrix) {
                otherMatrix.remove(h);
            }
        }*/
    }
}
