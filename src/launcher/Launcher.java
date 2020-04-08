package launcher;
import java.awt.EventQueue;

import controllers.AStarContreller;
import controllers.DataController;
import viewers.WindowViewer;

public class Launcher {

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					
					DataController data = new DataController();
					System.out.println("Estaciones:");
					System.out.println(data.getListNameStation());
					System.out.println("Estaciones cabeceras por línea:");
					System.out.println(data.getMapLines());
					AStarContreller a_star = new AStarContreller(data);
					
					WindowViewer window = new WindowViewer();
					window.setData(data);
					window.setAStar(a_star);
					window.start();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
