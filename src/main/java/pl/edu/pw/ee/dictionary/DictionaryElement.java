package pl.edu.pw.ee.dictionary;

import java.util.Objects;

public class DictionaryElement {
    public final char character;
    public final String prefixCode;

    public DictionaryElement(char character, String prefixCode) {
        this.character = character;
        this.prefixCode = prefixCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DictionaryElement that = (DictionaryElement) o;
        return character == that.character && Objects.equals(prefixCode, that.prefixCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(character, prefixCode);
    }
}
