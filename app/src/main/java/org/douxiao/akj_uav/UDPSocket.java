package org.douxiao.akj_uav;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class UDPSocket implements Runnable{


    private String name;
    private String ip;
    private int port;

    public UDPSocket(String name,String ip, int port){
        this.name = name;
        this.ip = ip;
        this.port = port;
    }
    public void run() {
        // Ïò¾ÖÓòÍøUDP¹ã²¥ÐÅÏ¢
        try {
            InetAddress serverAddress = InetAddress.getByName(ip);
            DatagramSocket s = new DatagramSocket(port);
            byte[] buf = name.getBytes();
            DatagramPacket packet = new DatagramPacket(buf, buf.length,
                    serverAddress, port);
            s.send(packet);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}