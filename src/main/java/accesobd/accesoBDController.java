package accesobd;

/**
 * @author Borja David Gómez Alayón
 */

import Dialogs.insertDialog;
import Dialogs.modifyDialog;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import conections.Access;
import conections.SQL;
import conections.Mysql;
import java.util.Optional;
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
import javafx.scene.control.ButtonType;
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

                //Establecemos los textos del ComboBox
		ObservableList<String> servers = FXCollections.observableArrayList();
		servers.addAll("MySQL", "SQL Server", "Access");

		tipCombx.setItems(servers);
                //Bindeamos con el modelo
		bdText.textProperty().bindBidirectional(bd);

		list.itemsProperty().bind(listaResidencia);
                //Listener que establece el nombre de la residencia seleccionada en el TextField para ello
		list.getSelectionModel().selectedItemProperty().addListener((ob, ol, n) -> {
			if (n != null) {
				name.setText(list.getSelectionModel().getSelectedItem().getNomRes());
			}
		});
                //Establecemos que hace cada botón y el ComboBox
		tipCombx.setOnAction(evt -> onTypeAction());
		conecta.setOnAction(evt -> onConnectAction());
		insertBt.setOnAction(evt -> onInsertAction());
		deleteBt.setOnAction(evt -> onDeleteAction());
                modifyBt.setOnAction(evt -> onModifyActtion());

	}
        //Función para llamar a su vista
	public VBox getView() {
		return view;
	}
        /**
         * Establecemos que cuando se seleccione algo en el ComboBox 
         * se habiliten el TextField para escribir si quieres una base de datos
         * específica o no
         */
	private void onTypeAction() {

		try {
			bdText.setDisable(false);
			conecta.setDisable(false);

		} catch (Exception e) {
			Alert errorDriver = new Alert(AlertType.ERROR);
			errorDriver.setTitle("ERROR");
			errorDriver.setHeaderText("No se ha encontrado el driver");
			errorDriver.setContentText("Contacte con Borja para más información");

			errorDriver.showAndWait();
		}

	}
        
        //Esta función se conecta a la base de datos seleccionada

	private void onConnectAction() {
		String type = tipCombx.getValue().toLowerCase();
		switch (type) {
		case "mysql":
			try {
				Mysql Database;
				if (bdText.getText() == null || bdText.getText() == "") {
					Database = new Mysql();
				} else {
					Database = new Mysql(bdText.getText());
				}
				PreparedStatement lista = Database.conexion.prepareStatement("select * from residencias");
				ResultSet resultado = lista.executeQuery();
                                /**
                                 * Tras conectarse y conseguir una lista de residencias, las inserta en la lista mostrada y 
                                 * habilita el resto de opciones
                                 */
                                
				listaResidencia.removeAll();
				list.getItems().clear();
				insertBt.setDisable(false);
				name.setDisable(false);
				modifyBt.setDisable(false);
				deleteBt.setDisable(false);
				while (resultado.next()) {
					listResidencia.add(new Residencia(resultado.getInt("codResidencia"),
							resultado.getString("nomResidencia"), resultado.getString("codUniversidad"),
							resultado.getInt("precioMensual"), resultado.getInt("comedor")));
				}
				list.setDisable(false);
				Database.conexion.close();
			} catch (SQLException e) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("ERROR");
				alert.setHeaderText("La base de datos es incorrecta");
				alert.setContentText("Asegúrese de que escribió bien el nombre de la base de datos.");

				alert.showAndWait();
			}

			break;

		case "access":
			try {
				Access Database;
				if (bdText.getText() == null || bdText.getText() == "") {
					Database = new Access();
				} else {
					Database = new Access(bdText.getText());
				}
				PreparedStatement lista = Database.conexion.prepareStatement("select * from residencias");
				ResultSet resultado = lista.executeQuery();
				listaResidencia.removeAll();
				list.getItems().clear();
				insertBt.setDisable(false);
				name.setDisable(false);
				modifyBt.setDisable(false);
				deleteBt.setDisable(false);
				while (resultado.next()) {
					listResidencia.add(new Residencia(resultado.getInt("codResidencia"),
							resultado.getString("nomResidencia"), 
							resultado.getString("codUniversidad"),
							resultado.getInt("precioMensual"), 
							resultado.getInt("comedor")));
				}
				list.setDisable(false);
				Database.conexion.close();
			} catch (SQLException e) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("ERROR");
				alert.setHeaderText("La base de datos es incorrecta");
				alert.setContentText("Asegúrese de que escribió bien el nombre de la base de datos.");

				alert.showAndWait();
			}
			break;

		case "sql server":
			try {
				SQL Database;
				if (bdText.getText() == null || bdText.getText() == "") {
					Database = new SQL();
				} else {
					Database = new SQL(bdText.getText());
				}
				PreparedStatement lista = Database.conexion.prepareStatement("select * from residencias");
				ResultSet resultado = lista.executeQuery();
				listaResidencia.removeAll();
				list.getItems().clear();
				insertBt.setDisable(false);
				name.setDisable(false);
				modifyBt.setDisable(false);
				deleteBt.setDisable(false);
				while (resultado.next()) {
					listResidencia.add(new Residencia(resultado.getInt("codResidencia"),
							resultado.getString("nomResidencia"), 
							resultado.getString("codUniversidad"),
							resultado.getInt("precioMensual"), 
							resultado.getInt("comedor")
							));
				}
				list.setDisable(false);
				Database.conexion.close();
			} catch (SQLException e) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("ERROR");
				alert.setHeaderText("La base de datos es incorrecta");
				alert.setContentText("Asegúrese de que escribió bien el nombre de la base de datos.");

				alert.showAndWait();
			}
			break;
		}
	}
        //Invoca el Diálogo de insertar
	private void onInsertAction() {
		try {
			String type = tipCombx.getValue().toLowerCase();
			insertDialog dialog = new insertDialog(type);
			dialog.showAndWait();
			onConnectAction();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
        //Acción al pulsar el botón borrar
	private void onDeleteAction() {
            //Saca un diálogo de confirmación
           Alert confirmation = new Alert(AlertType.CONFIRMATION);
		confirmation.setTitle("CONFIRMACION");
		confirmation.setHeaderText("¿Seguro que quieres eliminar esta estancia?");
		confirmation.setContentText("La residencia con nombre "+name.getText()+" será eliminada.");
                
                //Si acepta, dependiendo de la base de datos lo hace a su modo
                Optional<ButtonType> result = confirmation.showAndWait();
		if (result.get() == ButtonType.OK) {
                    String type = tipCombx.getValue().toLowerCase();
		switch (type) {
		case "mysql":

			try {
				Mysql database = new Mysql();
				PreparedStatement delete = database.conexion
						.prepareStatement("delete from residencias where nomResidencia = (?)");
				delete.setString(1, name.getText());
				delete.executeUpdate();
				database.conexion.close();
				onConnectAction();
			} catch (SQLException e) {
				Alert confirm = new Alert(AlertType.ERROR);
				confirm.setTitle("ERROR");
				confirm.setHeaderText("La residencia no existe");
				confirm.setContentText("Asegúrese de que la escribió correctamente.");

			}

			break;
		case "access":
			try {
				Access database = new Access();
				PreparedStatement delete = database.conexion
						.prepareStatement("delete from residencias where nomResidencia = (?)");
				delete.setString(1, name.getText());
				delete.executeUpdate();
				database.conexion.close();
				onConnectAction();
			} catch (SQLException e) {
				Alert confirm = new Alert(AlertType.ERROR);
				confirm.setTitle("ERROR");
				confirm.setHeaderText("La residencia no existe");
				confirm.setContentText("Asegúrese de que la escribió correctamente.");
			}
			break;
		case "sql server":
			try {
				SQL database = new SQL();
				PreparedStatement delete = database.conexion
						.prepareStatement("delete from residencias where nomResidencia = (?)");
				delete.setString(1, name.getText());
				delete.executeUpdate();
				database.conexion.close();
				onConnectAction();
			} catch (SQLException e) {
				Alert confirm = new Alert(AlertType.ERROR);
				confirm.setTitle("ERROR");
				confirm.setHeaderText("La residencia no existe");
				confirm.setContentText("Asegúrese de que la escribió correctamente.");
			}
		break;
		}
            }
	}

    //Invoca el diálogo modificar pasándole la residencia seleccionada
    private void onModifyActtion() {
        Residencia submited = null;
        
        for(int i = 0; i < listResidencia.size(); i++){
            if(listResidencia.get(i).getNomRes().equals(name.getText())){
                submited = listResidencia.get(i);
            }
        }
        
        if(submited != null){
            try {
			String type = tipCombx.getValue().toLowerCase();
			modifyDialog dialog = new modifyDialog(submited, type);
			dialog.showAndWait();
			onConnectAction();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        }else{
                Alert error = new Alert(AlertType.ERROR);
                error.setTitle("ERROR");
                error.setHeaderText("Algo está mal");
                error.setContentText("Asegúrate de que has puesto bien el nombre de la residencia.");
                
                error.showAndWait();
            }
         
    }
        
        
}
