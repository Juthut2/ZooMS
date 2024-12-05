package com.example.zooms;

public class Animal {
    private String animal_id;
    private String animal_name;
    private String animal_species;
    private String animal_sex;
    private int animal_age;
    private String animal_status;
    private String date_of_birth;
    private String enclosure_id;

    public Animal(String animal_id, String animal_name, String animal_species, String animal_sex, int animal_age, String animal_status, String date_of_birth, String enclosure_id) {
        this.animal_id = animal_id;
        this.animal_name = animal_name;
        this.animal_species = animal_species;
        this.animal_sex = animal_sex;
        this.animal_age = animal_age;
        this.animal_status = animal_status;
        this.date_of_birth = date_of_birth;
        this.enclosure_id = enclosure_id;
    }

    // Getter and Setter for animal_id
    public String getAnimal_id() {
        return animal_id;
    }

    public void setAnimal_id(String animal_id) {
        this.animal_id = animal_id;
    }

    // Getter and Setter for animal_name
    public String getAnimal_name() {
        return animal_name;
    }

    public void setAnimal_name(String animal_name) {
        this.animal_name = animal_name;
    }

    // Getter and Setter for animal_species
    public String getAnimal_species() {
        return animal_species;
    }

    public void setAnimal_species(String animal_species) {
        this.animal_species = animal_species;
    }

    // Getter and Setter for animal_sex
    public String getAnimal_sex() {
        return animal_sex;
    }

    public void setAnimal_sex(String animal_sex) {
        this.animal_sex = animal_sex;
    }

    // Getter and Setter for animal_age
    public int getAnimal_age() {
        return animal_age;
    }

    public void setAnimal_age(int animal_age) {
        this.animal_age = animal_age;
    }

    // Getter and Setter for animal_status
    public String getAnimal_status() {
        return animal_status;
    }

    public void setAnimal_status(String animal_status) {
        this.animal_status = animal_status;
    }

    // Getter and Setter for date_of_birth
    public String getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(String date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    // Getter and Setter for enclosure_id
    public String getEnclosure_id() {
        return enclosure_id;
    }

    public void setEnclosure_id(String enclosure_id) {
        this.enclosure_id = enclosure_id;
    }
}
