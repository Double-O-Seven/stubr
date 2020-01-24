package ch.leadrian.stubr.integrationtest.testdata;

import ch.leadrian.stubr.integrationtest.annotation.CollectionSize;

import java.util.List;

public interface Annotations {

    @CollectionSize(0)
    List<String> getEmptyList();

    @CollectionSize(5)
    List<String> getListOfFiveStrings();

    @CollectionSize(2)
    List<String> getListOfTwoStrings();

}
