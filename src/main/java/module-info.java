module com.example.scrap {
	requires javafx.controls;
	requires javafx.fxml;

	requires org.controlsfx.controls;
	requires com.dlsc.formsfx;
	requires htmlunit;
	requires sib.api.v3.sdk;
	requires dotenv.java;
	requires java.sql;

	opens fr.cda.disquesvinyles to javafx.fxml;
	exports fr.cda.disquesvinyles;
}