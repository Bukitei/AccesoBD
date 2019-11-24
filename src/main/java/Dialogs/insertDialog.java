package Dialogs;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import accesobd.Estancia;
import conections.Mysql;
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

public class insertDialog extends Dialog<Estancia>{

	//Cargamos los elementos del FXML
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
	
	//Creamos los elementos del modelo
	
	private ButtonType okButton, cancelButton;
	private Mysql conections = new Mysql();
        private int proc = 0;
        private int codRes;
        private String codUni;
        private String uniExist;
        private String resiInsert;
	
	ObservableList<String> nomUniList = FXCollections.observableArrayList(new ArrayList<String>());
	insertDialogModel model = new insertDialogModel();
	
	public insertDialog(int codRes, String type) throws IOException {
		
                this.codRes = codRes+1;
            
		setTitle("Insertar Residencia");
		setHeaderText("Rellena los datos:");
		setContentText("Rellene todos los datos para insertar");
		
		okButton = new ButtonType("Insertar", ButtonData.OK_DONE);
		cancelButton = new ButtonType("Cancelar", ButtonData.CANCEL_CLOSE);
		
		getDialogPane().getButtonTypes().addAll(okButton, cancelButton);
		
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/insertDialog.fxml"));
		fxmlLoader.setController(this);
		getDialogPane().setContent(fxmlLoader.load());
		
		try {
			
			//Obtener nombres de las Universidades
			PreparedStatement preparedUni = conections.conexion.prepareStatement("select nomUniversidad from universidades");
			ResultSet resultadoUni = preparedUni.executeQuery();
			
			uni.getItems().clear();
			uni.setPromptText("Residencias");
			
			while(resultadoUni.next()) {
				nomUniList.add(resultadoUni.getString(1));			
			}

			uni.setItems(nomUniList);
			
			resi.textProperty().bindBidirectional(model.nombreProperty());
			precio.textProperty().bindBidirectional(model.precioProperty());
                        
                        procedure.setOnAction(evt -> onProcedureAction());
                        
                        
			
			model.precioProperty().addListener(new ChangeListener<String>() {

				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					
                                    if(Integer.parseInt(model.getPrecio()) < 900 ){
						fallo.setText("El precio no puede ser menor a 900");
					}else {
                                                fallo.setText("");
					}
					
				}
			});
			
		} catch (SQLException e) {
			System.out.println("error de sql");
		}
		
		
		setResultConverter(bt -> { 
			
			if( bt.getButtonData() == ButtonData.OK_DONE ) 
			{onInsertBttn(ButtonData.OK_DONE, type);}
			
			else
			{ return null;
			}
			return null;
		});
		
	}
	
	
	private void onInsertBttn(ButtonData data, String type){
		
            String procedureText;
            String comedorText;
            int comedorValue;
            if(procedure.isSelected()){
                procedureText = "Si";
                proc = 1;
            }else{
                procedureText = "No";
                proc = 0;
            }
            if(comedor.isSelected()){
                comedorText = "Si";
                comedorValue = 1;
            }else{
                comedorText = "No";
                comedorValue = 0;
            }
            
		Alert confirmation = new Alert(AlertType.CONFIRMATION);
		confirmation.setTitle("CONFIRMACION");
		confirmation.setHeaderText("¿Seguro que quieres insertar esta estancia?");
		confirmation.setContentText("Datos: \n Pocedimiento: "+procedureText+"\n Nombre de la residencia: "+resi.getText()+"\n Universidad: "+uni.getValue()+"\n Precio Mensual: "+model.getPrecio()+"\n Comedor: "+comedorText);

		Optional<ButtonType> result = confirmation.showAndWait();
		if (result.get() == ButtonType.OK){
		   try {
                    if(type.equals("mysql")){
                        Mysql database = new Mysql();
                        
                        PreparedStatement rUni = database.conexion.prepareStatement("select codUniversidad from universidades where nomUniversidad = (?)");
                    rUni.setString(1, uni.getValue());
                    ResultSet codUnir = rUni.executeQuery();
                    while(codUnir.next()){
                        codUni = codUnir.getString("codUniversidad");
                    }
                    if(proc == 0){
	
                        PreparedStatement prep = database.conexion.prepareStatement("insert into residencias (nomResidencia, codUniversidad, precioMensual, comedor) values ((?), (?), (?), (?))");
                        
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
                        
                        if(call.getInt(5) == 1){
                            uniExist = "Si";
                        }else{
                            uniExist = "No";
                        }
                        
                        if(call.getInt(6) == 1){
                            resiInsert = "Si";
                        }else{
                            resiInsert = "No";
                        }
                       
                        System.out.println("¿Existe la universidad?"+uniExist+"\n ¿Se ha insertado correctamente la residencia?"+resiInsert);
                    
                        database.conexion.close();
                     }
                    }else{
                        procedure.setDisable(true);
                        Access database = new Access();
                        
                        PreparedStatement rUni = database.conexion.prepareStatement("select codUniversidad from universidades where nomUniversidad = (?)");
                    rUni.setString(1, uni.getValue());
                    ResultSet codUnir = rUni.executeQuery();
                    while(codUnir.next()){
                        codUni = codUnir.getString("codUniversidad");
                    }
                    
	
                        PreparedStatement prep = database.conexion.prepareStatement("insert into residencias ([nomResidencia], [codUniversidad], [precioMensual], [comedor]) values ((?), (?), (?), (?))");
                        
                        prep.setString(1, resi.getText());
                        prep.setString(2, codUni);
                        prep.setInt(3, Integer.parseInt(precio.getText()));
                        prep.setBoolean(4, comedor.isSelected());
                        prep.executeUpdate();			
                    
                        database.conexion.close();
                    }
                   
		} catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
		}
            }
	}
              
   

    private void onProcedureAction() {
        if(procedure.isSelected()){
            procedure.setText("Si");
            procedure.setTextFill(Color.BLUE);
            proc = 1;
        }else{
            procedure.setText("No");
            procedure.setTextFill(Color.RED);
            proc = 0;
        }
    }
}
