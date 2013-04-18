/**
    DO NOT MODIFY THIS DOCUMENT UNLESS
    YOU ARE ACCEPTING THE BONUS CHALLENGE
*/

/**
    Class LUDecomposition
    Menyediakan method-method untuk menjalankan faktorisasi LU 
    tanpa pivoting dan menyelesaikan sistem persamaan linier
    Ax = b (serta method pendukung lainnya, seperti untuk
    menghitung determinan, dll).
    @author Ikhsanul Habibie    
*/

public class LUDecomposition {
/* ------------------------
   Class variables
 * ------------------------ */
    
    /**
        matriks segitiga atas hasil faktorisasi LU (U)
    */
	private double[][] upperMatrix;
    
    /**
        matriks segitiga bawah hasil faktorisasi LU (L)
    */
	private double[][] lowerMatrix;

/* ------------------------
   Constructors
 * ------------------------ */
	
    public LUDecomposition() {

	}

    /** Faktorisasi LU dari matriks a melalui constructor
   @param a    matriks persegi
   */
	public LUDecomposition(double[][] a) {
		double[][] m = new double[a.length][a.length];
		m = array2DCopy(a);

		lowerMatrix = new double[m.length][m.length];
		for(int j = 0; j < m.length - 1; j++) {
			for(int i = 1 + j; i < m.length; i++) {
				lowerMatrix[i][j] = m[i][j]/m[j][j];
				double constFirstRowElem = m[i][j];

				for(int k = 0; k < m.length; k++) {
					m[i][k] = m[i][k] - m[j][k] *
						(constFirstRowElem/m[j][j]);
				}
			}
		}

		for(int i = 0; i < m.length; i++) {
			lowerMatrix[i][i] = 1;
		}

		upperMatrix = m;
	}
    
    /** Mengakses variable matriks segitiga bawah L
   @return     matriks L
   */
    public double[][] getLowerMatrix() {
		return lowerMatrix;
	}

    /** Mengakses variable matriks segitiga atas U
   @return     matriks U
   */
	public double[][] getUpperMatrix() {
		return upperMatrix;
	}

    /** Copy matriks
   @param array     matriks yang ingin di-copy
   @return     Hasil copy dari matriks
   */
	public double[][] array2DCopy(double[][] array) {
		double[][] newArray = new double[array.length][array.length];

		for(int i = 0; i < array.length; i++) {
			System.arraycopy(array[i], 0, newArray[i], 0, array.length);
		}

		return newArray;
	}

    /**
    * UNUSED METHOD - CURRENTLY USING STRASSEN'S
    */
	/*public double[][] matrixMultiplication(double a[][], double b[][]) {
		double c[][] = new double[a.length][b[0].length];

		for(int i = 0; i < a.length; i++) {
			for(int j = 0; j < b[0].length; j++) {
				for(int k = 0; k < a.length; k++) {
					c[i][j] += a[i][k] * b[k][j];
				}
			}
		}

		return c;
	}*/

    /** Menghitung determinan matriks (yang dimasukkan dalam argumen constructor class)
   @return     determinan matriks ()
   */
	public double computeDeterminant() {
		double determinant = upperMatrix[0][0];
		for(int i = 1; i < upperMatrix.length; i++) {
			determinant *= upperMatrix[i][i];
		}

		return determinant;
	}
    
    /**
        UNUSED METHOD
    */
    /**
    * from google search
    */
    /*
    public double determinant(double[][] mat)
    {
        double result = 0;  
        
        if(mat.length == 1) { 
            result = mat[0][0]; 
            //return result; 
        } 

        else if(mat.length == 2) { 
            result = mat[0][0] * mat[1][1] - mat[0][1] * mat[1][0]; 
            //return result; 
        }
        
        else {
            for(int i = 0; i < mat.length; i++) { 
                double temp[][] = new double[mat.length - 1][mat.length - 1]; 

                for(int j = 1; j < mat.length; j++) { 
                    System.arraycopy(mat[j], 0, temp[j-1], 0, i); 
                    System.arraycopy(mat[j], i+1, temp[j-1], i, mat[0].length-i-1); 
                } 

                result += mat[0][i] * Math.pow(-1, i) * determinant(temp); 
            }
        }            

        return result;
    }
    */

    /** Menyelesaikan sistem persamaan Ux = y dengan backward substitution
   @param b     matriks kolom pada ruas kanan sistem persamaan Ux = y (matriks y)
   @return     matriks kolom hasil penyelesaian Ux = y (matriks x)
   */
	public double[] backwardSubstitution(double[] b) {
		double[] solution = new double[b.length];
		for(int i = b.length - 1; i >= 0; i--) {
			solution[i] = b[i]/upperMatrix[i][i];

			for(int j = 0; j < i; j++) {
				b[j] -= upperMatrix[j][i] * solution[i];
			}
		}

		return solution;
	}

    /** Menyelesaikan sistem persamaan Ly = b dengan forward substitution
   @param b     matriks kolom pada ruas kanan sistem persamaan Ly = b (matriks b)
   @return     matriks kolom hasil penyelesaian Ly = b (matriks y)
   */
	public double[] forwardSubstitution(double[] b) {
		double[] solution = new double[b.length];
		for(int i = 0; i < lowerMatrix.length; i++) {
			solution[i] = b[i]/lowerMatrix[i][i];

			for(int j = i; j < b.length; j++) {
				b[j] -= lowerMatrix[j][i] * solution[i];
			}
		}

		return solution;
	}

    /** Mencetak isi dari matriks
   @param m     matriks yang ingin dicetak
   */
	public static void printMatrix(double m[][]) {
		for(int i = 0; i < m.length; i++) {
			for(int j = 0; j < m[0].length; j++) {
				System.out.print(m[i][j] + " |");
			}
			System.out.println();
		}
	}

    /** Menyelesaikan sistem persamaan LUx = b
   @param b     matriks kolom b pada LUx = b
   @return     matriks kolom hasil penyelesaian LUx = b (matriks x)
   */
	public double[] solveLSE(double[] b) {
		double y[] = new double[b.length];

		y = forwardSubstitution(b);
		return backwardSubstitution(y);
	}
} //akhir class
