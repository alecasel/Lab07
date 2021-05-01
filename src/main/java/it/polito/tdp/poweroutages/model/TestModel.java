package it.polito.tdp.poweroutages.model;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class TestModel {
	
	private static LocalDateTime start;
	private static LocalDateTime end;
	private static long duration;

	public static void main(String[] args) {
		
		Model model = new Model();
		System.out.println(model.getNercList());
		
		duration = start.until(end, ChronoUnit.HOURS);
		System.out.println(duration);

	}

}
