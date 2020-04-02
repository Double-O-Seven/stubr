package ch.leadrian.stubr.samples;

public interface FamilyFactory {

    Family create(Person mom, Person dad, Person... kids);

}
