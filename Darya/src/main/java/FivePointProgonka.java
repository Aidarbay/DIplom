import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FivePointProgonka {
    public static double vectorNorm(double[] vector) {
        double norm = Math.abs(vector[0]);
        for (int i = 0; i < vector.length; i++) {
            norm = Math.max(norm, Math.abs(vector[i]));
        }
        return norm;
    }

    public static double exactU(double x, double b, double A) {
        // точное решение
        return A*x*x*(x-b)*(x-b)/24;
    }

    public static double rightF(double x, double b, double A) {
        // правая часть, если известно точное решение
        return A*(x*x*x*x-2*x*x*x+13*x*x-12*x+26)/24;

    }

    public static double pressQ(double x, double b, double q0, double q1) {
        // нагрузка на стержень, балку
        return q0+q1*Math.sin(Math.PI*x/b);

    }


    static int counter = 0;
    public static void main(String[] args) {
        test();
    }


    public static void test() {

        double EPSILON = 1.e-14;
        int N1 = 28;
        int N = N1+3;
        int pp = 3;
        int M1 = pp*N1;
        int M = pp*N;

        double[] yy = new double[M];
        double[] a = new double[N];
        double[] b = new double[N];
        double[] c = new double[N];
        double[] d = new double[N];
        double[] e = new double[N];
        double[] f = new double[N];
        double[] x = new double[N];
        double[] sol = new double[N];
        double[] schema = new double[N];
        double[] err = new double[N];
        double[] dU0 = new double[M];

        int ExactSolution  = 1;   // точное решение задано, то ExactSolution  = 1;
        int iprint = 1;
        if (N > 50)
            iprint = 0;
        int task = 0;  // выбор задачи


        // task == 0     с постоянной  правой частью A
        // task == 1     задача Task1
        // task == 2     задача Task2

        // длина стержня bb  = L
        double L = 3;
        double aa = 0., bb = L;
        double h = 0;
        double h1 = 0;
        switch (task) {
            case (0):
                h = (bb - aa) / (N1);
                h1 = (bb - aa) / (M1);
                for (int i = 0; i < N; i++)
                    x[i] = aa - h + i * h;
                for (int i = 0; i < M; i++)
                    yy[i] = aa - h + i * h1;
                break;
            case (1):
                // Task1 --------------------------------
                bb = 6.0;
                h = (bb - aa) / (N-1);
                for (int i = 0; i < N; i++)
                    x[i] = aa + i * h;
                // ---------------------------------------
                break;
            case (2):
                // Task2 --------------------------------
                bb = 6.0;
                h = (bb - aa) / (N-1);
                for (int i = 0; i < N; i++)
                    x[i] = aa + i * h;
                // ---------------------------------------
                break;

        }
        //


        if(iprint == 1) {
            for (int i = 0; i < N; i++) {
                int k = pp * i;
                System.out.println("x" + i + " = " + x[i] + "  y" + k + " = " + yy[k]);
            }
        }


        double bbb = 1;
        double p = 0;
        double A = 0;
        //double dd = 0;  // добавка к  диагональному элементу


        double h2 = h*h;
        double h3 = h2*h;
        double h4 = h2*h2;
        double hh2 = h1*h1;
        double hh3 = hh2*h1;
        double hh4 = hh2*hh2;
        //-------------------------------------------------------
        // уравнение y^4 + a1* y''' + a2*y'' + a3* y' + a4*y = f
        //-------------------------------------------------------
        double a1,a2,a3,a4;
        a1 = 0;
        a2 = 0;
        a3 = 0;
        a4 = 0;
        //--------------------------------------------------------
        double w;
        double mu = 0;
        double mu1 = 0;
        switch (task) {
            case  (0):
                //характеристики материала
                double q = 200;
                double E = 2.1e+10;
                double J = 5.2e-7;
                bbb = E*J;
                // правая часть константа А
                A = q / bbb;
                p = - A * h4;
                if(ExactSolution  == 1){
                    // точное решение
                    for (int i = 0; i < M; i++){
                        w = aa + (i-1) * h1;
                        dU0[i] = exactU(w,bb,A);
                    }
                    // правая часть строится по известному точному решению
                    for (int i = 2; i < N-2; i++){
                        int k = pp * i;
                        f[i] = -((dU0[k - 2] - 4 * dU0[k - 1] + 6 * dU0[k] - 4 * dU0[k + 1] + dU0[k + 2]) * hh4+
                                a1 * (-dU0[k - 2] + 2 * dU0[k - 1] - 2 * dU0[k + 1] + dU0[k + 2]) * h1 / 2 +
                                a2 * (dU0[k - 1] - 2 * dU0[k] + dU0[k + 1]) * hh2 +
                                a3 * (dU0[k + 1] - dU0[k - 1]) * hh3 / 2 +
                                a4 * dU0[k] * hh4);
                    }
                }
                else{
                    // правая часть ( точное решение неизвестно)
                    for (int i = 0; i < N; i++)
                        f[i] = p;
                }
                // краевые условия слева
                f[0] = 0;
                f[1] = 0;
                // краевые условия справа
                f[N - 2] = 0;
                f[N - 1] = 0;
                break;
            case (1):
                //Task1  ----------------------------------------------------
                // длина стержня L = 6
                double cc = 200000;
                E = 210e+9;
                J = 5010e-8;
                bbb = E*J;
                double MM = 50000;
                double WW = 371e-6;
                double q0 = -8000;
                double q1 = -3000;
                mu1 = -5 + cc*2*h3/bbb;
                // правая часть
                for (int i = 0; i < N-2; i++)
                    f[i] = pressQ(x[i], bb, q0, q1)*h4/bbb;

                // краевые условия слева
                w = f[2];
                f[0] = MM*h2/bbb+3.0/2*w;
                f[1] = 3.0*w;
                // краевые условия справа
                f[N - 2] = 0;
                f[N - 1] = 0;
                //  -----------------------------------------------------------
                break;
            case (2):
                //Task2  ----------------------------------------------------
                // длина стержня L = 6
                q = 20000;
                E = 210e+9;
                J = 2550e-8;
                bbb = E*J;
                MM = 16000;
                double FF = 24000;
                // k = k0 * C  - коэффициент постели
                double kk = 50e+6 * 0.11;
                //------------------------------------------
                // упругое основание bbb * y^4 + kk *y = q
                //                         y^4 + a4*y = A
                //------------------------------------------
                a4 = kk/bbb;
                mu = 6 + a4*h4;
                // правая часть

                A = q / bbb;
                p = - A * h4;
                for (int i = 0; i < N-2; i++)
                    f[i] = p;
                // краевые условия слева
                w = f[2];
                f[0] = -MM*h2/bbb+3.0/2*w-FF*h3/bbb;
                f[1] = -2*FF*h3/bbb+3.0*w;
                // краевые условия справа
                f[N - 2] = 0;
                f[N - 1] = 0;
                break;
        }


        newFile(f, "f");

        if(iprint == 1) {
            System.out.println("Вывод правой части");
            for (int i = 0; i < N; i++)
                System.out.println("i = " + (i - 1) + "   " + f[i]);
        }
        if(iprint == 1 && ExactSolution  == 1) {
            System.out.println("Вывод правой части(точное)");
            for (int i = 0; i < N; i++){
                w = aa + (i-1) * h;
                System.out.println("i = " + (i - 1) + "   " + rightF(w,bb,A)*h4);
            }

        }

        if(iprint == 1) {
            System.out.println("Точное решение");
            for (int i = 0; i < N ; i++) {
                w = aa + (i-1) * h;
                sol[i] = A * w * (w - bb) * w * (w - bb) / 24;
                System.out.println("i = " + i + "   " + sol[i]);
            }
        }
        if(iprint == 1) {
            System.out.println("Разностное точное решение");
            for (int i = 0; i < N; i++) {
                w = (i-1)*1.0/N1;
                schema[i] = A * bb*bb*bb*bb*(w*w - 2.0 * w * w * w + w * w * w * w) / 24;
                System.out.println("i = " + i + "   " + schema[i]);
            }
        }

        for (int i = 0; i < N-2; i++){
            err[i] = schema[i] - sol[i];
        }
        System.out.println("error1 = " + vectorNorm(err));
        err[N-2] = 0;
        err[N-1] = 0;


        switch (task) {
            case  (0):
                // краевые условия слева
                a[0] = 0;
                b[0] = 0;
                c[0] = -1;
                d[0] = 0;
                e[0] = 1;
                a[1] = 0;
                b[1] = 0;
                c[1] = 1;
                d[1] = 0;
                e[1] = 0;
                // краевые условия справа
                a[N - 2] = 0;
                b[N - 2] = 0;
                c[N - 2] = 1;
                d[N - 2] = 0;
                e[N - 2] = 0;
                a[N - 1] = -1;
                b[N - 1] = 0;
                c[N - 1] = 1;
                d[N - 1] = 0;
                e[N - 1] = 0;
                break;
            case (1):
                // краевые условия слева
                a[0] = 0;
                b[0] = 0;            //   - b[0]
                c[0] = (mu1+7)/2;
                d[0] = 2;           //   - d[0]
                e[0] = 1;
                a[1] = 0;
                b[1] = -(mu1+3);    //   - b[1]
                c[1] = 6;
                d[1] = 6;          //   - d[1]
                e[1] = 2;
                // краевые условия справа
                a[N - 2] = 0;
                b[N - 2] = -1;    //   - b[N-2]
                c[N - 2] = -4;
                d[N - 2] = -3;    //   - d[N-2]
                e[N - 2] = 0;
                a[N - 1] = 0;
                b[N - 1] = 0;
                c[N - 1] = 1;
                d[N - 1] = 0;
                e[N - 1] = 0;
                break;
            case (2):
                // краевые условия слева
                a[0] = 0;
                b[0] = 0;     //   - b[0]
                c[0] = 1;
                d[0] = 2;     //  - d[0]
                e[0] = -8+3*mu/2;
                a[1] = 0;
                b[1] = 2;    //   - b[1]
                c[1] = 6;
                d[1] = 24-3*mu;  //  - d[1]
                e[1] = 2;

                // краевые условия справа
                a[N - 2] = 0;
                b[N - 2] = -1;  //  - b[N-2]
                c[N - 2] = -4;
                d[N - 2] = -3;  //  - d[N-2]
                e[N - 2] = 0;
                a[N - 1] = 0;
                b[N - 1] = 0;
                c[N - 1] = 1;
                d[N - 1] = 0;
                e[N - 1] = 0;
                break;
        }

        double[] dy0 = {0,0,1,0,0};
        double[] dy1 = {0,-1,0,1,0};    //    1/(2h)
        double[] dy2 = {0,1,-2,1,0};    //    1/h^2
        double[] dy3 = {1,-2,0,2,-1};   //    1/(2h^3)
        double[] dy4 = {1,-4,6,-4,1};   //    1/h^4
        for (int i = 0; i < 5; i++) {
            dy0[i] *= h4;
            dy1[i] *= h3/2;
            dy2[i] *= h2;
            dy3[i] *= h/2;
        }

        // коэффициенты основной матрицы
        for (int i = 2; i < N - 2; i++) {
            a[i] = dy4[0]+a1*dy3[0]+a2*dy2[0]+a3*dy1[0]+a4*dy0[0];    // 1;
            b[i] = -(dy4[1]+a1*dy3[1]+a2*dy2[1]+a3*dy1[1]+a4*dy0[1]);    // 4;
            c[i] = dy4[2]+a1*dy3[2]+a2*dy2[2]+a3*dy1[2]+a4*dy0[2] ;  // +  dd;    // 6;
            d[i] = -(dy4[3]+a1*dy3[3]+a2*dy2[3]+a3*dy1[3]+a4*dy0[3]);    // 4;
            e[i] = dy4[4]+a1*dy3[4]+a2*dy2[4]+a3*dy1[4]+a4*dy0[4];    // 1;
        }

        // 5-точечная прогонка

        double[] xResult = Calculate5D(a, b, c, d, e, f, N);
        if(iprint == 1) {
            System.out.println("Вывод решения");
            for (int i = 0; i < N; i++) {
                if(task == 0)
                    System.out.println("i = " + (i - 1) + "   " + xResult[i]);
                else
                    System.out.println("i = " + i + "   " + 100*xResult[i]);
            }
        }
        for (int i = 0; i < N; i++)
            sol[i] = 100 * xResult[i];
        newFile(sol, "Sol");

        if(ExactSolution  == 1) {
            for (int i = 0; i < N - 2; i++) {
                err[i] = sol[i] - xResult[i + 1];
            }
            err[N - 2] = 0;
            err[N - 1] = 0;
            System.out.println("error2 = " + vectorNorm(err));
        }
        if(iprint == 1) {
            System.out.println("Угол поворота");
            for (int i = 1; i < N-1; i++) {
                sol[i] = (xResult[i + 1] - xResult[i - 1])/2/h;
                System.out.println("i = " + i + "   " + sol[i]);
            }
            sol[0] = (-3*xResult[0] +4*xResult[1] - xResult[2])/2/h;
            sol[N-1] = (-3*xResult[N-1] +4*xResult[N-2] - xResult[N-3])/2/h;
        }

        newFile(sol, "fi");
        if(iprint == 1) {
            System.out.println("Изгибающий момент М");
            for (int i = 1; i < N-1; i++) {
                sol[i] = bbb*(xResult[i + 1] - 2*xResult[i] + xResult[i - 1])/h2;
                System.out.println("i = " + i + "   " + sol[i]);
            }
            sol[0] = bbb*(2*xResult[0] - 5*xResult[1] + 4*xResult[2]- xResult[3])/h2;
            sol[N-1] = bbb*(2*xResult[N-1] - 5*xResult[N-2] + 4*xResult[N-3]- xResult[N-4])/h2;
        }
        for (int i = 0; i < N; i++)
            sol[i] = sol[i] / 1000;
        newFile(sol, "M");
        if(iprint == 1) {
            System.out.println("Поперечная или перерезывающая сила Q");
            sol[1] = bbb*(-5*xResult[1] +18*xResult[2] - 24*xResult[3] + 14*xResult[4] - 3*xResult[5])/2/h3;
            sol[0] = bbb*(-5*xResult[0] +18*xResult[1] - 24*xResult[2] + 14*xResult[3] - 3*xResult[4])/2/h3;
            System.out.println("i = " + 1 + "   " + sol[1]);
            for (int i = 2; i < N-2; i++) {
                sol[i] = bbb*(xResult[i + 2] - 2*xResult[i+1] + 2*xResult[i-1] - xResult[i - 2])/2/h3;
                System.out.println("i = " + i + "   " + sol[i]);
            }

            sol[N-2] = -bbb*(-5*xResult[N-2] +18*xResult[N-3] - 24*xResult[N-4] + 14*xResult[N-5] - 3*xResult[N-6])/2/h3;
            sol[N-1] = -bbb*(-5*xResult[N-1] +18*xResult[N-2] - 24*xResult[N-3] + 14*xResult[N-4] - 3*xResult[N-5])/2/h3;
            System.out.println("i = " + (N-2) + "   " + sol[N-2]);
        }
        for (int i = 0; i < N; i++)
            sol[i] = sol[i] / 1000;
        newFile(sol, "Q");

        // Метод Гаусса-Зейделя
        double[] first =CalculateGaussZeidel(a,b,c,d,e,f,N,EPSILON);
        newFile(first, "SolG-Z");
        System.out.println("Число итераций = " + counter);


        double ArrayGZ[][] = new double[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                ArrayGZ[i][j] = 0;

        switch (task) {
            case (0):
                //первая строка
                ArrayGZ[0][0] = -1;
                ArrayGZ[0][2] = 1;
                //вторая строка
                ArrayGZ[1][1] = 1;
                //предпоследняя строка
                ArrayGZ[N - 2][N - 2] = 1;
                //последняя строка
                ArrayGZ[N - 1][N - 1] = 1;
                ArrayGZ[N - 1][N - 3] = -1;

                for (int i = 2; i < N - 2; i++) {
                    ArrayGZ[i][i] = 6;
                    ArrayGZ[i][i - 1] = -4;
                    ArrayGZ[i][i + 1] = -4;
                    ArrayGZ[i][i - 2] = 1;
                    ArrayGZ[i][i + 2] = 1;
                }
                break;
            case (1):
                //первая строка
                ArrayGZ[0][0] =(7 + mu1)/2;
                ArrayGZ[0][1] = -2;
                ArrayGZ[0][2] = 1;
                //вторая строка
                ArrayGZ[1][0] = 3 + mu1;
                ArrayGZ[1][1] = 6;
                ArrayGZ[1][2] = -6;
                ArrayGZ[1][3] = 2;

                for (int i = 2; i < N-2; i++)
                {
                    ArrayGZ[i][i-2] = 1; ArrayGZ[i][i-1] = -4; ArrayGZ[i][i] = 6; ArrayGZ[i][i+1] = -4; ArrayGZ[i][i+2] = 1;
                }
                //предпоследняя строка
                ArrayGZ[N-2][N-3] = 1;   ArrayGZ[N-2][N-2] = -4; ArrayGZ[N-2][N-1] = 3;
                //последняя строка
                ArrayGZ[N-1][N-1] = 1;
                break;
            case (2):
                //первая строка
                ArrayGZ[0][0] = 2;//1;
                ArrayGZ[0][1] = -5; // -2;
                ArrayGZ[0][2] = 4;  // 3*mu/2-8;
                ArrayGZ[0][3] = -1;
                ArrayGZ[0][0] = 1;
                ArrayGZ[0][1] = -2;
                ArrayGZ[0][2] =  3*mu/2-8;
                ArrayGZ[0][3] =  0;
                //вторая строка
                ArrayGZ[1][0] = -5; //-2;
                ArrayGZ[1][1] = 18; //6;
                ArrayGZ[1][2] = -24; //3*mu-24;
                ArrayGZ[1][3] = 14; //2;
                ArrayGZ[1][4] = -3;
                ArrayGZ[1][0] = -2;
                ArrayGZ[1][1] = 6;
                ArrayGZ[1][2] = 3*mu-24;
                ArrayGZ[1][3] = 2;
                ArrayGZ[1][4] = 0;
                for (int i = 2; i < N-2; i++)
                {
                    ArrayGZ[i][i-2] = 1; ArrayGZ[i][i-1] = -4; ArrayGZ[i][i] = mu; ArrayGZ[i][i+1] = -4; ArrayGZ[i][i+2] = 1;
                }
                //предпоследняя строка
                ArrayGZ[N-2][N-3] = 1;   ArrayGZ[N-2][N-2] = -4; ArrayGZ[N-2][N-1] = 3;
                //последняя строка
                ArrayGZ[N-1][N-1] = 1;

                break;
        }



        // Метод Гаусса-Зейделя 2
        first = CalculateGaussZeidel2(ArrayGZ, f, N, EPSILON);
        newFile(first, "SolG-Z-2");
        System.out.println("Число итераций = " + counter);

        if(iprint == 1) {
            for (int i = 0; i < N; i++) {
                System.out.println("i = " + (i - 1) + "   " + first[i]);
            }
        }
    }

    public static void newFile(double[] aray, String filename)  {
        File myFile =  new File(filename+".txt");
        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter(myFile));
            for (int i=0;i<aray.length;i++){

                writer.write(String.valueOf(aray[i])+"\n");
            }


            writer.flush();
            writer.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }





//=========================================Calculate for 5D Matrix==================================================


    public static double[] Calculate5D(double[] a, double[] b, double[] c, double[] d,
                                       double[] e, double[] f, int N) {

        double[] alfa = new double[N + 1];
        double[] beta = new double[N + 1];
        double[] gamma = new double[N + 1];
        double[] delta = new double[N + 1];
        double[] x = new double[N];
        for (int i = 0; i < N; i++) {
            x[i] = 0;
        }
        // c0*y0 - d0*y1 + e0*y2 = f0;
        // -b1*y0 + c1*y1 - d1*y2 + e1*y3 = f1;
        // ai*yi-2 - bi*yi-1 + ci*yi - di*yi+1 + ei*yi+2 = fi;
        // an-1*yn-3 - bn-1*yn-2 + cn-1*yn-1 - dn-1*yn = fn-1;
        // an*yn-2 - bn*yn-1 + cn*yn = fn;

        //============== i = 1  ============
        alfa[1] = d[0] / c[0];
        beta[1] = e[0] / c[0];
        gamma[1] = f[0] / c[0];
        delta[1] = c[1] - b[1] * alfa[1];

        //============== i = 2  ============

        alfa[2] = (d[1] - b[1] * beta[1]) / delta[1];
        beta[2] = e[1] / delta[1];
        gamma[2] = (f[1] + b[1] * gamma[1]) / delta[1];

        //============================================ Direct Way ===========================================================
        int n = N - 1;
        for (int i = 2; i < N - 1; i++) {   //  i < _str

            delta[i] = c[i] - a[i] * beta[i - 1] + alfa[i] * (a[i] * alfa[i - 1] - b[i]);
            if (i < (N - 2)) {
                beta[i + 1] = e[i] / delta[i];
            }
            alfa[i + 1] = (d[i] + beta[i] * (a[i] * alfa[i - 1] - b[i])) / delta[i];
            gamma[i + 1] = (f[i] - a[i] * gamma[i - 1] - gamma[i] * (a[i] * alfa[i - 1] - b[i])) / delta[i];
        }
        delta[n] = c[n] - a[n] * beta[n - 1] + alfa[n] * (a[n] * alfa[n - 1] - b[n]);
        gamma[n + 1] = (f[n] - a[n] * gamma[n - 1] - gamma[n] * (a[n] * alfa[n - 1] - b[n])) / delta[n];

        //=========================================== Reverse Way =============================================================

        x[n] = gamma[n + 1];
        x[n - 1] = alfa[n] * x[n] + gamma[n];

        for (int i = n - 2; i >= 0; i--) {
            x[i] = alfa[i + 1] * x[i + 1] - beta[i + 1] * x[i + 2] + gamma[i + 1];
        }
        return x;
    }
    //    public static double vectorNorm(double[] vector) {
//        double norm = Math.abs(vector[0]);
//        for (int i = 0; i < vector.length; i++) {
//            norm = Math.max(norm, Math.abs(vector[i]));
//        }
//        return norm;
//    }
///метод Гаусса-Зейделя
    public static double[]  CalculateGaussZeidel(double[] a, double[] b, double[] c, double[] d,
                                                 double[] e, double[] f, int N, double epsilon) {
        //counter = 0;
        boolean check = true;
        double ArrayGZ[][] = new double[N][N];
        ArrayGZ[0][0] = -1;
        ArrayGZ[0][2] = 1;
        ArrayGZ[1][1] = 1;
        ArrayGZ[N - 1][N - 1] = 1;
        ArrayGZ[N - 1][N - 3] = -1;
        ArrayGZ[N - 2][N - 2] = 1;
        for (int i = 2; i < N - 2; i++) {
            ArrayGZ[i][i] = 6;
            ArrayGZ[i][i - 1] = -4;
            ArrayGZ[i][i + 1] = -4;
            ArrayGZ[i][i - 2] = 1;
            ArrayGZ[i][i + 2] = 1;
        }
        double right[];
        right = f;
        double first[] = new double[N];
        double second[] = new double[N];
        double temp[] = new double[N];

        double qq = 0;
        check = true;
        //counter = 0;
        // ****************************
        while (true) {
            counter++;
            for (int i = 0; i < N; i++) {
                qq = right[i];
                for (int j = 0; j < N; j++) {
                    if (j != i)
                        qq -= ArrayGZ[i][j] * first[j];
                }
                first[i] = 1 / ArrayGZ[i][i] * qq;
            }
            for (int i = 0; i < N; i++) {
                temp[i] = second[i] - first[i];
            }
            //System.out.println(x1[0]);
            if (vectorNorm(temp) < epsilon) {
                break;
            }
            for (int i = 0; i < N; i++) {
                second[i] = first[i];
            }

        }
        /*System.out.println("Вывод решения после " + counter + " итераций");
        for (int i = 0; i < N; i++) {
            System.out.println("i = " + (i - 1) + "   " + first[i]);
        }*/

        return first;

    }

    //метод Гаусса-Зейделя 2
    public static double[]  CalculateGaussZeidel2(double[][] a, double[] f, int N, double epsilon) {
        //counter = 0;
        boolean check = true;
        double first[] = new double[N];
        double second[] = new double[N];
        double temp[] = new double[N];

        double qq = 0;
        check = true;
        //counter = 0;
        // ****************************
        while (true) {
            counter++;
            for (int i = 0; i < N; i++) {
                qq = f[i];
                for (int j = 0; j < N; j++) {
                    if (j != i)
                        qq -= a[i][j] * first[j];
                }
                first[i] = 1 / a[i][i] * qq;
            }
            for (int i = 0; i < N; i++) {
                temp[i] = second[i] - first[i];
            }
            //System.out.println(x1[0]);
            if (vectorNorm(temp) < epsilon) {
                break;
            }
            for (int i = 0; i < N; i++) {
                second[i] = first[i];
            }

        }
        /*System.out.println("Вывод решения после " + counter + " итераций");
        for (int i = 0; i < N; i++) {
            System.out.println("i = " + (i - 1) + "   " + first[i]);
        }*/

        return first;

    }










    private static void cl(double[][] ArrayGZ, double[] right, double[] first,
                           int N, int counter,boolean check,double epsilon){
        double qq=0;
        check= true;
        counter = 0;
        double second[]= new double[N];
        double temp[]= new double[N];
        // ****************************
        while (true) {
            counter++;
            for (int i = 0; i < N; i++) {
                qq = right[i];
                for (int j = 0; j < N; j++) {
                    if (j != i)
                        qq -= ArrayGZ[i][j] * first[j];
                }
                first[i] = 1 / ArrayGZ[i][i] * qq;
            }
            for (int i = 0; i < N; i++) {
                temp[i] = second[i] - first[i];
            }
            //System.out.println(x1[0]);
            if (vectorNorm(temp) < epsilon) {
                break;
            }

        }



        //**********************

        //        for (int i = 0; i<N; i++){
//            qq= 0;
//            for (int j=0 ;j<N;j++ ){
//                if(j==i){}
//                else{
//                    qq-=ArrayGZ[i][j]*first[j];
//                }
//            }
//            qq= right[i]+qq;
//            seccond[i]= 1/ArrayGZ[i][i]*qq;
//        }




//        for (int i=0;i>first.length;i++){
//            if(Math.abs(first[i]-seccond[i])> 1e-6){
//            }
//            else { check=false;
//                break;}
//        }
//        for (int i = 0 ; i < first.length;i++) {
//
//            first[i]=seccond[i];
//        }

    }
}
