package serial.read;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

public class ArduinoSerialReadUsingThread {
	public static void main(String[] args) {
		try {
			CommPortIdentifier commPortIdentifier = 
					CommPortIdentifier.getPortIdentifier("COM7");
			if(commPortIdentifier.isCurrentlyOwned()) {
				System.out.println("��Ʈ ����� �� �����ϴ�.");
			}else {
				System.out.println("��Ʈ ��밡��.");
				CommPort commPort =
						commPortIdentifier.open("basic_serial",
								5000);
				if(commPort instanceof SerialPort) {
					System.out.println("SerialPort");
					SerialPort serialPort = (SerialPort)commPort;
					//SerialPort�� ���� ����
					serialPort.setSerialPortParams(9600,
							SerialPort.DATABITS_8,
							SerialPort.STOPBITS_1,
							SerialPort.PARITY_NONE);
					InputStream in = serialPort.getInputStream();
					OutputStream out = serialPort.getOutputStream();
					
					//Arduino�� ���ؼ� �ݺ��ؼ� ������ �����͸� ���� �� �ֵ��� 
					//�ڵ� �ۼ� - ������� ó��
					Thread readThread = new Thread(new Runnable() {
						@Override
						public void run() {
							byte[] buffer = new byte[1024];
							int len=-1;
							try {
								while((len=in.read(buffer))!=-1) {
									System.out.println(new String(buffer,0,len));
									Thread.sleep(1000);
								}
							}catch(IOException e) {
								e.printStackTrace();
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					});
					readThread.start();
					
				}else {
					System.out.println("SerialPort�� �۾��� �� �ֽ��ϴ�.");
				}
			}
		} catch (NoSuchPortException e) {
			e.printStackTrace();
		} catch (PortInUseException e) {
			e.printStackTrace();
		} catch (UnsupportedCommOperationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
