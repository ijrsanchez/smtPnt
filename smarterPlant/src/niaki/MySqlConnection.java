package niaki;

/*
 * PruebaMySQL.java
 *
 * Programa de prueba para conexión a una base de datos de MySQL.
 * Presupone que el servidor de base de datos está arrancado, disponible,
 * en el puerto por defecto.
 * El usuario y password de conexión con la base de datos debe cambiarse.
 * En la base de datos se supone que hay una base de datos llamada prueba y que
 * tiene una tabla persona con tres campos, de esta manera:
 * mysql> create database prueba;
 * mysql> use prueba;
 * mysql> create table persona (id smallint auto_increment, nombre varchar(60), 
 *      nacimiento date, primary key(id)); 
 */

import java.sql.*;
import java.util.ResourceBundle;

import com.rapplogic.xbee.api.XBeeAddress64;

/**
 * Clase de prueba de conexión con una base de datos MySQL
 */
public class MySqlConnection {
    
    /** 
     * Crea una instancia de la clase MySQL y realiza todo el código 
     * de conexión, consulta y muestra de resultados.
     */
	Connection connection;
	
	public MySqlConnection() {
		try {
		// TODO Auto-generated constructor stub
		ResourceBundle rb = ResourceBundle.getBundle("config");
		
		String sqlHost = rb.getString("sqlHost");
		String sqlUser = rb.getString("sqlUser");
		String sqlPass = rb.getString("sqlPass");
		
		System.out.println("Se registra el Driver de MySQL");
        DriverManager.registerDriver(new org.gjt.mm.mysql.Driver());
        
        System.out.println("Se obtiene una conexión con la base de datos.");
        connection = DriverManager.getConnection ("jdbc:mysql://"+sqlHost+"/smart",sqlUser, sqlPass);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
    public Connection getConnection() {
		return connection;
	}



    public void persistir(Medicion medicion){
    	try
        {
            // Se registra el Driver de MySQL        
            System.out.println("Se crea un Statement, para realizar la consulta");
            // Se crea un Statement, para realizar la consulta
            Statement s = connection.createStatement();
                        
            System.out.println("Se realiza la consulta. Los resultados se guardan en el ");
            
            ///TODO: traer timestamp desde la medicion
            s.execute("INSERT INTO `mediciones` (puerto_id,valor,timestamp) VALUES ("+medicion.getPuerto_id()+","+medicion.getValor()+",now())");                     
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    	
    }
    
	public void persistir(GrupoMediciones medicion) 
    {
        // Se mete todo en un try por los posibles errores de MySQL
        try
        {
            // Se registra el Driver de MySQL
        
            System.out.println("Se crea un Statement, para realizar la consulta");
            // Se crea un Statement, para realizar la consulta
            Statement s = connection.createStatement();
            
            
            System.out.println("Se realiza la consulta. Los resultados se guardan en el ");
            s.execute("INSERT INTO `mediciones` (puerto_id,valor,timestamp) VALUES (1,"+medicion.getAnalog_0()+",now())");
            s.execute("INSERT INTO `mediciones` (puerto_id,valor,timestamp) VALUES (2,"+medicion.getAnalog_1()+",now())");
            s.execute("INSERT INTO `mediciones` (puerto_id,valor,timestamp) VALUES (3,"+medicion.getAnalog_2()+",now())");
            s.execute("INSERT INTO `mediciones` (puerto_id,valor,timestamp) VALUES (4,"+medicion.getAnalog_3()+",now())");
            
            // Se realiza la consulta. Los resultados se guardan en el 
            // ResultSet rs
            //ResultSet rs = s.executeQuery ("select * from persona");
            
            // Se recorre el ResultSet, mostrando por pantalla los resultados.
//            while (rs.next())
//            {
//                System.out.println (rs.getInt ("Id") + " " + rs.getString (2)+ 
//                    " " + rs.getDate(3));
//            }
//            
            // Se cierra la conexión con la base de datos.
     //       System.out.println("Se cierra la conexión con la base de datos.");
     //       connection.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    /**
     * Método principal, instancia una clase PruebaMySQL
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
        //new MySqlConnection().persistirMedicion(new Medicion(new XBeeAddress64(), 123, 321, 222, 333, true, true, true, true, 33));
    	//new MySqlConnection().getNodoId("0013A2004032E005");
    //	new MySqlConnection().getPuertos(1);
    	new MySqlConnection().getSensor(1);
    }



	public int getNodoId(String xbeeAddress64) {
		// TODO Auto-generated method stub
			int id = 0;
        	Statement s;
			try {
				s = connection.createStatement();
				s.execute("SELECT id FROM  `nodos` WHERE  `xbee_addr` =  '"+xbeeAddress64+"'");
				ResultSet rs = s.getResultSet();
				while (rs.next()){
					id = rs.getInt("id");
				}
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("ID: "+id);
			return id;	
	}
	
	public ResultSet getPuertos(int nodoId) {		
    	Statement s;
    	String detalle;
    	ResultSet rs = null;
    	int id;
		try {
			s = connection.createStatement();
			s.execute("SELECT * FROM  `puertos` WHERE  `nodo_id` =  '"+nodoId+"'");
			rs = s.getResultSet();
	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rs;
	}
	

	public void getSensor(int sensorId) {		
    	Statement s;
    	String detalle;
    	int multiplicador;
    	int adicion;
    	int id;
		try {
			s = connection.createStatement();
			s.execute("SELECT * FROM  `sensores` WHERE  `id` =  '"+sensorId+"'");
			ResultSet rs = s.getResultSet();
			while (rs.next()){
				id = rs.getInt("id");
				detalle = rs.getString("descripcion");
				multiplicador= rs.getInt("multiplicador");
				adicion = rs.getInt("adicion");
				System.out.println("Sensor: Id: "+id+" Detalle: "+detalle+
						"mult: "+multiplicador+"ad: "+adicion );
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}



	public int getPosicion(int puertoId) {
		// TODO Auto-generated method stub
		Statement s;
    	String detalle;
    	int posicion=-1;
		try {
			s = connection.createStatement();
			s.execute("SELECT * FROM  `puertos` WHERE  `id` =  '"+puertoId+"'");
			ResultSet rs = s.getResultSet();
			if (rs.next()){
				posicion = rs.getInt("direccion");				
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return posicion;
		
	}
	
    
}
