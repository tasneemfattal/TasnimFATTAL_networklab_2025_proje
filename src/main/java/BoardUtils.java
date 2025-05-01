
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

}
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
     