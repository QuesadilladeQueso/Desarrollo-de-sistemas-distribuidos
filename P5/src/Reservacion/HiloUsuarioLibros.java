/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Reservacion;

import java.sql.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;

/**
 *
 * @author JManuel
 */
public class HiloUsuarioLibros extends Thread {

    Servidor servidorPadre;
    DataInputStream input;
    DataOutputStream output;
    Connection conexion;
    Equipo usuario;
    JLabel portadaLibro;
    int idHilo;
    static int numHilos;

    @Override

    public void run() {
        while (true) {
            try {
                System.out.println("Hilo "+idHilo+" Esperando solicitud");
                int comando = input.readInt();//leemos el tipo de comando
                if (comando == Servidor.PEDIR_LIBRO) { //si es de tipo pedir libro enviamos un libro disponible al azar
                    //Primero se obtiene una lista de los libros que estan
                    //disponibles actualmente
                    System.out.println("Hilo "+idHilo+" Enviando libro");
                    try {
                        ArrayList<Libro> libros = new ArrayList<>();
                        Statement sLibros = conexion.createStatement();
                        ResultSet res = sLibros.executeQuery("SELECT idLibro,Nombre,Autor,Portada FROM libro WHERE Disponible=1");
                        //Agregamos libros disponibles a una lista de libros
                        while (res.next()) { //hasta que no haya mas filas
                            //agregar libro a lista
                            libros.add(new Libro(res.getInt("idLibro"),
                                    res.getString("Nombre"),
                                    res.getString("Autor"),
                                    res.getString("Portada")));
                        }
                        res.close();
                        System.out.println("Libros disponibles: "+libros.size());
                        //verificamos si quedan libros disponibles
                        if (libros.size() > 0) {
                            //de los libros disponibles enviamos uno al azar
                            Random rand = new Random(); //creamos random class
                            Libro libroEnviar = libros.get(rand.nextInt(libros.size()));//seleccionamos libro
                            //Cambiamos el estado del libro a no disponible en la base de datos
                            String query = "UPDATE libro SET Disponible = 0 WHERE idLibro=" + libroEnviar.getIdLibro();
                            sLibros.executeUpdate(query);
                            servidorPadre.EnviarQuery(query);
                            //Preparamos contenido a enviar al usuario
                            output.writeInt(Servidor.PEDIR_LIBRO);//enviamos tipo de peticion
                            output.flush();
                            output.writeBoolean(servidorPadre.NotificarReinicio());
                            servidorPadre.SetNotificarReinicio(false);
                            output.writeUTF(libroEnviar.getNombre()); //enviamos nombre del libro
                            output.flush();
                            output.writeUTF(libroEnviar.getAutor()); //enviamos autor del libro
                            output.flush();
                            //Registramos el pedido en la base de datos
                            RegistrarPedido(servidorPadre.getRelojLocal().GetHoraLocal(),
                                    usuario.getId(),usuario.getIp(),libroEnviar.getIdLibro(),
                                    servidorPadre.getIdSesion());
                            System.out.println("Hilo " + idHilo + " Libro enviado");
                            //De libro a enviar mostramos su portada en la UI
                            portadaLibro.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Portadas/" + libroEnviar.getPortada())));
                        }else{
                            //Si no hay libros disponibles reiniciamos la sesion
                            //borrando los libros pedidios por los clientes y limpiando sus listas
                            System.out.println("Notificando al cliente que no hay libros disponibles");
                            output.writeInt(Servidor.REINICIAR_SESION);
                            output.flush();
                            System.out.println("Esperando respuesta del usuario");
                            int reiniciar = input.readInt();
                            if(reiniciar == Servidor.REINICIAR_SESION){//si reiniciar
                                servidorPadre.ReiniciarEstadoLibros();//todos los libros estan disponibles de nuevo
                                System.out.println("Sesion reiniciada");
                            }
                        }
                    } catch (SQLException ex) {
                        Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(HiloUsuarioLibros.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public HiloUsuarioLibros(Socket cliente, Connection conexion,Equipo usuario, JLabel portadaLibro,Servidor servidor) {
        try {
            this.input = new DataInputStream(cliente.getInputStream());
            this.output = new DataOutputStream(cliente.getOutputStream());
            this.conexion = conexion;
            this.portadaLibro = portadaLibro;
            idHilo = numHilos;
            this.usuario = usuario;
            servidorPadre = servidor;
            numHilos++;
        } catch (IOException ex) {
            Logger.getLogger(HiloUsuarioLibros.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void RegistrarPedido(String hora, int idUsuario,String ipUsuario,int idLibro,int idSesion){
        try {
            Statement s = conexion.createStatement();
            //creamos registro
            System.out.println("idLibro: "+idLibro+" idUsuario: "+idUsuario+" idSesion: "+idSesion);
            String query = "INSERT INTO pedido (HoraPedido,ipUsuario,idLibro,idUsuario,idSesion)"
                    + "VALUES ('"+hora+"','"+ipUsuario+"',"+idLibro+","+idUsuario+","+idSesion+")";
            s.executeUpdate(query);
            servidorPadre.EnviarQuery(query);
            s.close();
        } catch (SQLException ex) {
            Logger.getLogger(HiloUsuarioLibros.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
