package OmGU.IMIT;

import java.util.*;

import static OmGU.IMIT.Matrix.multiply;

public class Second {
    public static void main(String[] args) {
        List<Matrix> group = getGroup();

        Map<Pair<Matrix, Matrix>, Map<Matrix, String>>
                generatorsAndPaths = new HashMap<>();
        List<Pair<Matrix, Matrix>> pairGenerators = new ArrayList<>();
        findGenerators(group, generatorsAndPaths, pairGenerators);
        Map<Pair<Pair<Matrix, Matrix>, Pair<Matrix, Matrix>>, List<List<Matrix>>> conjugatedMatrices =
                findClassesConjugatedMatrices(group, pairGenerators, generatorsAndPaths);


        for (Map.Entry<Pair<Pair<Matrix, Matrix>, Pair<Matrix, Matrix>>, List<List<Matrix>>> elem :
                conjugatedMatrices.entrySet()) {

            System.out.println(elem.getKey().getFirst().getFirst().toString());
            System.out.println(elem.getKey().getFirst().getSecond().toString());
            System.out.println("_______________________________________________");
            System.out.println(elem.getKey().getSecond().getFirst().toString());
            System.out.println(elem.getKey().getSecond().getSecond().toString());
            for (List<Matrix> set :
                    elem.getValue()) {
                System.out.println("Размер = " + set.size());
            }
        }

    }

    //нахожу группу невырожденных матриц 3*3 над полем (0, 1)
    private static List<Matrix> getGroup() {
        Random rand = new Random();
        List<Matrix> group = new ArrayList<>();

        while (group.size() != 168) {
            int[][] matrix = {{rand.nextInt(2), rand.nextInt(2), rand.nextInt(2)},
                    {rand.nextInt(2), rand.nextInt(2), rand.nextInt(2)},
                    {rand.nextInt(2), rand.nextInt(2), rand.nextInt(2)}};
            Matrix other = new Matrix(matrix);
            if (other.determinant() == 1 && !group.contains(other)) {
                group.add(other);
            }
        }
        return group;
    }

    private static void findGenerators(
            List<Matrix> group, Map<Pair<Matrix, Matrix>, Map<Matrix, String>> generatorsAndPaths,
            List<Pair<Matrix, Matrix>> pairGenerators
    ) {
        List<Matrix> secondOrderMatrix = new ArrayList<>();
        List<Matrix> thirdOrderMatrix = new ArrayList<>();
        boolean addElem = true;

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
                        Matrix matrix1 = multiply(mainElem.getKey(), secondElem);
                        Matrix matrix2 = multiply(mainElem.getKey(), thirdElem);
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
                    generatorsAndPaths.put(new Pair<>(secondElem, thirdElem), pathsForAllMatrices);
                    pairGenerators.add(new Pair<>(secondElem, thirdElem));
                }
                addElem = true;
            }
        }
    }

    //нахождение классов скрученной сопряженности
    private static Map<Pair<Pair<Matrix, Matrix>, Pair<Matrix, Matrix>>, List<List<Matrix>>> findClassesConjugatedMatrices(
            List<Matrix> group, List<Pair<Matrix, Matrix>> pairGenerators, Map<Pair<Matrix, Matrix>, Map<Matrix, String>>
            generatorsAndPaths
    ) {
        Map<Pair<Pair<Matrix, Matrix>, Pair<Matrix, Matrix>>, List<List<Matrix>>> conjugatedMatrices = new HashMap<>();
        int[][] unitArray = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
        Matrix unitMatrix = new Matrix(unitArray);
        List<Matrix> otherMatrices = new ArrayList<>();

        Pair<Matrix, Matrix> replace = pairGenerators.get(0);
        for (Pair<Matrix, Matrix> generate :
                pairGenerators) {
            if (!generate.equals(replace)) {
                int size = 0;
                otherMatrices.addAll(group);
                List<List<Matrix>> conjugatedMatrix = new ArrayList<>();

                while (otherMatrices.size() != 0) {
                    Matrix matrix1 = otherMatrices.get(0);
                    conjugatedMatrix.add(size, new ArrayList<Matrix>());

                    for (Matrix h :
                            group) {

                        //производим преобразование согласно полученному пути
                        String path = generatorsAndPaths.get(replace).get(h);
                        Matrix conversion = unitMatrix;
                        for (int i = 0; i < path.length(); i++) {
                            if (path.charAt(i) == '2') {
                                conversion = multiply(conversion, generate.getFirst());
                            } else {
                                conversion = multiply(conversion, generate.getSecond());
                            }
                        }

                        //ищем сопряженные элементы
                        Matrix newMatrix = multiply(multiply(conversion, matrix1), h.inversion());
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

                conjugatedMatrices.put(new Pair<>(generate, replace), conjugatedMatrix);


            }
        }
        return conjugatedMatrices;
    }
}