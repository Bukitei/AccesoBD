package conections;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class SQL {
public Connection conexion;
	
	
	public SQL() {
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			conexion = DriverManager.getConnection("jdbc:sqlserver://localhost;database=bdResidenciasEscolares","sa","sa");
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
	
	public SQL(String bd) {
		
			try {
				Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
				conexion = DriverManager.getConnection("jdbc:sqlserver://localhost;database="+bd,"sa","sa");
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
