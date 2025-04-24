
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
public class Server {
     private static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();

    public static void main(String[] args) {
        int port = 1234;

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server çalışıyor, oyuncular bekleniyor...");

            while (true) {
                Socket clientSocket = serverSocket.accept();

                // ÖNCE KONTROL EDECEĞİZ (EKLEMEDEN ÖNCE!)
                if (clientHandlers.size() >= 2) {
                    System.out.println("Bağlanmaya çalışan fazla bir oyuncu var, bağlantı reddedildi.");
                    PrintWriter tempOut = new PrintWriter(clientSocket.getOutputStream(), true);
                    tempOut.println("Oyun dolu! Bağlantı kapatılıyor.");
                    clientSocket.close();
                    continue; // Bu oyuncuyu kabul etme, sıradakine geç
                }

                System.out.println("Yeni bir oyuncu bağlandı!");
                ClientHandler clientHandler = new ClientHandler(clientSocket, clientHandlers);
                clientHandlers.add(clientHandler);
                clientHandler.start();
            }

        } catch (IOException e) {
            System.out.println("Server hatası: " + e.getMessage());
        }
    }
  /* public static void main(String[] args) {
        int port = 1234;
        ArrayList<Socket> clientSockets = new ArrayList<>();
        ArrayList<String> playerNames = new ArrayList<>();

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server çalışıyor, oyuncular bekleniyor...");

            while (clientSockets.size() < 2) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Bir oyuncu bağlandı!");

                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

                String message = in.readLine();
                if (message.startsWith("JOIN")) {
                    String playerName = message.substring(5); // "JOIN " den sonrası
                    playerNames.add(playerName);
                    clientSockets.add(clientSocket);

                    System.out.println("Oyuncu katıldı: " + playerName);
                    out.println("Hoş geldin " + playerName + ", lütfen bekle...");
                }
            }

            // İki oyuncu bağlandıktan sonra:
            System.out.println("İki oyuncu hazır! Oyun başlıyor!");
            for (Socket clientSocket : clientSockets) {
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                out.println("İki oyuncu hazır! Oyun başlıyor!");
            }

        } catch (IOException e) {
            System.out.println("Hata: " + e.getMessage());
        }
    }*/
}
