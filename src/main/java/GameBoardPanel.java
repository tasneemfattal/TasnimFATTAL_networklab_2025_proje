
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author tasni
 */


/**
 * Bu sınıf, oyun tahtasını çizen özel bir JPanel'dir.
 * Oyuncu taşlarını pozisyonlarına göre yerleştirir.
 * Her kare için (1-100) grafiksel koordinatlar hesaplanır.
 */
public class GameBoardPanel extends JPanel {

    // Oyuncuların tahtadaki pozisyonları
    private int player1Position = 0;  // 0 → henüz oyuna başlamadı
    private int player2Position = 0;

    // Oyuncu adları GUI'den dinamik olarak alınır
    private String player1Name = "";
    private String player2Name = "";

    // 10x10 tahta üzerindeki her kutunun merkez koordinatı
    private Point[][] boardMatrix;

    /**
     * Oyuncu isimlerini belirleyen metot
     */
    public void setPlayerNames(String name1, String name2) {
        this.player1Name = name1;
        this.player2Name = name2;
        System.out.println("setPlayerNames çağrıldı → Player1: [" + player1Name + "], Player2: [" + player2Name + "]");
        repaint();  // yeniden çiz
    }

    // Getter'lar (isim ve pozisyon almak için)
    public String getPlayer1Name() { return player1Name; }
    public String getPlayer2Name() { return player2Name; }
    public int getPlayer1Position() { return player1Position; }
    public int getPlayer2Position() { return player2Position; }

    /**
     * Oyuncu taşlarının pozisyonunu günceller
     */
    public void updatePlayerPosition(String playerName, int newPosition) {
        System.out.println("Gelen isim: [" + playerName + "]");
        System.out.println("Player1 name: [" + player1Name + "]");
        System.out.println("Player2 name: [" + player2Name + "]");

        if (playerName.equalsIgnoreCase(player1Name)) {
            player1Position = newPosition;
        } else if (playerName.equalsIgnoreCase(player2Name)) {
            player2Position = newPosition;
        } else {
            System.out.println("? Sunun icin eslesen oyuncu bulunamadi: " + playerName);
        }

        repaint();  // yeniden çizdir
    }

    /**
     * Tahta çizimi ve oyuncu taşlarının ekranda gösterimi
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // İlk kez çiziliyorsa matrix'i oluştur
        if (boardMatrix == null) {
            boardMatrix = BoardUtils.generateBoardMatrix(55, 24, 22);  // kutu boyutu ve konum offsetleri
        }

        // Oyuncu taşlarını çiz
        drawPlayer(g, player1Position, Color.GREEN, -8);  // oyuncu 1 hafif yukarıda
        drawPlayer(g, player2Position, Color.BLUE, +2);   // oyuncu 2 hafif aşağıda
    }

    /**
     * Verilen pozisyondaki taşın tahtada çizimini yapar
     *
     * @param g         Graphics nesnesi
     * @param position  1–100 arası pozisyon
     * @param color     Taşın rengi
     * @param yOffset   Taşların üst üste binmesini önlemek için dikey ofset
     */
    private void drawPlayer(Graphics g, int position, Color color, int yOffset) {
        if (position <= 0 || position > 100 || boardMatrix == null) return;

        // Pozisyona göre satır/sütun hesapla (zigzag mantığı)
        int index = position - 1;
        int row = index / 10;
        int col = index % 10;

        int actualRow = 9 - row;
        int actualCol = (row % 2 == 0) ? col : 9 - col;

        Point p = boardMatrix[actualRow][actualCol];

        int playerSize = 20;  // taş boyutu
        int x = p.x - playerSize / 2;
        int y = p.y - playerSize / 2 + yOffset;

        // Taşı çiz
        g.setColor(color);
        g.fillOval(x, y, playerSize, playerSize);
        g.setColor(Color.BLACK);
        g.drawOval(x, y, playerSize, playerSize);
    }
}