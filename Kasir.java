import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Kasir {
    public int noFaktur = 1;
    public String kodeBarang = "";
    public String lanjut = "";

    User user = new User();
    Barang barang = new Barang();

    public void sistemKasir(){

        Scanner input = new Scanner (System.in);

        while (true) {

            boolean pembayaran = false;

            while (true){

                System.out.print("\nMasukkan Kode Barang: ");
                kodeBarang = input.nextLine();

                if (kodeBarang.equals("0")){
                    break;
                }

                if (barang.isThereBarang(kodeBarang)){
                    String sql = "SELECT * FROM barang WHERE kode_barang = ?";
                    Connection con = Koneksi.getConnection();
                    try {
                        PreparedStatement preparedstm = con.prepareStatement(sql);
                        preparedstm.setString(1, kodeBarang);

                        try (ResultSet resultset = preparedstm.executeQuery()){
                            if(resultset.next()){
                                barang.kode_barang = resultset.getString("kode_barang");
                                barang.nama_barang = resultset.getString("nama_barang");
                                barang.harga_barang = resultset.getFloat("harga_barang");
                                barang.jumlah_barang = resultset.getInt("jumlah_barang");
                            }
                        }
                    }

                    catch (SQLException e) {
                        System.out.println("Error: " + e.getMessage());
                    }

                    break;
                }

                else {
                    System.out.println("\nKode barang tidak valid. Silahkan coba lagi.");
                }
            }

            if (kodeBarang.equals("0")){
                break;
            }

            try {
                System.out.println("\nNama Barang: " + barang.nama_barang);
                System.out.print("\nMasukkan Jumlah Barang: ");
                int jumlahBarang = input.nextInt();
                input.nextLine();

                if (jumlahBarang < 1){
                    System.out.println("Minimum Input 1");
                }

                if (jumlahBarang > 0){
                
                    barang.jumlah_beli = jumlahBarang;        
                    barang.total = (float) (barang.harga_barang * jumlahBarang);
                    barang.no_faktur = noFaktur;
                        
                    System.out.println("\nTotal Harga: " + barang.total);

                    while (pembayaran == false) {
            
                        System.out.print("\nSilahkan Input Uang Untuk Pembayaran: ");
                        float bayar = input.nextInt();
                        input.nextLine();

                        if(bayar < barang.total){
                            System.out.println("\nUang Anda Kurang!");
                            break;
                        }

                        else {
                            System.out.println("\nPembayaran Berhasil!");
                            barang.faktur();
                            noFaktur++;
                            pembayaran = true;
                        }
                    }
                }
            }

            catch (InputMismatchException e) {
                System.out.println("\nInput Error");
                input.nextLine();
            }

            while (true){
                try {
                    System.out.print("\nIngin Belanja Lagi? (1 = ya)/(0 = tidak): ");
                    lanjut =  input.nextLine();

                    if(lanjut.equals("1")){
                        break;
                    }
                        
                    else if (lanjut.equals("0")) {
                        break;
                    }

                    else {
                        //Memberikan exception ketika pilihan bukan 0 atau 1
                        throw new IllegalArgumentException("Pilihan Hanya 0 atau 1");
                    }
                }

                //Melakukan catch untuk menjalankan exception
                catch (IllegalArgumentException a) {
                    System.out.println(a.getMessage());
                }
            }

            if (lanjut.equals("0")){
                break;
            }
        }

        input.close();
        System.out.println("\nTerimakasih Telah Berkunjung");
    }
}