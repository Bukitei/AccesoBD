package accesobd;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Estancia {

	private IntegerProperty codEs, codRes, precio;
	private StringProperty fechaIn, fechaFn;
	
	
	public Estancia(int codEs, int codRes, String fechaIn, String fechaFn, int precio) {
		
		this.codEs = new SimpleIntegerProperty(codEs);
		this.codRes = new SimpleIntegerProperty(codRes);
		this.fechaIn = new SimpleStringProperty(fechaIn);
		this.fechaFn = new SimpleStringProperty(fechaFn);
		this.precio = new SimpleIntegerProperty(precio);
	}


	public final IntegerProperty codEsProperty() {
		return this.codEs;
	}
	


	public final int getCodEs() {
		return this.codEsProperty().get();
	}
	


	public final void setCodEs(final int codEs) {
		this.codEsProperty().set(codEs);
	}
	


	public final IntegerProperty codResProperty() {
		return this.codRes;
	}
	


	public final int getCodRes() {
		return this.codResProperty().get();
	}
	


	public final void setCodRes(final int codRes) {
		this.codResProperty().set(codRes);
	}
	


	public final IntegerProperty precioProperty() {
		return this.precio;
	}
	


	public final int getPrecio() {
		return this.precioProperty().get();
	}
	


	public final void setPrecio(final int precio) {
		this.precioProperty().set(precio);
	}
	


	public final StringProperty fechaInProperty() {
		return this.fechaIn;
	}
	


	public final String getFechaIn() {
		return this.fechaInProperty().get();
	}
	


	public final void setFechaIn(final String fechaIn) {
		this.fechaInProperty().set(fechaIn);
	}
	


	public final StringProperty fechaFnProperty() {
		return this.fechaFn;
	}
	


	public final String getFechaFn() {
		return this.fechaFnProperty().get();
	}
	


	public final void setFechaFn(final String fechaFn) {
		this.fechaFnProperty().set(fechaFn);
	}
	
	
		
	
}
