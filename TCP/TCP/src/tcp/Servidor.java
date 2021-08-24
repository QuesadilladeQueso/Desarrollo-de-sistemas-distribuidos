/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tcp;

import java.io.IOException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author tuben
 */
public class Servidor {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
            ServerSocket servidor = null;
            Socket sc = null;
            DataInputStream in;
            DataOutputStream out;
            
            final int PUERTO = 5000;
        try {    
            servidor = new ServerSocket(PUERTO);
            System.out.println("Servidor iniciado");
            while(true)
            {
                sc = servidor.accept();
                System.out.println("Cliente conectado");
                in = new DataInputStream(sc.getInputStream());
                out = new DataOutputStream(sc.getOutputStream());
                String mensaje = in.readUTF();
                System.out.println(mensaje);
                
                out.writeUTF("Hola desde el servidor");
                sc.close();
                System.out.println("Cliente se desconecto");
            }
            
            // TODO code application logic here
        } catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
