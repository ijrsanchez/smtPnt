package niaki;

import java.util.Date;

public class Puerto {
	int id;
	int nodo_id;
	int direccion;
	int sensor_id;
	boolean baja;
	String pnid;
	String detalle;
	int refresco;
	Date lastUpdate;
		
	public Puerto(int id, int nodo_id, int direccion, int sensor_id,
			boolean baja, String pnid, String detalle, int refresco) {
		super();
		this.id = id;
		this.nodo_id = nodo_id;
		this.direccion = direccion;
		this.sensor_id = sensor_id;
		this.baja = baja;
		this.pnid = pnid;
		this.detalle = detalle;
		this.refresco = refresco;
	}

	public Date getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
	
	
	
	
}
