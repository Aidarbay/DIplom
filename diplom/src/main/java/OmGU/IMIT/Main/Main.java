package OmGU.IMIT.Main;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        int[][] matrix = {{1, 0, 1}, {0, 1, 0}, {0, 1, 1}};
        Matrix matrix1 = new Matrix(matrix, 3);
        System.out.println(matrix1.toString());
        Random rand = new Random();
        List<Matrix> group = new ArrayList<Matrix>();
        List<Matrix> sopryaj = new ArrayList<Matrix>();
        boolean flag = false;
        while (!flag) {
            int[][] matrix2 = {{rand.nextInt(2), rand.nextInt(2), rand.nextInt(2)},
                    {rand.nextInt(2), rand.nextInt(2), rand.nextInt(2)},
                    {rand.nextInt(2), rand.nextInt(2), rand.nextInt(2)}};
            Matrix other = new Matrix(matrix2, 3);
            if (other.determinant() == 1 && !group.contains(other)) {
                group.add(other);
                //System.out.println(other.toString());
            }
            if (group.size() == 168) {
                flag = true;
            }
        }
        for (Matrix h :
                group) {
            Matrix newMatrix = Matrix.umnojit(Matrix.umnojit(h,matrix1), h);
            if(!sopryaj.contains(newMatrix)) {
                   sopryaj.add(newMatrix);
                   //System.out.println(h.toString());
            }
        }
        System.out.println(sopryaj.size());

        for (Matrix h :
                group) {
            if(!sopryaj.contains(h)) {
                System.out.println(h.toString());
            }
        }
    }
}
