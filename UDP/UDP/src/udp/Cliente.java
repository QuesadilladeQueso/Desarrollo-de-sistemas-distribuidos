/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package udp;
import java.io.IOException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.DatagramPacket;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 *
 * @author tuben
 */
public class Cliente {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        final int PUERTO_SERVIDOR = 8080;
        byte[] buffer = new byte[1024];
        try {
            InetAddress direccionServidor = InetAddress.getByName("pruebadistro.ddns.net");
            
            DatagramSocket socketUDP = new DatagramSocket(); 
            String mensaje = "Hola desde el cliente";
            
            buffer = mensaje.getBytes();
            
            DatagramPacket pregunta = new DatagramPacket(buffer, buffer.length,direccionServidor, PUERTO_SERVIDOR );
            
            socketUDP.send(pregunta);
            
            DatagramPacket peticion = new DatagramPacket(buffer, buffer.length);
            
            socketUDP.receive(peticion);
            
            mensaje = new String(peticion.getData());
            System.out.println(mensaje);
            
            socketUDP.close();
            
        } catch (SocketException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
    
}
