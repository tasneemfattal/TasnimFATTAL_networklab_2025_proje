package snakesandladders;


import java.awt.Point;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author tasni
 */




/**
 * Bu sınıf, oyun tahtası üzerindeki 100 kutunun her birinin piksel konumlarını hesaplar.
 * Tahtayı 10x10'luk bir matris olarak düzenler.
 * Zigzag sıralı bir yapı oluşturur: 
 * Alt satırdan başlar, 1 → 2 → ... → 10, sonra 11 ← 12 ← ... ← 20, vb.
 */
public class BoardUtils {

    /**
     * Oyun tahtası için pozisyon koordinatlarını (x, y) döner.
     * Her hücre ortasına göre hizalanır.
     *
     * @param cellSize Her kutunun kenar uzunluğu (piksel)
     * @param offsetX   X yönünde tahta kenar boşluğu
     * @param offsetY   Y yönünde tahta kenar boşluğu
     * @return 10x10'luk pozisyon matrisini döner
     */
    public static Point[][] generateBoardMatrix(int cellSize, int offsetX, int offsetY) {
        Point[][] matrix = new Point[10][10];  // 10x10 tahta oluştur

        // 1'den 100'e kadar tüm kutular için koordinat hesapla
        for (int gamePos = 1; gamePos <= 100; gamePos++) {
            int index = gamePos - 1;

            int row = index / 10;  // satır (0–9)
            int col = index % 10;  // sütun (0–9)

            // Görseldeki gibi en alttan başlatmak için satır ters çevrilir
            int actualRow = 9 - row;

            // Zigzag sistemi: tek sıra sağdan sola
            int actualCol = (row % 2 == 0) ? col : 9 - col;

            // Hücrenin tam ortası hedeflenir
            int x = offsetX + actualCol * cellSize + cellSize / 2;
            int y = offsetY + actualRow * cellSize + cellSize / 2;

            matrix[actualRow][actualCol] = new Point(x, y);
        }

        return matrix;
    }
}


