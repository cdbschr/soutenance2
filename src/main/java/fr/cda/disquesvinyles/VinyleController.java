package fr.cda.disquesvinyles;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import javafx.application.Platform;
import javafx.event.ActionEvent;
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
import java.util.List;

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
	private TextArea showResult;

	File resultFile = new File("result.txt");

	@FXML
	protected void onClickSearch() {
		//TODO function recuperation des infos saisies
		String searchTitle = title.getText();
		String categorie = category.getValue();
		String searchDate = pickDate.getValue()
						.toString()
						.substring(0, 4);
		String searchPriceMin = priceMin.getText();
		String searchPriceMax = priceMax.getText();
		boolean searchDiscogs = discogs.isSelected();
		boolean searchFnac = fnac.isSelected();
		boolean searchVinylCorner = vinylcorner.isSelected();
		boolean searchLeboncoin = leboncoin.isSelected();
		boolean searchMesVinyles = mesvinyles.isSelected();
		boolean searchCultureFactory = culturefactory.isSelected();

		//TODO function make crawl on website

		//TODO function afficher le résultat
	}

	//Actions des boutons dans le home
	@FXML
	protected void onClickClear() {
		title.setText("");
		category.getSelectionModel().selectFirst();
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
				fileChooser.setInitialDirectory(new File("scrap"));
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
		button1.setOnAction(e ->
						Mail.send(tf1.getText(), resultFile));
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

	public void ScrapLeboncoin(String searchTitle, String searchPriceMin, String searchPriceMax) {
		String url = "https://leboncoin.fr/recherche?category=26&text=" + searchTitle + "&price=" + searchPriceMin + "-" + searchPriceMax;

		try {
			WebClient webClient = new WebClient();

			webClient.getOptions().setUseInsecureSSL(true);
			webClient.getOptions().setCssEnabled(false);
			webClient.getOptions().setJavaScriptEnabled(false);
			HtmlPage htmlPage = webClient.getPage(url);

			List<HtmlElement> li = htmlPage.getByXPath("//div[1]/div[1]/p");

			String res = "";
			for (HtmlElement e : li) {
				HtmlPage htmlPage1 = webClient.getPage(e.click().getUrl());
				String nomArticle = "";
				String prixArticle = "";
				String description = "";

				List<HtmlElement> nom = htmlPage1.getByXPath("//h1[@data-qa-id='adview_title']");
				List<HtmlElement> prix = htmlPage1.getByXPath("//span[@class='Roh2X _3gP8T _35DXM _25LNb']");
				List<HtmlElement> desc = htmlPage1.getByXPath("//p[@class='sc-bhlBdH gOkeRT']");

				for (HtmlElement n : nom) {
					nomArticle = n.getTextContent();

				}
				for (HtmlElement p : prix) {
					prixArticle = p.getTextContent();
					prixArticle = prixArticle.replace("\u00a0", "");
				}
				for (HtmlElement d : desc) {
					description = d.getTextContent();
				}

				res += "Article : " + nomArticle +
								"\n Prix : " + prixArticle +
								"\n Description de l'article : " + description +
								"\n Lien : " + htmlPage1.getUrl() +
								"\n--------------------------------------------------------------------\n";
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}