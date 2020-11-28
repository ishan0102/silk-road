/*
 * EE422C Final Project submission by
 * Ishan Shah
 * irs428
 * 16165
 * Fall 2020
 */

import java.time.Instant;

class Guest {
	private final Integer id;
	private final String name;
    private final String email;
    private final String password;
	private Instant lastVisit;

    Guest(String name, String email, String password) {
        this.id = null;
        this.name = name;
        this.email = email;
        this.password = password;
        this.lastVisit = Instant.now();
	}

	Guest(Integer id, String name, String email, String password, Instant lastVisit) {
		this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
		this.lastVisit = lastVisit;
	}
	
	Integer getId() {
		return id;
	}

	String getName() {
		return name;
	}

	String getEmail() {
        return email;
    }

    String getPassword() {
        return password;
    }

	Instant getLastVisit() {
		return lastVisit;
	}

	void recordVisit() {
		lastVisit = Instant.now();
    }
    
    @Override
    public String toString() {
        return "id: " + id + ", name: " + name + ", email: " + email + ", password: " + password + ", lastVisit: " + lastVisit;
    }
}
