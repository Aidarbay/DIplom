package OmGU.IMIT;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        int size = 0;
        int[][] matrix = {{1, 1, 0}, {1, 1, 1}, {0, 1, 0}};
        Matrix matrix1 = new Matrix(matrix, 3);
        Random rand = new Random();
        List<Matrix> group = new ArrayList<>();

        List<Matrix> firstConjugate = new ArrayList<Matrix>();
        List<Matrix> secondConjugate = new ArrayList<>();
        List<List<Matrix>> conjugate = new ArrayList<>();

        while (group.size() != 168) {
            int[][] matrix2 = {{rand.nextInt(2), rand.nextInt(2), rand.nextInt(2)},
                    {rand.nextInt(2), rand.nextInt(2), rand.nextInt(2)},
                    {rand.nextInt(2), rand.nextInt(2), rand.nextInt(2)}};
            Matrix other = new Matrix(matrix2, 3);
            if (other.determinant() == 1 && !group.contains(other)) {
                group.add(other);
                //System.out.println(other.toString());
            }
        }

        while (size < 168) {
            firstConjugate.clear();
            for (Matrix h :
                    group) {
                Matrix newMatrix = Matrix.multiply(Matrix.multiply(h.transpose().inversion(), matrix1), h.inversion());
                if (!firstConjugate.contains(newMatrix)) {
                    firstConjugate.add(newMatrix);
                }
            }
            size += firstConjugate.size();
            System.out.println(firstConjugate.size());
            conjugate.add(firstConjugate);

            for (Matrix h :
                    group) {
                if (!firstConjugate.contains(h)) {
                }
            }
            System.out.println(secondConjugate.size());
        }

    }
}
