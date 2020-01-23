package ch.leadrian.stubr.integrationtest.testdata;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.Set;

public interface Collections {

    Collection<String> getCollection();

    List<String> getList();

    Set<String> getSet();

    NavigableSet<String> getNavigableSet();

    Map<Integer, String> getMap();

    NavigableMap<Integer, String> getNavigableMap();

}
