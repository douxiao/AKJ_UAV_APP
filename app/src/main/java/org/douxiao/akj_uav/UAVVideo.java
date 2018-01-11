package org.douxiao.akj_uav;

import java.net.DatagramSocket;
import java.net.SocketException;

public class UAVVideo extends Thread {

     private DatagramSocket videoStreamSocket;

     public UAVVideo() throws SocketException {

//         videoStreamSocket = new DatagramSocket(Constant.VIDEO_PORT);
         videoStreamSocket.setSoTimeout(3000);

     }




}