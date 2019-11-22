package conections;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class Access {

		public Connection conexion;
		
		
		public Access() {
			try {
				Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
				conexion = DriverManager.getConnection("jdbc:ucanaccess://Access/bdResidenciasEscolaresAccess.accdb");
			} catch (ClassNotFoundException e) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("ERROR");
				alert.setHeaderText("Driver no encontrado");
				alert.setContentText("Consulta con Borja para más información");

				alert.showAndWait();
			} catch (SQLException e) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("ERROR");
				alert.setHeaderText("Algo falló en la consulta");
				alert.setContentText("Consulta con Borja para más información");

				alert.showAndWait();
			}
		}
		
		public Access(String bd) {
			
				try {
					Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
					conexion = DriverManager.getConnection("jdbc:ucanaccess://Access/"+bd+".accdb");
				} catch (ClassNotFoundException e) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("ERROR");
					alert.setHeaderText("Driver no encontrado");
					alert.setContentText("Consulta con Borja para más información");

					alert.showAndWait();
				} catch (SQLException e) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("ERROR");
					alert.setHeaderText("Algo falló en la consulta");
					alert.setContentText("Consulta con Borja para más información");

					alert.showAndWait();
				}
			
		}	
	
}
