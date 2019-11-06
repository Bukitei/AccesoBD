package accesobd;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import conections.Mysql;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class accesoBDController implements Initializable {

	// Recogemos los datos del FXML

	@FXML
	private VBox view;

	@FXML
	private ComboBox<String> tipCombx;

	@FXML
	private TableView<Estancia> list;

	@FXML
	private TextField name, bdText;

	@FXML
	private Button insertBt, modifyBt, deleteBt, conecta;

	// Creamos los elementos del modelo
	
	private StringProperty bd = new SimpleStringProperty();
	private ObservableList<Estancia> listEstancia = FXCollections.observableArrayList();
	private ListProperty<Estancia> listaEstancia = new SimpleListProperty<Estancia>(listEstancia);
	

	// constructor

	public accesoBDController() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/accesoBDFXML.fxml"));
		loader.setController(this); // Este ser� nuestro controlador
		loader.load();
	}

	public void initialize(URL location, ResourceBundle resources) {
			
		ObservableList<String> servers = FXCollections.observableArrayList();
		servers.addAll("MySQL", "SQL Server", "Access");
		
		tipCombx.setItems(servers);
		tipCombx.setOnAction(evt -> onTypeAction(tipCombx.getValue().toLowerCase()));
		
		bdText.textProperty().bindBidirectional(bd);
		
		list.itemsProperty().bind(listaEstancia);
		
	}
	
	public VBox getView() {
		return view;
	}
	
	private void onTypeAction(String bd) {
		
		try {
			bdText.setDisable(false);
			conecta.setDisable(false);
			conecta.setOnAction(evt -> onConnectAction(bd, bd.toString()));
			
			
			
		}catch (Exception e) {
			Alert errorDriver = new Alert(AlertType.ERROR);
			errorDriver.setTitle("ERROR");
			errorDriver.setHeaderText("No se ha encontrado el driver");
			errorDriver.setContentText("Contacte con Borja para más información");

			errorDriver.showAndWait();
		}
		
	}
	
	private void onConnectAction(String type, String bd) {
		switch(type) {
		case "mysql":
			try {
				Mysql Database = new Mysql();
				PreparedStatement lista = Database.conexion.prepareStatement("select * from estancias");
				ResultSet resultado = lista.executeQuery();
				while(resultado.next()) {
					listEstancia.add(new Estancia(
							resultado.getInt("codEstudiante"),
							resultado.getInt("codResidencia"),
							resultado.getDate("fechaInicio").toString(),
							resultado.getDate("fechaFin").toString(),
							resultado.getInt("precioPagado")
							)
							);
				}
				list.setDisable(false);
			} catch (SQLException e) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("ERROR");
				alert.setHeaderText("Algo falló en la consulta");
				alert.setContentText("Consulta con Borja para más información");

				alert.showAndWait();
			}
			
		break;
		}
	}

}
