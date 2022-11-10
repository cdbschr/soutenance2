package fr.cda.disquesvinyles;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * The type Vinyle application.
 */
public class VinyleApplication extends Application {
	@Override
	public void start(Stage stage) throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(VinyleApplication.class.getResource("ihmAccueil.fxml"));
		Scene scene = new Scene(fxmlLoader.load(), 748, 750);
		stage.setTitle("Scrapping");
		stage.setScene(scene);
		stage.show();
	}

	/**
	 * The entry point of application.
	 *
	 * @param args the input arguments
	 */
	public static void main(String[] args) {
		launch();
	}
}