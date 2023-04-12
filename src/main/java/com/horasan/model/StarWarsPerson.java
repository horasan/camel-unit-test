package com.horasan.model;

import java.util.List;

public class StarWarsPerson {
									// doc: https://swapi.dev/documentation#people
	private String name;			// string -- The name of this person.
	private String birth_year;		// string -- The birth year of the person, using the in-universe standard of BBY or ABY - Before the Battle of Yavin or After the Battle of Yavin. The Battle of Yavin is a battle that occurs at the end of Star Wars episode IV: A New Hope.
	private String eye_color;		// string -- The eye color of this person. Will be "unknown" if not known or "n/a" if the person does not have an eye.
	private String gender;			// string -- The gender of this person. Either "Male", "Female" or "unknown", "n/a" if the person does not have a gender.
	private String hair_color;		// string -- The hair color of this person. Will be "unknown" if not known or "n/a" if the person does not have hair.
	private String height;			// string -- The height of the person in centimeters.
	private String mass;			// string -- The mass of the person in kilograms.
	private String skin_color;		// string -- The skin color of this person.
	private String homeworld;		// string -- The URL of a planet resource, a planet that this person was born on or inhabits.
	private List<String> films;		// array -- An array of film resource URLs that this person has been in.
	private List<String> species;	// array -- An array of species resource URLs that this person belongs to.
	private List<String> starships;	// array -- An array of starship resource URLs that this person has piloted.
	private List<String> vehicles;	// array; // -- An array of vehicle resource URLs that this person has piloted.
	private String url;				// string -- the hypermedia URL of this resource.
	private String created;			// string -- the ISO 8601 date format of the time that this resource was created.
	private String edited;			// string -- the ISO 8601 date format of the time that this resource was edited

	public StarWarsPerson() {}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBirth_year() {
		return birth_year;
	}

	public void setBirth_year(String birth_year) {
		this.birth_year = birth_year;
	}

	public String getEye_color() {
		return eye_color;
	}

	public void setEye_color(String eye_color) {
		this.eye_color = eye_color;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getHair_color() {
		return hair_color;
	}

	public void setHair_color(String hair_color) {
		this.hair_color = hair_color;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getMass() {
		return mass;
	}

	public void setMass(String mass) {
		this.mass = mass;
	}

	public String getSkin_color() {
		return skin_color;
	}

	public void setSkin_color(String skin_color) {
		this.skin_color = skin_color;
	}

	public String getHomeworld() {
		return homeworld;
	}

	public void setHomeworld(String homeworld) {
		this.homeworld = homeworld;
	}

	public List<String> getFilms() {
		return films;
	}

	public void setFilms(List<String> films) {
		this.films = films;
	}

	public List<String> getSpecies() {
		return species;
	}

	public void setSpecies(List<String> species) {
		this.species = species;
	}

	public List<String> getStarships() {
		return starships;
	}

	public void setStarships(List<String> starships) {
		this.starships = starships;
	}

	public List<String> getVehicles() {
		return vehicles;
	}

	public void setVehicles(List<String> vehicles) {
		this.vehicles = vehicles;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public String getEdited() {
		return edited;
	}

	public void setEdited(String edited) {
		this.edited = edited;
	}

	@Override
	public String toString() {
		return "StarWarsPeople [name=" + name + ", birth_year=" + birth_year + ", eye_color=" + eye_color + ", gender="
				+ gender + ", hair_color=" + hair_color + ", height=" + height + ", mass=" + mass + ", skin_color="
				+ skin_color + ", homeworld=" + homeworld + ", films=" + films + ", species=" + species + ", starships="
				+ starships + ", vehicles=" + vehicles + ", url=" + url + ", created=" + created + ", edited=" + edited
				+ "]";
	}
	
	
}
