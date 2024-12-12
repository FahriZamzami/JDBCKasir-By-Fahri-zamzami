import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Scanner;

public class Barang { //Super Class

    public String kode_barang;
    public String nama_barang;
    public float harga_barang;
    public int jumlah_barang;
    private float newHargaBarang;
    private int newJumlahBarang;
    User user;

    public static Connection con;
    public static PreparedStatement preparedstm;

    public static boolean deleteBarangSuccess = false;

    public int no_faktur;
    public int jumlah_beli;
    public float total;

    public boolean isThereBarang (String kode_barang){
        String sql = "SELECT 1 FROM barang WHERE kode_barang = ?";

        try {
            con = Koneksi.getConnection(); 
            preparedstm = con.prepareStatement(sql);

            preparedstm.setString(1, kode_barang);

            try (ResultSet resultSet = preparedstm.executeQuery()){

                return resultSet.next();
            }
        }

        catch (SQLException e){
            e.printStackTrace();
            return true;
        }
    }

    public boolean updateBarang (String kode_barang, float newHargaBarang, int newJumlahBarang){
        
        String sql = "UPDATE barang SET harga_barang = ?, jumlah_barang = jumlah_barang + ? WHERE kode_barang = ?";

        try {

            con = Koneksi.getConnection(); 
            preparedstm = con.prepareStatement(sql);

            preparedstm.setFloat(1, newHargaBarang);
            preparedstm.setInt(2, newJumlahBarang);
            preparedstm.setString(3, kode_barang);

            int rowUpdate = preparedstm.executeUpdate();

            return rowUpdate > 0;
        }

        catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteBarang (String kdoe_barang){    

        String sql = "DELETE FROM barang WHERE kode_barang = ?";

        try {

            con = Koneksi.getConnection(); 
            preparedstm = con.prepareStatement(sql);
            
            preparedstm.setString(1, kode_barang);

            int rowDelete = preparedstm.executeUpdate();
            return rowDelete > 0;
        }

        catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    public void addBarang(Scanner input){
        
        boolean isAddBarang = true;

        while (isAddBarang){
            System.out.print("\nKode Barang: ");
            kode_barang = input.nextLine();

            if (kode_barang.equals("0")){
                isAddBarang = false;
                break;
            }

            else if (isThereBarang(kode_barang)){
                System.out.println("\nKode Barang Sudah Ada");
            }

            else{
                break;
            }
        }

        while (isAddBarang){
            System.out.print("Nama Barang: ");
            nama_barang = input.nextLine();

            System.out.print("Harga Barang: ");
            harga_barang = input.nextInt();
            input.nextLine();

            System.out.print("Jumlah Barang");
            jumlah_barang = input.nextInt();
            input.nextLine();

            try {
                Connection con = Koneksi.getConnection();

                if (con != null) {
                    String sql = "INSERT INTO barang (kode_barang, nama_barang, harga_barang, jumlah_barang) VALUES (?,?,?,?)";
                    PreparedStatement preparedstm = con.prepareStatement(sql);

                    preparedstm.setString(1, kode_barang);
                    preparedstm.setString(2, nama_barang);
                    preparedstm.setFloat(3, harga_barang);
                    preparedstm.setInt(4, jumlah_barang);
                    
                    int row = preparedstm.executeUpdate();

                    if(row > 0){
                        
                        System.out.println("\nBarang Berhasil Ditambahkan");
                        isAddBarang = false;
                    }
                }

                else {
                    System.out.println("\nBarang Gagal Ditambahkan");
                }
            }

            catch (Exception e) {
                System.err.println("\nError" + e.getMessage());
            }
        }
    }

    public void tampilkanSemuaData() {
        String sql = "SELECT * FROM barang";
    
        try {
            Connection con = Koneksi.getConnection();
    
            if (con != null) {
                PreparedStatement preparedstm = con.prepareStatement(sql);
    
                try (ResultSet resultSet = preparedstm.executeQuery()) {
                    while (resultSet.next()) {
                        System.out.println("Kode Barang: " + resultSet.getString("kode_barang"));
                        System.out.println("Nama Barang: " + resultSet.getString("nama_barang"));
                        System.out.println("Harga Barang: " + resultSet.getFloat("harga_barang"));
                        System.out.println("Jumlah Barang: " + resultSet.getInt("jumlah_barang"));
                        System.out.println("------------------------");
                    }
                }
            }
    
            else {
                System.out.println("Koneksi gagal");
            }
        }
    
        catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    public void update_Barang (Scanner input){
        System.out.print("Kode Barang: ");
        kode_barang = input.nextLine();

        String sql = "SELECT * FROM barang WHERE kode_barang = ?";
        Connection con = Koneksi.getConnection();
        try {
        
            PreparedStatement preparedstm = con.prepareStatement(sql);
            preparedstm.setString(1, kode_barang);

            try (ResultSet resultset = preparedstm.executeQuery()){
                if(resultset.next()){
                    System.out.println("Nama Barang:" + resultset.getString("nama_barang"));
                }
            }
        }

        catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        System.out.print("\nMasukkan Harga Barang Baru: ");
        newHargaBarang = input.nextFloat();

        System.out.print("\nTambahkan Jumlah Barang: ");
        newJumlahBarang = input.nextInt();

        boolean updateSuccess = updateBarang(kode_barang, newHargaBarang, newJumlahBarang);
        
        if (updateSuccess){
            System.out.println("\nBarang Berhasil Diupdate");
        }

        else {
            System.out.println("\nBarang Gagal Diupate");
        }
    }

    public void delete_Barang (Scanner input){
        System.out.print("Kode Barang: ");
        kode_barang = input.nextLine();

        String sql = "SELECT * FROM barang WHERE kode_barang = ?";
        Connection con = Koneksi.getConnection();
        try {
        
            PreparedStatement preparedstm = con.prepareStatement(sql);
            preparedstm.setString(1, kode_barang);

            try (ResultSet resultset = preparedstm.executeQuery()){
                if(resultset.next()){
                    System.out.println("Nama Barang:" + resultset.getString("nama_barang"));
                }
            }
        }

        catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        System.out.println("\nYakin Ingin Mengahapus Barang?");

        System.out.print("\nY/N: ");
        String pilihan = input.nextLine();
        String teksPilihan = pilihan.toUpperCase(); 

        if (teksPilihan.equals("Y")){
            deleteBarangSuccess = deleteBarang(kode_barang);

            if(deleteBarangSuccess){
                System.out.println("\nBarang Berhasil Dihapus");
            }

            else {
                System.out.println("\nBarang Gagal Dihapus");
            }

        }

        else if (teksPilihan.equals("N")){
            System.out.println("\nBarang Batal Dihapus");
        }

        else{
            System.out.println("Pilihan Hanya Y/N");
        }
    }

    public void faktur () {
        Date date = new Date(); //membuat objek date
        String str = String.format("Current Date/Time : %tc", date);
        System.out.printf(str);
        
        System.out.printf(str);
        System.out.println("\n=============================================================\n");
        System.out.println("No. Faktur   : " + no_faktur);
        System.out.println("Kode Barang  : " + kode_barang);
        System.out.println("Nama Barang  : " + nama_barang);
        System.out.println("Harga Barang : " + harga_barang);
        System.out.println("Jumlah Beli  : " + jumlah_beli);
        System.out.println("Total        : " + total);
        System.out.println("=============================================================\n");
        System.out.println("Kasir: fahri");
        System.out.println("=============================================================\n");
        System.out.println("Terimakasih Telah Berbelanja");
    }
}