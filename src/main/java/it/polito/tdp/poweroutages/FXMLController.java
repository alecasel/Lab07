/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.poweroutages;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import it.polito.tdp.poweroutages.model.Model;
import it.polito.tdp.poweroutages.model.Nerc;
import it.polito.tdp.poweroutages.model.PowerOutage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {

	@FXML // ResourceBundle that was given to the FXMLLoader
	private ResourceBundle resources;

	@FXML // URL location of the FXML file that was given to the FXMLLoader
	private URL location;

	@FXML // fx:id="cmbNerc"
	private ComboBox<Nerc> cmbNerc; // Value injected by FXMLLoader

	@FXML // fx:id="txtYears"
	private TextField txtYears; // Value injected by FXMLLoader

	@FXML // fx:id="txtHours"
	private TextField txtHours; // Value injected by FXMLLoader

	@FXML // fx:id="txtResult"
	private TextArea txtResult; // Value injected by FXMLLoader

	private Model model;

	@FXML
	void doRun(ActionEvent event) {
		txtResult.clear();

		try {
			/*
			 * CONTROLLO 1): selezione nerc
			 */
			Nerc nerc = cmbNerc.getValue();
			if (nerc == null) {
				txtResult.setText("Select a NERC");
				return;
			}

			/*
			 * CONTROLLO 2): selezione years
			 */
			int maxYears = Integer.parseInt(txtYears.getText());
			if (maxYears <= 0) {
				txtResult.setText("Select a number > 0");
				return;
			}

			/*
			 * CONTROLLO 3): selezione hours
			 */
			int maxHours = Integer.parseInt(txtHours.getText());
			if (maxHours <= 0) {
				txtResult.setText("Select a number > 0");
				return;
			}

			// Siccome il processo può richiedere molto tempo, decido di scrivere in
			// txtResult che sta avvenendo l'elaborazione
			txtResult.setText("Computing the worst case analysis...");

			// Parte il processo
			List<PowerOutage> rslt = model.findBest(nerc, maxYears, maxHours);

			// Pulisco txtResult e ci metto il risultato dell'algoritmo
			txtResult.clear();
			txtResult.appendText("Tot people affected" + model.affectedPeople(rslt) + "\n");
			txtResult.appendText("Tot hours of outage" + model.sumHours(rslt) + "\n");

			for (PowerOutage powerOutage : rslt) {
				txtResult.appendText(powerOutage.getYear() + " " + powerOutage.getStart() + " " + powerOutage.getEnd()
						+ " " + powerOutage.getDuration() + " " + powerOutage.getCustomersAffected() + "\n");
			}

		} catch (NumberFormatException e) {
			txtResult.setText("Insert a valid number of years and of hours");
		}
	}

	@FXML // This method is called by the FXMLLoader when initialization is complete
	void initialize() {
		assert cmbNerc != null : "fx:id=\"cmbNerc\" was not injected: check your FXML file 'Scene.fxml'.";
		assert txtYears != null : "fx:id=\"txtYears\" was not injected: check your FXML file 'Scene.fxml'.";
		assert txtHours != null : "fx:id=\"txtHours\" was not injected: check your FXML file 'Scene.fxml'.";
		assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

		// Utilizzare questo font per incolonnare correttamente i dati;
		txtResult.setStyle("-fx-font-family: monospace");
	}

	public void setModel(Model model) {
		this.model = model;

		/*
		 * Devo inserire nella combo le possibilità
		 */
		List<Nerc> nercList = model.getNercList();
		cmbNerc.getItems().addAll(nercList);
	}
}
