package accesobd;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import Dialogs.insertDialog;
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
	private TableView<Residencia> list;

	@FXML
	private TextField name, bdText;

	@FXML
	private Button insertBt, modifyBt, deleteBt, conecta;

	// Creamos los elementos del modelo
	
	private StringProperty bd = new SimpleStringProperty();
	private ObservableList<Residencia> listResidencia = FXCollections.observableArrayList();
	private ListProperty<Residencia> listaResidencia = new SimpleListProperty<Residencia>(listResidencia);
	

	// constructor

	public accesoBDController() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/accesoBDFXML.fxml"));
		loader.setController(this); // Este será nuestro controlador
		loader.load();
	}

	public void initialize(URL location, ResourceBundle resources) {
			
		ObservableList<String> servers = FXCollections.observableArrayList();
		servers.addAll("MySQL", "SQL Server", "Access");
		
		tipCombx.setItems(servers);
		
		bdText.textProperty().bindBidirectional(bd);
		
		list.itemsProperty().bind(listaResidencia);
		
		tipCombx.setOnAction(evt -> onTypeAction());
		conecta.setOnAction(evt -> onConnectAction());
		insertBt.setOnAction(evt -> onInsertAction());
		
	}
	
	public VBox getView() {
		return view;
	}
	
	private void onTypeAction() {
		
		try {
			bdText.setDisable(false);
			conecta.setDisable(false);
			
			
			
			
		}catch (Exception e) {
			Alert errorDriver = new Alert(AlertType.ERROR);
			errorDriver.setTitle("ERROR");
			errorDriver.setHeaderText("No se ha encontrado el driver");
			errorDriver.setContentText("Contacte con Borja para más información");

			errorDriver.showAndWait();
		}
		
	}
	
	private void onConnectAction() {
		String type = tipCombx.getValue().toLowerCase();
		switch(type) {
		case "mysql":
			try {
				Mysql Database;
				if(bdText.getText() != "") {
					Database = new Mysql();
				}else {
					Database = new Mysql(bdText.getText());
				}				
				PreparedStatement lista = Database.conexion.prepareStatement("select * from residencias");
				ResultSet resultado = lista.executeQuery();
				listaResidencia.removeAll();
				list.getItems().clear();
				insertBt.setDisable(false);
				name.setDisable(false);
				modifyBt.setDisable(false);
				deleteBt.setDisable(false);
				while(resultado.next()) {
					listResidencia.add(new Residencia(
							resultado.getInt("codResidencia"),
							resultado.getString("nomResidencia"),
							resultado.getString("codUniversidad"),
							resultado.getInt("precioMensual"),
							resultado.getInt("comedor")
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
	
	private void onInsertAction() {
		try {
			
			insertDialog dialog = new insertDialog();
			dialog.showAndWait();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
