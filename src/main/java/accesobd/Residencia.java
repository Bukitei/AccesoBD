package accesobd;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import conections.Mysql;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Residencia {
	
	private IntegerProperty codRes, precio, comedor;
	private StringProperty nomRes, codUni, nomUni, comedorS;
	
	
	private Mysql connection = new Mysql();
	
	public Residencia(int codRes, String nomRes, String codUni, int precio, int comedor) {
		try {
			this.codRes = new SimpleIntegerProperty(codRes);
			this.nomRes = new SimpleStringProperty(nomRes);
			this.codUni = new SimpleStringProperty(codUni);
			this.precio = new SimpleIntegerProperty(precio);
			this.comedor = new SimpleIntegerProperty(comedor);
			
			//Le damos automáticamente el nombre de la universidad que tiene su código
			PreparedStatement prepared = connection.conexion.prepareStatement("select nomUniversidad from universidades where codUniversidad = ?");
			prepared.setString(1, codUni);
			ResultSet result = prepared.executeQuery();
			
			result.next();
			this.nomUni = new SimpleStringProperty(result.getString(1));
			
			//Definimos que según el resultado del boolean nos devuelva un String para que se vea mejor
			if(comedor == 1) {
				this.comedorS = new SimpleStringProperty("Si");
			}else {
				this.comedorS = new SimpleStringProperty("No");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
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
	

	public final StringProperty nomResProperty() {
		return this.nomRes;
	}
	

	public final String getNomRes() {
		return this.nomResProperty().get();
	}
	

	public final void setNomRes(final String nomRes) {
		this.nomResProperty().set(nomRes);
	}
	

	public final StringProperty codUniProperty() {
		return this.codUni;
	}
	

	public final String getCodUni() {
		return this.codUniProperty().get();
	}
	

	public final void setCodUni(final String codUni) {
		this.codUniProperty().set(codUni);
	}
	

	public final StringProperty nomUniProperty() {
		return this.nomUni;
	}
	

	public final String getNomUni() {
		return this.nomUniProperty().get();
	}
	

	public final void setNomUni(final String nomUni) {
		this.nomUniProperty().set(nomUni);
	}

	public final StringProperty comedorSProperty() {
		return this.comedorS;
	}
	

	public final String getComedorS() {
		return this.comedorSProperty().get();
	}
	

	public final void setComedorS(final String comedorS) {
		this.comedorSProperty().set(comedorS);
	}

	public final IntegerProperty comedorProperty() {
		return this.comedor;
	}
	

	public final int getComedor() {
		return this.comedorProperty().get();
	}
	

	public final void setComedor(final int comedor) {
		this.comedorProperty().set(comedor);
	}
	
	
	
	
	
	
	

}
