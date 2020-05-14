package basic;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
/*
1. CommPortIdentifier를 포트의 유효성과 통신 가능상태인지 점검
2. CommPortIdentifier의 open메소드를 이용해서 시리얼통신을 할 수 있는 준비 상태로 셋팅
   => 시리얼통신을 하기 위해 필요한 포트객체가 리턴
3. CommPort는 종류가 두가지
   -Serial
   -Parallel
   : CAN통신은 Serial통신, 아두이노와 라떼판다도 Serial통신
   => 각 상황에 맞는 CommPort객체를 얻어야 작업할 수 있다.

*/


public class SerialConnectionTest {
	public SerialConnectionTest() {
		
	}
	public void connect(String portName) {
		try {
			//COM포트가 실제 존재하고 사용가능한 상태인지 확인
			CommPortIdentifier commPortIdentifier = 
					CommPortIdentifier.getPortIdentifier(portName);
			//포트가 사용중인지 체크
			if(commPortIdentifier.isCurrentlyOwned()) {
				System.out.println("포트를 사용할 수 없습니다.");
			}else {
				System.out.println("포트 사용가능");
				//port가 사용 가능하면 포트를 열고 포트객체를 얻어오기
				//매개변수1 : 포트를 열고 사용하는 프로그램의 이름(문자열)
				//매개변수2 : 포트를 열고 통신하기 위해서 기다리는 시간(밀리세컨드)
				CommPort commPort = commPortIdentifier.open("basic_serial", 3000);
				System.out.println(commPort);
			}
		} catch (NoSuchPortException e) {
			e.printStackTrace();
		} catch (PortInUseException e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		new SerialConnectionTest().connect("COM7");
	}
}
