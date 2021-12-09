package pl.edu.pw.ee.dictionary;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class Dictionary implements Iterable<DictionaryElement> {
    private final List<DictionaryElement> elems = new ArrayList<>(255);

    @Override
    public Iterator<DictionaryElement> iterator() {
        return elems.iterator();
    }

    public void put(char character, String binaryCode) {
        if (binaryCode == null || binaryCode.isEmpty() || binaryCode.isBlank()) {
            throw new IllegalArgumentException("Wrong prefix code was provided");
        }
        elems.add(new DictionaryElement(character, binaryCode));
    }

    public String getPrefixCode(char ch) {
        Optional<DictionaryElement> first = elems.stream().filter((node) -> node.character == ch).findFirst();
        return first.isEmpty() ? null : first.get().prefixCode;
    }

    public boolean has(String prefixCode) {
        for (DictionaryElement elem : elems) {
            if (elem.prefixCode.equals(prefixCode)) {
                return true;
            }
        }
        return false;
    }

    public char getByPrefixCode(String prefixCode) {
        for (DictionaryElement elem : elems) {
            if (elem.prefixCode.equals(prefixCode)) {
                return elem.character;
            }
        }
        throw new IllegalArgumentException("Dictionary doesn't contain character for " + prefixCode);
    }
}
