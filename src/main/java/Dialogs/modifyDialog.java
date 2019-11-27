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
import accesobd.Residencia;
import conections.Mysql;
import conections.SQL;
import conections.Access;
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
import models.insertDialogModel;

public class modifyDialog extends Dialog<Estancia> {

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
	private Label fallo;

	// Creamos los elementos del modelo

	private ButtonType okButton, cancelButton;
	
	private int proc = 0;
	private String codUni;
        private Residencia submited;

	ObservableList<String> nomUniList = FXCollections.observableArrayList(new ArrayList<String>());
	insertDialogModel model = new insertDialogModel();

        //Construimos el diálogo
        
	public modifyDialog(Residencia submited, String type) throws IOException {
                
                this.submited = submited;

		setTitle("Modificar Residencia");
		setHeaderText("Cambia los datos los datos:");
		setContentText("Cambia todos los datos para modificar");

                //Establecemos los textos de los botones que por defecto son OK y Cancelar
		okButton = new ButtonType("Modificar", ButtonData.OK_DONE);
		cancelButton = new ButtonType("Cancelar", ButtonData.CANCEL_CLOSE);

		getDialogPane().getButtonTypes().addAll(okButton, cancelButton);

		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/modifyDialog.fxml"));
		fxmlLoader.setController(this);
		getDialogPane().setContent(fxmlLoader.load());

		

			// Obtener nombres de las Universidades
			

			uni.getItems().clear();
			uni.setPromptText("Residencias");

			uni.setItems(nomUniList);
                        uni.setValue(submited.getNomUni());
                       

			resi.textProperty().bindBidirectional(model.nombreProperty());
			precio.textProperty().bindBidirectional(model.precioProperty());
                        
                        //Le damos los valores de la Residencia seleccionada
                        
                        resi.setText(submited.getNomRes());
                        precio.setText(Integer.toString(submited.getPrecio()));
                        
                        if(submited.getComedor() == 1){
                            comedor.setSelected(true);
                        }
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
			 
			setResultConverter(bt -> {

				if (bt.getButtonData() == ButtonData.OK_DONE) {
					onModifyBttnMySQL(ButtonData.OK_DONE);
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
			//Le decimos que si da al botón ok, aunque el padre ejecute otra función, ejecute la siguiente
			setResultConverter(bt -> {

				if (bt.getButtonData() == ButtonData.OK_DONE) {
					onModifyBttnSQL(ButtonData.OK_DONE);
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
			
			setResultConverter(bt -> {

				if (bt.getButtonData() == ButtonData.OK_DONE) {
					onModifyBttnAccess(ButtonData.OK_DONE);
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
             * Función para modificar, las tres siguientes son iguales, sólo cambiando 
             * algunos detalles dependiendo de la base de datos seleccionada
             */
            
	private void onModifyBttnAccess(ButtonData okDone) {
		String comedorText;
		int comedorValue;
                //Establecemos el texto que se representará en el mensaje
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
		confirmation.setHeaderText("¿Seguro que quieres modificiar esta estancia?");
		confirmation.setContentText("Antiguos valores: \n    -Nombre de la residencia: "+submited.getNomRes()+
                        "\n    -Universidad: "+submited.getNomUni()+"\n    -Precio mensual: "+submited.getPrecio()+"\n    -Comedor: "+
                        submited.getComedorS()+"\n Nuevos valores: \n    -Nombre de la residencia: "+resi.getText()+
                        "\n    -Universidad: "+uni.getValue()+"\n    -Precio mensual: "+precio.getText()+"\n    -Comedor: "+
                        comedorText);
                //Si ha seleccionado que si, saldrá lo siguiente:
		Optional<ButtonType> result = confirmation.showAndWait();
		if (result.get() == ButtonType.OK) {
			try {

				Access database = new Access();
                                 //Recibimos el código de la universidad dependiendo del nombre escogido
				PreparedStatement rUni = database.conexion
						.prepareStatement("select codUniversidad from universidades where nomUniversidad = (?)");
				rUni.setString(1, uni.getValue());
				ResultSet codUnir = rUni.executeQuery();
				while (codUnir.next()) {
					codUni = codUnir.getString("codUniversidad");
				}
				
                                        //Ejecutmos la modificación
					PreparedStatement prep = database.conexion.prepareStatement(
					"update residencias  set nomResidencia = (?), codUniversidad = (?), "
                                                + "precioMensual = (?), comedor = (?) where codResidencia = (?)");

					prep.setString(1, resi.getText());
					prep.setString(2, codUni);
					prep.setString(3, precio.getText());
					prep.setInt(4, comedorValue);
                                        prep.setInt(5, submited.getCodRes());
					prep.executeUpdate();

				
					database.conexion.close();
				

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			
		}
		}
	}

	private void onModifyBttnSQL(ButtonData okDone) {

		
		String comedorText;
		int comedorValue;
		
		if (comedor.isSelected()) {
			comedorText = "Si";
			comedorValue = 1;
		} else {
			comedorText = "No";
			comedorValue = 0;
		}

		Alert confirmation = new Alert(AlertType.CONFIRMATION);
		confirmation.setTitle("CONFIRMACION");
		confirmation.setHeaderText("¿Seguro que quieres modificiar esta estancia?");
		confirmation.setContentText("Antiguos valores: \n    -Nombre de la residencia: "+submited.getNomRes()+
                        "\n    -Universidad: "+submited.getNomUni()+"\n    -Precio mensual: "+submited.getPrecio()+"\n    -Comedor: "+
                        submited.getComedorS()+"\n Nuevos valores: \n    -Nombre de la residencia: "+resi.getText()+
                        "\n    -Universidad: "+uni.getValue()+"\n    -Precio mensual: "+precio.getText()+"\n    -Comedor: "+
                        comedorText);

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
							"update residencias  set nomResidencia = (?), codUniversidad = (?), "
                                                + "precioMensual = (?), comedor = (?) where codResidencia = (?)");

					prep.setString(1, resi.getText());
					prep.setString(2, codUni);
					prep.setString(3, precio.getText());
					prep.setInt(4, comedorValue);
                                        prep.setInt(5, submited.getCodRes());
					prep.executeUpdate();

				}

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void onModifyBttnMySQL(ButtonData data) {

		
		String comedorText;
		int comedorValue;
		
		if (comedor.isSelected()) {
			comedorText = "Si";
			comedorValue = 1;
		} else {
			comedorText = "No";
			comedorValue = 0;
		}

		Alert confirmation = new Alert(AlertType.CONFIRMATION);
		confirmation.setTitle("CONFIRMACION");
		confirmation.setHeaderText("¿Seguro que quieres modificiar esta estancia?");
		confirmation.setContentText("Antiguos valores: \n    -Nombre de la residencia: "+submited.getNomRes()+
                        "\n    -Universidad: "+submited.getNomUni()+"\n    -Precio mensual: "+submited.getPrecio()+"\n    -Comedor: "+
                        submited.getComedorS()+"\n Nuevos valores: \n    -Nombre de la residencia: "+resi.getText()+
                        "\n    -Universidad: "+uni.getValue()+"\n    -Precio mensual: "+precio.getText()+"\n    -Comedor: "+
                        comedorText);

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
							"update residencias  set nomResidencia = (?), codUniversidad = (?), "
                                                + "precioMensual = (?), comedor = (?) where codResidencia = (?)");

					prep.setString(1, resi.getText());
					prep.setString(2, codUni);
					prep.setString(3, precio.getText());
					prep.setInt(4, comedorValue);
                                        prep.setInt(5, submited.getCodRes());
					prep.executeUpdate();

				}

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
