/*
Hecho por Andre 
*/
package Chat;

/* 
Esta son las librerias que utilizaremos
*/
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import java.awt.event.ActionEvent;

import java.util.ArrayList;
import javax.swing.JButton;


public class Chat extends JFrame{
    
    /*
    Esta son las variables del proyecto del chat 
    */
    public static final int HOST_MODE = 0;
    public static final int CLIENT_MODE = 1;
    JButton btn_send;
    JScrollPane jScrollPane1;
    JTextArea jTextArea1;
    JLabel lbl_ipNroomName;
    JTextField txt_mymsg;
    int mode;
    String nombre;
    String room;
    InetAddress host;
    Chat cht;
    DatagramSocket socket;
    ArrayList<client> ClientList;
    byte[] b;
   
   
    
   
    public static void main(String[] args) {
        try {
            String host = "", room = "";
            String name = JOptionPane.showInputDialog("Indica tu nombre: ");
            if (name == null || name.equals("")) {
                JOptionPane.showMessageDialog(null, "No sheas malo, indica tu nombre");
                return;
            }
            int mode = JOptionPane.showConfirmDialog(null, "¿Quieres crear una salita?"
                    + "\nSí: crear una nueva sala.\nNo: únete a una sala existente.", "Selecciona una opción", JOptionPane.YES_NO_OPTION);
            if (mode == 1) {
                host = JOptionPane.showInputDialog("Ingresa la IP");
                if (host == null || host.equals("")) {
                    JOptionPane.showMessageDialog(null, "De antemano, te pido que agregues la IP");
                    return;
                }
            } else {
                room = JOptionPane.showInputDialog("Ingresa el nombre de la sala:");
            }
            Chat obj = new Chat(name, mode, host, room);
            obj.setVisible(true);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }
  
    
    

    public void broadcast(String str) {
        try {
            DatagramPacket pack = new DatagramPacket(str.getBytes(), str.length());
            for (int i = 0; i < ClientList.size(); i++) {
                pack.setAddress(InetAddress.getByName(ClientList.get(i).ip));
                pack.setPort(ClientList.get(i).port);
                socket.send(pack);
            }
            jTextArea1.setText(jTextArea1.getText() + "\n" + str);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(cht, ex);
        }
    }
    
    
    /*
    Este es el host
    */
    public void sendToHost(String str) {
        DatagramPacket pack = new DatagramPacket(str.getBytes(), str.length(), host, 37988);
        try {
            socket.send(pack);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(cht, "La conexión con los servidores falló.");
        }
    }
   
    
    /*
    Aqui sera el chat o el tipo messenger 
    */
    Thread Messenger = new Thread() {
        public void run() {
            try {
                while (true) {
                    b = new byte[300];
                    DatagramPacket pkt = new DatagramPacket(b, 300);
                    socket.setSoTimeout(0);
                    socket.receive(pkt);
                    String s = new String(pkt.getData());
                    if (mode == HOST_MODE) {
                        if (s.contains("!!^^")) {
                            client temp = new client();
                            temp.ip = pkt.getAddress().getHostAddress();
                            temp.port = pkt.getPort();
                            broadcast(s.substring(4, s.indexOf("^^!!")) + " Se unió.");
                            ClientList.add(temp);
                            s = "!!^^" + room + "^^!!";
                            pkt = new DatagramPacket(s.getBytes(), s.length(), InetAddress.getByName(temp.ip), temp.port);
                            socket.send(pkt);
                            btn_send.setEnabled(true);
                            txt_mymsg.setEnabled(true);
                        } else {
                            broadcast(s);
                        }
                    } else {
                        jTextArea1.setText(jTextArea1.getText() + "\n" + s);
                    }
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(cht, ex);
                System.exit(0);
            }
        }
    };
    
     /*
    
    Este es el chat 
    */
    public Chat(String myname, int mod, String ip, String room) {
        try {
            nombre = myname;
            mode = mod;
            host = InetAddress.getByName(ip);
            room = room;
            setLayout(null);
            setSize(400, 460);
            lbl_ipNroomName = new JLabel("", SwingConstants.CENTER);
            txt_mymsg = new JTextField();
            btn_send = new JButton("Send");
            jScrollPane1 = new JScrollPane();
            jTextArea1 = new JTextArea(8, 15);
            ClientList = new ArrayList<>();
            setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            add(lbl_ipNroomName);
            lbl_ipNroomName.setBounds(10, 10, getWidth() - 30, 40);
            add(txt_mymsg);
            cht = this;
            txt_mymsg.setBounds(10, lbl_ipNroomName.getY() + lbl_ipNroomName.getHeight(), getWidth() - 130, 30);
            add(btn_send);
            btn_send.setBounds(txt_mymsg.getWidth() + 20, txt_mymsg.getY(), 80, 30);
            jScrollPane1.setViewportView(jTextArea1);
            add(jScrollPane1);
            jScrollPane1.setBounds(10, btn_send.getY() + 40, lbl_ipNroomName.getWidth(), getHeight() - 20 - jScrollPane1.getY() - 110);
            btn_send.setEnabled(false);
            jTextArea1.setEditable(false);
            txt_mymsg.setEnabled(false);
            btn_send.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    String s = txt_mymsg.getText();
                    if (s.equals("") == false) {
                        if (mode == HOST_MODE) {
                            broadcast(nombre + ": " + s);
                        } else {
                            sendToHost(nombre + ": " + s);
                        }
                        txt_mymsg.setText("");
                    }
                }
            });

            if (mode == HOST_MODE) {
                socket = new DatagramSocket(37988);
                lbl_ipNroomName.setText("Dirección IP: " + InetAddress.getLocalHost().getHostAddress());
            } else {
                socket = new DatagramSocket();
                String reqresp = "!!^^" + nombre + "^^!!";
                DatagramPacket pk = new DatagramPacket(reqresp.getBytes(), reqresp.length(), host, 37988);
                socket.send(pk);
                b = new byte[300];
                pk = new DatagramPacket(b, 300);
                socket.setSoTimeout(6000);
                socket.receive(pk);
                reqresp = new String(pk.getData());
                if (reqresp.contains("!!^^")) {
                    room = reqresp.substring(4, reqresp.indexOf("^^!!"));
                    lbl_ipNroomName.setText("Sala: " + room);
                    btn_send.setEnabled(true);
                    txt_mymsg.setEnabled(true);
                } else {
                    JOptionPane.showMessageDialog(cht, "No hay respuesta del servidor.");
                    System.exit(0);
                }
            }
            Messenger.start();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }
    
    
}

/*
Este es el cliente 
*/
    class client {
        public String ip;
        public int port;
        public String name;
    }

