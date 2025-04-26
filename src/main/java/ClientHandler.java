
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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
public class ClientHandler extends Thread {

    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private String playerName;
    private ArrayList<ClientHandler> clientHandlers;

    public static int currentPlayerIndex = 0;// Şu an hangi oyuncunun sırası

    public ClientHandler(Socket socket, ArrayList<ClientHandler> clientHandlers) {
        this.clientSocket = socket;
        this.clientHandlers = clientHandlers;
    }

    @Override
    public void run() {
        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String message = in.readLine();
            if (message != null && message.startsWith("JOIN")) {
                playerName = message.substring(5);
                System.out.println("Oyuncu katıldı: " + playerName);

                if (clientHandlers.size() > 2) {
                    out.println("Oyun dolu! Bağlantı kapatılıyor.");
                    clientSocket.close();
                    return;
                } else {
                    out.println("Hoş geldin " + playerName + ", lütfen bekle...");
                }

                if (clientHandlers.size() == 2) {
                    sendMessageToAll("İki oyuncu hazır! Oyun başlıyor!");
                    // İlk başlayan oyuncuyu bildiriyoruz:
                    String turnMessage = "SIRA " + clientHandlers.get(currentPlayerIndex).playerName;
                    sendMessageToAll(turnMessage);
                }
            }

            while (true) {
                String input = in.readLine();
                if (input == null) {
                    break;
                }

                System.out.println(playerName + ": " + input);

                if (input.startsWith("ROLL")) {
                    int myIndex = clientHandlers.indexOf(this);

                    // SIRA KONTROLÜ
                    if (myIndex == currentPlayerIndex) {
                        int diceResult = (int) (Math.random() * 6) + 1;
                        String rollMessage = "Oyuncu " + playerName + " zar attı: " + diceResult;
                        sendMessageToAll(rollMessage);

                        // Sıra diğer oyuncuya geçiyor
                        currentPlayerIndex = (currentPlayerIndex + 1) % clientHandlers.size();
                        String turnMessage = "SIRA " + clientHandlers.get(currentPlayerIndex).playerName;
                        sendMessageToAll(turnMessage);

                    } else {
                        // Sıra beklenmiyorsa:
                        out.println("Sıra sende değil! Lütfen bekle.");
                    }
                }
            }

            clientSocket.close();
            System.out.println("Oyuncu bağlantıyı kapattı: " + playerName);

        } catch (IOException e) {
            System.out.println("Hata (ClientHandler): " + e.getMessage());
        }
    }

    private void sendMessageToAll(String message) {
        for (ClientHandler client : clientHandlers) {
            client.out.println(message);
        }
    }

}
