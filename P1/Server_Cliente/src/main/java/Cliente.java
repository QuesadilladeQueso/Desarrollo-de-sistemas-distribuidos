
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;



public class Cliente {
    public static void main(String[] args) {

        final String HOST = "192.168.100.9";
        final int puerto = 5000;

        DataInputStream in;
        DataOutputStream out, out2;
        
        long tiempo0, tiempo1, Cs, C, tiempoCristian;
        String tiempoFinal, T0, T1;
        
        
        
        try {
            Socket sc = new Socket(HOST, puerto);

            in = new DataInputStream(sc.getInputStream());
            out = new DataOutputStream(sc.getOutputStream());
            String message;
            
            
            Scanner entrada = new Scanner(System.in);
            System.out.println("Ingrese mensaje");
            message = entrada.next();
            tiempo0 = System.currentTimeMillis(); 
            out.writeUTF(message);
            
            
            T0 = String.valueOf(Long.toString(tiempo0));
            System.out.println("El tiempo de solicitud es = " +(new Date(Long.parseLong(T0)))+ " O en milisegundos "+ T0);
            
            String mensaje = in.readUTF();
            
            Cs = Long.parseLong(mensaje);
            System.out.println("El tiempo de servidor respuesta del servidor es = "+(new Date(Long.parseLong(mensaje)))+" o en milisegundos " + Cs);
            
            
            tiempo1 = System.currentTimeMillis();
            T1 = String.valueOf(Long.toString(tiempo1));
            System.out.println("El tiempo despues de solicitud es = " +(new Date(Long.parseLong(T1)))+ " o en milisegundos"+ T1);
            
                 
            C = Cs + ((tiempo1 - tiempo0)/2);
            
            out2 = new DataOutputStream(sc.getOutputStream());
            tiempoFinal = String.valueOf(C);
            System.out.println("La hora final del calculo es "+(new Date(Long.parseLong(tiempoFinal)))+" o en milisegundos " + tiempoFinal );
            //Prueba
            tiempoCristian = Long.valueOf(tiempoFinal);
            //DateFormat df = new SimpleDateFormat("dd-MM-yy");
            //DateFormat df = new SimpleDateFormat("dd:MM:yy:HH:mm:ss");
            //String ejecutable = new SimpleDateFormat("dd-MM-yy").format(tiempoCristian);
            SimpleDateFormat DateFor = new SimpleDateFormat("dd-MM-yyyy");
            String ejecutable = DateFor.format(tiempoCristian);
            System.out.println("Fecha a para actualizar: " + ejecutable);
            //formatted value of current Date
            Process proc;
            Runtime rt = Runtime.getRuntime();
            //proc = rt.exec("cmd /C date " + ejecutable);
            rt.exec("runas /profile /user:Administrador \"cmd.exe /c date" + ejecutable);
           //proc = rt.exec("cmd /C time " + timestr);
            //Fin de prueba
                         
            out.writeUTF(tiempoFinal);
            sc.close();

        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
