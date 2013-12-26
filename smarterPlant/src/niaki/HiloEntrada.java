package niaki;


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */




import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.sql.Date;
import java.sql.Time;
import java.util.ResourceBundle;
import java.util.Vector;
//import java.util.logging.Level;
//import java.util.logging.Logger;


/**
 *
 * @author jara
 */

public class HiloEntrada extends Thread{
	InputStream isComm;
	String url=null;
	Vector<HiloNodo> nodos = new Vector<HiloNodo>();;
	String trama="";
	ResourceBundle rb;

	public HiloEntrada(String paquete){    	
		isComm = new ByteArrayInputStream(paquete.getBytes());
		rb = ResourceBundle.getBundle("id");
		if (rb == null) {System.out.println("Rb no funca");}
		else {System.out.println("Rb si funca");}
	}

	HiloNodo buscarNodo(String id) {
		System.out.println("Buscando Nodo: ID"+nodos.size());
		for (int i =0; i<nodos.size();i++){                        
			HiloNodo s = (HiloNodo)nodos.elementAt(i);
		
			if (s.xbeeAddress64String.equalsIgnoreCase(id)){
				return(s);
			}              
		}
		return (null);
	}

	void eliminarNodo(HiloNodo s) {
		try{
			System.out.println("elimnando nodo;"); 
			nodos.removeElement(s);    
		}
		catch(Exception e){
			e.printStackTrace();
		}       
	}

	public void procesarMedicion(GrupoMediciones medicion){

		String xbeeAddress64,temp,rssi, tension;    
		Float t=new Float(0);
		HiloNodo unNodo;

		xbeeAddress64 = arrayToString(medicion.getxBeeAddress64().getAddress(),"");
		
		MySqlConnection mySqlConnection = new MySqlConnection();
		

		try {    
			unNodo = buscarNodo(xbeeAddress64);

			if (unNodo != null) {
				try {
					unNodo.actualizarEstado(medicion);
				} catch (Exception e) {            
					e.printStackTrace();
				}

			} else {
				System.out.println("BUSCO Nodo:" + xbeeAddress64);
				try {
					//int id_nro = new Integer(rb.getString(xbeeAddress64.toUpperCase()));
					
					int id_nro = mySqlConnection.getNodoId(xbeeAddress64);

					System.out.println("Numero Nodo" + id_nro);

					System.out.println("CREO Nodo ID:" + xbeeAddress64);

					HiloNodo nodoNuevo = new HiloNodo(xbeeAddress64, new Monitor(), this, id_nro);											

					nodos.addElement(nodoNuevo);

					nodoNuevo.start();

					System.out.println("Cantidad de Nodos:"+nodos.size());
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("No se encuentra Nodo");
				}

			}    
			trama="";
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public void run() {
		System.out.println("HiloEntrada is running..");
		String laTrama;
		while(true){
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





