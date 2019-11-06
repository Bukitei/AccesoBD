package main;

import accesobd.accesoBDController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class App extends Application {

	accesoBDController access;
	
	@Override
	public void start(Stage primaryStage) throws Exception {

		access = new accesoBDController();
		
		VBox root = access.getView();
		Scene scene = new Scene(root, 800, 600);
		
		primaryStage.setTitle("Acceso a base de datos");
		primaryStage.setScene(scene);
		primaryStage.show();

	}

	public static void main(String[] args) {
		
		launch(args);

	}

}
