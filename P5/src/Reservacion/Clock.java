/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Reservacion;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;

/**
 *
 * @author JManuel
 */
public class Clock extends Thread{
    private int hora, minutos, segundos,segundero,segsToWait=0;
    private JLabel label;
    private Boolean start;
    
    public Clock(JLabel label){
        hora = GetRandom(0,24);
        minutos = GetRandom(0,60);
        segundos = GetRandom(0,60);
        this.label = label;
        label.setText(hora+":"+minutos+":"+segundos);
        start = true;
        segundero = 1000;
    }
    public Clock(int hora,int minutos,int segundos,JLabel label){
        this.hora = hora;
        this.minutos = minutos;
        this.segundos = segundos;
        this.label = label;
        label.setText(hora+":"+minutos+":"+segundos);
        start = true;
        segundero = 1000;
    }
    
    public Clock(int hora,int minutos,int segundos,JLabel label,int segundero){
        this.hora = hora;
        this.minutos = minutos;
        this.segundos = segundos;
        this.label = label;
        label.setText(hora+":"+minutos+":"+segundos);
        start = true;
        this.segundero = segundero;
    }
    
    public Clock(JLabel label,int segundero){
        hora = GetRandom(0,24);
        minutos = GetRandom(0,60);
        segundos = GetRandom(0,60);
        this.label = label;
        label.setText(hora+":"+minutos+":"+segundos);
        start = true;
        this.segundero = segundero;
    }
    
    public int ClockInSeconds(){
        return segundos + minutos*60 + hora*3600;
    }
    
    public void SetClockInSeconds(int seconds){
        hora = seconds/3600;
        int temp = seconds%3600;
        minutos = temp/60;
        segundos = temp%60;
    }

    @Override
    public void run() {
        while(true){
                if(start)
                    segundos++;
                if(segundos>=60){
                    segundos = 0;
                    minutos++;
                }
                if(minutos>=60){
                    minutos = 0;
                    hora++;
                }
                if(hora>=24){
                    hora=0;
                }
                label.setText(hora+":"+minutos+":"+segundos);
                if(start){
                    try {
                        if(segsToWait > 0){
                            System.out.println("Reloj esperando");
                            Thread.sleep(segsToWait);
                            segsToWait = 0;
                        }else
                            Thread.sleep(segundero);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Clock.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
        }
    }
    
    public String GetHoraLocal(){
        return hora + ":" + minutos + ":" + segundos;
    }
    
    public void Stop(){
        start = false;
    }
    
    public void Start(){
        start = true;
    }
    
    public void Esperar(int tiempo){
        segsToWait = tiempo;
    }
    
    private int GetRandom(int min,int max){
        Random rand = new Random();
        return rand.nextInt(max-min)+min;
    }

    public int getHora() {
        return hora;
    }

    public void setHora(int hora) {
        this.hora = hora;
    }

    public int getMinutos() {
        return minutos;
    }

    public void setMinutos(int minutos) {
        this.minutos = minutos;
    }

    public int getSegundos() {
        return segundos;
    }

    public void setSegundos(int segundos) {
        this.segundos = segundos;
    }

    public int getSegundero() {
        return segundero;
    }

    public void setSegundero(int segundero) {
        this.segundero = segundero;
    }

    @Override
    public String toString() {
        return hora + ":" + minutos + ":" + segundos + ":" + segundero + ":";
    }
}
