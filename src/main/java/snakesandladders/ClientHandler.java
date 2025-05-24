package snakesandladders;

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
/*Oyuncu bağlanınca adını alıyor.

Eğer iki kişi olduysa, hemen bir GameSession başlatıyor.

Eğer tek kişi ise "Rakip bekleniyor..." mesajı gönderiyor.

Zar atınca veya restart isteyince GameSession'a yönlendiriyor.

Her oyuncu kendi GameSession nesnesine bağlı.

 */
/**
 * Her bağlanan oyuncu için bir thread (iş parçacığı) başlatır. Oyuncuların
 * gönderdiği mesajlara göre: - Oyuncuyu oyun eşleştirme listesine ekler, - Zar
 * atma, tekrar oynama gibi talepleri işler, - GameSession sınıfıyla iletişim
 * kurarak oyunun mantığını sürdürür. Oyunculardan biri tekrar oynamayı
 * reddederse, diğer oyuncuya haber verip oyunu kapatır.
 */
public class ClientHandler extends Thread {

    private Socket clientSocket; // Oyuncunun bağlantı soketi
    private PrintWriter out;     // Server'dan oyuncuya veri göndermek için yazıcı
    private BufferedReader in;   // Oyuncudan veri almak için okuyucu
    private String playerName;   // Oyuncunun adı

    private ArrayList<ClientHandler> waitingList; // Oyuncu eşleşmesini bekleyenler listesi
    private GameSession gameSession; // Bu oyuncunun dahil olduğu oyun oturumu

    // private boolean  = false; // Oyuncu tekrar oynamak istiyor mu?
    private static int replayVotes = 0;     // Toplam tekrar isteği cevap sayısı
    private static int replayYesVotes = 0;  // Evet diyen oyuncu sayısı

    // Yapıcı metot: ClientHandler nesnesi oluşturulurken soket ve bekleyenler listesi verilir
    public ClientHandler(Socket socket, ArrayList<ClientHandler> waitingList) {
        this.clientSocket = socket;
        this.waitingList = waitingList;
    }

    @Override
    public void run() {
        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            // Oyuncudan ilk gelen mesaj oyuncu adıdır: "JOIN Ahmet"
            String message = in.readLine();
            if (message != null && message.startsWith("JOIN")) {
                playerName = message.substring(5);
                System.out.println("Oyuncu katildi: " + playerName);

                // Oyuncu eşleştirme işlemi yapılır
                synchronized (waitingList) {
                    waitingList.removeIf(handler -> handler.clientSocket.isClosed());  // Bağlantısı kopmuş oyuncular varsa listeden çıkar
                    waitingList.add(this);// ClientHandler nesnesi bekleyen oyuncular listesine eklenir; eşleşme 2 kişi olunca başlar

                    if (waitingList.size() >= 2) {
                        ClientHandler player1 = waitingList.remove(0);
                        ClientHandler player2 = waitingList.remove(0);
                        GameSession session = new GameSession(player1, player2); // Yeni bir oyun başlat
                        session.startGame(); // Oyunu başlat
                    } else {
                        sendMessage("Rakip bekleniyor..."); // Tek oyuncu varsa beklet
                    }
                }
            }

            // Oyuncudan gelen mesajları dinle
            while (true) {
                String input = in.readLine();
                if (input == null) {
                    break; // Oyuncu bağlantıyı kapattı
                }

                System.out.println(playerName + ": " + input);

                if (input.startsWith("ROLL")) {
                    if (gameSession != null) {
                        gameSession.handleRoll(this); // Zar atma işlemini oyun oturumuna yönlendir
                    }

                } else if (input.equals("REPLAY_YES")) {
                    //wantsReplay = true;
                    synchronized (ClientHandler.class) {
                        replayVotes++;
                        replayYesVotes++;

                        // Her iki oyuncu da tekrar oynamak istediyse
                        if (replayVotes == 2) {
                            if (replayYesVotes == 2) {
                                gameSession.setReplayConfirmed(true);   //  İkisi de EVET dediyse oyun başlasın
                                gameSession.restartGame(); // Oyunu baştan başlat
                            } else {
                                gameSession.sendMessageToBoth("Yeni oyun başlatılamadı. Bir oyuncu reddetti.");
                            }

                            // Sayaçları sıfırla
                            replayVotes = 0;
                            replayYesVotes = 0;
                        } else {
                            gameSession.setReplayConfirmed(false); //  Daha sadece 1 kişi evet dedi
                        }
                    }

                } else if (input.equals("REPLAY_NO")) {
                    synchronized (ClientHandler.class) {
                        //replayVotes++;

                        // Bir oyuncu tekrar oynamak istemiyorsa hemen oyunu sonlandır
                        // if (replayVotes == 1) {
                        gameSession.sendMessageToBoth("*** Oyun sonlandı. " + playerName + " tekrar oynamak istemedi. ***");
                        gameSession.sendMessageToBoth("GAME_OVER");

                        replayVotes = 0;
                        replayYesVotes = 0;
                        //}
                    }
                }
            }

        } catch (IOException e) {
            System.out.println("Hata (ClientHandler): " + e.getMessage());
        } finally {
            // Oyuncu çıkınca listeden temizle
            synchronized (waitingList) {
                waitingList.remove(this);
            }
            // Oyuncu bağlantıdan çıkınca diğer oyuncuya haber ver
            if (gameSession != null) {
                gameSession.sendMessageToBoth("Oyuncu " + playerName + " oyunu terk etti. Oyun sonlandırıldı.");
                gameSession.sendMessageToBoth("GAME_OVER");
            }

            try {
                clientSocket.close();
            } catch (IOException ex) {
                System.out.println("Socket kapatma hatasi: " + ex.getMessage());
            }

            System.out.println("Oyuncu baglantiyi kapatti: " + playerName);
        }
    }

    // Bu oyuncuya mesaj gönder
    public void sendMessage(String message) {
        out.println(message);
    }

    // Oyuncu ismini getir
    public String getPlayerName() {
        return playerName;
    }

    // Bu client’e bağlı olan oyun oturumunu ayarla
    public void setGameSession(GameSession session) {
        this.gameSession = session;
    }
}
