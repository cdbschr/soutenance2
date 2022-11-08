package fr.cda.disquesvinyles;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;

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
	private ProgressBar searchBar;
	@FXML
	private TextArea showResult;

	//fonction de recherche au clic sur le bouton "Rechercher"
	@FXML
	protected void onClickSearch() {
		//TODO function recuperation des infos saisies
		String searchTitle = title.getText();

		String searchCategory = category.getValue();
		searchCategory = searchCategory != null ? searchCategory : "";

		String searchDate = "";
		if (pickDate.getValue() == null) {
			showResult.setText("Veuillez saisir une date avant d'effectuer une recherche");
		}
		searchDate = Integer.toString(pickDate.getValue().getYear());

		String searchPriceMin = priceMin.getText();
		searchPriceMin = searchPriceMin != null ? searchPriceMin : "";
		String searchPriceMax = priceMax.getText();
		searchPriceMax = searchPriceMax != null ? searchPriceMax : "";

		if (discogs.isSelected()) {
			try {
				String res = Scrapper.scrapDiscogs(searchTitle, searchCategory, searchDate, searchPriceMin, searchPriceMax);
				System.out.println("result = " + res);
				showResult.setText(res);
			} catch (IOException e) {
				e.printStackTrace();
				String result = "Une erreur est survenue, merci de réessayer plus tard";
				showResult.setText(result);
			}
		}

		if (fnac.isSelected()) {
			try {
				String res = Scrapper.scrapFnac(searchTitle, searchDate, searchPriceMin, searchPriceMax);
				System.out.println("result = " + res);
				showResult.setText(res);
			} catch (IOException e) {
				e.printStackTrace();
				String result = "Une erreur est survenue, merci de réessayer plus tard";
				showResult.setText(result);
			}
		}

		if (vinylcorner.isSelected()) {
			try {
				String res = Scrapper.scrapVinylCorner(searchTitle, searchCategory, searchDate, searchPriceMin, searchPriceMax);
				System.out.println("result = " + res);
				showResult.setText(res);
			} catch (IOException e) {
				e.printStackTrace();
				String result = "Une erreur est survenue, merci de réessayer plus tard";
				showResult.setText(result);
			}
		}

		if (leboncoin.isSelected()) {
			try {
				String res = Scrapper.scrapLeboncoin(searchTitle, searchPriceMin, searchPriceMax);
				System.out.println("result = " + res);
				showResult.setText(res);
			} catch (IOException e) {
				e.printStackTrace();
				String result = "Une erreur est survenue, merci de réessayer plus tard";
				showResult.setText(result);
			}
		}

		if (mesvinyles.isSelected()) {
			try {
				String res = Scrapper.scrapMesVinyles(searchTitle, searchDate, searchPriceMin, searchPriceMax);
				System.out.println("result = " + res);
				showResult.setText(res);
			} catch (IOException e) {
				e.printStackTrace();
				String result = "Une erreur est survenue, merci de réessayer plus tard";
				showResult.setText(result);
			}
		}

		if (culturefactory.isSelected()) {
			try {
				String res = Scrapper.scrapCultureFactory(searchTitle, searchCategory, searchPriceMin, searchPriceMax);
				System.out.println("result = " + res);
				showResult.setText(res);
			} catch (IOException e) {
				e.printStackTrace();
				String result = "Une erreur est survenue, merci de réessayer plus tard";
				showResult.setText(result);
			}
		}
	}

	//Actions des boutons dans le home
	@FXML
	protected void onClickClear() {
		title.setText("");
		category.getSelectionModel().clearSelection();
		refreshComboBox();
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

	//Réinitialise la ComboBox
	public void refreshComboBox() {
		category.setButtonCell(new ListCell<>() {
			@Override
			protected void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);
				if (item == null || empty) {
					setText(category.getPromptText());
				} else {
					setText(item);
				}
			}
		});
	}

	//Actions du Menu Fichier
	@FXML
	public void saveFile() throws IOException {
		if (showResult.getText().equals("") || showResult.getText().equals("Veuiller lancer une recherche, pour afficher un résultat et lancer un enregistrement.")) {
			showResult.setText("Veuiller lancer une recherche, pour afficher un résultat et lancer un enregistrement.");

		} else {
			String searchTxt = showResult.getText();

			try {
				FileChooser fileChooser = new FileChooser();
				FileChooser.ExtensionFilter extFilter =
								new FileChooser.ExtensionFilter("TEXT files (*.txt)", "*.txt");
				fileChooser.getExtensionFilters().add(extFilter);
				fileChooser.setInitialDirectory(new File("./recherches/"));
				fileChooser.setTitle("");
				File selectedFile = fileChooser.showSaveDialog(null);
				String path = selectedFile.getAbsolutePath();

				PrintWriter print = new PrintWriter(new BufferedWriter
								(new FileWriter(path)));
				print.println(searchTxt);
				print.close();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	//affichage modal pour les Mails
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

		button1.setOnAction(e -> {
							Mail.send(tf1.getText(), showResult.getText());
							modal.close();
						});

		button2.setOnAction(e -> modal.close());

		VBox layout = new VBox(15);
		layout.getChildren().addAll(label1, label2, tf1, button1, button2);
		layout.setAlignment(Pos.CENTER);
		Scene scene1 = new Scene(layout, 300, 250);
		modal.setScene(scene1);
		modal.showAndWait();
	}

	//affichage de la modal pour la sauvegarde de données
	public void modalSaveDb() throws IOException {
		Stage modal = new Stage();

		modal.initModality(Modality.APPLICATION_MODAL);
		modal.setTitle("Transmission BDD");
		Label label1 = new Label("Transmission des données à la base de données");
		Label label2 = new Label("Cliquez sur Valider pour lancer la transmission");
		Button button1 = new Button("Valider");
		Button button2 = new Button("Fermer");

		//TODO récupérer les valeurs du scrapping

		//TODO récupérer les valeurs entrées dans la fenêtre ihmBDD

		//TODO faire une boucle avec l'envoi des informations dans la base de données via des requêtes

		button2.setOnAction(e -> modal.close());

		VBox layout = new VBox(15);
		layout.getChildren().addAll(label1, label2, button1, button2);
		layout.setAlignment(Pos.CENTER);
		Scene scene1 = new Scene(layout, 300, 250);
		modal.setScene(scene1);
		modal.showAndWait();
	}

	//ferme le software
	@FXML
	protected void onCloseClick() {
		Platform.exit();
	}

	//ferme les modals
	@FXML
	protected void onClickCloseScene() {
		Stage stage = (Stage) closeDb.getScene().getWindow();
		stage.close();
	}

	//Page pour la base de données
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