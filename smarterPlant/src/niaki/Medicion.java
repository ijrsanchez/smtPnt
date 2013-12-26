package niaki;

import java.util.Date;

public class Medicion {
	int id;
	int puertoId;
	int valor;
	Date timeStamp;
	
	public Medicion(int id, int puertoId, int valor, Date timeStamp) {
		super();
		this.id = id;
		this.puertoId = puertoId;
		this.valor = valor;
		this.timeStamp = timeStamp;
	}

	public int getId() {
		return id;
	}

	public int getPuerto_id() {
		return puertoId;
	}

	public int getValor() {
		return valor;
	}

	public Date getTimeStamp() {
		return timeStamp;
	}		
	
	
}
