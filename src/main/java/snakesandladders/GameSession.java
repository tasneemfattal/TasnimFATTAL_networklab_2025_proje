 package snakesandladders;

import java.util.Map;

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

yılanlar ve merdivenlere kontrol ediyor

Kazanma durumunda yeni oyun başlatabiliyor.*/
public class GameSession {

    private ClientHandler player1;
    private ClientHandler player2;
    private int currentPlayerIndex = 0; // 0: player1, 1: player2
    private int[] playerPositions = {0, 0};  // Oyuncuların pozisyonları

    private boolean isReplayConfirmed = true;  // ilk oyun başladığında true, tekrar oyunlarda kontrol ederiz

    // Merdivenler (başlangıç → çıkış)
    private static final Map<Integer, Integer> ladders = Map.of(
            1, 38,
            4, 14,
            8, 30,
            21, 42,
            28, 76,
            50, 67,
            71, 92,
            80, 99
    );

    //  Yılanlar (başlangıç → kuyruk)
    private static final Map<Integer, Integer> snakes = Map.of(
            32, 10,
            36, 6,
            48, 26,
            62, 18,
            88, 24,
            95, 56,
            97, 78
    );

    public GameSession(ClientHandler player1, ClientHandler player2) {
        this.player1 = player1;
        this.player2 = player2;

        // Her oyuncuya bu GameSession'u bildir
        player1.setGameSession(this);
        player2.setGameSession(this);
    }

    public void startGame() {
        // Oyuncu isimlerini gönder
        String namesMessage = "PLAYER_NAMES " + player1.getPlayerName() + " " + player2.getPlayerName();
        player1.sendMessage(namesMessage);
        player2.sendMessage(namesMessage);

        // Oyunun başladığını bildir
        sendMessageToBoth("iki oyuncu hazir! Oyun basliyor!");

        // İlk sıradaki oyuncuyu bildir
        sendTurnInfo();
    }

    public void handleRoll(ClientHandler roller) {
        if (!isReplayConfirmed) {
            roller.sendMessage("Diğer oyuncu tekrar oynamayı onaylamadı. Lütfen bekleyin...");
            return;
        }

        int playerIndex = (roller == player1) ? 0 : 1;

        if (playerIndex != currentPlayerIndex) {
            roller.sendMessage("Sıra sende değil! Lütfen bekle.");
            return;
        }

        int diceResult = (int) (Math.random() * 6) + 1;
        int oldPos = playerPositions[playerIndex];
        int newPos = oldPos + diceResult;

        if (newPos > 100) {
            newPos = oldPos; // Fazla gidemez
        }

        // 🪜 Merdiven kontrolü
        if (ladders.containsKey(newPos)) {
            int ladderEnd = ladders.get(newPos);
            System.out.println("MERDIVEN: " + newPos + " -> " + ladderEnd);
            newPos = ladderEnd;
        } // 🐍 Yılan kontrolü
        else if (snakes.containsKey(newPos)) {
            int snakeTail = snakes.get(newPos);
            System.out.println("YILAN: " + newPos + " -> " + snakeTail);
            newPos = snakeTail;
        }

        playerPositions[playerIndex] = newPos;
        String rollMessage = "ROLL_RESULT " + roller.getPlayerName() + " " + diceResult + " " + oldPos + " " + newPos;
        sendMessageToBoth(rollMessage);
        // Sunucu konsoluna log yaz
        System.out.println(" " + roller.getPlayerName() + " zar attı: " + diceResult
                + " | Eski pozisyon: " + oldPos + " -> Yeni pozisyon: " + newPos);

        if (newPos == 100) {
            System.out.println("Oyun bitti! Kazanan oyuncu: " + roller.getPlayerName());
            sendMessageToBoth("*** " + roller.getPlayerName() + " kazandı! ***");
            restartGame();
        } else {
            currentPlayerIndex = (currentPlayerIndex + 1) % 2;
            sendTurnInfo();
        }
    }

    /* -Her iki oyuncunun tahtadaki yeri temizlenir
    -Sıra başa döner (player1 başlar)
    -Her iki client GUI’sine tahtayı sıfırlama komutu gider
    -GUI’de sadece sırası gelen oyuncunun zar butonu açılır
     */

    public void restartGame() {
        playerPositions[0] = 0;
        playerPositions[1] = 0;
        currentPlayerIndex = 0;
        isReplayConfirmed = true; // Yeni oyun başlamaya hazır demektir

        sendMessageToBoth("RESET");
        sendTurnInfo();
    }

    void sendMessageToBoth(String message) {
        player1.sendMessage(message);
        player2.sendMessage(message);
    }

    private void sendTurnInfo() {
        String currentPlayerName = (currentPlayerIndex == 0) ? player1.getPlayerName() : player2.getPlayerName();
        sendMessageToBoth("SIRA " + currentPlayerName);
    }

    public boolean isReplayConfirmed() {
        return isReplayConfirmed;
    }

    public void setReplayConfirmed(boolean confirmed) {
        this.isReplayConfirmed = confirmed;
    }

}
