/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Reservacion;

/**
 *
 * @author JManuel
 */
public class Equipo {
    private int id,puerto,latencia,horaLocal;
    private String nombre,ip;

    public Equipo(int id, int puerto, String nombre, String ip, int horaLocal) {
        this.id = id;
        this.puerto = puerto;
        this.latencia = 0;
        this.nombre = nombre;
        this.ip = ip;
        this.horaLocal = horaLocal;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPuerto() {
        return puerto;
    }

    public void setPuerto(int puerto) {
        this.puerto = puerto;
    }

    public int getLatencia() {
        return latencia;
    }

    public void setLatencia(int latencia) {
        this.latencia = latencia;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getHoraLocal() {
        return horaLocal;
    }

    public void setHoraLocal(int horaLocal) {
        this.horaLocal = horaLocal;
    }
    
    public String getHoraEnFormato(){
        int temp = horaLocal%3600;
        return (horaLocal/3600)+":"+(temp/60)+":"+(temp%60);
    }
}
