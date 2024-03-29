package models;


import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

//Modelo del diálogo insert

public class insertDialogModel {

	//modelos de los property con los que trabajaremos en el diálogo
	StringProperty nombre = new SimpleStringProperty();
	StringProperty precio = new SimpleStringProperty();
	BooleanProperty comedor = new SimpleBooleanProperty();
       
	public final StringProperty nombreProperty() {
		return this.nombre;
	}
	
	public final String getNombre() {
		return this.nombreProperty().get();
	}
	
	public final void setNombre(final String nombre) {
		this.nombreProperty().set(nombre);
	}

	public final StringProperty precioProperty() {
		return this.precio;
	}
	

	public final String getPrecio() {
		return this.precioProperty().get();
	}
	

	public final void setPrecio(final String precio) {
		this.precioProperty().set(precio);
	}
	
	
	
}
