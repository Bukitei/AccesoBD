package accesobd;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class accesoBDController implements Initializable {

	// Recogemos los datos del FXML

	@FXML
	private VBox view;

	@FXML
	private ComboBox<String> tipCombx, bdCombx;

	@FXML
	private TableView<String> list;

	@FXML
	private TextField name;

	@FXML
	private Button insertBt, modifyBt, deleteBt;

	// Creamos los elementos del modelo

	// constructor

	public accesoBDController() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/accesoBDFXML.fxml"));
		loader.setController(this); // Este será nuestro controlador
		loader.load();
	}

	public void initialize(URL location, ResourceBundle resources) {

	}
	
	public VBox getView() {
		return view;
	}

}
