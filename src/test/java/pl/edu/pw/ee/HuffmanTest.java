package pl.edu.pw.ee;
import org.junit.jupiter.api.*;

import java.io.*;
import java.util.Objects;

/**
 * IMPORTANT !!!
 *
 * Running these tests (exactly decodeTest) from maven lifecycle may produce error.
 * It's connected with characters encoding which maven uses
 * If your text contains not only latin letters please set
 * appropriate property into maven or run these tests using IDE.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class HuffmanTest {
    Huffman huffman;

    @BeforeEach
    void setUpBeforeEach() {
        huffman = new Huffman();
    }

    @Test
    void encodeTest() {
        huffman.huffman("src/test/resources/files/toEncode", true);
        Assertions.assertTrue(true);
    }

    @Test
    void deleteUnnecessaryFromEncodeFolder() {
        File folder = new File("src/test/resources/files/toEncode");
        for (File file : Objects.requireNonNull(folder.listFiles())) {
            if (file.getName().endsWith("_compressed.txt") || file.getName().endsWith("_dictionary.txt")) {
                boolean isDeleted = file.delete();
                if (!isDeleted) {
                    Assertions.fail("Failed to delete file " + file.getName());
                }
            }
        }
    }

    @Test
    void decodeTest() throws IOException {
        huffman.huffman("src/test/resources/files/toDecode", false);
        File directory = new File("src/test/resources/files/toDecode");
        File[] files = directory.listFiles(pathname -> !pathname.getName().endsWith("_compressed.txt") && !pathname.getName().endsWith("_dictionary.txt"));
        Assertions.assertNotNull(files);

        for (File file : files) {
            File originalFile = new File("src/test/resources/files/toValidate/" + file.getName().replaceAll(".txt", "_original.txt"));
            Assertions.assertTrue(originalFile.exists());
            BufferedReader frForDecoded = new BufferedReader(new FileReader(file));
            BufferedReader frForOriginal = new BufferedReader(new FileReader(originalFile));
            String origLine;
            String decodedLine;
            while ((origLine = frForOriginal.readLine())!=null){
                decodedLine = frForDecoded.readLine();
                Assertions.assertNotNull(decodedLine);
                Assertions.assertEquals(origLine, decodedLine);
            }
        }
    }

    @Test
    void deleteUnnecessaryFromDecodeFolder(){
        File folder = new File("src/test/resources/files/toDecode");
        for (File file : Objects.requireNonNull(folder.listFiles())) {
            if (!file.getName().endsWith("_compressed.txt") && !file.getName().endsWith("_dictionary.txt")) {
                boolean isDeleted = file.delete();
                if (!isDeleted) {
                    Assertions.fail("Failed to delete file " + file.getName());
                }
            }
        }
    }

    @Test
    void specificFilesTest(){
        huffman.huffman("src/test/resources/files/specific", true);
        Assertions.assertTrue(true);
    }

    @Test
    void deleteUnnecessaryFromSpecificFolder(){
        File folder = new File("src/test/resources/files/specific");
        for (File file : Objects.requireNonNull(folder.listFiles())) {
            if (file.getName().endsWith("_compressed.txt") || file.getName().endsWith("_dictionary.txt")) {
                boolean isDeleted = file.delete();
                while (!isDeleted) {
                    isDeleted = file.delete();
                }
            }
        }
    }



}
