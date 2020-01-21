package ch.leadrian.stubr.integrationtest.testdata;

import ch.leadrian.equalizer.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.List;

import static ch.leadrian.equalizer.Equalizer.equalsAndHashCodeBuilder;

public final class Person {

    private static final EqualsAndHashCode<Person> EQUALS_AND_HASH_CODE = equalsAndHashCodeBuilder(Person.class)
            .compareAndHash(Person::getFirstName)
            .compareAndHash(Person::getLastName)
            .compareAndHash(Person::getDateOfBirth)
            .compare(Person::getPets)
            .build();

    private final String firstName;
    private final String lastName;
    private final LocalDate dateOfBirth;
    private final List<Animal> pets;

    public Person(String firstName, String lastName, LocalDate dateOfBirth, List<Animal> pets) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.pets = pets;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public List<Animal> getPets() {
        return pets;
    }

    @Override
    public boolean equals(Object obj) {
        return EQUALS_AND_HASH_CODE.equals(this, obj);
    }

    @Override
    public int hashCode() {
        return EQUALS_AND_HASH_CODE.hashCode(this);
    }
}
