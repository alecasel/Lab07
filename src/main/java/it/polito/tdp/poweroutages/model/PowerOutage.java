package it.polito.tdp.poweroutages.model;

import java.time.temporal.ChronoUnit;
import java.time.LocalDateTime;

public class PowerOutage implements Comparable<PowerOutage> {
	
	private int id;
	private Nerc nerc;
	private int customersAffected;
	private LocalDateTime start;
	private LocalDateTime end;

	/*
	 * Creo parametri che mi servono nel Model
	 */
	private long duration; // long !!!
	private int year;

	public PowerOutage(int id, Nerc nerc, int customersAffected, LocalDateTime start, LocalDateTime end) {
		this.id = id;
		this.nerc = nerc;
		this.customersAffected = customersAffected;
		this.start = start;
		this.end = end;

		this.duration = start.until(end, ChronoUnit.HOURS);
		/*
		 * Non c'è scritto nella consegna ma come anno prendo quello di start.
		 * ATT! getYear() è un metodo pre-confezionato per la classe LDT
		 */
		this.year = start.getYear();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Nerc getNerc() {
		return nerc;
	}

	public void setNerc(Nerc nerc) {
		this.nerc = nerc;
	}

	public int getCustomersAffected() {
		return customersAffected;
	}

	public void setCustomersAffected(int customersAffected) {
		this.customersAffected = customersAffected;
	}

	public LocalDateTime getStart() {
		return start;
	}

	public void setStart(LocalDateTime start) {
		this.start = start;
	}

	public LocalDateTime getEnd() {
		return end;
	}

	public void setEnd(LocalDateTime end) {
		this.end = end;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PowerOutage other = (PowerOutage) obj;
		if (id != other.id)
			return false;
		return true;
	}

	public long getDuration() {
		return duration;
	}

	/**
	 * Devo ordinare per LocalDateTime: confronto gli START dei due POutages
	 */
	@Override
	public int compareTo(PowerOutage o) {
		return this.getStart().compareTo(o.getStart());
	}

	public int getYear() {
		return year;
	}

}
