import java.util.Scanner;

public class App {
    public static void main(String[] args) throws Exception {

        Koneksi koneksi = new Koneksi();
        
        koneksi.checkConnection();

        String pilihan;

        User user = new User();
        Kasir kasir = new Kasir();
        Barang barang = new Barang();
        Scanner input = new Scanner(System.in);

        while (Koneksi.koneksiBerhasil){
        
            while (!User.loginSuccess && Koneksi.koneksiBerhasil){
                System.out.println("\nSelamat Datang");
                System.out.println("\n1. Buat Akun");
                System.out.println("2. LogIn");
                System.out.println("3. Keluar");

                System.out.print("\nSilahkan Masukkan Pilihan: ");
                pilihan = input.nextLine();

                if (pilihan.equals("1")){
                    System.out.println("\n0 Untuk Batal");
                    user.createAccount(input);
                }

                if (pilihan.equals("2")){
                    user.LogIn(input);

                    if(User.loginSuccess){
                        User.deleteSuccess = false;
                        break;
                    }
                }

                if (pilihan.equals("3")){
                    Koneksi.closeConnection();
                    
                }
            }

            if (User.loginSuccess){
                while (true){
                    System.out.println("\n1. Perbarui Password");
                    System.out.println("2. Kasir");
                    System.out.println("3. Barang");
                    System.out.println("4. Hapus Akun");

                    System.out.print("\nSilahkan Masukkan Pilihan: ");
                    pilihan = input.nextLine();

                    if(pilihan.equals("1")){
                        user.updatePassword(input);
                    }

                    if (pilihan.equals("2")){
                        kasir.sistemKasir();
                    }

                    if (pilihan.equals("3")){
                        System.out.println("\nBarang");
                        System.out.println("1.Tambah Barang");
                        System.out.println("2. Daftar Barang");
                        System.out.println("3. Edit Barang");
                        System.out.println("4. Hapus Barang");

                        System.out.print("\nPilihan Anda: ");
                        pilihan = input.nextLine();

                        switch (pilihan) {
                            case "1":
                                barang.addBarang(input);
                                break;

                            case "2":
                                barang.tampilkanSemuaData();
                                break;

                            case "3":
                                barang.update_Barang(input);
                                break;

                            case "4":
                                barang.delete_Barang(input);
                                break;
                        
                            default:
                                break;
                        }
                    }

                    if (pilihan.equals("4")){
                        user.delete_account(input);

                        if(User.deleteSuccess){
                            User.loginSuccess = false;
                            break;
                        }
                    }
                }

                if(User.deleteSuccess){
                    break;
                }
            }
        }
    }
}
