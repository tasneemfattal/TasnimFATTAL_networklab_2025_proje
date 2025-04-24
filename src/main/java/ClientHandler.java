
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

                // ❗ Eğer 2 kişiden fazla bağlanırsa:
                if (clientHandlers.size() > 2) {
                    out.println("Oyun dolu! Bağlantı kapatılıyor.");
                    clientSocket.close();
                    return; // Bu client'ı sonlandır
                } else {
                    out.println("Hoş geldin " + playerName + ", lütfen bekle...");
                }

                //  İki oyuncu bağlandığında oyun başlasın:
                if (clientHandlers.size() == 2) {
                    sendMessageToAll("İki oyuncu hazır! Oyun başlıyor!");
                }
            }

            //  Oyuncuların diğer mesajlarını burada okuyabilirsin (zar atma vs.)
            while (true) {
                String input = in.readLine();
                if (input == null) break; // Client çıkarsa döngüden çıkar
                System.out.println(playerName + ": " + input);
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
