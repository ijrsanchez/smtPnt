package niaki;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ResourceBundle;

import com.ghgande.j2mod.modbus.ModbusCoupler;
import com.ghgande.j2mod.modbus.io.ModbusTCPTransaction;
import com.ghgande.j2mod.modbus.msg.WriteMultipleRegistersRequest;
import com.ghgande.j2mod.modbus.msg.WriteSingleRegisterRequest;
import com.ghgande.j2mod.modbus.net.ModbusTCPListener;
import com.ghgande.j2mod.modbus.net.TCPMasterConnection;
import com.ghgande.j2mod.modbus.procimg.Register;
import com.ghgande.j2mod.modbus.procimg.SimpleProcessImage;
import com.ghgande.j2mod.modbus.procimg.SimpleRegister;

public class ModBusSlave {
	
	ModbusTCPListener listener = null;
	SimpleProcessImage spi = null;
	Integer integer_port;	
	
	TCPMasterConnection con = null; //the connection
	ModbusTCPTransaction trans = null; //the transaction
	
	
	ResourceBundle rb = ResourceBundle.getBundle("config");
	
	static InetAddress addr = null; //the slave's address
	
	int port;
	
	
	public ModBusSlave(String portModBus, int index) {
		super();
		integer_port = Integer.parseInt(portModBus);
		port = integer_port.intValue();

		//2. Prepare a process image
		  spi = new SimpleProcessImage();		  	    	  
		  
		  for (int i=0;i<8;i++){	    		  
			  spi.addRegister(new SimpleRegister(0));
		  }

		  //3. Set the image on the coupler
		  ModbusCoupler.getReference().setProcessImage(spi);
		  ModbusCoupler.getReference().setMaster(false);
		  ModbusCoupler.getReference().setUnitID(index);   
		  
		//4. Create a listener with 3 threads in pool
		  listener = new ModbusTCPListener(3);    	  
		  listener.setPort(port);
		  try {
			//listener.setAddress(InetAddress.getByName("127.0.0.1"));
			listener.start();  
		  System.out.println("Listening:"+InetAddress.getByName("127.0.0.1").toString()+":"+ port);
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		
	}
	
	public void setValues(Medicion medicion) {
		// TODO Auto-generated method stub
		System.out.println("Actualizando ModBus");
	    	try {
	    System.out.println("   -> Abriendo Conexion");
	    addr = InetAddress.getByName(rb.getString("masterIp"));
  	    
	    con = new TCPMasterConnection(addr);
  	    
	    con.setPort(port);  	    	    
  	    
  	    con.connect();
  	    
  	    if (con.isConnected()) {
  	    	System.out.println("   -> Seteo Timeout");
  	    	con.setTimeout(1000);
  	    }

  	    //3. Prepare the request
  	    System.out.println("   -> Preparando Registros");  	   
  	   
  	  WriteSingleRegisterRequest req0 = new WriteSingleRegisterRequest(new MySqlConnection().getPosicion(medicion.puertoId), new SimpleRegister(medicion.valor));
  	        	      	    
  		trans = new ModbusTCPTransaction(con);
  	    
  	    //4. Prepare the transaction  	      	     	    
  	    trans.setRequest(req0);
  	   
	  	System.out.println("   -> Ejecutando actualizaci�n");
  	    trans.execute();
	  	con.close();
	  	System.out.println("   -> Cierro conexi�n");
	  	}
	    catch (Exception e) {
		  		e.printStackTrace();
		  		}	  			
	}

	public void setValues(GrupoMediciones medicion_actual) {
		// TODO Auto-generated method stub

		System.out.println("Actualizando ModBus");
	    	try {
  	  //2. Open the connection
	    System.out.println("   -> Abriendo Conexion");
	    addr = InetAddress.getByName("192.168.0.110");
  	    
	    con = new TCPMasterConnection(addr);
  	    
	    con.setPort(port);  	    	    
  	    
  	    con.connect();
  	    
  	    if (con.isConnected()) {
  	    	System.out.println("   -> Seteo Timeout");
  	    	con.setTimeout(1000);
  	    }

  	    //3. Prepare the request
  	    System.out.println("   -> Preparando Registros");
  	    Register[] registers = {  	    		
  	    		new SimpleRegister(medicion_actual.getAnalog_0()),
  	    		new SimpleRegister(medicion_actual.getAnalog_1()),
  	    		new SimpleRegister(medicion_actual.getAnalog_2()),
  	    		new SimpleRegister(medicion_actual.getAnalog_3()),
  	    		};  	      	   
  	    
  		WriteMultipleRegistersRequest writeMultipleRegistersRequest = 
  				new WriteMultipleRegistersRequest(0, registers);
  	    
  	    
  		trans = new ModbusTCPTransaction(con);
  		/*
  	    WriteSingleRegisterRequest req0 = new WriteSingleRegisterRequest(0, new SimpleRegister(medicion_actual.getAnalog_0()));
  	    WriteSingleRegisterRequest req1 = new WriteSingleRegisterRequest(1, new SimpleRegister(medicion_actual.getAnalog_1()));
  		WriteSingleRegisterRequest req2 = new WriteSingleRegisterRequest(2, new SimpleRegister(medicion_actual.getAnalog_2()));
  		WriteSingleRegisterRequest req3 = new WriteSingleRegisterRequest(3, new SimpleRegister(medicion_actual.getAnalog_3()));*/
  		
  	    
  	    //4. Prepare the transaction
  	      	     	    
  	    trans.setRequest(writeMultipleRegistersRequest);
  	   
	  	
	  	/*
	  	trans.setRequest(req1);
	  	trans.execute();
	  	trans.setRequest(req2);
	  	trans.execute();
	    trans.setRequest(req3);
	  	trans.execute();*/
  	    
  	    //trans.setRequest(writeMultipleRegistersRequest);
	  	System.out.println("   -> Ejecutando actualizaci�n");
  	    trans.execute();
	  	con.close();
	  	System.out.println("   -> Cierro conexi�n");
	  	}
	    catch (Exception e) {
		  		e.printStackTrace();
		  		}
	  	
		
	}
		
	
	
}
