/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Reservacion;

import java.sql.*;
import java.io.*;
import java.net.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BoxLayout;

/**
 *
 * @author JManuel
 */
public class ServidorReloj extends javax.swing.JFrame {
   
    final private int PUERTO1 = 5000,PUERTO2 = 5001;
    byte[] buffer = new byte[1024];
    private Connection conexion;
    private DatagramSocket socketConexiones,socketSync;
    final private int syncDelay = 10000;
    private PanelReloj relojReferencia;
    private int idSesion;
    private ArrayList<PanelReloj> panelRelojs = new ArrayList<>();
    private static boolean sincronizar = true;
    /**
     * Creates new form ServidorReloj
     */
    public ServidorReloj() {
        initComponents();
        contentPanel.setLayout(new BoxLayout(contentPanel,BoxLayout.Y_AXIS));
        LocalDateTime now = LocalDateTime.now();
        relojReferencia = new PanelReloj("Reloj Referencia",now.getHour(),now.getMinute(),now.getSecond(),1000);
        contentPanel.add(relojReferencia);
        CargarDriver();
        try {
            //Creamos conexion a la base de datos
            conexion = DriverManager.getConnection("jdbc:mysql://localhost/servidorreloj?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC","root" ,"1234");
            System.out.println("Conectado a base de datos");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        try {
            socketConexiones = new DatagramSocket(PUERTO1);
            socketSync = new DatagramSocket(PUERTO2);
        } catch (SocketException ex) {
            Logger.getLogger(ServidorReloj.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        labelTitulo = new javax.swing.JLabel();
        scrollPanel = new javax.swing.JScrollPane();
        contentPanel = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        labelTitulo.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        labelTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelTitulo.setText("Servidor Reloj");

        javax.swing.GroupLayout contentPanelLayout = new javax.swing.GroupLayout(contentPanel);
        contentPanel.setLayout(contentPanelLayout);
        contentPanelLayout.setHorizontalGroup(
            contentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 718, Short.MAX_VALUE)
        );
        contentPanelLayout.setVerticalGroup(
            contentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 483, Short.MAX_VALUE)
        );

        scrollPanel.setViewportView(contentPanel);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelTitulo, javax.swing.GroupLayout.DEFAULT_SIZE, 374, Short.MAX_VALUE)
                    .addComponent(scrollPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labelTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scrollPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 248, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        try {
            //Creamos una nueva sesion en la base de datos
            Statement stateSesion = conexion.createStatement();
            stateSesion.executeUpdate("INSERT INTO sesion (HoraInicio) VALUES ('" + relojReferencia.getReloj().GetHoraLocal() + "')");

            //Obtenemos el id mas nuevo
            ResultSet resultado = stateSesion.executeQuery("SELECT MAX(idSesion) FROM sesion");
            while (resultado.next()) {
                idSesion = resultado.getInt("MAX(idSesion)");
            }
            resultado.close();
            stateSesion.close();
            System.out.println("Sesion " + idSesion + " creada");
        } catch (SQLException ex) {
            System.out.println("Error al crear sesion: " + ex.getMessage());
        }
        //Creamos hilo que detecta peticiones
        Thread nuevasConexiones = new Thread(() -> {
            while (true) {
                try {
                    System.out.println("Esperando peticion");
                    DatagramPacket recibido = new DatagramPacket(buffer, buffer.length);
                    socketConexiones.receive(recibido);
                    //creamos nuevo statement para agregar nuevo usuario
                    Statement stateNuevoUsuario = conexion.createStatement();
                    //creamos inputstream y obtenemos paquete
                    DataInputStream input = new DataInputStream(new ByteArrayInputStream(recibido.getData()));
                    System.out.println("Nueva peticion desde " + recibido.getAddress().getHostAddress() + ":" + recibido.getPort());
                    //Leemos el tipo de comando
                    int tipoComando = input.readInt();
                    if (tipoComando == Servidor.NUEVA_CONEXION) {//si es nueva conexion
                        System.out.println("Nueva conexion detectada");
                        String nombreCliente = input.readUTF(); //Leemos nombre del cliente
                        String relojCliente = input.readUTF(); //Leemos reloj del cliente
                        String[] comp = relojCliente.split(":");
                        //creamos nuevo panelReloj en la UI
                        PanelReloj panel = new PanelReloj(nombreCliente, Integer.parseInt(comp[0]), Integer.parseInt(comp[1]), Integer.parseInt(comp[2]), 1000, recibido.getAddress(), recibido.getPort());
                        contentPanel.add(panel);
                        panelRelojs.add(panel);
                        //Agregamos cliente a la base de datos
                        stateNuevoUsuario.executeUpdate("INSERT INTO equipos (Nombre,IP,Puerto,HoraLocal,idSesion)"
                                + "VALUES ('" + nombreCliente + "', '" + recibido.getAddress().getHostAddress() + "', " + recibido.getPort() + ", '" + relojCliente + "', " + idSesion + ")");
                        System.out.println("Datos registrados");
                    }
                    stateNuevoUsuario.close();
                } catch (IOException | SQLException ex) {
                    Logger.getLogger(ServidorReloj.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        nuevasConexiones.start();

        //creamos hilo para sincronizar relojes
        Thread sincronizarClientes = new Thread(() -> {
            while (true) {
                if (sincronizar) {
                    try {
                        System.out.println("Iniciando sincronizacion de relojes");
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        try {
                            //creamos statement para sincronizacion
                            Statement sync = conexion.createStatement();
                            ResultSet equipos = sync.executeQuery("SELECT COUNT(idEquipo) AS totalEquipos FROM equipos WHERE idSesion=" + idSesion);
                            equipos.next();
                            int totalEquipos = equipos.getInt("totalEquipos");
                            System.out.println("Equipos a actualizar: " + totalEquipos);
                            if (totalEquipos >= 1) {
                                //Obtenemos de la base de datos los equipos conectados a la sesion actual
                                ResultSet res = sync.executeQuery("SELECT idEquipo,Nombre,IP,Puerto,HoraLocal FROM equipos WHERE idSesion=" + idSesion);
                                ArrayList<Equipo> arrayEquipos = new ArrayList<>();
                                //Por cada uno de los equipos
                                while (res.next()) {
                                    //instanciamos nuevo equipo
                                    arrayEquipos.add(new Equipo(res.getInt("idEquipo"),//id
                                            res.getInt("Puerto"), res.getString("Nombre"),//nombre
                                            res.getString("IP"),//ip
                                            HoraEnSegundos(res.getString("HoraLocal"))));//hora en segundos
                                }
                                res.close();
                                for (int i = 0; i < arrayEquipos.size(); i++) {
                                    //solicitamos reloj
                                    //enviamos peticion al cliente
                                    DataOutputStream output = new DataOutputStream(baos);
                                    output.writeInt(Servidor.RELOJ);//tipo de comando
                                    output.flush();
                                    byte[] b = baos.toByteArray();
                                    //creamos paquete
                                    DatagramPacket paquete = new DatagramPacket(b, b.length, InetAddress.getByName(arrayEquipos.get(i).getIp()), arrayEquipos.get(i).getPuerto());
                                    socketSync.send(paquete); //enviamos paquete
                                    int T0 = relojReferencia.getReloj().ClockInSeconds(); //t0
                                    System.out.println("Peticion enviada");
                                    //Esperamos a que nos responda el cliente
                                    buffer = new byte[1024];
                                    DatagramPacket comando = new DatagramPacket(buffer, buffer.length);
                                    socketSync.receive(comando);
                                    int T1 = relojReferencia.getReloj().ClockInSeconds();//T1
                                    DataInputStream input = new DataInputStream(new ByteArrayInputStream(comando.getData()));
                                    int Cs = input.readInt(); //Reloj del cliente
                                    int latencia = (T1 - T0) / 2;
                                    sync.executeUpdate("UPDATE equipos SET Latencia =" + latencia + " WHERE idEquipo=" + arrayEquipos.get(i).getId());
                                    System.out.println("hora recibida");
                                    arrayEquipos.get(i).setHoraLocal(Cs + latencia); //Guardamos la hora correguida
                                    arrayEquipos.get(i).setLatencia(latencia);
                                }
                                //Despues de obtener todos las horas y latencias
                                //obtemenos el promedio de hora
                                int suma = 0;
                                for (int i = 0; i < arrayEquipos.size(); i++) {
                                    suma += arrayEquipos.get(i).getHoraLocal();
                                }
                                System.out.println("Se obtiene suma");
                                //sumamos la hora del reloj de referencia
                                suma += relojReferencia.getReloj().ClockInSeconds();
                                int prom = suma / (arrayEquipos.size() + 1);
                                System.out.println("Promedio obtenido");
                                //cambiamos la hora del reloj de referencia
                                relojReferencia.getReloj().SetClockInSeconds(prom);
                                //cambiamos la hora de todos los relojes en la UI
                                panelRelojs.forEach(pReloj -> {
                                    pReloj.getReloj().SetClockInSeconds(prom);
                                });
                                System.out.println("Nuevo reloj: " + relojReferencia.getReloj());
                                //Enviamso a cada cliente su propio valor de hora
                                baos = new ByteArrayOutputStream();
                                System.out.println("Enviando nuevas horas");
                                for (int i = 0; i < arrayEquipos.size(); i++) {
                                    DataOutputStream output = new DataOutputStream(baos);
                                    //agregamos tipo de comando
                                    output.writeInt(Servidor.NUEVO_RELOJ);//tipo actualizar reloj
                                    output.flush();
                                    String horaAnterior = arrayEquipos.get(i).getHoraEnFormato();
                                    int nuevaHora = prom + arrayEquipos.get(i).getLatencia();
                                    arrayEquipos.get(i).setHoraLocal(nuevaHora);
                                    sync.executeUpdate("INSERT INTO horaequipos (idEquipo,horaActual,horaAnterior) VALUES (" + arrayEquipos.get(i).getId() + ",'" + arrayEquipos.get(i).getHoraEnFormato() + "','" + horaAnterior + "')");
                                    output.writeInt(nuevaHora);//reloj mas latencia del viaje
                                    output.flush();
                                    byte[] b = baos.toByteArray();
                                    //Creamos paquete
                                    DatagramPacket paquete = new DatagramPacket(b, b.length, InetAddress.getByName(arrayEquipos.get(i).getIp()), arrayEquipos.get(i).getPuerto());
                                    socketSync.send(paquete); //enviamos paquete
                                }
                            }
                            equipos.close();
                            sync.close();
                        } catch (SQLException ex) {
                            Logger.getLogger(ServidorReloj.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (IOException ex) {
                            Logger.getLogger(ServidorReloj.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        System.out.println("Durmiendo sync");
                        Thread.sleep(syncDelay);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(ServidorReloj.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }else
                    System.out.println("Sincronizacion suspendida");
            }
        });
        sincronizarClientes.start();
    }//GEN-LAST:event_formWindowOpened

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        try {
            System.out.println("Cerrando");
            //indicamos hora de termino de sesion en la base de datos
            Statement sesion = conexion.createStatement();
            sesion.executeUpdate("UPDATE sesion SET HoraTermino ='"+relojReferencia.getReloj().GetHoraLocal()+"' WHERE idSesion="+idSesion);
            
            sesion.close();
        } catch (SQLException ex) {
            Logger.getLogger(ServidorReloj.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_formWindowClosing
    
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
            java.util.logging.Logger.getLogger(ServidorReloj.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ServidorReloj.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ServidorReloj.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ServidorReloj.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ServidorReloj().setVisible(true);
            }
        });
    }
    
    private int HoraEnSegundos(String hora){
        String comp[] = hora.split(":");
        return Integer.parseInt(comp[0])*3600+Integer.parseInt(comp[1])*60+Integer.parseInt(comp[2]);
    }
    
    private void CargarDriver(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            System.out.println("Error al cargar el driver");
        }
    }
    
    public static void SincronizarRelojes(boolean value){
        sincronizar = value;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel contentPanel;
    private javax.swing.JLabel labelTitulo;
    private javax.swing.JScrollPane scrollPanel;
    // End of variables declaration//GEN-END:variables
}
