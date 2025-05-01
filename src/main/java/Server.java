
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
  private static ArrayList<ClientHandler> waitingPlayers = new ArrayList<>(); // Bekleyen oyuncular

    public static void main(String[] args) {
        int port = 1234;

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server çalışıyor, oyuncular bekleniyor...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Yeni bir oyuncu bağlandı!");

                ClientHandler clientHandler = new ClientHandler(clientSocket, waitingPlayers);
                clientHandler.start();
            }

        } catch (IOException e) {
            System.out.println("Server hatası: " + e.getMessage());
        }
    }
}
   /* private static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();

    public static void main(String[] args) {
        int port = 1234;

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server calısıyor, oyuncular bekleniyor...");

            while (true) {
                Socket clientSocket = serverSocket.accept();

                if (clientHandlers.size() >= 2) {
                    System.out.println("Baglanmaya calısan fazla bir oyuncu var, bağlantı reddedildi.");
                    PrintWriter tempOut = new PrintWriter(clientSocket.getOutputStream(), true);
                    tempOut.println("Oyun dolu! Bağlantı kapatılıyor.");
                    clientSocket.close();
                    continue;
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

    // Tüm clientlara mesaj gönder
    public static void sendMessageToAll(String message) {
        for (ClientHandler client : clientHandlers) {
            client.sendMessage(message);
        }
    }

    // Oyuncu isimlerini iki kişi bağlanınca gönder
    public static void sendPlayerNames() {
        if (clientHandlers.size() == 2) {  // İki oyuncu varsa isimleri yolla
            String player1Name = clientHandlers.get(0).getPlayerName().trim();
            String player2Name = clientHandlers.get(1).getPlayerName().trim();
            String namesMessage = "PLAYER_NAMES " + player1Name + " " + player2Name;

            System.out.println("GÖNDERİLEN Player1: [" + player1Name + "]");
            System.out.println("GÖNDERİLEN Player2: [" + player2Name + "]");

            for (ClientHandler handler : clientHandlers) {
                handler.sendMessage(namesMessage);
            }

            sendMessageToAll("İki oyuncu hazır! Oyun başlıyor!");
            String turnMessage = "SIRA " + clientHandlers.get(ClientHandler.currentPlayerIndex).getPlayerName();
            sendMessageToAll(turnMessage);
        }
    }
}*/

/* private static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();

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
    }*/
