
import java.awt.Color;
import java.awt.Graphics;
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

    private int rows = 10;  // 10 satır
    private int cols = 10;  // 10 sütun (100 kare)

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int cellWidth = getWidth() / cols;
        int cellHeight = getHeight() / rows;

        int number = 100;
        boolean leftToRight = true;

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                int actualCol = leftToRight ? col : (cols - 1 - col);
                int x = actualCol * cellWidth;
                int y = row * cellHeight;

                // Kutu çiz
                g.setColor(Color.BLACK);
                g.drawRect(x, y, cellWidth, cellHeight);

                // Kutu numarası
                g.drawString(String.valueOf(number), x + cellWidth / 2 - 5, y + cellHeight / 2);
                number--;
            }
            leftToRight = !leftToRight;  // Zigzag numaralama
        }

        //  Kenar çizgileri (ekstra çizgiler)
        g.drawLine(getWidth() - 1, 0, getWidth() - 1, getHeight()); // sağ dikey çizgi
        g.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1); // alt yatay çizgi
    }
}
