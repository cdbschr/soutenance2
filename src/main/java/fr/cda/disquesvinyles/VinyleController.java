package fr.cda.disquesvinyles;

import javafx.fxml.FXML;
import javafx.scene.control.*;

public class VinyleController {
	@FXML
	private TextField title;

	@FXML
	private ComboBox category;

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
	protected void onClickSearch() {
	}

	@FXML
	protected void onClickClear() {
		title.setText("");
//		category.set;
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
}