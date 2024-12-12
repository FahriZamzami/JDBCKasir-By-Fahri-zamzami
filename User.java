import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
import java.util.Scanner;

public class User {

    public String username;
    public String password;
    private String newPassword;
    private String loggedinUser;

    public static Connection con;
    public static PreparedStatement preparedstm;

    public static boolean loginSuccess = false;
    public static boolean deleteSuccess = false;

    public boolean isThereUsername (String username){
        String sql = "SELECT 1 FROM user WHERE username = ?";

        try {
            con = Koneksi.getConnection(); 
            preparedstm = con.prepareStatement(sql);

            preparedstm.setString(1, username);

            try (ResultSet resultSet = preparedstm.executeQuery()){

                return resultSet.next();
            }
        }

        catch (SQLException e){
            e.printStackTrace();
            return true;
        }

    }

    public String loginVerifikasi (String username, String password){
        String sql = "SELECT * FROM user WHERE username = ? AND password = ?";

        try {

            con = Koneksi.getConnection(); 
            preparedstm = con.prepareStatement(sql);
            
            preparedstm.setString(1, username);
            preparedstm.setString(2, password);

            try (ResultSet resultset = preparedstm.executeQuery()){

                if (resultset.next()){
                    return username; 
                }
            }
        }

        catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public boolean update_password (String username, String password){
        
        String sql = "UPDATE user SET password = ? WHERE username = ?";

        try {

            con = Koneksi.getConnection(); 
            preparedstm = con.prepareStatement(sql);

            preparedstm.setString(1, newPassword);
            preparedstm.setString(2, username);

            int rowUpdate = preparedstm.executeUpdate();

            return rowUpdate > 0;
        }

        catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteAccount (String username){    

        String sql = "DELETE FROM user WHERE username = ?";

        try {

            con = Koneksi.getConnection(); 
            preparedstm = con.prepareStatement(sql);
            
            preparedstm.setString(1, username);

            int rowDelete = preparedstm.executeUpdate();
            return rowDelete > 0;
        }

        catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    public void createAccount(Scanner input){
        
        boolean isCreateAccount = true;

        while (isCreateAccount){
            System.out.print("\nUsername: ");
            username = input.nextLine();

            if (username.equals("0")){
                isCreateAccount = false;
                break;
            }

            else if (isThereUsername(username)){
                System.out.println("\nUsername Sudah Digunakan");
            }

            else{
                break;
            }
        }

        while (isCreateAccount){
            System.out.print("Password: ");
            password = input.nextLine();

            try {
                Connection con = Koneksi.getConnection();

                if (con != null) {
                    String sql = "INSERT INTO user (username, password) VALUES (?,?)";
                    PreparedStatement preparedstm = con.prepareStatement(sql);

                    preparedstm.setString(1, username);
                    preparedstm.setString(2, password);
                    
                    int row = preparedstm.executeUpdate();

                    if(row > 0){
                        
                        System.out.println("\nPembuatan Akun berhasil");
                        isCreateAccount = false;
                    }
                }

                else {
                    System.out.println("\nPembuatan Akun Gagal");
                }
            }

            catch (Exception e) {
                System.err.println("\nError" + e.getMessage());
            }
        }
    }



    public void LogIn(Scanner input) {

        boolean isLogin = true;

        while (isLogin) {

            System.out.print("\nUsername: ");
            username = input.nextLine();

            if (username.equals("0")){
                isLogin = false;
                break;
            }

            else {
                break;
            }
        }
        
        while (isLogin){
            System.out.print("Password: ");
            password = input.nextLine();

            loggedinUser = loginVerifikasi(username, password);

            // CAPTCHA generation
            String captcha = generateCaptcha();
            System.out.println("\nCAPTCHA: " + captcha);
            System.out.print("Masukkan CAPTCHA: ");
            String captchaInput = input.nextLine();

            if (loggedinUser != null && captcha.equals(captchaInput)) {
                System.out.println("\nLogIn Berhasil");
                System.out.println("\nSelamat Datang di Supermarket ElevenTwelve");
                loginSuccess = true;
                isLogin = false;
                break;
            }

            else if(loggedinUser == null || !captcha.equals(captchaInput)){
                System.out.println("\nLogIn Gagal. Silahkan Diulangi");
                loginSuccess = false;
            }
        }
    }

    //Method string untuk captcha
    private String generateCaptcha() {
        int length = 6; // Panjang CAPTCHA
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder captcha = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            captcha.append(characters.charAt(index));
        }

        return captcha.toString();
    }

    public void updatePassword (Scanner input){
        System.out.print("\nMasukkan Password Baru: ");
        newPassword = input.nextLine();

        boolean updateSuccess = update_password (username, newPassword);
        
        if (updateSuccess){
            System.out.println("\nPassword Berhasil Diperbarui");
        }

        else {
            System.out.println("\nPassword Gagal Dierbarui");
        }
    }

    public void delete_account (Scanner input){

        System.out.println("\nYakin Ingin Mengahapus Akun?");

        System.out.print("\nY/N: ");
        String pilihan = input.nextLine();
        String teksPilihan = pilihan.toUpperCase(); 

        if (teksPilihan.equals("Y")){
            deleteSuccess = deleteAccount(loggedinUser) ;

            if(deleteSuccess){
                System.out.println("\nAkun Berhasil Dihapus");
            }

            else {
                System.out.println("\nAkun Gagal Dihapus");
            }

        }

        else if (teksPilihan.equals("N")){
            System.out.println("\nAkun Batal Dihapus");
        }

        else{
            System.out.println("Pilihan Hanya Y/N");
        }
    }
}