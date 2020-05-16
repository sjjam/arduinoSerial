package android.control;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;


public class ReceiverThread extends Thread{
	Socket socket;
	private BufferedReader br;
	private PrintWriter pr;
	SerialArduinoLEDTest serialObj;
	OutputStream out;
	public ReceiverThread(Socket socket) {
		this.socket = socket;
		try {
			br = new BufferedReader(
					new InputStreamReader(
							socket.getInputStream()));
			pr = new PrintWriter(socket.getOutputStream());
			serialObj = new SerialArduinoLEDTest();
			serialObj.connect("COM7");
			out = serialObj.getOutput();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	public void run() {
			while(true){
				try {
					String msg = br.readLine();
					System.out.println("클라이언트에게 받은 메시지:"+msg);
					if(msg.equals("led_on")) {
						out.write('1');
					}else {
						out.write('0');
					}
				} catch (IOException e) {
					System.out.println(e);
				}
			}
	}
}
