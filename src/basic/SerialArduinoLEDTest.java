package basic;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;
/*
   1. CommPortIdentifier를 포트의 유효성과 통신 가능상태인지 점검
   2. CommPortIdentifier의 open메소드를 이용해서 시리얼통신을 할 수 있는 준비 상태로 셋팅
      => 시리얼통신을 하기 위해 필요한 포트객체가 리턴
   3. CommPort는 종류가 두가지
      -Serial
      -Parallel
      : CAN통신은 Serial통신, 아두이노와 라떼판다도 Serial통신
      => 각 상황에 맞는 CommPort객체를 얻어야 작업할 수 있다.
   4. CommPort를 SerialPort로 casting
   5. SerialPort객체의 setSerialPortParams메소드를 이용해서 Serial통신을 위한 기본내용을 설정
      setSerialPortParams
      (9600,               => 통신속도
      SerialPort.DATABITS_8,   => 전송하는 데이터의 길이
      SerialPort.STOPBITS_1,   => stop bit에 설정
      SerialPort.PARITY_NONE   => PARTIY 비트를 사용하지 않겠다고 결정
      )
      
      => Serial 포트를 open하고 설정을 잡아놓은 상태
      => 전달되는 데이터 frame을 받을 수 있는 상태
   6. 데이터를 주고받을 수 있도록 SerialPort객체에서 Input/Output 스트림을 어든다.
      => 바이트 단위로 데이터가 송수신되므로 Reader, Writer 계열의 스트림을 사용할 수 없고 InputStream, OutputStream객체를 사용해야 한다.
      시리얼 포트 객체.getInputStream()
      시리얼 포트 객체.getOutputStream()
   7. 데이터 수신과 송신에 대한 처리
      1) 쓰레드로 처리
      2) 이벤트에 반응하도록 처리
 */
public class SerialArduinoLEDTest {
	public SerialArduinoLEDTest() {
		
	}
	public void connect(String portName) {
		try {
			//COM포트가 실제 존재하고 사용가능한 상태인지 확인
			CommPortIdentifier commPortIdentifier = 
					CommPortIdentifier.getPortIdentifier(portName);
			//포트가 사용중인지 체크
			if(commPortIdentifier.isCurrentlyOwned()) {
				System.out.println("포트 사용할 수 없습니다.");
			}else {
				System.out.println("포트 사용가능.");
				//port가 사용 가능하면 포트를 열고 포트객체를 얻어오기
				//매개변수1 : 포트를 열고 사용하는 프로그램의 이름(문자열)
				//매개변수2 : 포트를 열고 통신하기 위해서 기다리는 시간(밀리세컨드)
				CommPort commPort =
						commPortIdentifier.open("basic_serial",
								5000);
				if(commPort instanceof SerialPort) {
					System.out.println("SerialPort");
					SerialPort serialPort = (SerialPort)commPort;
					//SerialPort에 대한 설정
					serialPort.setSerialPortParams(9600,
							SerialPort.DATABITS_8,
							SerialPort.STOPBITS_1,
							SerialPort.PARITY_NONE);
					InputStream in = serialPort.getInputStream();
					OutputStream out = serialPort.getOutputStream();
					
					//데이터를 주고 받는 작업을 여기에
					//안드로이드에서 입력받은 값을 아두이노로 전송하는 쓰레드 
					new SerialArduinoWriterThread(out).start();
					
				}else {
					System.out.println("SerialPort만 작업할 수 있습니다.");
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
	public static void main(String[] args) {
		new SerialArduinoLEDTest().connect("COM7");
	}
}
