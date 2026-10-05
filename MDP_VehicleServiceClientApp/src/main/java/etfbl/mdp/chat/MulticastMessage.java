package etfbl.mdp.chat;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;
import java.util.List;

import etfbl.mdp.constants.Constants;
import etfbl.mdp.logger.ClientLogger;

public class MulticastMessage extends Thread {

	public static List<String> messages = new ArrayList<>();
	
	public void run() {
		MulticastSocket socket = null;
		byte[] buffer = new byte[256];
		try {
			socket = new MulticastSocket(Constants.MULTICAST_PORT);
			InetAddress address = InetAddress.getByName(Constants.MULTICAST_HOST);
			socket.joinGroup(address);
			while(true) {
				DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                String received = new String(packet.getData(), 0, packet.getLength());
                messages.add(received);
			}
		}
		catch(Exception e) {
			 ClientLogger.logger.severe(e.getMessage());
		}
	}
}
