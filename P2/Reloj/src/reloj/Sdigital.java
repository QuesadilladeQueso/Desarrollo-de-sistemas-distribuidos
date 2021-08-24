package reloj;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.*;


public class Sdigital extends javax.swing.JFrame implements Runnable{
    private Servidor s;
    int hora, minutos, segundos;
    Calendar calendario;
    Thread h1;
    Random random = new Random();
    int millisInDay = 24*60*60*1000;
    Date dt = new Date((long)random.nextInt(millisInDay));
    SimpleDateFormat sdf24 = new SimpleDateFormat("HH:mm:ss");
    Calendar cal = Calendar.getInstance();
       
    
    
    public Sdigital() {
        initComponents();
        h1 = new Thread(this);
        h1.start();
        
        setLocationRelativeTo(null);
        setTitle("Reloj");
        setVisible(true);
    }


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        relojDigital = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        btnActualizar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        relojDigital.setFont(new java.awt.Font("Trebuchet MS", 1, 36)); // NOI18N
        relojDigital.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        jLabel1.setText("Servidor");

        btnActualizar.setText("Actualizar letreros");
        btnActualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(relojDigital, javax.swing.GroupLayout.PREFERRED_SIZE, 692, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(27, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnActualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 287, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(216, 216, 216))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 74, Short.MAX_VALUE)
                .addComponent(relojDigital, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(78, 78, 78)
                .addComponent(btnActualizar)
                .addGap(27, 27, 27))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarActionPerformed

        long reloj = Long.parseLong(this.relojDigital.getText());
        
        String[] nombres = {"reloj"};
        long[] valores = {reloj};

        s.enviarInfo(nombres, valores);

    }//GEN-LAST:event_btnActualizarActionPerformed

    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnActualizar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel relojDigital;
    // End of variables declaration//GEN-END:variables

    @Override
    public void run() {
        
        Thread ct = Thread.currentThread();
        while(ct == h1){
            calcula();
            try{
                Thread.sleep(1000);
            }catch(InterruptedException e){}
        }
        
    }

    private void calcula() {
         cal.setTime(dt);
         long espera = 1000;
         while(true){
         cal.add(Calendar.SECOND,1);
             espera(espera);
        Date dat = cal.getTime();
        String time24 = sdf24.format(dat);
         //Meter en label
        relojDigital.setText(time24);
        
         }
              
    }
    
    public static void espera(long a){
    try {
        Thread.sleep(a);
    }catch (InterruptedException e)
    {
        e.printStackTrace();
    }  
    
}
    
    
}
