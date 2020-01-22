package ch.leadrian.stubr.integrationtest.testdata;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Set;
import java.util.SortedSet;

public interface Collections {

    Collection<String> getCollection();

    List<String> getList();

    Set<String> getSet();

    SortedSet<String> getSortedSet();

    Map<Integer, String> getMap();

    NavigableMap<Integer, String> getNavigableMap();

}
