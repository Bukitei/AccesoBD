package Dialogs;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

import com.sun.tools.jconsole.JConsoleContext.ConnectionState;

import accesobd.Estancia;
import conections.Mysql;
import javafx.beans.property.ListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

public class insertDialog extends Dialog<Estancia>{

	//Cargamos los elementos del FXML
	@FXML
	private ComboBox<String> codEs;
	
	@FXML
	private ComboBox<String> codRes;
	
	@FXML
	private DatePicker fechaIn;
	
	@FXML
	private DatePicker fechaFn;
	
	@FXML
	private TextField precio;
	
	//Creamos los elementos del modelo
	
	private ButtonType okButton, cancelButton;
	private Mysql conections = new Mysql();
	
	ObservableList<String> nomEsList = FXCollections.observableArrayList(new ArrayList<String>());
	ObservableList<String> nomResList = FXCollections.observableArrayList(new ArrayList<String>());
	
	public insertDialog() throws IOException {
		
		setTitle("Insertar Estancia");
		setHeaderText("Rellena los datos:");
		setContentText("Rellene todos los datos para insertar");
		
		okButton = new ButtonType("Insertar", ButtonData.OK_DONE);
		cancelButton = new ButtonType("Cancelar", ButtonData.CANCEL_CLOSE);
		
		getDialogPane().getButtonTypes().addAll(okButton, cancelButton);
		
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/insertDialog.fxml"));
		fxmlLoader.setController(this);
		getDialogPane().setContent(fxmlLoader.load());
		
		try {
			//Obtener nombres de los estudiantes
			PreparedStatement preparedEs = conections.conexion.prepareStatement("select nomEstudiante from estudiantes");
			ResultSet resultadoEs = preparedEs.executeQuery();
			
			codEs.getItems().clear();
			codEs.setPromptText("Estudiantes");
			
			while(resultadoEs.next()) {
				nomEsList.add(resultadoEs.getString(1));			
			}

			codEs.setItems(nomEsList);
			
			//Obtener nombres de las residencias
			
			PreparedStatement preparedRes = conections.conexion.prepareStatement("select nomResidencia from residencias");
			ResultSet resultadoRes = preparedEs.executeQuery();
			
			codRes.getItems().clear();
			codRes.setPromptText("Residencias");
			
			while(resultadoRes.next()) {
				nomResList.add(resultadoEs.getString(1));			
			}

			codRes.setItems(nomResList);
			
			
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
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
