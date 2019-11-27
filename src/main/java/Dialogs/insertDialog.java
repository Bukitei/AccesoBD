package Dialogs;

/**
 * @author Borja David Gómez Alayón
 */

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import accesobd.Estancia;
import conections.Mysql;
import conections.SQL;
import conections.Access;
import java.sql.CallableStatement;
import java.util.Optional;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.paint.Color;
import models.insertDialogModel;

public class insertDialog extends Dialog<Estancia> {

	// Cargamos los elementos del FXML
	@FXML
	private TextField resi;

	@FXML
	private ComboBox<String> uni;

	@FXML
	private TextField precio;

	@FXML
	private CheckBox comedor;

	@FXML
	private ToggleButton procedure;

	@FXML
	private Label fallo;

	// Creamos los elementos del modelo

	private ButtonType okButton, cancelButton;
	
	private int proc = 0;
	private String codUni;
	private String uniExist;
	private String resiInsert;

	ObservableList<String> nomUniList = FXCollections.observableArrayList(new ArrayList<String>());
	insertDialogModel model = new insertDialogModel();

        //Construimos el diálogo
	public insertDialog(String type) throws IOException {

		

		setTitle("Insertar Residencia");
		setHeaderText("Rellena los datos:");
		setContentText("Rellene todos los datos para insertar");

                //Establecemos los textos de los botones que por defecto son OK y Cancelar
		okButton = new ButtonType("Insertar", ButtonData.OK_DONE);
		cancelButton = new ButtonType("Cancelar", ButtonData.CANCEL_CLOSE);

		getDialogPane().getButtonTypes().addAll(okButton, cancelButton);

		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/insertDialog.fxml"));
		fxmlLoader.setController(this);
		getDialogPane().setContent(fxmlLoader.load());

		

			// Obtener nombres de las Universidades
			

			uni.getItems().clear();
			uni.setPromptText("Residencias");

			uni.setItems(nomUniList);

			resi.textProperty().bindBidirectional(model.nombreProperty());
			precio.textProperty().bindBidirectional(model.precioProperty());

			procedure.setOnAction(evt -> onProcedureAction());

                        //Añadimos un listener a precio que ponga un texto en rojo si el precio es menor a 900
			model.precioProperty().addListener(new ChangeListener<String>() {

				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

					if (Integer.parseInt(model.getPrecio()) < 900) {
						fallo.setText("El precio no puede ser menor a 900");
					} else {
						fallo.setText("");
					}

				}
			});

		
                 //Rellena los datos del combobox de universidades según la lista de universidades de la tabla de datos, por si son diferentes
		switch (type) {
		case "mysql":
			 PreparedStatement preparedUni;
			try {
				Mysql conections = new Mysql();
				 
				 preparedUni = conections.conexion
							.prepareStatement("select nomUniversidad from universidades");
					ResultSet resultadoUni = preparedUni.executeQuery();
					
					while (resultadoUni.next()) {
						nomUniList.add(resultadoUni.getString(1));
					}
			} catch (SQLException e) {
				
				System.out.println("error de sql");
			}
			 //Le decimos que si da al botón ok, aunque el padre ejecute otra función, ejecute la siguiente
			setResultConverter(bt -> {
                                
				if (bt.getButtonData() == ButtonData.OK_DONE) {
					onInsertBttnMySQL(ButtonData.OK_DONE);
				}

				else {
					return null;
				}
				return null;
			});
			break;
		case "sql server":
			ResultSet resultadoUniS;
			try {
				SQL conectionsS = new SQL();
				
				PreparedStatement preparedUniS = conectionsS.conexion
						.prepareStatement("select nomUniversidad from universidades");
				resultadoUniS = preparedUniS.executeQuery();
				
				while (resultadoUniS.next()) {
					nomUniList.add(resultadoUniS.getString(1));
				}
			} catch (SQLException e) {
				System.out.println("error de sql");
			}
			
			setResultConverter(bt -> {

				if (bt.getButtonData() == ButtonData.OK_DONE) {
					onInsertBttnSQL(ButtonData.OK_DONE);
				}

				else {
					return null;
				}
				return null;
			});
			break;
		case "access":
			try {
				Access conectionsA = new Access();
				
				PreparedStatement preparedUniA = conectionsA.conexion
						.prepareStatement("select nomUniversidad from universidades");
				ResultSet resultadoUniA = preparedUniA.executeQuery();
				
				while (resultadoUniA.next()) {
					nomUniList.add(resultadoUniA.getString(1));
				}
			} catch (SQLException e) {
				System.out.println("error de sql");
			}
			
			procedure.setDisable(true);
			
			setResultConverter(bt -> {

				if (bt.getButtonData() == ButtonData.OK_DONE) {
					onInsertBttnAccess(ButtonData.OK_DONE);
				}

				else {
					return null;
				}
				return null;
			});
			break;
		}

	}

        /**
             * 
             * Función para insertar, las tres siguientes son iguales, sólo cambiando 
             * algunos detalles dependiendo de la base de datos seleccionada
             */
        
	private void onInsertBttnAccess(ButtonData okDone) {
		String procedureText;
		String comedorText;
		int comedorValue;
                //Establecemos el texto que se representará en el mensaje
		if (procedure.isSelected()) {
			procedureText = "Si";
			proc = 1;
		} else {
			procedureText = "No";
			proc = 0;
		}
		if (comedor.isSelected()) {
			comedorText = "Si";
			comedorValue = 1;
		} else {
			comedorText = "No";
			comedorValue = 0;
		}

                //Diálogo de confirmación
		Alert confirmation = new Alert(AlertType.CONFIRMATION);
		confirmation.setTitle("CONFIRMACION");
		confirmation.setHeaderText("¿Seguro que quieres insertar esta estancia?");
		confirmation.setContentText("Datos: \n Pocedimiento: " + procedureText + "\n Nombre de la residencia: "
				+ resi.getText() + "\n Universidad: " + uni.getValue() + "\n Precio Mensual: " + model.getPrecio()
				+ "\n Comedor: " + comedorText);

                //Si ha seleccionado que si, saldrá lo siguiente:
		Optional<ButtonType> result = confirmation.showAndWait();
		if (result.get() == ButtonType.OK) {
			try {

				Access database = new Access();

				PreparedStatement rUni = database.conexion
						.prepareStatement("select codUniversidad from universidades where nomUniversidad = (?)");
				rUni.setString(1, uni.getValue());
				ResultSet codUnir = rUni.executeQuery();
				while (codUnir.next()) {
					codUni = codUnir.getString("codUniversidad");
				}
				

					PreparedStatement prep = database.conexion.prepareStatement(
							"insert into residencias (nomResidencia, codUniversidad, precioMensual, comedor) values ((?), (?), (?), (?))");

					prep.setString(1, resi.getText());
					prep.setString(2, codUni);
					prep.setString(3, precio.getText());
					prep.setInt(4, comedorValue);
					prep.executeUpdate();

				
					database.conexion.close();
				

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			
		}
		}
	}

	private void onInsertBttnSQL(ButtonData okDone) {

		String procedureText;
		String comedorText;
		int comedorValue;
		if (procedure.isSelected()) {
			procedureText = "Si";
			proc = 1;
		} else {
			procedureText = "No";
			proc = 0;
		}
		if (comedor.isSelected()) {
			comedorText = "Si";
			comedorValue = 1;
		} else {
			comedorText = "No";
			comedorValue = 0;
		}

		Alert confirmation = new Alert(AlertType.CONFIRMATION);
		confirmation.setTitle("CONFIRMACION");
		confirmation.setHeaderText("¿Seguro que quieres insertar esta estancia?");
		confirmation.setContentText("Datos: \n Pocedimiento: " + procedureText + "\n Nombre de la residencia: "
				+ resi.getText() + "\n Universidad: " + uni.getValue() + "\n Precio Mensual: " + model.getPrecio()
				+ "\n Comedor: " + comedorText);

		Optional<ButtonType> result = confirmation.showAndWait();
		if (result.get() == ButtonType.OK) {
			try {

				SQL database = new SQL();

				PreparedStatement rUni = database.conexion
						.prepareStatement("select codUniversidad from universidades where nomUniversidad = (?)");
				rUni.setString(1, uni.getValue());
				ResultSet codUnir = rUni.executeQuery();
				while (codUnir.next()) {
					codUni = codUnir.getString("codUniversidad");
				}
				if (proc == 0) {

					PreparedStatement prep = database.conexion.prepareStatement(
							"insert into residencias (nomResidencia, codUniversidad, precioMensual, comedor) values (?, ?, ?, ?)");

					prep.setString(1, resi.getText());
					prep.setString(2, codUni);
					prep.setString(3, precio.getText());
					prep.setInt(4, comedorValue);
					prep.executeUpdate();

				} else {
					CallableStatement call = database.conexion.prepareCall("exec ap_resiInsert ?,?,?,?,?,?");

					call.setString(1, resi.getText());
					call.setString(2, codUni);
					call.setInt(3, Integer.parseInt(precio.getText()));
					call.setBoolean(4, comedor.isSelected());
					call.registerOutParameter(5, java.sql.Types.INTEGER);
					call.registerOutParameter(6, java.sql.Types.INTEGER);

					call.execute();

					if (call.getInt(5) == 1) {
						uniExist = "Si";
					} else {
						uniExist = "No";
					}

					if (call.getInt(6) == 1) {
						resiInsert = "Si";
					} else {
						resiInsert = "No";
					}

					System.out.println("¿Existe la universidad? " + uniExist
							+ "\n ¿Se ha insertado correctamente la residencia? " + resiInsert);

					database.conexion.close();
				}

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void onInsertBttnMySQL(ButtonData data) {

		String procedureText;
		String comedorText;
		int comedorValue;
		if (procedure.isSelected()) {
			procedureText = "Si";
			proc = 1;
		} else {
			procedureText = "No";
			proc = 0;
		}
		if (comedor.isSelected()) {
			comedorText = "Si";
			comedorValue = 1;
		} else {
			comedorText = "No";
			comedorValue = 0;
		}

		Alert confirmation = new Alert(AlertType.CONFIRMATION);
		confirmation.setTitle("CONFIRMACION");
		confirmation.setHeaderText("¿Seguro que quieres insertar esta estancia?");
		confirmation.setContentText("Datos: \n Pocedimiento: " + procedureText + "\n Nombre de la residencia: "
				+ resi.getText() + "\n Universidad: " + uni.getValue() + "\n Precio Mensual: " + model.getPrecio()
				+ "\n Comedor: " + comedorText);

		Optional<ButtonType> result = confirmation.showAndWait();
		if (result.get() == ButtonType.OK) {
			try {

				Mysql database = new Mysql();

				PreparedStatement rUni = database.conexion
						.prepareStatement("select codUniversidad from universidades where nomUniversidad = (?)");
				rUni.setString(1, uni.getValue());
				ResultSet codUnir = rUni.executeQuery();
				while (codUnir.next()) {
					codUni = codUnir.getString("codUniversidad");
				}
				if (proc == 0) {

					PreparedStatement prep = database.conexion.prepareStatement(
							"insert into residencias (nomResidencia, codUniversidad, precioMensual, comedor) values ((?), (?), (?), (?))");

					prep.setString(1, resi.getText());
					prep.setString(2, codUni);
					prep.setString(3, precio.getText());
					prep.setInt(4, comedorValue);
					prep.executeUpdate();

				} else {
					CallableStatement call = database.conexion.prepareCall("call ap_resiInsert (?,?,?,?,?,?)");

					call.setString(1, resi.getText());
					call.setString(2, codUni);
					call.setInt(3, Integer.parseInt(precio.getText()));
					call.setBoolean(4, comedor.isSelected());
					call.registerOutParameter(5, java.sql.Types.INTEGER);
					call.registerOutParameter(6, java.sql.Types.INTEGER);

					call.execute();

					if (call.getInt(5) == 1) {
						uniExist = "Si";
					} else {
						uniExist = "No";
					}

					if (call.getInt(6) == 1) {
						resiInsert = "Si";
					} else {
						resiInsert = "No";
					}

					System.out.println("¿Existe la universidad? " + uniExist
							+ "\n ¿Se ha insertado correctamente la residencia? " + resiInsert);

					database.conexion.close();
				}

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void onProcedureAction() {
		if (procedure.isSelected()) {
			procedure.setText("Si");
			procedure.setTextFill(Color.BLUE);
			proc = 1;
		} else {
			procedure.setText("No");
			procedure.setTextFill(Color.RED);
			proc = 0;
		}
	}
}
