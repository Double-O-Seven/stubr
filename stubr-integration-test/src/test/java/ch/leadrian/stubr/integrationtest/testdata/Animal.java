package ch.leadrian.stubr.integrationtest.testdata;

import ch.leadrian.equalizer.EqualsAndHashCode;

import static ch.leadrian.equalizer.Equalizer.equalsAndHashCodeBuilder;

public class Animal {

    private static final EqualsAndHashCode<Animal> EQUALS_AND_HASH_CODE = equalsAndHashCodeBuilder(Animal.class)
            .compareAndHash(Animal::getName)
            .build();

    private final String name;

    public Animal(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
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
