
import java.awt.Point;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author tasni
 */
public class BoardUtils {

    public static Point[][] generateBoardMatrix(int cellSize, int offsetX, int offsetY) {
        Point[][] matrix = new Point[10][10];

        for (int gamePos = 1; gamePos <= 100; gamePos++) {
            int index = gamePos - 1;
            int row = index / 10;  // sıra numarası (0–9)
            int col = index % 10;  // sütun numarası (0–9)

            // Alt satırdan başlamak için:
            int actualRow = 9 - row;

            // Zigzag yönü: satır çiftse soldan sağa, tekse sağdan sola
            int actualCol = (row % 2 == 0) ? col : 9 - col;

            int x = offsetX + actualCol * cellSize + cellSize / 2;
            int y = offsetY + actualRow * cellSize + cellSize / 2;

            matrix[actualRow][actualCol] = new Point(x, y);
        }

        return matrix;
    }

}

/*public static Point[][] generateBoardMatrix(int cellSize, int offsetX, int offsetY) {
        Point[][] matrix = new Point[10][10];

        for (int row = 0; row < 10; row++) {
            boolean leftToRight = (row % 2 == 0); // Alt satırdan başlayacak
            for (int col = 0; col < 10; col++) {
                int x = leftToRight ? offsetX + col * cellSize
                        : offsetX + (9 - col) * cellSize;
                int y = offsetY + (9 - row) * cellSize;
                matrix[row][col] = new Point(x, y);
            }
        }

        return matrix;
    }

}*/

 /*public static Point[][] generateBoardMatrix(int cellSize, int offsetX, int offsetY) {
    Point[][] matrix = new Point[10][10];

    for (int row = 0; row < 10; row++) {
        boolean leftToRight = (row % 2 == 0); // Alt satırdan başlayacak
        for (int col = 0; col < 10; col++) {
            int x = leftToRight ? offsetX + col * cellSize
                                : offsetX + (9 - col) * cellSize;
            int y = offsetY + (9 - row) * cellSize;
            matrix[row][col] = new Point(x, y);
        }
    }

    return matrix;
}*/
 /* public static Point[][] generateBoardMatrix() {
    Point[][] matrix = new Point[10][10];

    int cellSize = 60;        // Her kare 60x60 px
    int offsetX = 4;          // Görselin sol kenar boşluğu (gözle test ederek ayarlandı)
    int offsetY = 7;          // Görselin üst kenar boşluğu (gözle test ederek ayarlandı)

     for (int row = 0; row < 10; row++) {
        int gameRow = 9 - row; // Alt satırdan başla

        boolean leftToRight = (gameRow % 2 == 0);  // Zigzag: 1-10, 11-20, ...

        for (int col = 0; col < 10; col++) {
            int x = leftToRight
                    ? offsetX + col * cellSize + cellSize / 2
                    : offsetX + (9 - col) * cellSize + cellSize / 2;

            int y = offsetY + row * cellSize + cellSize / 2; // row sabit kaldı

            matrix[gameRow][col] = new Point(x, y);  // dikkat: gameRow kullanılıyor
        }
    }

    return matrix;
}*/

 /* public static Point[][] generateBoardMatrix(int startX, int startY, int cellSize) {
        Point[][] matrix = new Point[10][10];
        boolean leftToRight = true;
        //int number = 1;

        for (int row = 9; row >= 0; row--) {
            
            for (int col = 0; col < 10; col++) {
                int actualCol = leftToRight ? col : 9 - col;
                int x = startX + actualCol * cellSize;
                int y = startY + (9 - row) * cellSize;
                matrix[row][actualCol] = new Point(x, y);
               // number++;
            }
            leftToRight = !leftToRight;
        }
        return matrix;
    }
}*/
