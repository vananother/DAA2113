
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Class yang berisi fungsi Main, menjalankan program.
 * @author Vann
 */
public class DAA2113 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
        /* <----------------------------------------------------------------> */

        //input berasal dari file bernama "input.txt". Ubah file jika ingin mengubah input.
        BufferedReader buff = new BufferedReader(new FileReader("input.txt"));
        String a = ""; // variabel string, merupakan hasil parse input.

        String line = buff.readLine();
        while (line != null) {
            a += line + "\n";
            line = buff.readLine();
        }

        double cols = 20; // ukuran 1 block = 20
        GGHAlgorithm asd = new GGHAlgorithm(); //objek baru GGHAlgorithm. berguna untuk melakukan encrypting-decrypting serta mengambil public+private key
        double rows = Math.ceil((double) a.length() / cols); //banyaknya block, yaitu hasil fungsi ceil dari panjang message dibagi 3.

        System.out.println("Messages: " + a); //print message
        System.out.println();


        /*
        Berikut ini adalah implementasi pemetaan message dari String ke block ASCII.
         */
        double message[][] = new double[(int) rows][(int) cols]; //variabel yang berisi block-block ASCII dari message.
        int k = 0; // penanda iterasi
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (k < a.length()) {
                    message[i][j] = a.charAt(k); // mengisi block-block ASCII. Pada Java, character dinyatakan sebagai nilai integer yang melambangkan nilai ASCII karakter tersebut.
                } else {
                    message[i][j] = ' '; // jika semua huruf sudah masuk ke dalam block, block kosong diisi dengan ASCII dari karakter spasi
                }
                k++;
            }
        }

        System.out.println("Messages Block (Ascii) nx3: ");
        asd.printMatrix(message); //melakukan print block ASCII dengan fungsi yang sudah tersedia

        double[][] privateKey = asd.getPrivateKey(); // variabel yang berisi private key.
        double[][] publicKey = asd.getPublicKey(privateKey); // variabel yang berisi public key.
        double[][] cpB = new double[(int) rows][(int) cols]; // variabel yang merupakan cipher block dari block ASCII, merupakan hasil enkripsi data.
        double[][] plB = new double[(int) rows][(int) cols]; // variabel yang merupakan block-block ASCII hasil dekripsi cipher block.

        System.out.println("Private Key:");
        asd.printMatrix(privateKey); // melakukan print private key
        System.out.println();

        System.out.println("Public Key:"); // melakukan print public key
        asd.printMatrix(publicKey);

        long st = System.nanoTime(); // menandakan awal dimulai proses enkripsi

        // proses enkripsi
        for (int i = 0; i < rows; i++) {
            cpB[i] = asd.encryptGGH(message[i], publicKey); // mengisi stiap baris dari cipher block dengan hasil enkripsi data block ASCII. Inti dari proses enkripsi
            // enkripsi menggunakan public key yang digenerate oleh objek GGHalgorithm
        }

        long end = System.nanoTime(); // menandakan berakhirnya proses dekripsi

        System.out.println("Hasil Enkripsi dalam Vector: ");
        asd.printMatrix(cpB); // melakukan print cipher block hasil enkripsi
        long time = end - st; // lamanya waktu proses enkripsi, dalam satuan nanoseconds
        double stime = (double) time * 10E-9; // lamanya waktu proses enkripsi, dalam satuan seconds
        System.out.println();
        System.out.println("Elapse time for encrypting: " + stime + " seconds"); // menampilkan lamanya proses enkripsi


        st = System.nanoTime(); // menandakan awal dmulai proses dekripsi
        // proses dekripsi
        for (int i = 0; i < rows; i++) {
            plB[i] = asd.decryptGGH(privateKey, publicKey, cpB[i]); // mengisi setiap baris dari block ASCII dengan hasil dekripsi dari cipher block
            //proses ini membutuhkan private key dan public key yang digenerate oleh objek GGHalgorithm
        }

        end = System.nanoTime(); // menandakan akhir proses dekripsi

        time = end - st; // lamanya proses dekripsi, dalam satuan nanoseconds
        stime = (double) time * 10E-9; // lamanya proses dekripsi, dalam satuan seconds.

        System.out.println("Hasil Dekripsi dalam Vector: ");
        asd.printMatrix(plB); // menampilkan hasil dekripsi
        System.out.println();

        // hasil dari proses dekripsi masih merupakan bilangan double yang belum dibulatkan namun diduga mendekati kode ASCII yang diharapkan.
        // untuk didapatkan hasil yang akurat juga agar bisa ditranslasikan ke ASCII, bilangan tersebut harus dibulatkan
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                plB[i][j] = Math.round(plB[i][j]); // membulatkan bilangan pada block hasil dekripsi
            }
        }

        System.out.println("Hasil Dekripsi dalam Vector (Rounded):");
        asd.printMatrix(plB); // menampilkan hasil dekripsi yang sudah dibulatkan
        System.out.println();

        System.out.println("Elapse time for decrypting: " + stime + " seconds"); // menampilkan lamanya proses dekripsi
        System.out.println();

        System.out.print("Plain Text: ");
        k = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (k < a.length()) {
                    System.out.print((char) plB[i][j]); // menampilkan pesan hasil dekripsi yang berupa ASCII menjadi karakter ke layar
                    k++; // variabel ini diperlukan agar pesan yang ditampilkan ukurannya sama dengan pesan yang diterima. Tidak lebih.
                }
            }
        }
        System.out.println();
    }
}
