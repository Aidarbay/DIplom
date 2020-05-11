package OmGU.IMIT;

import javax.sound.sampled.LineUnavailableException;
import java.util.*;

public class Second {
    public static void main(String[] args) {
        int[][] unitArray = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
        Matrix matrix1;
        Matrix matrix2;
        Matrix unitMatrix = new Matrix(unitArray, 3);
        Random rand = new Random();
        List<Matrix> group = new ArrayList<>();
        boolean addElem = true;

        Map<Pair<Matrix, Matrix>, Map<Matrix, String>> generatingMatrix = new HashMap<>();
        List<Matrix> secondOrderMatrix = new ArrayList<>();
        List<Matrix> thirdOrderMatrix = new ArrayList<>();
        Map<Matrix, String> otherMatrix = new HashMap<>();
        Map<Matrix, String> diffMatrix = new HashMap<>();

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
            if (unitMatrix.equals(Matrix.multiply(elem, elem)) && !unitMatrix.equals(elem)) {
                secondOrderMatrix.add(elem);
            } else if (unitMatrix.equals(Matrix.multiply(Matrix.multiply(elem, elem), elem)) && !unitMatrix.equals(elem)) {
                thirdOrderMatrix.add(elem);
            }
        }
        for (Matrix secondElem :
                secondOrderMatrix) {
            for (Matrix thirdElem :
                    thirdOrderMatrix) {
                otherMatrix.clear();
                otherMatrix.put(secondElem, "2");
                otherMatrix.put(thirdElem, "3");
                otherMatrix.put(Matrix.multiply(secondElem, secondElem), "22");
                if (!otherMatrix.containsKey(Matrix.multiply(thirdElem, thirdElem))) {
                    otherMatrix.put(Matrix.multiply(thirdElem, thirdElem), "33");
                }
                diffMatrix.putAll(otherMatrix);
                while (otherMatrix.size() < 168 && addElem) {
                    addElem = false;
                    for (Map.Entry<Matrix, String> mainElem :
                            otherMatrix.entrySet()) {
                        matrix1 = Matrix.multiply(mainElem.getKey(), secondElem);
                        matrix2 = Matrix.multiply(mainElem.getKey(), thirdElem);
                        if (!diffMatrix.containsKey(matrix1)) {
                            diffMatrix.put(matrix1, mainElem.getValue() + "2");
                            addElem = true;
                        }
                        if (!diffMatrix.containsKey(matrix2)) {
                            diffMatrix.put(matrix2, mainElem.getValue() + "3");
                            addElem = true;
                        }
                    }
                    otherMatrix.putAll(diffMatrix);
                }
                if (otherMatrix.size() == 168) {
                    generatingMatrix.put(new Pair<Matrix, Matrix>(secondElem, thirdElem), otherMatrix);
                    System.out.println(secondElem.toString());
                    System.out.println(thirdElem.toString());
                }
                addElem = true;

            }
        }

        System.out.println(generatingMatrix.size() + "\n");

    }
}
