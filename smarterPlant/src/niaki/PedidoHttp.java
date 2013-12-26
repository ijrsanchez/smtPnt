package niaki;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;

public class PedidoHttp {

	/**
	 * @param args
	 */

	public static void main(String[] args) {
		// actualizarWeb();
		// TODO Auto-generated method stub

	}

	public static void actualizarWeb(HiloNodo hiloNodo, GrupoMediciones medicion) {

		URL url;
		URLConnection urlConn;
		DataInputStream dis;
		String trama, stringUrl;

		System.out.println("Sensor : intentando actualizar web. "
				+ hiloNodo.getId());
		try {
			stringUrl = "http://localhost:80/index.html?index=";
			stringUrl = stringUrl.concat(hiloNodo.getIndex() + "&XbeeAddress"
					+ hiloNodo.getXbeeAddress64String() + "&an0="
					+ new Float(medicion.getAnalog_0()).toString() + "&an1="
					+ new Float(medicion.getAnalog_1()).toString() + "&an2="
					+ new Float(medicion.getAnalog_2()).toString() + "&an3="
					+ new Float(medicion.getAnalog_3()).toString() + "&di1="
					+ medicion.isDigital_in_1() + "&di2="
					+ medicion.isDigital_in_2() + "&do1="
					+ medicion.isDigital_out_1() + "&do2="
					+ medicion.isDigital_out_2());

			System.out.println("TRAMA= " + stringUrl.toString());
			url = new URL(stringUrl);
			urlConn = url.openConnection();
			urlConn.setDoInput(true);
			urlConn.setUseCaches(false);
			BufferedReader in = new BufferedReader(new InputStreamReader(
					url.openStream()));
			in.close();
			System.out.println("TRAMA= " + url);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("TRAMA FALLO");
			e.printStackTrace();
		}

	}

}
