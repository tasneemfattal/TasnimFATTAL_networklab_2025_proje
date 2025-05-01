/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author tasni
 */



/*İki oyuncuyu eşleştiriyor.

İsimleri ve oyun başlangıcını gönderiyor.

Zar atıldığında sonucu işliyor ve güncelliyor.

Sıra yönetimi yapıyor.

Kazanma durumunda yeni oyun başlatabiliyor.*/
public class GameSession {
     private ClientHandler player1;
    private ClientHandler player2;
    private int currentPlayerIndex = 0; // 0: player1, 1: player2
    private int[] playerPositions = {0, 0};  // Oyuncuların pozisyonları

    public GameSession(ClientHandler player1, ClientHandler player2) {
        this.player1 = player1;
        this.player2 = player2;

        // Bu GameSession'u her iki client'a da haber verelim:
        player1.setGameSession(this);
        player2.setGameSession(this);
    }

    public void startGame() {
        // Oyuncu isimlerini gönder
        String namesMessage = "PLAYER_NAMES " + player1.getPlayerName() + " " + player2.getPlayerName();
        player1.sendMessage(namesMessage);
        player2.sendMessage(namesMessage);

        // Oyun başladığını söyle
        sendMessageToBoth("İki oyuncu hazır! Oyun başlıyor!");

        // İlk kimin sırada olduğunu söyle
        sendTurnInfo();
    }

    public void handleRoll(ClientHandler roller) {
        int playerIndex = (roller == player1) ? 0 : 1;

        if (playerIndex != currentPlayerIndex) {
            roller.sendMessage("Sıra sende değil! Lütfen bekle.");
            return;
        }

        int diceResult = (int) (Math.random() * 6) + 1;
        int oldPos = playerPositions[playerIndex];
        int newPos = oldPos + diceResult;
        if (newPos > 100) {
            newPos = 100;
        }
        playerPositions[playerIndex] = newPos;

        // Sonucu her iki oyuncuya gönder
        String rollMessage = "ROLL_RESULT " + roller.getPlayerName() + " " + diceResult + " " + newPos;
        sendMessageToBoth(rollMessage);

        // Oyuncu kazandı mı kontrol et
        if (newPos == 100) {
            sendMessageToBoth("*** " + roller.getPlayerName() + " kazandı! ***");
            restartGame();  // İstersen burada yeni oyun başlatabilirsin
        } else {
            // Sıra değiştir
            currentPlayerIndex = (currentPlayerIndex + 1) % 2;
            sendTurnInfo();
        }
    }

    public void restartGame() {
        playerPositions[0] = 0;
        playerPositions[1] = 0;
        currentPlayerIndex = 0;
        sendMessageToBoth("RESET");
        sendTurnInfo();
    }

    private void sendMessageToBoth(String message) {
        player1.sendMessage(message);
        player2.sendMessage(message);
    }

    private void sendTurnInfo() {
        String currentPlayerName = (currentPlayerIndex == 0) ? player1.getPlayerName() : player2.getPlayerName();
        sendMessageToBoth("SIRA " + currentPlayerName);
    }
}
