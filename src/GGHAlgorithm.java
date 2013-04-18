import java.util.Random;

/**
    DO NOT MODIFY THIS DOCUMENT UNLESS
    YOU ARE ACCEPTING THE BONUS CHALLENGE
*/
/**
    Class GGHAlgorithm
    Menyediakan method-method untuk menjalankan proses enkripsi-dekripsi
    GGH cryptosystem (yang disederhanakan). Menggunakan bantuan class 
    LUDecomposition untuk menyelesaikan sistem persamaan pada proses dekripsi.
    @author Ikhsanul Habibie (di-update oleh Aruni Yasmin Azizah)   
*/

public class GGHAlgorithm {
/* ------------------------
   Class variables
 * ------------------------ */
    
    /**
        private key dengan dimensi 20 dengan range nilai -200 s/d 200
        Hadamard ratio sekitar 0.8
    */
    private double[][] privateKey = {
        {17, -6, 2, -1, -7, -2, -8, -7, 1, -3, 0, 6, -4, -3, 2, 3, -1, -7, -7, 0},
        {0, -21, 21, -7, 12, -1, -1, 10, -5, -5, 0, 7, 11, -12, -2, 7, -19, 8, -8, 2},
        {0, -13, -26, 0, 11, -25, -5, 22, -27, -5, -25, 16, 13, 12, 2, -17, -15, 5, 14, -8},
        {0, -1, -2, -9, 54, 6, 9, 6, -13, -13, -3, 0, -21, 18, -21, 21, -10, -16, 21, 22},
        {0, 9, 18, 8, 9, -54, 23, 16, -15, -32, 2, 9, 8, 22, 1, 9, -22, 11, 7, 35},
        {0, -4, -8, -28, -4, 24, 32, -6, 8, -4, 4, -7, -9, 4, -7, -1, 22, 9, 17, 10},
        {0, 0, 0, 3, 0, 0, 3, 7, -48, 36, 22, 2, -25, 0, 0, -26, 37, -16, 31, -5},
        {0, 0, 0, 17, 0, 0, 17, -3, -22, 7, 18, 52, -23, 7, -12, 31, -15, 6, -16, 11},
        {0, 0, 0, 1, 0, 0, 1, -3, -34, 19, -11, -1, 2, -6, -14, 4, 19, -31, -63, -23},
        {0, 0, 0, 13, 0, 0, 13, -35, 10, -18, -3, -13, 4, -26, -11, -4, 2, 21, 4, 2},
        {0, 0, 0, -12, 0, 0, -12, 8, 21, 1, -49, 12, 28, -4, 20, 2, 6, -8, 19, 13},
        {0, 0, 0, 12, 0, 0, 12, -18, 15, 45, 33, -12, 18, 1, -10, -15, -7, -8, 4, -25},
        {0, 0, 0, 16, 0, 0, 16, 16, 0, 0, -16, -16, 8, 24, -17, 8, 23, 17, 9, 14},
        {0, 0, 0, 12, 0, 0, 12, 12, 0, 0, -12, -12, -33, -40, 22, 23, -21, 4, 19, -1},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -5, 48, -17, 43, 9, 20, -12, -8},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4, 0, 48, 4, -12, -6, -30, -12},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 43, 0, 0, 43, -23, 22, 1, -24},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 55, -15, -40, -16},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 62, -62, -28},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 195}
    };
    
 /* ------------------------
    Constructor
 * ------------------------ */   
    public GGHAlgorithm() {
    
    }
    
    /** Mengakses privateKey
   @return      private key
   */
    public double[][] getPrivateKey() {
		return this.privateKey;
	}
	
    /** Memberi nilai pada privateKey
   @param privateKey    matriks 20x20 yang ingin dijadikan private key        
   */
	public void setPrivateKey(double[][] privateKey){
		this.privateKey = privateKey;
	}
    
    /** Membuat public key
   @param g    private key
   @param imax      range nilai public key (imax, berarti antara -imax s/d imax, inclusive)
   @param hrat      batas atas hadamard ratio untuk public key yang ingin dibuat
   @return      public key
   */
    public double[][] generatePublicKey(double[][] g, int imax, double hrat)
    {
        LUDecomposition copyarray = new LUDecomposition();
        double[][] b = copyarray.array2DCopy(g);
        int dim = b.length;
        double[][] v = new double[dim][dim];
        
        //CONSTRUCT StrassensAlgorithm OBJECT
        StrassensAlgorithm multiply = new StrassensAlgorithm();
        
        //randomunimodmatrix
        double[][] u = this.generateUnimodularMatrix(dim,imax);
        
        while(this.hadamardRatio(b) > hrat)
        {
            //B = G*U
            //USING STRASSENS
            b = multiply.strassenMatrixMulti(g,u);

            //generate randomunimodmatrix, save to V
            v = this.generateUnimodularMatrix(dim,imax);            
            
            //U = U*V
            //USING STRASSENS
            u = multiply.strassenMatrixMulti(u,v);
        }
        
        return b;
    }
    
    /** Mendapatkan public key, menggunakan generatePublicKey untuk membuat public key 
   @param privateKey    private key
   @return      public key
   */
    public double[][] getPublicKey(double[][] privateKey){
		//hadamard ratio of 0.5 is already "bad"
        return this.generatePublicKey(privateKey,200,0.5);
	}
    
    /** Melakukan proses enkripsi sesuai langkah-langkah pada GGH cryptosystem
   @param blockAscii    array yang berisi bagian pesan yang ingin di-enkripsi
   @param publicKey     public key
   @return      array hasil enkripsi
   */
    public double[] encryptGGH(double[] blockAscii, double[][] publicKey){
		double[] cipherMatrix = new double[blockAscii.length];
		
		double[] perturbationVector, multMatrix;
		perturbationVector = this.generatePerturbationVector(publicKey.length);
		multMatrix = this.matrixVectorMul(publicKey, blockAscii);
		
		for(int j = 0; j < multMatrix.length; j++){
			cipherMatrix[j] = multMatrix[j] + perturbationVector[j];
		}
		
		return cipherMatrix;
	}

    /** Melakukan proses dekripsi sesuai langkah-langkah pada GGH cryptosystem
   @param privateKey    private key
   @param publicKey     public key
   @param cipherText    array yang berisi cipher text
   @return      array hasil dekripsi
   */
	public double[] decryptGGH(double[][] privateKey, double[][] publicKey, double[] cipherText) {
		
        //CONSTRUCTING OBJECT FROM LUDecomposition
        LUDecomposition priv = new LUDecomposition(privateKey);
        
        //SOLVE (privateKey)(cipherLinearCombination) = (cipherText)
		double[] cipherLinearCombination = priv.solveLSE(cipherText);

		double[] closestVertex = babaiClosestVertexAlgorithm(
			cipherLinearCombination, privateKey);

        //CONSTRUCTING OBJECT FROM LUDecomposition
		LUDecomposition pub = new LUDecomposition(publicKey);
        
        //SOLVE (publicKey)x = (privateKey*closestVertex)
		return pub.solveLSE(matrixVectorMul(privateKey, closestVertex));
	}

    /** Membuat matriks dimensi dimxdim yang diisi dengan integer (random)
   @param imax    nilai maksimum elemen matriks
   @param dim   dimensi matriks persegi
   @return      matriks dimxdim
   */
    public double[][] randomMatrix(int imax, int dim)
    {
        double m[][] = new double[dim][dim];
		Random random = new Random();

		for(int i = 0; i < dim; i++) {
			for(int j = 0; j < dim; j++) {
				m[i][j] = (double)(random.nextInt(imax - 1) + 1);
			}
		}

		//System.out.println("in randomMatrix");
		//this.printMatrix(m);

		return m;
    }
    
    /** Implementasi proses derangement
   @param n     ukuran array (jumlah posisi yang ingin dipermutasi)
   @return      array hasil derangement
   */
    public int[] derangement(int n)
    {
        int m[] = new int[n];

		for(int i = 0; i < n; i++) {
			m[i] = i;
		}

		return this.randperm(m);   
    }
    
    /** Membantu proses derangement, permutasi dimana di hasil akhir tidak ada posisi isi array yg sama dengan posisi awal, e.g 1 != 1'
   @param a     array yang ingin di-derangement
   @return      array hasil dekripsi
   */
    public int[] randperm(int[] a)
    {
        int m[] = new int[a.length];

		for(int i = 0; i < a.length; i++) {
			m[i] = a[i];
		}

		Random random = new Random();

		for(int i = 0; i < m.length; i++) {
			int rand = random.nextInt(a.length - 1) + 1;

			while (rand == i) {
				rand = random.nextInt(a.length - 1) + 1;
			}

			int temp = m[i];
			m[i] = m[rand];
			m[rand] = temp;
		}

		return m;
    }

    /** Membuat perturbation vector
   @param dim   ukuran vektor
   @return      perturbation vector
   */
	public double[] generatePerturbationVector(int dim) {
		double[] v = new double[dim];

		for(int i = 0; i < dim; i++) {
			v[i] = Math.round((Math.random() * 10) - 5);
		}

		while(vectorNorm(v) == 0) {
			for(int i = 0; i < dim; i++) {
				v[i] = Math.round((Math.random() * 10) - 5);
			}
		}
		
		/*
		System.out.println();
		System.out.println("perturbation Vector");
		for(int i = 0; i < dim; i++) {
			System.out.println(v[i]);
		}
		*/
        //System.out.println("\nPerturbation Vector");
        //this.printVector(v);
        //System.out.println();
		return v;
	}
    
    /** Membuat matriks unimodular
   @param dim    ukuran matriks, dimxdim
   @param intMax     nilai maksimum matriks
   @return      matriks unimodular
   */
    public double[][] generateUnimodularMatrix(int dim, int intMax)
    {
        
        double u[][] = new double[dim][dim];

        double A[][] = this.randomMatrix(intMax, dim);
		double Atriangular[][] = new double[dim][dim];

		for(int i = 0; i < dim; i++) {
		    for(int j = 0; j < dim; j++) {
			    if (i>j) {
				    Atriangular[i][j] = (double)0;
			    }
			    else if (i == j) {
				    Atriangular[i][j] = (double)1;
			    }
			    else {
				    Atriangular[i][j] = (double)A[i][j];
			    }
		    }
		}

		    //System.out.println("in generateUnimodularMatrix, Atriangular:");
		    //this.printMatrix(Atriangular);
            //System.out.println();

		int p[] = this.derangement(dim);

		    //System.out.println("in generateUnimodularMatrix, derangement:");
		    //this.printVector(p);
            //System.out.println();

		for(int i = 0; i < dim; i++) {
		    for(int j = 0; j < dim; j++) {
			    u[j][i] = Atriangular[j][p[i]];
		    }
		}
            
		//System.out.println("in generateUnimodularMatrix, final matrix:");
		//this.printMatrix(u);

		return u;
    }

    /** Operasi inner product antara dua vektor
   @param u    vektor
   @param v     vektor
   @return      hasil inner product 
   */
	public double innerProduct(double[] u, double[] v) {
		double w = 0;
		for(int i = 0; i < u.length; i++) {
			w += u[i] * v[i];
		}

		return w;
	}

    /** Perkalian skalar dengan vektor
   @param scalar    skalar
   @param v     vektor
   @return      hasil perkalian skalar dengan vektor
   */
	public double[] scalarVectorMul(double scalar, double[] v) {
		for(int i = 0; i < v.length; i++) {
			v[i] += scalar * v[i];
		}

		return v;
	}

    /** Penjumlahan antara dua vektor
   @param u    vektor
   @param v     vektor
   @return      vektor hasil penjumlahan
   */
	public double[] vectorAddition(double[] u, double[] v) {
		double[] newVector = new double[u.length];
		for(int i = 0; i < u.length; i++) {
			newVector[i] = u[i] + v[i];
		}

		return newVector;
	}

    /** Perkalian antara matriks dengan vektor (matriks * vektor = vektor)
   @param a    matriks
   @param b     vektor
   @return      hasil perkalian (vektor)
   */
	public double[] matrixVectorMul(double a[][], double b[]) {
		double c[] = new double[a.length];

		for(int i = 0; i < a.length; i++) {
			for(int j = 0; j < b.length; j++) {
				c[i] += a[i][j] * b[j];
			}
		}

		return c;
	}

    /** Menghitung norm-2 sebuah vektor
   @param v     vektor
   @return      norm-2 vektor
   */
	public double vectorNorm(double[] v) {
		double norm = 0;

		for(int i = 0; i < v.length; i++) {
			norm += Math.pow(v[i], 2);
		}

		return Math.sqrt(norm);
	}

    /** Meyelesaikan closest vertex problem (CVP) dengan algoritma Babai
   @param w    vektor (jadi, solusi CVP adalah vektor terdekat dengan w)
   @param baseVectors       matriks yang berisi basis
   @return      solusi CVP
   */
	public double[] babaiClosestVertexAlgorithm(double[] w, double[][] baseVectors) {
		double wLinearCombination[] = new double[w.length];
		System.arraycopy(w, 0, wLinearCombination, 0, w.length);

		for(int i = 0; i < w.length; i++) {
			wLinearCombination[i] = Math.round(wLinearCombination[i]);
		}

		return wLinearCombination;
	}

    /** Menghitung hadamard ratio dari key
   @param m     matriks/key yang ingin dicari hadamard ratio-nya
   @return      hadamard ratio
   */
	public double hadamardRatio(double m[][]) {
		LUDecomposition lu = new LUDecomposition(m);

		double detL = Math.abs(lu.computeDeterminant());
		double[] firstVector = new double[m.length];

		for(int i = 0; i < firstVector.length; i++) {
			firstVector[i] = m[i][0];
		}

		double norms = vectorNorm(firstVector);

		for(int i = 1; i < m.length; i++) {
			double baseVector[] = new double[m.length];

			for(int j = 0; j < m.length; j++) {
				baseVector[j] = m[j][i];
			}

			norms *= vectorNorm(baseVector);
		}
		return Math.pow(detL/norms, (double)1.0/m.length);
	}

    /** Mencetak matriks ke output
   @param m     matriks
   */
	public static void printMatrix(double m[][]) {
		for(int i = 0; i < m.length; i++) {
			for(int j = 0; j < m[0].length; j++) {
				System.out.print(m[i][j] + " |");
			}
			System.out.println();
		}
	}
    
    /** Mencetak vektor ke output (dengan isi elemen adalah integer)
   @param m     vektor
   */
    public static void printVector(int[] m) {
		for(int i = 0; i < m.length; i++) {
			System.out.print(m[i] + " ");
			System.out.println();
		}
	}
    
    /** Mencetak vektor ke output (dengan isi elemen adalah double)
   @param m     vektor
   */
    public static void printVector(double[] m) {
		for(int i = 0; i < m.length; i++) {
			System.out.print(m[i] + " ");
			System.out.println();
		}
	}
}

