package niaki;
import com.rapplogic.xbee.api.XBeeAddress64;


public class GrupoMediciones {	
	public GrupoMediciones(XBeeAddress64 xbeeAddress64, int analog_1, int analog_2,
			int analog_3, int analog_0, boolean digital_in_1,
			boolean digital_in_2, boolean digital_out_1, boolean digital_out_2,
			int supplyVoltage) {
		super();	
		this.xBeeAddress64 = xbeeAddress64;
		this.analog_1 = analog_1;
		this.analog_2 = analog_2;
		this.analog_3 = analog_3;
		this.analog_0 = analog_0;
		this.digital_in_1 = digital_in_1;
		this.digital_in_2 = digital_in_2;
		this.digital_out_1 = digital_out_1;
		this.digital_out_2 = digital_out_2;
		this.supplyVoltage = supplyVoltage;
	}
	
	int analog_1, analog_2,analog_3,analog_0;
	int supplyVoltage;
	
	public int getSupplyVoltage() {
		return supplyVoltage;
	}

	boolean digital_in_1, digital_in_2;
	boolean digital_out_1, digital_out_2;
	
	XBeeAddress64 xBeeAddress64;
	
	public XBeeAddress64 getxBeeAddress64() {
		return xBeeAddress64;
	}
	public void setxBeeAddress64(XBeeAddress64 xBeeAddress64) {
		this.xBeeAddress64 = xBeeAddress64;
	}
	public int getAnalog_1() {
		return analog_1;
	}
	public void setAnalog_1(int analog_1) {
		this.analog_1 = analog_1;
	}
	public int getAnalog_2() {
		return analog_2;
	}
	public void setAnalog_2(int analog_2) {
		this.analog_2 = analog_2;
	}
	public int getAnalog_3() {
		return analog_3;
	}
	public void setAnalog_3(int analog_3) {
		this.analog_3 = analog_3;
	}
	public int getAnalog_0() {
		return analog_0;
	}
	public void setAnalog_0(int analog_0) {
		this.analog_0 = analog_0;
	}
	public boolean isDigital_in_1() {
		return digital_in_1;
	}
	public void setDigital_in_1(boolean digital_in_1) {
		this.digital_in_1 = digital_in_1;
	}
	public boolean isDigital_in_2() {
		return digital_in_2;
	}
	public void setDigital_in_2(boolean digital_in_2) {
		this.digital_in_2 = digital_in_2;
	}
	public boolean isDigital_out_1() {
		return digital_out_1;
	}
	public void setDigital_out_1(boolean digital_out_1) {
		this.digital_out_1 = digital_out_1;
	}
	public boolean isDigital_out_2() {
		return digital_out_2;
	}
	public void setDigital_out_2(boolean digital_out_2) {
		this.digital_out_2 = digital_out_2;
	}
	
	
	
}
