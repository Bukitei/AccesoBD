package Dialogs;

import java.io.IOException;
import java.util.Optional;

import accesobd.Estancia;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;

public class insertDialogController extends Dialog<Estancia>{

	@FXML
	private ComboBox<Integer> codEs;
	
	@FXML
	private ComboBox<Integer> codRes;
	
	@FXML
	private DatePicker fechaIn;
	
	@FXML
	private DatePicker fechaFn;
	
	@FXML
	private TextField precio;
	
	private ButtonType okButton, cancelButton;
	
	public insertDialogController() throws IOException {
		
		setTitle("Insertar Estancia");
		setHeaderText("Rellena los datos:");
		setContentText("Rellene todos los datos para insertar");
		
		okButton = new ButtonType("Insertar", ButtonData.OK_DONE);
		cancelButton = new ButtonType("Cancelar", ButtonData.CANCEL_CLOSE);
		
		getDialogPane().getButtonTypes().addAll(okButton, cancelButton);
		
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/insertDialog.fxml"));
		fxmlLoader.setController(this);
		fxmlLoader.load();
		
		setResultConverter(bt -> onInsertBttn());
		
		
	}
	
	private Estancia onInsertBttn(){
		Alert confirmation = new Alert(AlertType.CONFIRMATION);
		confirmation.setTitle("CONFIRMACION");
		confirmation.setHeaderText("¿Seguro que quieres insertar esta estancia?");
		confirmation.setContentText("Datos: \n Estudiante: "+codEs.getValue()+"\n Residencia: "+codRes.getValue()+"\n Fecha de Inicio: "+fechaIn.getValue().toString()+"\n Fecha de Final: "+fechaFn.getValue().toString()+"\n Precio Pagado: "+precio.getText());

		Optional<ButtonType> result = confirmation.showAndWait();
		if (result.get() == ButtonType.OK){
		    // ... user chose OK
		} else {
		    // ... user chose CANCEL or closed the dialog
		}
		
		return null;
	}
	
}
