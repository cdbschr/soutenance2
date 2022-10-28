package fr.cda.disquesvinyles;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;

public class VinyleController {
	@FXML
	private TextField title;
	@FXML
	private ComboBox<String> category;
	@FXML
	private DatePicker pickDate;
	@FXML
	private TextField priceMin;
	@FXML
	private TextField priceMax;
	@FXML
	private Button search;
	@FXML
	private Button clear;
	@FXML
	private CheckBox discogs;
	@FXML
	private CheckBox fnac;
	@FXML
	private CheckBox vinylcorner;
	@FXML
	private CheckBox leboncoin;
	@FXML
	private CheckBox mesvinyles;
	@FXML
	private CheckBox culturefactory;
	@FXML
	private Button closeDb;

	@FXML
	protected void onClickSearch() {
		//TODO function recuperation des infos saisies
		//TODO function make crawl on website
		//TODO function afficher le résultat
	}

	//Actions des boutons dans le home
	@FXML
	protected void onClickClear() {
		title.setText("");
		pickDate.setValue(null);
		priceMin.setText("");
		priceMax.setText("");
		discogs.setSelected(false);
		fnac.setSelected(false);
		vinylcorner.setSelected(false);
		leboncoin.setSelected(false);
		mesvinyles.setSelected(false);
		culturefactory.setSelected(false);
	}

	//Actions du Menu Fichier
	public void modalMail() throws IOException {
		Stage modal = new Stage();

		modal.initModality(Modality.APPLICATION_MODAL);
		modal.setTitle("Envoi Courriel");
		Label label1 = new Label("Saisie du courriel");
		Label label2 = new Label("Veuillez saisir l'email de l'expediteur :");
		TextField tf1 = new TextField();
		tf1.setPromptText("email@email.com");
		Button button1 = new Button("Envoyer");
		Button button2 = new Button("Fermer");

		//TODO récupérer valeur du scrap
		//TODO récupérer valeur du Textfield
		//TODO envoyer par email

		button2.setOnAction(e -> modal.close());

		VBox layout = new VBox(15);
		layout.getChildren().addAll(label1, label2, tf1, button1, button2);
		layout.setAlignment(Pos.CENTER);
		Scene scene1 = new Scene(layout, 300, 250);
		modal.setScene(scene1);
		modal.showAndWait();
	}

	public void modalSaveDb() throws IOException {
		Stage modal = new Stage();

		modal.initModality(Modality.APPLICATION_MODAL);
		modal.setTitle("Transmission BDD");
		Label label1 = new Label("Transmission des données à la base de données");
		Label label2 = new Label("Cliquez sur Valider pour lancer la transmission");
		Button button1 = new Button("Valider");
		Button button2 = new Button("Fermer");

		//TODO récupérer les valeurs du scrapping
		//TODO faire une boucle avec l'envoi des informations dans la base de données via des requêtes

		button2.setOnAction(e -> modal.close());

		VBox layout = new VBox(15);
		layout.getChildren().addAll(label1, label2, button1, button2);
		layout.setAlignment(Pos.CENTER);
		Scene scene1 = new Scene(layout, 300, 250);
		modal.setScene(scene1);
		modal.showAndWait();
	}

	@FXML
	protected void onCloseClick() {
		Platform.exit();
	}

	@FXML
	protected void onClickCloseScene() {
		Stage stage = (Stage) closeDb.getScene().getWindow();
		stage.close();
	}

	public void DbPage() throws IOException {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(VinyleApplication.class.getResource("ihmBDD.fxml"));

			Scene scene = new Scene(loader.load(), 772, 428);

			Stage stage = new Stage();
			stage.setTitle("Paramètres de la base de données");
			stage.setScene(scene);
			stage.show();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}