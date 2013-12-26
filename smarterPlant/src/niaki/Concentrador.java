package niaki;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.InputStream;
import java.net.InetAddress;
import java.util.ResourceBundle;
import java.util.Vector;

import gnu.io.CommPortIdentifier;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.rapplogic.xbee.api.ApiId;
import com.rapplogic.xbee.api.PacketListener;
import com.rapplogic.xbee.api.XBee;
import com.rapplogic.xbee.api.XBeeResponse;
import com.rapplogic.xbee.api.wpan.IoSample;
import com.rapplogic.xbee.api.wpan.RxResponseIoSample;
import com.rapplogic.xbee.api.zigbee.ZNetRxIoSampleResponse;
import com.rapplogic.xbee.examples.zigbee.ZNetIoSampleExample;


public class Concentrador {
	
	 
			
	public static void main(String[] args) {

		try {						
			
			ResourceBundle rb = ResourceBundle.getBundle("config");
																		
			XBeeSamplesReceiver xB = new XBeeSamplesReceiver(rb.getString("commPort"));
			
			

			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
