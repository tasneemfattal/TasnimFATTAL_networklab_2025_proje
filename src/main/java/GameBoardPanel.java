
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
public class GameBoardPanel extends JPanel {

    // private int rows = 10;  // 10 satır
    //private int cols = 10;  // 10 sütun (100 kare)
    private int player1Position = 0;  // 0 = henüz tahtada değil
    private int player2Position = 0;

    private String player1Name = "";  // Dinamik oyuncu 1 ismi
    private String player2Name = "";  // Dinamik oyuncu 2 ismi

    //private Point[] cellCoordinates;
    private Point[][] boardMatrix;  // 10x10 tahta koordinatları

    public void setPlayerNames(String name1, String name2) {
        this.player1Name = name1;
        this.player2Name = name2;
        System.out.println("setPlayerNames çağrıldı → Player1: [" + player1Name + "], Player2: [" + player2Name + "]");
        repaint();
    }

    // Getter'lar (isimleri almak için):
    public String getPlayer1Name() {
        return player1Name;
    }

    public String getPlayer2Name() {
        return player2Name;
    }

    public int getPlayer1Position() {
        return player1Position;
    }

    public int getPlayer2Position() {
        return player2Position;
    }

    //  Oyuncuların pozisyonunu güncelle:
    public void updatePlayerPosition(String playerName, int newPosition) {
        System.out.println("Gelen isim: [" + playerName + "]");
        System.out.println("Player1 name: [" + player1Name + "]");
        System.out.println("Player2 name: [" + player2Name + "]");

        if (playerName.equalsIgnoreCase(player1Name)) {
            player1Position = newPosition;
        } else if (playerName.equalsIgnoreCase(player2Name)) {
            player2Position = newPosition;
        } else {
            System.out.println("? No matching player found for: " + playerName);
        }
        repaint();
    }

    //  Tahta çizimi:
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Sadece bir kez oluşturulmalı
        if (boardMatrix == null) {
             //boardMatrix = generateBoardMatrix(55, 20, 20);  // hücre boyutu ve başlangıç offsetleri
            boardMatrix = BoardUtils.generateBoardMatrix(55, 30, 50);// X, Y, kutu boyutu
        }

        
        // Oyuncu taşlarını çiz
        drawPlayer(g, player1Position, Color.RED, -8);
        drawPlayer(g, player2Position, Color.BLUE, +8);
    }
    private void drawPlayer(Graphics g, int position, Color color, int yOffset) {
        if (position <= 0 || position > 100) return;

        int index = position - 1;
        int row = index / 10;
        int col = index % 10;

        Point p = boardMatrix[row][col];
        int size = 20;
        int x = p.x - size / 2;
        int y = p.y - size / 2 + yOffset;
         //int x = p.x+10;
        //int y = p.y+ yOffset;

        g.setColor(color);
        g.fillOval(x, y, size, size);
        g.setColor(Color.BLACK);
        g.drawOval(x, y, size, size);
        //System.out.println("x:"+x+" y:"+y);
    }

    /*private Point[][] generateBoardMatrix(int cellSize, int offsetX, int offsetY) {
        Point[][] matrix = new Point[10][10];

        for (int row = 0; row < 10; row++) {
            boolean leftToRight = (row % 2 == 0);
            for (int col = 0; col < 10; col++) {
                int x = leftToRight ? offsetX + col * cellSize
                                    : offsetX + (9 - col) * cellSize;
                int y = offsetY + (9 - row) * cellSize;
                matrix[row][col] = new Point(x, y);
            }
        }

        return matrix;
    }*/
}

    /*private void drawPlayer(Graphics g, int position, Color color, int yOffset) {
        if (position <= 0 || position > 100 || boardMatrix == null) {
            return;
        }

        int index = position - 1;
        int row = index / 10;
        int col = index % 10;
        if ((9 - row) % 2 == 1) { // satır sağdan sola gidiyorsa
            col = 9 - col;
        }

        Point p = boardMatrix[9 - row][col]; // Yukarıdan aşağıya indeks düzelt
        int playerSize = 20;
        int x = p.x - playerSize / 2;
        int y = p.y - playerSize / 2 + yOffset;

        g.setColor(color);
        g.fillOval(x, y, playerSize, playerSize);
        g.setColor(Color.BLACK);
        g.drawOval(x, y, playerSize, playerSize);
    }
}*/
   

