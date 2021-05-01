package it.polito.tdp.poweroutages.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import it.polito.tdp.poweroutages.DAO.PowerOutageDAO;

public class Model {

	PowerOutageDAO podao;

	private ArrayList<PowerOutage> best;
	private List<PowerOutage> powerOutageList;
	
	private int maxAffectedPeople;

	public Model() {
		podao = new PowerOutageDAO();
	}

	public List<Nerc> getNercList() {
		return podao.getNercList();
	}

	/**
	 * Metodo che sfrutta la ricorsione
	 * @param nerc
	 * @param maxYears
	 * @param maxHours
	 * @return best
	 */
	public List<PowerOutage> findBest(Nerc nerc, int maxYears, int maxHours) {
		
		/**
		 * Devo creare una lista per utilizzare il metodo search()
		 */
		List<PowerOutage> partial = new ArrayList<>();
		
		// INIZIALIZZAZIONI (CASO PEGGIORE)
		powerOutageList = podao.getPwrOutagesByNerc(nerc);
		best = null;
		maxAffectedPeople = 0;
		
		search(partial, maxYears, maxHours);
		
		return best;
	}
	
	/**
	 * RICORSIONE
	 * 
	 * @param partial
	 * @param level
	 */
	public void search(List<PowerOutage> partial, int maxYears, int maxHours) {

		// CASO TERMINALE: NON ESISTE
		// Selezionare blackout che si sono verificati in un numero MASSIMO di X anni
		// per un totale di Y ore di disservizio MASSIMO
		/*
		 * Di conseguenza, non ha senso mettere il level di ricorsione!
		 */

		// OBIETTIVO: massimizzare il numero totale di persone coinvolte
		/*
		 * ogni volta che ottengo una nuova soluzione parziale, devo controllare se è
		 * migliore delle precedenti ovvero guardare il numero delle persone
		 * interessate. Per trovare la best, allora devo salvare per ogni partial il
		 * numero di persone interessate e ogni volta confrontare con il nuovo numero
		 * trovato. Se trovo un nuovo massimo, mi salvo la nuova partial (e il nuovo
		 * massimo) come best
		 */
		if (affectedPeople(partial) > maxAffectedPeople) {
			maxAffectedPeople = affectedPeople(partial);
			best = new ArrayList<>(partial);
		}

		// VINCOLI da tener conto per comporre la partial
		/*
		 * Prendo l'elenco di tutti gli oggetti PowerOutage presenti nel db, li scorro
		 * uno ad uno andando a inserire in partial soltanto quelli che rispettano i
		 * vincoli dati.
		 */
		// ADD + BACKTRACKING
		for (PowerOutage powerOutage : powerOutageList) {
			if (isValid(powerOutage, partial, maxYears, maxHours)) {
				partial.add(powerOutage);
				search(partial, maxYears, maxHours);
				partial.remove(partial.size() - 1);
			}
		}

	}

	private boolean isValid(PowerOutage powerOutage, List<PowerOutage> partial, int maxYears, int maxHours) {

		/*
		 * Perché l'aggiunta sia valida, è innanzitutto necessario che powerOutage non
		 * sia già presente in partial. Altrimenti sarebbe ripetere un evento unico.
		 */
		if (partial.contains(powerOutage)) {
			return false;
		}

		// VINCOLO 1): numero totale di ore di partial deve essere al massimo = a
		// maxHours
		if (sumHours(partial) > maxHours) {
			return false;
		}

		// VINCOLO 2): differenza tra l'anno del powerOutage più recente e quello più
		// vecchio dev'essere al massimo = a maxYears
		if (differenceYears(partial) > maxYears) {
			return false;
		}

		return true;
	}

	private int differenceYears(List<PowerOutage> partial) {
		/*
		 * Per fare una differenza tra l'evento più recente e quello più vecchio
		 * contenuti in partial, mi conviene ordinare tutti gli eventi di partial in
		 * ordine crescente, così so che devo fare la differenza tra l'ultimo e il primo
		 * della lista
		 */
		Collections.sort(partial);

		/*
		 * Importante tener conto che il controllo va fatto se partial è composto da
		 * almeno due POutages
		 */
		
		int difference = 0;
		
		if (partial.size() >= 2) {
			int firstYear = partial.get(0).getYear();
			int lastYear = partial.get(partial.size()-1).getYear();
			difference = lastYear - firstYear;
		}

		return difference;
	}

	public int sumHours(List<PowerOutage> partial) {
		/*
		 * Devo sommare la durata di ogni POutage presente in partial. Per trovare la
		 * durata di ogni POutage, devo fare la differenza tra l'ora di fine e quella di
		 * inizio => mi servo di un parametro dell'oggetto che creo direttamente nel
		 * Bean
		 */

		int sum = 0;

		for (PowerOutage powerOutage : partial) {
			sum += powerOutage.getDuration();
		}

		return sum;
	}

	public int affectedPeople(List<PowerOutage> partial) {
		int sum = 0;
		
		for (PowerOutage powerOutage : partial) {
			sum += powerOutage.getCustomersAffected();
		}
		
		return sum;
	}

}
