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

    // Constructor
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

    // Getters and Setters
    public String getAnimalId() {
        return animal_id;
    }

    public String getAnimalName() {
        return animal_name;
    }

    public String getAnimalSpecies() {
        return animal_species;
    }

    public String getAnimalSex() {
        return animal_sex;
    }

    public int getAnimalAge() {
        return animal_age;
    }

    public String getAnimalStatus() {
        return animal_status;
    }

    public String getDateOfBirth() {
        return date_of_birth;
    }

    public String getEnclosureId() {
        return enclosure_id;
    }
}
