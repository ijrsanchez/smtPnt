package niaki;


import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.text.Format;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.rapplogic.xbee.*;
import com.rapplogic.xbee.api.*;
import com.rapplogic.xbee.api.wpan.IoSample;
import com.rapplogic.xbee.api.wpan.RxResponseIoSample;
import com.rapplogic.xbee.api.zigbee.ZNetRxIoSampleResponse;
import com.rapplogic.xbee.examples.zigbee.ZNetIoSampleExample;


public class XBeeSamplesReceiver implements PacketListener {
	
	
	
	private final static Logger log = Logger.getLogger(ZNetIoSampleExample.class);
	HiloEntrada hiloEntrada;
	XBee xbee;
	
	public XBeeSamplesReceiver(String commPort) throws Exception {
		
		// TODO Auto-generated constructor stub
	xbee = new XBee();
	
	//initProcess();
	
	hiloEntrada = new HiloEntrada("");

		try {			
			
			// replace with the com port of your XBee coordinator			
			//xbee.open("COM14", 9600);
			System.out.println("Abriendo puerto: "+commPort);
			xbee.open(commPort, 9600);
			xbee.addPacketListener(this);
		
			xbee.sendAsynchronous(new AtCommand("NT"));
			
			// wait forever
			synchronized(this) { this.wait(); }
		} finally {
			xbee.close();
		}
	}
	
	
	void xbee_pin_turn_on(XBeeAddress64 addr64,String NombrePin){

		RemoteAtRequest request = new RemoteAtRequest(addr64, NombrePin, new int[]{XBeePin.Capability.DIGITAL_OUTPUT_HIGH.getValue()});

		try {
			xbee.sendAsynchronous(request);
			RemoteAtResponse response = (RemoteAtResponse) xbee.getResponse();

			if (response.isOk()) {
				System.out.println("Successfully turned on "+NombrePin);
			} else {
				System.out.println("Attempt to turn on "+NombrePin+" failed.  Status: " + response.getStatus());
			}

		} catch (XBeeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	

	void xbee_pin_turn_off(XBeeAddress64 addr64,String NombrePin){

		RemoteAtRequest request = new RemoteAtRequest(addr64, NombrePin, new int[]{XBeePin.Capability.DIGITAL_OUTPUT_LOW.getValue()});

		try {
			xbee.sendAsynchronous(request);
			RemoteAtResponse response = (RemoteAtResponse) xbee.getResponse();

			if (response.isOk()) {
				System.out.println("Successfully turned off "+NombrePin);
			} else {
				System.out.println("Attempt to turn off "+NombrePin+" failed.  Status: " + response.getStatus());
			}

		} catch (XBeeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Called by XBee API thread when a packet is received
	 */
	public void processResponse(XBeeResponse response) {
		System.out.println("Processing response:"+response.getApiId().toString());
		
		
		// This is a I/O sample response.  You will only get this is you are connected to a Coordinator that is configured to
		// receive I/O samples from a remote XBee.
					
		if (response.getApiId() == ApiId.ZNET_IO_SAMPLE_RESPONSE) {
			System.out.println("Processing response:"+response.getApiId().toString());
			
			ZNetRxIoSampleResponse ioSample = (ZNetRxIoSampleResponse) response;
			//xbee_pin_turn_on(ioSample.getRemoteAddress64(),"P1");
			//xbee_pin_turn_off(ioSample.getRemoteAddress64(),"P0");
			
			//log.debug("received i/o sample packet.  contains analog is " + ioSample.containsAnalog() + ", contains digital is " + ioSample.containsDigital());
			
			// check the value of the input pins
			if ( false) {
			System.out.println("pin 20 (D0) analog is " + ioSample.getAnalog0());
			System.out.println("pin 19 (D1) analog is " + ioSample.getAnalog1());
			System.out.println("pin 18 (D2) analog is " + ioSample.getAnalog2());
			System.out.println("pin 17 (D3) analog is " + ioSample.getAnalog3());
			System.out.println("pin 24 (D4) digital is " + ioSample.isD4On());
			System.out.println("pin XX (D10) digital is " +ioSample.isD10On());
			System.out.println("pin XX (D11) digital is " +ioSample.isD11On());
			System.out.println("pin XX (D12) digital is " +ioSample.isD12On());
			System.out.println("Supply Voltage is " +ioSample.getSupplyVoltage());
			}
			
			String stringMedicion = new String("$"	+arrayToString(ioSample.getRemoteAddress64().getAddress(),"")
										+","+ioSample.getAnalog0()
										+","+ioSample.getAnalog1()
										+","+ioSample.getAnalog2()
										+","+ioSample.getAnalog3()
										+","+ioSample.isD4On()
										+","+ioSample.isD10On()		
										+","+ioSample.isD11On()		
										+","+ioSample.isD12On()		
										+","+ioSample.getSupplyVoltage());
						
			System.out.println(stringMedicion);
			
			//hiloEntrada.
			
			hiloEntrada.procesarMedicion(new GrupoMediciones(ioSample.getRemoteAddress64(), ioSample.getAnalog0(),					
			ioSample.getAnalog1(), ioSample.getAnalog2(), ioSample.getAnalog3(), 
			false, false, 
			false, false,0));
			
//			hiloEntrada.procesarMedicion(new Medicion(ioSample.getRemoteAddress64(), ioSample.getAnalog0(),					
//					ioSample.getAnalog1(), ioSample.getAnalog2(), ioSample.getAnalog3(), 
//					ioSample.isD4On(), ioSample.isD10On(), 
//					ioSample.isD11On(), ioSample.isD12On(),0));
			
		}
	}
	
	
	public static String arrayToString(int[] a, String separator) {
	    String result = "";	    
	  
	    if (a.length > 0) {
	        //result =(char) a[0];    // start with the first element
	        for (int i=0; i<a.length; i++) {
	          //result = result + Integer.toHexString(a[i]);
	            result = result + new String().format("%2x", a[i]);
	            
	        }
	    }
	    return result.replace(" ","0");
	}
}
