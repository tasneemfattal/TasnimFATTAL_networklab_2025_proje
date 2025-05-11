
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author tasni
 */
/**
 * Bu sınıf oyun sunucusunu başlatır.
 * Oyuncular bağlandıkça onları ClientHandler nesnesiyle eşleştirir.
 * Her client için ayrı bir iş parçacığı (thread) oluşturur.
 */

/*
*Bu sınıf oyunun sunucu tarafını yönetir.

*Oyuncular Socket ile bağlandıkça onları bir listeye alır (waitingPlayers).

*İki oyuncu hazır olduğunda, ClientHandler sınıfı bu oyuncuları eşleştirip oyunu başlatır.

*Sunucunun kendisi grafik arayüz içermez, sadece konsoldan çalışır ve istemcilerle ağ iletişimi kurar.
*/
public class Server {

    // Bağlantı bekleyen oyuncular listesi
    private static ArrayList<ClientHandler> waitingPlayers = new ArrayList<>();

    public static void main(String[] args) {
        int port = 1234;  // Sunucunun dinleyeceği port numarası

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server calisiyor, oyuncular bekleniyor...");

            // Sonsuz döngü: Yeni oyuncular bağlandıkça işle
            while (true) {
                Socket clientSocket = serverSocket.accept(); // Yeni bağlantı kabul edilir
                System.out.println("Yeni bir oyuncu baglandi!");

                // Her oyuncu için ayrı bir ClientHandler oluştur ve başlat
                ClientHandler clientHandler = new ClientHandler(clientSocket, waitingPlayers);
                clientHandler.start(); // Thread başlatılır
            }

        } catch (IOException e) {
            System.out.println("Server hatasi: " + e.getMessage());
        }
    }
}
   