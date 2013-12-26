package niaki;



import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

/**
 *
 * @author jara
 */
public class HiloNodo extends Thread {
    
	int index; //Index al array de valores del 485
    String xbeeAddress64String; //MAC del M�dulo Xbee  
    
    MySqlConnection mySqlConnection;
    
    public String getXbeeAddress64String() {
		return xbeeAddress64String;
	}


	public void setXbeeAddress64String(String xbeeAddress64String) {
		this.xbeeAddress64String = xbeeAddress64String;
	}

	//Control del Thread
    Monitor myMonitor;
    Timer timerSuicida,timerScheduledUpdate;
    HiloEntrada unHiloEntrada;
    MyTaskSuicide mySuicide= new MyTaskSuicide(this);
    MyTaskScheduledUpdate myTaskScheduledUpdate= new MyTaskScheduledUpdate(this);
    int schedule=0;
    Suicide suicideTimer;
    Date lastUpdateTime;
    
    //Mediciones
    GrupoMediciones grupoMedicionesAnterior, grupoMedicionesActual;

    //Control de actualizaciones
    boolean update=true, merezcovivir=true;    

    
    String trama, stringUrl;
        
    URL                url; 
    URLConnection      urlConn; 
    DataInputStream    dis;
    
    ModBusSlave modBusSlave;
    Vector<Puerto> Puertos;
    

    HiloNodo(String id, Monitor unMonitor, HiloEntrada he, int index){
        this.index = index;
    	this.xbeeAddress64String=id;
        this.myMonitor = unMonitor;        
        unHiloEntrada = he;
        timerSuicida = new Timer();        
        timerScheduledUpdate = new Timer(); 
        this.suicideTimer = new Suicide(this);
        suicideTimer.schedule();
        lastUpdateTime = new Date(0);
        System.out.println("HN: Nodo creado"+ id);        
        modBusSlave = new ModBusSlave("502",index);
        mySqlConnection = new MySqlConnection();
        Puertos = new Vector<Puerto>();
        popularPuertos(mySqlConnection.getPuertos(index));            
        };
        
        	
        	/*		while (rs.next()){
			id = rs.getInt("id");
			detalle = rs.getString("detalle");
			System.out.println("Puerto: Id: "+id+" Detalle:"+detalle);
		}*/
        
    
    
    
private void popularPuertos(ResultSet resultSet) {
		// TODO Auto-generated method stub
	try {
		while (resultSet.next()) {
			//	id	nodo_id	direccion	sensor_id	baja	pnid	detalle
			Puertos.add(
					new Puerto(resultSet.getInt("id"), 
									resultSet.getInt("nodo_id"),
									resultSet.getInt("direccion"),
									resultSet.getInt("sensor_id"),
									resultSet.getBoolean("baja"),
									resultSet.getString("pnid"),
									resultSet.getString("detalle"),
									resultSet.getInt("refresco")
									)
					);
		}
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    
	}


public int getIndex() {
		return index;
	}


	public void setIndex(int index) {
		this.index = index;
	}


public void actualizarEstado(GrupoMediciones grupoMediciones){
	
	if (grupoMedicionesActual!=null) 
		grupoMedicionesAnterior=grupoMedicionesActual;
	
	grupoMedicionesActual = grupoMediciones;
    suicideTimer.cancel();
    suicideTimer = new Suicide(this);
    suicideTimer.schedule();
    
   for (Puerto puerto : Puertos) {
	   System.out.println("Ultima Actualizacion> "+lastUpdateTime.toString());
		if (puerto.getLastUpdate().before(new Date(System.currentTimeMillis()-puerto.refresco))){
			puerto.setLastUpdate(new Date(System.currentTimeMillis()));						
			mySqlConnection.persistir(
					new Medicion(0, 1, 1, 
							new Date(System.currentTimeMillis())
							)
					);
		}  
	myMonitor.put(1);	
}    
	
}
    
public void run() {
   int wassanow;
    boolean merezcovivir = true;
    
    /*
     * wassanow sería el indicador de por qué despierto.
     * 1: el lector me actualizo los datos
     * 2: no merezco vivir.
     * etc.
     */
    
    while (merezcovivir) {
            
        wassanow = myMonitor.get();

        switch (wassanow) {
            
            case 1:
            	System.out.println("Nodo "+ xbeeAddress64String + ": actualizando ModBus.");            	
            	//modBusSlave.setValues(grupoMedicionesActual);
           
            	for (Puerto puerto : Puertos) {
            		   System.out.println("Ultima Actualizacion> "+lastUpdateTime.toString());
            			if (puerto.getLastUpdate().before(new Date(System.currentTimeMillis()-puerto.refresco))){
            				puerto.setLastUpdate(new Date(System.currentTimeMillis()));						
            				mySqlConnection.persistir(
            						new Medicion(0, 1, 1, 
            								new Date(System.currentTimeMillis())
            								)
            						);
            			}  
            		myMonitor.put(1);	
            	}    
            	
            	
            	/*
                System.out.println("Nodo "+ xbeeAddress64String + ": actualizar Web o lo que sea.");                                      
                                
                PedidoHttp.actualizarWeb(this,medicion_actual);
                */
            	
                break;
                
            case 2: 
                System.out.println("Nodo "+ xbeeAddress64String + ": no merezco vivir. Hacer algo.");                
                //enviarTestamento();
                //modBusSlave.setSlaveValue(index, 0);
                unHiloEntrada.eliminarNodo(this);
                merezcovivir = false;                
                break;
                
            default:
                break;
        }      
     }
                 
}


void enviarTestamento(){
	//PedidoHttp.actualizarWeb(this.id,0,0,0);
	System.out.println("Nodo "+ xbeeAddress64String + ": He muerto. ");
}        
            
     String getViaHttpConnection(String url) throws IOException {
    	 URL url2 = new URL(url);
    	 

         try {
        	 urlConn = url2.openConnection(); 
             urlConn.setDoInput(true); 
             urlConn.setUseCaches(false);
             
             
             /*url2.getContent();
             rc = c.getResponseCode();
             if (rc != HttpConnection.HTTP_OK) {
                 if (rc==HttpConnection.HTTP_MOVED_PERM||rc==HttpConnection.HTTP_MOVED_TEMP){
                      new_uri=c.getHeaderField("Location");
                     c.close();
                     c = (HttpConnection) Connector.open(new_uri); // Breaks here
                     System.out.println(new_uri);
                 } else throw new IOException("HTTP response code: " + rc);
             } else {
                 new_uri=url;
             }
             is = c.openInputStream();
             // Get the ContentType
             String type = c.getType();
             // Get the length and process the data
                int len = (int)c.getLength();
                /*int ch;
                while ((ch = is.read()) != -1) {                     
                     System.out.print((char)ch);                                  
             }*/
         } catch (ClassCastException e) {
             throw new IllegalArgumentException("Not an HTTP URL");
         } finally {
            /* if (is != null)
                 is.close();
             if (c != null)
                 c.close();*/
         }
         return url2.toExternalForm();
     }
}


    class MyTaskScheduledUpdate extends TimerTask {
    HiloNodo myHilo; 
        MyTaskScheduledUpdate(HiloNodo myHilo){
            this.myHilo=myHilo;
        }
        public void run() {
            myHilo.update=true;
            myHilo.notify();
    }
}       
    
class MyTaskSuicide extends TimerTask {
        HiloNodo myHilo; 
        MyTaskSuicide(HiloNodo myHilo){
                this.myHilo=myHilo;
        }        
        public void run() {
            myHilo.merezcovivir=false;
            myHilo.notify();            
    }
}    
   

