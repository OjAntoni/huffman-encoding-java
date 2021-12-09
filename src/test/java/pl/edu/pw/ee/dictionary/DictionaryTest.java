package pl.edu.pw.ee.dictionary;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class DictionaryTest {
    private Dictionary dict;
    private ArrayList<DictionaryElement> dictionaryElements;

    @BeforeEach
    void setUpBeforeEach() throws NoSuchFieldException, IllegalAccessException {
        dict = new Dictionary();
        Field elems = Dictionary.class.getDeclaredField("elems");
        elems.setAccessible(true);
        dictionaryElements = (ArrayList<DictionaryElement>) elems.get(dict);
    }

    @Test
    void put() {
        dict.put('a', "0000");
        assertTrue(dictionaryElements.contains(new DictionaryElement('a', "0000")));
    }

    @Test
    void getPrefixCode(){
        dictionaryElements.add(new DictionaryElement('z', "0101"));
        String prefixForZ = dict.getPrefixCode('z');
        assertEquals("0101", prefixForZ);
    }

    @Test
    void has() {
        dictionaryElements.add(new DictionaryElement('1', "111"));
        assertEquals(dictionaryElements.contains(new DictionaryElement('1', "111")), dict.has("111"));
    }

    @Test
    void getByPrefixCode() {
        dictionaryElements.add(new DictionaryElement('?', "0110"));
        assertEquals('?', dict.getByPrefixCode("0110"));
    }

    @Test
    void invalidDataTest(){
        assertThrows(IllegalArgumentException.class, () -> dict.put('x', "      "));
        assertThrows(IllegalArgumentException.class, () -> dict.put('x', ""));
        assertNull(dict.getPrefixCode('f'));
    }
}