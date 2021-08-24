package cliente;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.text.SimpleDateFormat;
import java.util.Date;




public class Cliente {
    public static void main(String[] args) {
        //final String HOST = "192.168.100.9";
        final String HOST = "arenasdistribuidos.ddns.net";
        final int puerto = 5000;

        DataInputStream in;
        DataOutputStream out, out2;
        
        long tiempo0, tiempo1, Cs, C, tiempoCristian, tiempoServ;
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
            System.out.println("El tiempo de servidor respuesta del servidor es = "+(new Date (Long.parseLong(mensaje)))+" o en milisegundos " + Cs);
            
            
            tiempo1 = System.currentTimeMillis();
            T1 = String.valueOf(Long.toString(tiempo1));
            System.out.println("El tiempo despues de solicitud es = " +(new Date(Long.parseLong(T1)))+ " o en milisegundos"+ T1);
            
                 
            C = Cs + ((tiempo1 - tiempo0)/2);
            
            out2 = new DataOutputStream(sc.getOutputStream());
            tiempoFinal = String.valueOf(C);
            System.out.println("La hora final del calculo es "+(new Date(Long.parseLong(tiempoFinal)))+" o en milisegundos " + tiempoFinal );
            tiempoCristian = Long.valueOf(tiempoFinal);
            tiempoServ = Long.valueOf(T1);
            Date d0 = new Date(Long.parseLong(T0));
            Date df = new Date(Long.parseLong(tiempoFinal));
            Date diff = new Date(d0.getTime() - df.getTime());
            long espera = diff.getTime();
            if (df.after(d0)) {
                     SimpleDateFormat DateFor = new SimpleDateFormat("dd-MM-yyyy");
                     String ejecutablef = DateFor.format(tiempoCristian);
                     System.out.println("Fecha a para actualizar: " + ejecutablef);
                     Process proc;
                     Runtime rt = Runtime.getRuntime();
                     proc = rt.exec("cmd /C date " + ejecutablef);
                     
                     SimpleDateFormat DateFor2 = new SimpleDateFormat("HH:mm:ss");
                     String ejecutablet = DateFor2.format(tiempoCristian);
                     System.out.println("Tiempo a para actualizar: " + ejecutablet);
                     Process proc2;
                     Runtime rt2 = Runtime.getRuntime();
                     proc2 = rt2.exec("cmd /C time " + ejecutablet);
                     System.out.println("Ya estan sincronizados");
                }
                if (df.before(d0)){
                            System.out.println("Esperando al servidor..."); 
                            espera(espera);
                            SimpleDateFormat DateFor = new SimpleDateFormat("dd-MM-yyyy");
                            String ejecutablef = DateFor.format(tiempoServ);
                            System.out.println("Fecha a para actualizar: " + ejecutablef);
                            Process proc;
                            Runtime rt = Runtime.getRuntime();
                            proc = rt.exec("cmd /C date " + ejecutablef);
                            SimpleDateFormat DateFor2 = new SimpleDateFormat("HH:mm:ss");
                            String ejecutablet = DateFor2.format(tiempoServ);
                            System.out.println("Tiempo a para actualizar: " + ejecutablet);
                            Process proc2;
                            Runtime rt2 = Runtime.getRuntime();
                            proc2 = rt2.exec("cmd /C time " + ejecutablet);
                            System.out.println("Ya estan sincronizados");
                                               
                    
                } 
                if (df.equals(d0)) {
                    System.out.println("Ya estan sincronizados");
                    }
                
            out.writeUTF(tiempoFinal);
            sc.close();

        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
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