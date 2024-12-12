import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class Koneksi {
    //private Connection connection;
    public static boolean koneksiBerhasil = false;
    public static Connection con;
    public static Statement stm;

    private final static String url = "jdbc:mysql://localhost/kasir";
    private final static String username = "root";
    private final static String password = "";

    public Koneksi(){
        try {
            
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(url, username, password);
            stm = con.createStatement();
            koneksiBerhasil = true;
        }

        catch (Exception e){
            koneksiBerhasil = false;
        }
    }

    public static Connection getConnection () {
        return con;
    }

    public void checkConnection(){
        if(koneksiBerhasil){
            System.out.println("Koneksi Berhasil");
        }

        else {
            System.out.println("Koneksi Gagal");
        }
    }

    public static void closeConnection (){
        if (con != null){
            try{
                con.close();
                stm.close();

                if (User.preparedstm != null){
                    User.preparedstm.close();
                }

                System.out.println("\nBerhasil Keluar");
                System.out.println("Menutup Koneksi Berhasil");

                koneksiBerhasil = false;
            }

            catch (Exception e){
                System.out.println("Gagal Menutup Koneksi" + e.getMessage());
                koneksiBerhasil = true;
            }
        }
    }

}
