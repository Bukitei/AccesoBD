package conections;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class Mysql {

	public Connection conexion;
	
	
	public Mysql() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/bdresidenciasescolares","root","");
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
	
	public Mysql(String bd) {
		
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
				conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/+"+bd,"root","");
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
