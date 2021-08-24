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
public class Cliente {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
            final String HOST = "187.185.37.213";
            final int PUERTO = 5000;
            DataInputStream in;
            DataOutputStream out;
            
            try {    
            Socket sc = new Socket (HOST,PUERTO);
            
            in = new DataInputStream(sc.getInputStream());
            out = new DataOutputStream(sc.getOutputStream());
            
            out.writeUTF("Hola mundo desde el cliente");
            
            String mensaje = in.readUTF();
            
            System.out.println(mensaje);
            
            sc.close();
            
            // TODO code application logic here
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
