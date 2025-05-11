
import java.awt.Image;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
/**
 *
 * @author tasni
 */

/*
*GameScreen sınıfı, bir oyuncunun oyuna bağlandıktan sonra gördüğü asıl oyun ekranıdır. Burada:

-Oyunun görsel tahtası (board.png) gösterilir.

-Oyuncular zar atabilir.

-Kimin sırası olduğu, zar sonucu ve pozisyon değişimleri gösterilir.

-Kazanan tespit edilince kullanıcıya bildirilir ve tekrar oynamak isteyip istemediği sorulur.

-Tahta üzerindeki piyonlar (taşlar) doğru pozisyonlara çizilir.
*/
public class GameScreen extends javax.swing.JFrame {

    private PrintWriter out;
    private String playerName;
    private BufferedReader in;
    private GameBoardPanel boardPanel;

    /**
     * Creates new form GameScreen
     */
    public GameScreen(String playerName, PrintWriter out1, BufferedReader in) {
        initComponents();

        this.playerName = playerName;
        this.out = out1;
        this.in = in;
        jLabelWelcome.setText("Oyun başladı, hoş geldin " + playerName + "!");

        loadBoardImage();  // board.png güvenli şekilde yüklenir

        boardPanel = new GameBoardPanel();
        boardPanel.setOpaque(false);
        boardPanel.setBounds(0, 0, 900, 900);

        // boardPanel.generateMatrix();//
        //jPanel1.add(boardPanel);
        jPanel1.add(boardPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 900, 900));
        jPanel1.setComponentZOrder(boardPanel, 0);

        // Zar sonucu dinlemeye başla:
        new Thread(() -> listenForMessages()).start();

    }

    // ️ board.png'yi güvenli şekilde yükleyen metot
    private void loadBoardImage() {
        String imagePath = "/images/board.png";
        java.net.URL imgURL = getClass().getResource(imagePath);

        if (imgURL == null) {
            System.out.println("️ board.png bulunamadı! Yol: " + imagePath);
            lbl_image.setText("?");
            return;
        }

        ImageIcon icon = new ImageIcon(imgURL);
        lbl_image.setIcon(icon);

        
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        lbl_image = new javax.swing.JLabel();
        jLabelWelcome = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabelDiceResult = new javax.swing.JLabel();
        jLabelRollResult = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lbl_image.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/board.png"))); // NOI18N
        jPanel1.add(lbl_image, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 0, 590, 570));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 40, -1, 570));

        jLabelWelcome.setText("Oyun başladı, hoş geldin X!");
        getContentPane().add(jLabelWelcome, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, -1, -1));

        jButton1.setText("Zar At");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 630, -1, -1));

        jLabelDiceResult.setText("sıra ...");
        getContentPane().add(jLabelDiceResult, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 620, 250, 30));

        jLabelRollResult.setText("Zar sonucu.....");
        getContentPane().add(jLabelRollResult, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 640, 250, 30));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        out.println("ROLL " + playerName);  // Zar atma mesajını server'a yolluyoruz
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(GameScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GameScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GameScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GameScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
 /*java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                String playerName = null;
                new GameScreen(playerName).setVisible(true);
           
        }); }*/
    }

    private void listenForMessages() {
        try {
            String serverMessage;
            while ((serverMessage = in.readLine()) != null) {
                System.out.println("Server'dan gelen: " + serverMessage);
                final String messageToShow = serverMessage;

                javax.swing.SwingUtilities.invokeLater(() -> {
                    if (messageToShow.startsWith("PLAYER_NAMES")) {
                        String[] parts = messageToShow.split(" ");
                        boardPanel.setPlayerNames(parts[1].trim(), parts[2].trim());

                    } else if (messageToShow.startsWith("SIRA ")) {
                        String currentTurnPlayer = messageToShow.substring(5);
                        jButton1.setEnabled(currentTurnPlayer.equals(playerName));
                        jLabelDiceResult.setText(currentTurnPlayer.equals(playerName)
                                ? "Senin sıran! Zar atabilirsin."
                                : "Şu an " + currentTurnPlayer + " oynuyor, bekle.");

                    } else if (messageToShow.startsWith("ROLL_RESULT ")) {
                        String[] parts = messageToShow.split(" ");
                        String gelenOyuncu = parts[1];
                        int zarSonucu = Integer.parseInt(parts[2]);
                        int eskiPozisyon = Integer.parseInt(parts[3]);
                        int yeniPozisyon = Integer.parseInt(parts[4]);

                        jLabelRollResult.setText("Oyuncu " + gelenOyuncu + " zar attı: " + zarSonucu);
                        boardPanel.updatePlayerPosition(gelenOyuncu, yeniPozisyon);

                        if (eskiPozisyon + zarSonucu != yeniPozisyon) {
                            jLabelDiceResult.setText(gelenOyuncu + " yılan/merdiven ile "
                                    + (eskiPozisyon + zarSonucu) + " → " + yeniPozisyon + " gitti!");
                        }

                        checkForWin(gelenOyuncu, yeniPozisyon);

                    } else if (messageToShow.equals("RESET")) {
                        restartGame();

                    } else if (messageToShow.equals("GAME_OVER")) {
                        JOptionPane.showMessageDialog(
                                GameScreen.this,
                                "Oyun sona erdi. Teşekkürler!",
                                "Oyun Bitti",
                                JOptionPane.INFORMATION_MESSAGE);
                        System.exit(0);  // Uygulamayı tamamen kapat

                    } else {
                        jLabelDiceResult.setText(messageToShow);
                    }
                });
            }
        } catch (IOException e) {
            System.out.println("Mesaj dinleme hatası: " + e.getMessage());
        }
    }

    private void checkForWin(String playerName, int position) {
        if (position >= 100) {
            jButton1.setEnabled(false);  // Zar atmayı kapat

            JOptionPane.showMessageDialog(this,
                    "*** " + playerName + " kazandı! ***",
                    "Oyun Bitti",
                    JOptionPane.INFORMATION_MESSAGE);

            // Bu oyuncu tekrar oynamak istiyor mu?
            int choice = JOptionPane.showConfirmDialog(this,
                    "Tekrar oynamak ister misiniz?",
                    "Yeni Oyun?",
                    JOptionPane.YES_NO_OPTION);

            if (choice == JOptionPane.YES_OPTION) {
                out.println("REPLAY_YES");
            } else {
                out.println("REPLAY_NO");
            }
        }
    }


    
    private void restartGame() {
        // Pozisyonları sıfırla
        boardPanel.updatePlayerPosition(boardPanel.getPlayer1Name(), 0);
        boardPanel.updatePlayerPosition(boardPanel.getPlayer2Name(), 0);

        jLabelRollResult.setText("Zar sonucu.....");
        jLabelDiceResult.setText("Oyun yeniden başlıyor, sıranı bekle...");
        jButton1.setEnabled(false);  // Sıra gelene kadar zar atma kapalı
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabelDiceResult;
    private javax.swing.JLabel jLabelRollResult;
    private javax.swing.JLabel jLabelWelcome;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lbl_image;
    // End of variables declaration//GEN-END:variables
}
