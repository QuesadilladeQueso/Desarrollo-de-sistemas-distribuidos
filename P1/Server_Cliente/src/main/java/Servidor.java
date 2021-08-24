import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Servidor {
    public static void main(String[] args){
    ServerSocket servidor = null;
        Socket sc = null;
        DataInputStream in, in2;
        DataOutputStream out;
        
        final int PUERTO = 5000;
        String Cs;
    
    try {
            servidor = new ServerSocket(PUERTO);
            System.out.println("Inicia servidor");
        
            while(true){
                sc = servidor.accept();
                System.out.println("Se conectó el clinte" + sc.getInetAddress().getHostName());
                
                in = new DataInputStream(sc.getInputStream());
                out  = new DataOutputStream(sc.getOutputStream());
                
                String mensaje = in.readUTF();

                if(mensaje.equals("cristian")){
                    System.out.println("El cliente solicito " + mensaje);
                    Cs = String.valueOf(Long.toString(System.currentTimeMillis()));
                    System.out.println("El mensje enviado fue a las "+ (new Date(Long.parseLong(Cs))) + " o en milisegundos " + (Cs));
                    out.writeUTF(Cs);
                    
                    in2 = new DataInputStream(sc.getInputStream());
                    String horaCristian = in2.readUTF();
                    
                    System.out.println("El tiempo final de cristian es " +(new Date(Long.parseLong(horaCristian)))+ "o en milisegundos" + horaCristian);
                  
                }else{ if (mensaje.equals("Cerrar")){
                    System.out.println("El cliente me mandó " + mensaje);
                    sc.close();
                    System.out.println("Se desconecto el cliente");
                }
                    System.out.println("El cliente me mandó " + mensaje);
                }
                    
            }
            
        } catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
}
}
