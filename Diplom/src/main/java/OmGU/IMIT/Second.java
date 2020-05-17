package OmGU.IMIT;

import java.util.*;

public class Second {
    public static void main(String[] args) {
        int size = 0;
        int[][] unitArray = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
        Matrix matrix1;
        Matrix matrix2;
        Matrix unitMatrix = new Matrix(unitArray, 3);
        Random rand = new Random();
        List<Matrix> group = new ArrayList<>();
        boolean addElem = true;

        Map<Pair, Map<Matrix, String>> generatingMatrix = new HashMap<>();
        List<Matrix> secondOrderMatrix = new ArrayList<>();
        List<Matrix> thirdOrderMatrix = new ArrayList<>();
        List<Pair> pairGeneratingMatrix = new ArrayList<>();
        List<Matrix> conjugatedMatrix = new ArrayList<Matrix>();
        List<Matrix> otherMatr = new ArrayList<Matrix>();

        //полностью заполняю группу 168 элементами
        while (group.size() != 168) {
            int[][] matrix = {{rand.nextInt(2), rand.nextInt(2), rand.nextInt(2)},
                    {rand.nextInt(2), rand.nextInt(2), rand.nextInt(2)},
                    {rand.nextInt(2), rand.nextInt(2), rand.nextInt(2)}};
            Matrix other = new Matrix(matrix, 3);
            if (other.determinant() == 1 && !group.contains(other)) {
                group.add(other);
            }
        }
        //заполняю два множества элементов с порядками 2 и 3
        for (Matrix elem :
                group) {
            if (elem.getOrder() == 2) {
                secondOrderMatrix.add(elem);
            } else if (elem.getOrder() == 3) {
                thirdOrderMatrix.add(elem);
            }
        }
        //нахождение пар порождающих элементов и их комбинации для получения всех элементов группы
        for (Matrix secondElem :
                secondOrderMatrix) {
            for (Matrix thirdElem :
                    thirdOrderMatrix) {
                Map<Matrix, String> newMatrix = new HashMap<>();
                newMatrix.put(secondElem, "2");
                newMatrix.put(thirdElem, "3");
                newMatrix.put(Matrix.multiply(secondElem, secondElem), "22");
                if (!newMatrix.containsKey(Matrix.multiply(thirdElem, thirdElem))) {
                    newMatrix.put(Matrix.multiply(thirdElem, thirdElem), "33");
                }
                Map<Matrix, String> diffMatrix = new HashMap<>(newMatrix);
                while (newMatrix.size() < 168 && addElem) {
                    addElem = false;
                    for (Map.Entry<Matrix, String> mainElem :
                            newMatrix.entrySet()) {
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
                    newMatrix.putAll(diffMatrix);
                }
                if (newMatrix.size() == 168) {
                    generatingMatrix.put(new Pair(secondElem, thirdElem), newMatrix);
                    pairGeneratingMatrix.add(new Pair(secondElem, thirdElem));
                }
                addElem = true;
            }
        }

        //нахождение классов скрученной сопряженности
        Pair generate = pairGeneratingMatrix.get(0);
        for (Pair replace :
                pairGeneratingMatrix) {
            if (!generate.equals(replace)) {
                size = 0;
                otherMatr.addAll(group);
                System.out.println(generate.getFirst().toString() + generate.getSecond().toString());
                System.out.println(replace.getFirst().toString() + replace.getSecond().toString());
                while (size != 168) {
                    matrix1 = otherMatr.get(0);

                    conjugatedMatrix.clear();

                    for (Matrix h :
                            group) {
                        String path = generatingMatrix.get(replace).get(h);

                        Matrix conversion = unitMatrix;
                        for (int i = 0; i < path.length(); i++) {
                            if (path.charAt(i) == '2') {
                                conversion = Matrix.multiply(conversion, generate.getFirst());
                            } else {
                                conversion = Matrix.multiply(conversion, generate.getSecond());
                            }
                        }

                        Matrix newMatrix = Matrix.multiply(Matrix.multiply(conversion, matrix1), h.inversion());
                        if (!conjugatedMatrix.contains(newMatrix)) {
                            conjugatedMatrix.add(newMatrix);
                            System.out.println(newMatrix.getOrder());
                        }
                    }

                    size += conjugatedMatrix.size();
                    System.out.println(conjugatedMatrix.size() + "\n");

                    for (Matrix h :
                            conjugatedMatrix) {
                        otherMatr.remove(h);
                    }
                }
            }
        }


    }
}