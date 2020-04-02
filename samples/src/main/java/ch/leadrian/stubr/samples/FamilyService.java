package ch.leadrian.stubr.samples;

public class FamilyService {

    private final FamilyFactory familyFactory;

    public FamilyService(FamilyFactory familyFactory) {
        this.familyFactory = familyFactory;
    }

    public Family performConservativeMarriage(Person woman, Person man) {
        return familyFactory.create(woman, man);
    }

}
