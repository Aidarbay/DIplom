package OmGU.IMIT;

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

        Map<Pair, Map<Matrix, String>> generatorsAndPaths = new HashMap<>();
        List<Matrix> secondOrderMatrix = new ArrayList<>();
        List<Matrix> thirdOrderMatrix = new ArrayList<>();
        List<Pair> pairGenerators = new ArrayList<>();
        List<List<Matrix>> conjugatedMatrix = new ArrayList<>();
        List<Matrix> otherMatrices = new ArrayList<Matrix>();

        //нахожу группу невырожденных матриц 3*3 над полем (0, 1)
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
                Map<Matrix, String> pathsForAllMatrices = new HashMap<>();
                pathsForAllMatrices.put(secondElem, "2");
                pathsForAllMatrices.put(thirdElem, "3");
                Map<Matrix, String> copyOfPaths = new HashMap<>(pathsForAllMatrices);
                while (pathsForAllMatrices.size() < 168 && addElem) {
                    addElem = false;
                    for (Map.Entry<Matrix, String> mainElem :
                            pathsForAllMatrices.entrySet()) {
                        matrix1 = Matrix.multiply(mainElem.getKey(), secondElem);
                        matrix2 = Matrix.multiply(mainElem.getKey(), thirdElem);
                        if (!copyOfPaths.containsKey(matrix1)) {
                            copyOfPaths.put(matrix1, mainElem.getValue() + "2");
                            addElem = true;
                        }
                        if (!copyOfPaths.containsKey(matrix2)) {
                            copyOfPaths.put(matrix2, mainElem.getValue() + "3");
                            addElem = true;
                        }
                    }
                    pathsForAllMatrices.putAll(copyOfPaths);
                }
                if (pathsForAllMatrices.size() == 168) {
                    generatorsAndPaths.put(new Pair(secondElem, thirdElem), pathsForAllMatrices);
                    pairGenerators.add(new Pair(secondElem, thirdElem));
                }
                addElem = true;
            }
        }

        //нахождение классов скрученной сопряженности
        Pair generate = pairGenerators.get(0);
        System.out.println(generate.getFirst().toString() + generate.getSecond().toString());
        for (Pair replace :
                pairGenerators) {
            if (!generate.equals(replace)) {
                int size = 0;
                otherMatrices.addAll(group);
                conjugatedMatrix.clear();
                System.out.println(replace.getFirst().toString() + replace.getSecond().toString());
                while (otherMatrices.size() != 0) {
                    matrix1 = otherMatrices.get(0);
                    conjugatedMatrix.add(size, new ArrayList<Matrix>());

                    for (Matrix h :
                            group) {

                        //производим преобразование согласно полученному пути
                        String path = generatorsAndPaths.get(replace).get(h);
                        Matrix conversion = unitMatrix;
                        for (int i = 0; i < path.length(); i++) {
                            if (path.charAt(i) == '2') {
                                conversion = Matrix.multiply(conversion, generate.getFirst());
                            } else {
                                conversion = Matrix.multiply(conversion, generate.getSecond());
                            }
                        }

                        //ищем сопряженные элементы
                        Matrix newMatrix = Matrix.multiply(Matrix.multiply(conversion, matrix1), h.inversion());
                        if (!conjugatedMatrix.get(size).contains(newMatrix)) {
                            conjugatedMatrix.get(size).add(newMatrix);
                        }
                    }

                    for (List<Matrix> h :
                            conjugatedMatrix) {
                        otherMatrices.removeAll(h);
                    }
                    size++;
                }

                for (List<Matrix> h :
                        conjugatedMatrix) {
                    System.out.println("Размер = " + h.size());
                }
            }
        }


    }
}