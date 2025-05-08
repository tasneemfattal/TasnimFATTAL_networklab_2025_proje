
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
public class ClientHandler extends Thread {

    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private String playerName;
    private ArrayList<ClientHandler> waitingList; // Bekleyen oyuncular listesi
    private GameSession gameSession; // Her client kendi GameSession'ını tutar
    private boolean wantsReplay = false;
    private static int replayVotes = 0;
    private static int replayYesVotes = 0;

    public ClientHandler(Socket socket, ArrayList<ClientHandler> waitingList) {
        this.clientSocket = socket;
        this.waitingList = waitingList;
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

                synchronized (waitingList) {
                    waitingList.add(this);
                    if (waitingList.size() >= 2) {
                        ClientHandler player1 = waitingList.remove(0);
                        ClientHandler player2 = waitingList.remove(0);
                        GameSession session = new GameSession(player1, player2);
                        session.startGame();
                    } else {
                        sendMessage("Rakip bekleniyor...");
                    }
                }
            }

            while (true) {
                String input = in.readLine();
                if (input == null) {
                    break;
                }

                System.out.println(playerName + ": " + input);

                if (input.startsWith("ROLL")) {
                    if (gameSession != null) {
                        gameSession.handleRoll(this);
                    }
                } else if (input.equals("REPLAY_YES")) {
                    wantsReplay = true;
                    synchronized (ClientHandler.class) {
                        replayVotes++;
                        replayYesVotes++;
                        if (replayVotes == 2) {
                            if (replayYesVotes == 2) {
                                gameSession.restartGame();
                            } else {
                                gameSession.sendMessageToBoth("Yeni oyun başlatılamadı. Bir oyuncu reddetti.");
                            }
                            replayVotes = 0;
                            replayYesVotes = 0;
                        }
                    }
                } else if (input.equals("REPLAY_NO")) {
                    synchronized (ClientHandler.class) {
                        replayVotes++;
                        if (replayVotes == 1) {
                            gameSession.sendMessageToBoth("*** Oyun sonlandı. Bir oyuncu tekrar oynamak istemedi. ***");
                            gameSession.sendMessageToBoth("GAME_OVER");
                            replayVotes = 0;
                            replayYesVotes = 0;
                        }
                    }
                }
            }

            clientSocket.close();
            System.out.println("Oyuncu bağlantıyı kapattı: " + playerName);

        } catch (IOException e) {
            System.out.println("Hata (ClientHandler): " + e.getMessage());
        }
    }

    /*public void run() {
        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String message = in.readLine();
            if (message != null && message.startsWith("JOIN")) {
                playerName = message.substring(5);
                System.out.println("Oyuncu katıldı: " + playerName);

                synchronized (waitingList) {
                    waitingList.add(this);
                    if (waitingList.size() >= 2) {
                        ClientHandler player1 = waitingList.remove(0);
                        ClientHandler player2 = waitingList.remove(0);
                        GameSession session = new GameSession(player1, player2);
                        session.startGame();
                    } else {
                        sendMessage("Rakip bekleniyor...");
                    }
                }
            }

            while (true) {
                String input = in.readLine();
                if (input == null) {
                    break;
                }

                System.out.println(playerName + ": " + input);

                if (input.startsWith("ROLL")) {
                    if (gameSession != null) {
                        gameSession.handleRoll(this);
                    }
                } else if (input.startsWith("RESTART")) {
                    if (gameSession != null) {
                        gameSession.restartGame();
                    }
                }
            }

            clientSocket.close();
            System.out.println("Oyuncu bağlantıyı kapattı: " + playerName);

        } catch (IOException e) {
            System.out.println("Hata (ClientHandler): " + e.getMessage());
        }
    }*/
    public void sendMessage(String message) {
        out.println(message);
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setGameSession(GameSession session) {
        this.gameSession = session;
    }
}

/* private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private String playerName;
    private ArrayList<ClientHandler> clientHandlers;
    
    
    

    public static int currentPlayerIndex = 0;

    // Her oyuncunun pozisyonu:
    private static int[] playerPositions = new int[2];  // [0] → oyuncu 1, [1] → oyuncu 2

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
                    Server.sendPlayerNames();
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
                    if (myIndex == currentPlayerIndex) {
                        int diceResult = (int) (Math.random() * 6) + 1;
                        int eskiPozisyon = playerPositions[myIndex];
                        int yeniPozisyon = eskiPozisyon + diceResult;
                        if (yeniPozisyon > 100) {
                            yeniPozisyon = 100;
                        }
                        playerPositions[myIndex] = yeniPozisyon;

                        String rollMessage = "ROLL_RESULT " + playerName + " " + diceResult + " " + yeniPozisyon;
                        Server.sendMessageToAll(rollMessage);

                        currentPlayerIndex = (currentPlayerIndex + 1) % clientHandlers.size();
                        String turnMessage = "SIRA " + clientHandlers.get(currentPlayerIndex).playerName;
                        Server.sendMessageToAll(turnMessage);
                    } else {
                        out.println("Sıra sende değil! Lütfen bekle.");
                    }
                } // ️ BURASI oyuncu oyun bitince "RESTART" mesajını yollar → server pozisyonları sıfırlar → clientlara bildirir.
                else if (input.startsWith("RESTART")) {
                    resetPositions();                          // Pozisyonları sıfırla
                    Server.sendMessageToAll("RESET");          // Tüm clientlara RESET mesajı gönder
                    Server.sendPlayerNames();                  // Oyuncu isimlerini tekrar yolla
                }
            }

            clientSocket.close();
            System.out.println("Oyuncu bağlantıyı kapattı: " + playerName);

        } catch (IOException e) {
            System.out.println("Hata (ClientHandler): " + e.getMessage());
        }
    }

    public void sendMessage(String message) {
        out.println(message);
    }

    public String getPlayerName() {
        return playerName;
    }
//reset

    public static void resetPositions() {
        playerPositions[0] = 0;
        playerPositions[1] = 0;
        currentPlayerIndex = 0;
        System.out.println("Pozisyonlar sıfırlandı, oyun yeniden başlıyor!");
    }

}*/

 /*private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    String playerName;
    private ArrayList<ClientHandler> clientHandlers;

    public static int currentPlayerIndex = 0;

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

                //  İki oyuncu bağlandıysa isimleri herkese gönder:
                if (clientHandlers.size() == 2) {
                    String p1 = clientHandlers.get(0).playerName;
                    String p2 = clientHandlers.get(1).playerName;

                    for (ClientHandler client : clientHandlers) {
                        client.out.println("PLAYER_NAMES " + p1 + " " + p2);
                    }

                    sendMessageToAll("İki oyuncu hazır! Oyun başlıyor!");
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
                    if (myIndex == currentPlayerIndex) {
                        int diceResult = (int) (Math.random() * 6) + 1;
                        String rollMessage = "Oyuncu " + playerName + " zar attı: " + diceResult;
                        sendMessageToAll(rollMessage);

                        // Sırayı değiştir:
                        currentPlayerIndex = (currentPlayerIndex + 1) % clientHandlers.size();
                        String turnMessage = "SIRA " + clientHandlers.get(currentPlayerIndex).playerName;
                        sendMessageToAll(turnMessage);
                    } else {
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

    public String getPlayerName() {
        return playerName;
    }

    public void sendMessage(String message) {
        out.println(message);
    }

}*/
