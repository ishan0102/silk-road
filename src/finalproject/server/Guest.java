/*
 * EE422C Final Project submission by
 * Ishan Shah
 * irs428
 * 16165
 * Fall 2020
 */

package finalproject.server;

import java.time.Instant;

class Guest {
	private final Integer id;
	private final String name;
	private final String note;

	private int visits;
	private Instant lastVisit; 

	Guest(String name, String note) {
		this(null, name, 0, note, Instant.now());
	}
	
	Guest(Integer id, String name, int visits, String note, Instant lastVisit) {
		this.id = id;
		this.name = name;
		this.visits = visits;
		this.note = note;
		this.lastVisit = lastVisit;
	}
	
	Integer getId() {
		return id;
	}

	String getName() {
		return name;
	}

	int getVisits() {
		return visits;
	}

	String getNote() {
		return note;
	}

	Instant getLastVisit() {
		return lastVisit;
	}

	void recordVisit() {
		visits++;
		lastVisit = Instant.now();
	}
	
	String info() {
		return name + " (" + note + ") has visited " + visits + " time(s). "
				+ "Their last visit was at " + lastVisit + ".";  
	}
}
