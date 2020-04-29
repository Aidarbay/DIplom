package OmGU.IMIT;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        int size = 0;
        Matrix matrix1;
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
        /*while (size != 168) {
            matrix1 = otherMatrix.get(0);
            System.out.println(matrix1.toString());

            conjugatedMatrix.clear();

            for (Matrix h :
                    group) {
                for (Matrix g :
                        group) {

                    Matrix newMatrix = Matrix.multiply(Matrix.multiply(Matrix.multiply(Matrix.multiply
                            (g.inversion(), h), g), matrix1), h.inversion());
                    if (!conjugatedMatrix.contains(newMatrix)) {
                        conjugatedMatrix.add(newMatrix);
                    }
                }
            }

            size += conjugatedMatrix.size();
            System.out.println(conjugatedMatrix.size() + "\n");

            for (Matrix h :
                    conjugatedMatrix) {
                otherMatrix.remove(h);
            }
        }*/

        boolean flag = false;

        while(!flag) {

            conjugatedMatrix.clear();

            for (Matrix h :
                    group) {
                for (Matrix g :
                        group) {

                }
            }
        }
    }
}
