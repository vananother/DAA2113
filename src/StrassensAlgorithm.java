
/**
 * Class ini mengatur perkalian 2 buah matriks dengan menggunakan algoritma Strassen.
 * @author Vann
 */
class StrassensAlgorithm {

    /**
     *
     * @param g Matriks G
     * @param u Matriks U
     * @return Hasil kali matriks G dengan matriks U menggunakan algoritma Strassen
     */
    double[][] strassenMatrixMulti(double[][] g, double[][] u) {
        int n = g.length; // ukuran matriks

        //ukuran dari matriks yang diinginkan adalah nilai perpangkatan 2.
        int degree = (int) Math.ceil(Math.log(n) / Math.log(2));
        int size = (int) Math.pow(2, degree); //ukuran matriks yang diinginkan.
        /* nilai dari size adalah nilai terkecil yang lebih besar dari n, yang
         * merupakan perpangkatan dari 2.
         */

        double[][] a = new double[size][size];
        double[][] b = new double[size][size];

        /* dibawah ini adalah proses copy matriks g dan u kedalam matriks a dan b
         * ukuran matriks a dan b lebih besar daripada matriks g dan u
         * sehingga bagian selebihnya dari matriks a dan b bernilai 0.
         */
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                a[i][j] = g[i][j];
                b[i][j] = u[i][j];
            }
        }

        //rekursive strassen untuk matriks berukuran pangkat dari 2
        double[][] c = strassenRecursive(a, b);
        double[][] result = new double[n][n];

        /* copy nilai dari c ke variabel result;
         * karena ukuran c lebih besar dari n, maka batasnya adalah n.
         * diasumsikan perhitungan strassenRecursive benar sehingga bagian array
         * c pada indeks yang lebih besar dari n berisi 0 dan bisa dihilangkan.
         */
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                result[i][j] = c[i][j];
            }
        }
        return result;
    }

    /**
     * Merupakan method rekursif dari algoritma Strassen.
     * Mengalikan dua buah matriks berukuran n x n, dimana n merupakan
     * perpangkatan dari 2.
     * @param a Matriks A
     * @param b Matriks B
     * @return Hasil perkalian matriks A dan B dengan algoritma Strassen.
     */
    private double[][] strassenRecursive(double[][] a, double[][] b) {
        int n = a.length; //ukuran dari matriks input merupakan perpangkatan 2

        if (n == 2) {
            return mul2x2Mat(a, b); //base case, ketika ukuran matriks 2 x 2.
        } else {
            int n2 = n / 2;
            double[][] a11 = new double[n2][n2]; //kiri atas
            double[][] a12 = new double[n2][n2]; //kanan atas
            double[][] a21 = new double[n2][n2]; //kiri bawah
            double[][] a22 = new double[n2][n2]; //kanan bawah

            double[][] b11 = new double[n2][n2]; //kiri atas
            double[][] b12 = new double[n2][n2]; //kanan atas
            double[][] b21 = new double[n2][n2]; //kiri bawah
            double[][] b22 = new double[n2][n2]; //kanan bawah

            //membagi matriks A dan B menjadi 4 submatriks
            for (int i = 0; i < n2; i++) {
                for (int j = 0; j < n2; j++) {
                    a11[i][j] = a[i][j];
                    a12[i][j] = a[i][j + n2];
                    a21[i][j] = a[i + n2][j];
                    a22[i][j] = a[i + n2][j + n2];

                    b11[i][j] = b[i][j];
                    b12[i][j] = b[i][j + n2];
                    b21[i][j] = b[i + n2][j];
                    b22[i][j] = b[i + n2][j + n2];
                }
            }

            //menghitung P1 sampai P7:
            //acuan: Slide kuliah algoritma Strassen halaman 30/42

            double[][] p1 = strassenRecursive(addSqMatrix(a11, a22), addSqMatrix(b11, b22));
            //P1 = (A11 + A22)(B11 + B22)

            double[][] p2 = strassenRecursive(addSqMatrix(a21, a22), b11);
            //P2 = (A21 + A22)B11

            double[][] p3 = strassenRecursive(a11, subSqMatrix(b12, b22));
            //P3 = A11(B12 - B22)

            double[][] p4 = strassenRecursive(a22, subSqMatrix(b21, b11));
            //P4 = A22(-B11 + B21) = A22(B21 - B11);

            double[][] p5 = strassenRecursive(addSqMatrix(a11, a12), b22);
            //P5 = (A11 + A12)B22

            double[][] p6 = strassenRecursive(subSqMatrix(a21, a11), subSqMatrix(b11, b12));
            //P6 = (-A11 + A21)(B11 + B12) = (A21 - A11)(B11 + B12)

            double[][] p7 = strassenRecursive(subSqMatrix(a12, a22), addSqMatrix(b21, b22));
            //P7 = (A12 - A22)(B21 + B22)

            //menghitung C11, C12, C21, dan C22:
            double[][] c11 = addSqMatrix(p1, addSqMatrix(p4, subSqMatrix(p7, p5)));
            //C11 = P1 + P4 - P5 + P7 = P1 + (P4 + (P7 - P5))

            double[][] c12 = addSqMatrix(p3, p5);
            //C12 = P3 + P5

            double[][] c21 = addSqMatrix(p2, p4);
            //C21 = P2 + P4

            double[][] c22 = addSqMatrix(p1, addSqMatrix(p3, subSqMatrix(p6, p2)));
            //C22 = P1 + P3 - P2 + P6 = P1 + (P3 + (P6 - P2))

            //menggabungkan kembali C11, C12, C21, dan C22 kedalam satu matriks.
            double[][] result = new double[n][n];
            for (int i = 0; i < n2; i++) {
                for (int j = 0; j < n2; j++) {
                    result[i][j] = c11[i][j];
                    result[i][j + n2] = c12[i][j];
                    result[i + n2][j] = c21[i][j];
                    result[i + n2][j + n2] = c22[i][j];
                }
            }
            return result;
        }
    }

    /**
     * Fungsi pembantu yang mengalikan 2 buah matriks berukuran 2 x 2.
     * @param a Matriks A.
     * @param b Matriks B.
     * @return Hasil kali matriks A dan B dimana A dan B berukuan 2 x 2.
     */
    private double[][] mul2x2Mat(double[][] a, double[][] b) {
        double[][] r = new double[2][2];
        r[0][0] = a[0][0] * b[0][0] + a[0][1] * b[1][0];
        r[0][1] = a[0][0] * b[0][1] + a[0][1] * b[1][1];
        r[1][0] = a[1][0] * b[0][0] + a[1][1] * b[1][0];
        r[1][1] = a[1][0] * b[0][1] + a[1][1] * b[1][1];
        return r;
    }

    /**
     * Menjalankan operasi penjumlahan 2 buah matriks persegi.
     * @param a Matriks A
     * @param b Matriks B
     * @return Hasil penjumlahan matriks A dengan matriks B
     */
    private double[][] addSqMatrix(double[][] a, double[][] b) {
        int row = a.length;
        double[][] result = new double[row][row];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < row; j++) {
                result[i][j] = a[i][j] + b[i][j];
            }
        }
        return result;
    }

    /**
     * Menjalankan operasi pengurangan 2 buah matriks persegi.
     * @param a Matriks A
     * @param b Matriks B
     * @return Hasil pengurangan matriks A dengan matriks B
     */
    private double[][] subSqMatrix(double[][] a, double[][] b) {
        int row = a.length;

        double[][] result = new double[row][row];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < row; j++) {
                result[i][j] = a[i][j] - b[i][j];
            }
        }
        return result;
    }
}
