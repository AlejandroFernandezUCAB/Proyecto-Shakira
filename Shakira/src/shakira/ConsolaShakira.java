/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shakira;

import java.awt.Event;
import java.awt.event.KeyEvent;

/**
 *
 * @author pedro
 */
public class ConsolaShakira extends javax.swing.JFrame {

    /**
     * Creates new form ConsolaShakira
     */
    public ConsolaShakira() {
        
        initComponents();
        this.setSize(800, 587);
        this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        consolaTextArea = new javax.swing.JTextArea();
        inputComando = new javax.swing.JTextField();
        enviarBoton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setResizable(false);
        setSize(new java.awt.Dimension(800, 600));

        panel.setPreferredSize(new java.awt.Dimension(800, 575));

        consolaTextArea.setColumns(20);
        consolaTextArea.setFont(new java.awt.Font("Ubuntu Mono", 0, 15)); // NOI18N
        consolaTextArea.setRows(5);
        consolaTextArea.setEnabled(false);
        jScrollPane1.setViewportView(consolaTextArea);

        inputComando.setFont(new java.awt.Font("Ubuntu Mono", 0, 15)); // NOI18N
        inputComando.setText("Inserte comando aquí");
        inputComando.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                inputComandoMouseClicked(evt);
            }
        });
        inputComando.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                inputComandoKeyPressed(evt);
            }
        });

        enviarBoton.setFont(new java.awt.Font("Ubuntu Mono", 1, 15)); // NOI18N
        enviarBoton.setText("Enviar");
        enviarBoton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                enviarBotonActionPerformed(evt);
            }
        });
        enviarBoton.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                enviarBotonKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout panelLayout = new javax.swing.GroupLayout(panel);
        panel.setLayout(panelLayout);
        panelLayout.setHorizontalGroup(
            panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(panelLayout.createSequentialGroup()
                        .addComponent(inputComando, javax.swing.GroupLayout.DEFAULT_SIZE, 664, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(enviarBoton, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        panelLayout.setVerticalGroup(
            panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 494, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(inputComando)
                    .addComponent(enviarBoton, javax.swing.GroupLayout.DEFAULT_SIZE, 44, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void enviarBotonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_enviarBotonActionPerformed
        
        enviarInformacion();
        
    }//GEN-LAST:event_enviarBotonActionPerformed

    private void inputComandoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_inputComandoMouseClicked
        
    }//GEN-LAST:event_inputComandoMouseClicked

    /**
     * Metodo que al presionar enter se envía al servidor
     * @param evt evento 
     */
    private void enviarBotonKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_enviarBotonKeyPressed
        
       enviarInformacion(evt);
       String prueba;
       
    }//GEN-LAST:event_enviarBotonKeyPressed

    private void inputComandoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_inputComandoKeyPressed
       
        enviarInformacion(evt);
       
    }//GEN-LAST:event_inputComandoKeyPressed

    /**
     * Metodo que envía información a al servidor en caso de que se presione enter
     * @param evt Al presionar tecla enter
     */
    
    public void enviarInformacion(java.awt.event.KeyEvent evt){
        
        if(evt.getKeyCode() == KeyEvent.VK_ENTER){
            
            Controladora controladora = new Controladora(panel ,inputComando, consolaTextArea);
            controladora.enviarInformacion();
            inputComando.setText("");
            
        }
        
    }
    
    /**
     * Metodo que envía la linea de comando al servidor
     */
     public void enviarInformacion(){

        Controladora controladora = new Controladora(panel ,inputComando, consolaTextArea);
        controladora.enviarInformacion();
        inputComando.setText("");
        
    }
    
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
            java.util.logging.Logger.getLogger(ConsolaShakira.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ConsolaShakira.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ConsolaShakira.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ConsolaShakira.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ConsolaShakira().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea consolaTextArea;
    private javax.swing.JButton enviarBoton;
    private javax.swing.JTextField inputComando;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel panel;
    // End of variables declaration//GEN-END:variables
}
