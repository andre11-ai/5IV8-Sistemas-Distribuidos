/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Studio
 */


import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Servidor implements Runnable {
    
    public Socket usuario;
    private static ServerSocket server;
    public static int port;
    
    public Servidor(Socket usuario) {
        this.usuario = usuario;
    }
    
    public static void main ( String[] args ) throws IOException {  
        port = 7375;
        server = new ServerSocket(port);
        System.out.println("Servidor conectado.");
        System.out.println("Ouvindo a porta " + port);
        
        while(true) {
            Socket client = server.accept();
            Servidor jogo = new Servidor(client);
            Thread t = new Thread(jogo);
            t.start();
        }
    }
    
    @Override
    public void run() {
        System.out.println("Cliente conectado: " + usuario.getInetAddress());
        DataTransfer dto = DataTransfer.getInstance();
        int jugador = -1;
        System.out.println("Entrando na fila...");
        while(jugador == -1) jugador = dto.addPlayer();
        String msg;
        while(true) {
            try {
                DataInputStream inputStream;
                inputStream = new DataInputStream(usuario.getInputStream());
                msg = inputStream.readUTF();
                System.out.println("Mensagem Recebida: " + msg + " User: " + jugador);
                DataOutputStream outputStream = new DataOutputStream(usuario.getOutputStream());
                switch (msg) {
                    case "getScore":
                        msg = "" + dto.receiveLines(jugador);
                        outputStream.writeUTF(msg);
                        outputStream.flush();
                        break;
                    case "sendLines":
                        msg = "howMany";
                        outputStream.writeUTF(msg);
                        outputStream.flush();
                        String r = inputStream.readUTF();
                        int l = Integer.parseInt(r);
                        dto.sendLines(jugador, l);
                        break;
                    case "getSentence":
                        int estado = dto.getState(jugador);
                        System.out.println("Estado del jugador " + jugador + ": " + estado);
                        msg = "" + dto.getState(jugador);
                        outputStream.writeUTF(msg);
                        outputStream.flush();
                        break;
                    case "lostGame":
                        dto.setState(jugador, DataTransfer.derrota);
                        break;
                    
                    case "close":
                        dto.freePlayer(jugador);
                        usuario.close();
                }               
            } catch (IOException ex) {
                
            }
        }
    }
}
