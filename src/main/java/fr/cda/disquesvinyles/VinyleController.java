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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import io.github.cdimascio.dotenv.Dotenv;


/**
 * The type Vinyle controller.
 */
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

	/**
	 * The Ip server.
	 */
	@FXML
	TextField ipServer;
	/**
	 * The Db name.
	 */
	@FXML
	TextField dbName;
	/**
	 * The Port number.
	 */
	@FXML
	TextField portNumber;
	/**
	 * The User log.
	 */
	@FXML
	TextField userLog;
	/**
	 * The User pwd.
	 */
	@FXML
	TextField userPwd;

	/**
	 * The constant articles.
	 */
	public static ArrayList<Article> articles = new ArrayList<Article>();

	/**
	 * Affiche article string.
	 *
	 * @return the string
	 */
	public String afficheArticle() {
		String result = "";
		for (Article article : articles) {
			result += article.toString();
		}
		return result;
	}

	/**
	 * On click search.
	 */
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

		String finalRes = "";
		if (discogs.isSelected()) {
			try {
				String res = Scrapper.scrapDiscogs(searchTitle, searchCategory, searchDate, searchPriceMin, searchPriceMax);
				System.out.println("result = " + res);
				finalRes += res;
			} catch (IOException e) {
				e.printStackTrace();
				String result = "Une erreur est survenue sur Discogs, merci de réessayer plus tard";
				showResult.setText(result);
			}
		}

		if (fnac.isSelected()) {
			try {
				String res = Scrapper.scrapFnac(searchTitle, searchDate, searchPriceMin, searchPriceMax);
				System.out.println("result = " + res);
				finalRes += res;
			} catch (IOException e) {
				e.printStackTrace();
				String result = "Une erreur est survenue sur Fnac, merci de réessayer plus tard";
				showResult.setText(result);
			}
		}

		if (vinylcorner.isSelected()) {
			try {
				String res = Scrapper.scrapVinylCorner(searchTitle, searchCategory, searchDate, searchPriceMin, searchPriceMax);
				System.out.println("result = " + res);
				finalRes += res;
			} catch (IOException e) {
				e.printStackTrace();
				String result = "Une erreur est survenue sur VinylCorner, merci de réessayer plus tard";
				showResult.setText(result);
			}
		}

		if (leboncoin.isSelected()) {
			try {
				String res = Scrapper.scrapLeboncoin(searchTitle, searchPriceMin, searchPriceMax);
				System.out.println("result = " + res);
				finalRes += res;
			} catch (IOException e) {
				e.printStackTrace();
				String result = "Une erreur est survenue sur Leboncoin, merci de réessayer plus tard";
				showResult.setText(result);
			}
		}

		if (mesvinyles.isSelected()) {
			try {
				String res = Scrapper.scrapMesVinyles(searchTitle, searchDate, searchPriceMin, searchPriceMax);
				System.out.println("result = " + res);
				finalRes += res;
			} catch (IOException e) {
				e.printStackTrace();
				String result = "Une erreur est survenue sur MesVinyles, merci de réessayer plus tard";
				showResult.setText(result);
			}
		}

		if (culturefactory.isSelected()) {
			try {
				String res = Scrapper.scrapCultureFactory(searchTitle, searchCategory, searchPriceMin, searchPriceMax);
				System.out.println("result = " + res);
				finalRes += res;
				showResult.setText(res);
			} catch (IOException e) {
				e.printStackTrace();
				String result = "Une erreur est survenue sur CultureFactory, merci de réessayer plus tard";
				showResult.setText(result);
			}
		}
		showResult.setText(finalRes);
	}

	/**
	 * On click clear.
	 */
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

	/**
	 * Refresh combo box.
	 */
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

	/**
	 * Save file.
	 *
	 * @throws IOException the io exception
	 */
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

	/**
	 * Modal mail.
	 *
	 * @throws IOException the io exception
	 */
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

	/**
	 * Modal save db.
	 *
	 * @throws IOException the io exception
	 */
//affichage de la modal pour la sauvegarde de données
	public void modalSaveDb() throws IOException {
		Stage modal = new Stage();

		modal.initModality(Modality.APPLICATION_MODAL);
		modal.setTitle("Transmission BDD");
		Label label1 = new Label("Transmission des données à la base de données");
		Label label2 = new Label("Cliquez sur Valider pour lancer la transmission");
		Button button1 = new Button("Valider");
		Button button2 = new Button("Fermer");

		button1.setOnAction(e -> {
			onClickSaveDb();
			modal.close();
		});
		button2.setOnAction(e -> modal.close());

		VBox layout = new VBox(15);
		layout.getChildren().addAll(label1, label2, button1, button2);
		layout.setAlignment(Pos.CENTER);
		Scene scene1 = new Scene(layout, 300, 250);
		modal.setScene(scene1);
		modal.showAndWait();
	}

	/**
	 * On close click.
	 */
//ferme le software
	@FXML
	protected void onCloseClick() {
		Platform.exit();
	}

	/**
	 * On click close scene.
	 */
//ferme les modals
	@FXML
	protected void onClickCloseScene() {
		Stage stage = (Stage) closeDb.getScene().getWindow();
		stage.close();
	}

	/**
	 * Db page.
	 *
	 * @throws IOException the io exception
	 */
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

	/**
	 * Test db connexion.
	 */
	public void testDbConnexion() {
		String serverName = ipServer.getText();
		String mydatabase = dbName.getText();
		String port = portNumber.getText();
		String user = userLog.getText();
		String password = userPwd.getText();

		try {
			Connection connexion = DriverManager.getConnection("jdbc:mysql://" + serverName + ":" + port + "/" + mydatabase, user, password);
			System.out.println("Connexion réussie");

		} catch (SQLException e) {
			System.out.println("Connexion échouée");
			e.printStackTrace();
		}
	}

	/**
	 * Save db.
	 *
	 * @param title    the title
	 * @param desc     the desc
	 * @param price    the price
	 * @param category the category
	 * @param year     the year
	 */
	public void saveDb(String title, String desc, String price, int category, String year) {
		Dotenv dotenv = Dotenv.load();

		try (Connection connexion = DriverManager.getConnection(
						"jdbc:mysql://" + dotenv.get("DB_HOST") + ":" + dotenv.get("DB_PORT") + "/" + dotenv.get("DB_NAME"), dotenv.get("DB_USER"), dotenv.get("DB_PASSWORD"));
				 PreparedStatement preparedStatement = connexion.prepareStatement("INSERT INTO recherche (titre, description, prix, genre, description) VALUES (?, ?, ?, ?, ?)")) {
			preparedStatement.setString(1, title);
			preparedStatement.setString(2, desc);
			preparedStatement.setString(3, price);
			preparedStatement.setInt(4, category);
			preparedStatement.setString(5, year);
			int row = preparedStatement.executeUpdate();
			System.out.println(row + " row(s) affected");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * On click save db.
	 */
	@FXML
	public void onClickSaveDb() {
		try {
			testDbConnexion();

			for (Article article : articles) {
				int idGenre = switch (article.getCategory()) {
					case "Rock" -> 1;
					case "Blues" -> 2;
					case "Jazz" -> 3;
					case "Reggae" -> 4;
					case "Funk" -> 5;
					case "Electro" -> 6;
					case "DubStep" -> 7;
					case "Soul" -> 8;
					default -> 0;
				};

				saveDb(article.getTitle(), article.getDescription(), article.getPrice(), idGenre, article.getDate());
				System.out.println("Tout est sauvegardé");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Mode d'emploi for users
	 */
	public void onClickManual() throws IOException {
		File manual = new File("manual.txt");
		String line;
		String res = "";

		Stage popupwindow=new Stage();
		popupwindow.initModality(Modality.APPLICATION_MODAL);
		popupwindow.setTitle("Mode d'emploi");
		TextArea ta = new TextArea();
		ta.setPrefWidth(1000);
		ta.setPrefHeight(515);

		FileReader fr = new FileReader(manual);
		BufferedReader br = new BufferedReader(fr);

		while((line = br.readLine()) != null)
		{
			res += line +"\n";
		}

		fr.close();
		ta.setText(res);
		ta.setEditable(false);

		VBox layout= new VBox(8);
		layout.getChildren().addAll(ta);
		layout.setAlignment(Pos.CENTER);
		Scene scene1= new Scene(layout, 1000, 515);
		popupwindow.setScene(scene1);
		popupwindow.showAndWait();
	}
}