package pl.edu.pw.ee;

import pl.edu.pw.ee.dictionary.Dictionary;
import pl.edu.pw.ee.dictionary.DictionaryElement;
import pl.edu.pw.ee.tree.HaffmanTree;
import pl.edu.pw.ee.tree.Node;
import pl.edu.pw.ee.woods.Woods;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Huffman {
    private int counter;

    public int huffman(String pathToRootDir, boolean compress) {
        File directory = new File(pathToRootDir);

        if (!directory.isDirectory()) {
            throw new IllegalStateException("Path to file provided. Expected path to directory");
        }

        counter = 0;
        if (compress) {
            File[] filesToCompress = directory.listFiles((dir, name) -> !name.matches(".*_compressed.txt") && !name.matches(".*_dictionary.txt"));
            validateDirectoryFiles(filesToCompress);
            try {
                for (File toCompress : filesToCompress) {
                    compress(toCompress);
                }
            } catch (IOException io) {
                System.out.println(io.getMessage());
            }
        } else {
            File[] filesToDecompress = directory.listFiles((dir, name) -> name.matches(".*_compressed.txt"));
            if (filesToDecompress == null || filesToDecompress.length == 0) {
                throw new IllegalStateException("IO exception occurred while searching for appropriate files or no matches found");
            }
            try {
                for (File toDecompress : filesToDecompress) {
                    decompress(toDecompress);
                }
            } catch (IOException io) {
                System.out.println(io.getMessage());
            }
        }
        return counter;
    }

    private void compress(File file) throws IOException {
        if (file.length() == 0) {
            return;
        }
        FileReader fr = new FileReader(file.getAbsolutePath());
        int[] charCounts = countChars(fr);
        List<Node> nodeList = new ArrayList<>();

        for (int i = 0; i < charCounts.length; i++) {
            if (charCounts[i] > 0) {
                nodeList.add(new Node((char) i, charCounts[i]));
            }
        }

        Woods woods = new Woods(nodeList);
        while (!woods.hasOneTree()) {
            HaffmanTree leftSubTree = woods.pullMinTree();
            HaffmanTree rightSubTree = woods.pullMinTree();
            HaffmanTree mergedTree = HaffmanTree.merge(leftSubTree, rightSubTree);
            woods.putTree(mergedTree);
        }

        HaffmanTree haffmanTree = woods.getTrees().get(0);

        Dictionary dictionary = haffmanTree.getDictionary();

        fr = new FileReader(file);
        String compressedFileName = getNameForCompressedFile(file);
        FileWriter fw = new FileWriter(compressedFileName);

        int ch;
        while ((ch = fr.read()) != -1) {
            String prefixCode = dictionary.getPrefixCode((char) ch);
            fw.write(prefixCode);
            counter += prefixCode.length();
        }
        fw.flush();

        String dictionaryFileName = getNameForDictionaryFile(file);
        fw = new FileWriter(dictionaryFileName);
        for (DictionaryElement e : dictionary) {
            fw.write(String.format("%d:%s\n", (int) e.character, e.prefixCode));
        }
        fw.flush();
    }

    private int[] countChars(FileReader fr) {
        BufferedReader br = new BufferedReader(fr);
        int[] chars = new int[65535];
        try {
            int ch;
            while ((ch = br.read()) != -1) {
                chars[ch]++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return chars;
    }

    private void decompress(File file) throws IOException {
        File directory = file.getParentFile();
        File dictionaryFile = null;
        File[] files = directory.listFiles();
        validateDirectoryFiles(files);
        for (File f : files) {
            if (f.getName().equals(file.getName().replaceAll("_compressed.txt", "_dictionary.txt"))) {
                dictionaryFile = f;
                break;
            }
        }
        if (dictionaryFile == null) {
            throw new FileNotFoundException("Unable to find a dictionary file for " + file.getName());
        }

        FileWriter fw = new FileWriter(getNameForNormalFile(file));
        Dictionary dictionary = parseToDictionary(dictionaryFile);
        FileReader fr = new FileReader(file);
        StringBuilder buffer = new StringBuilder();
        int ch;
        while ((ch = fr.read()) != -1) {
            buffer.append(((char) ch));
            if (dictionary.has(buffer.toString())) {
                char byPrefixCode = dictionary.getByPrefixCode(buffer.toString());
                fw.write(byPrefixCode);
                buffer.setLength(0);
                counter++;
            }
        }
        fw.flush();
    }

    private Dictionary parseToDictionary(File file) {
        try (BufferedReader bf = new BufferedReader(new FileReader(file))) {
            Dictionary dictionary = new Dictionary();
            String str;
            while ((str = bf.readLine()) != null) {
                String[] split = str.split(":");
                char c = (char) Integer.parseInt(split[0]);
                String prefixCode = split[1];
                dictionary.put(c, prefixCode);
            }
            return dictionary;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return new Dictionary();
        }
    }


    private String getNameForCompressedFile(File normalFile) {
        return normalFile.getAbsolutePath().replaceAll(normalFile.getName(), normalFile.getName().replaceAll(".txt", "") + "_compressed.txt");
    }

    private String getNameForDictionaryFile(File normalFile) {
        return normalFile.getAbsolutePath().replaceAll(normalFile.getName(), normalFile.getName().replaceAll(".txt", "") + "_dictionary.txt");
    }

    private String getNameForNormalFile(File compressedFile) {
        return compressedFile.getAbsolutePath().replaceAll(compressedFile.getName(), compressedFile.getName().replaceAll("_compressed.txt", ".txt"));
    }

    private void validateDirectoryFiles(File[] files) {
        if (files == null || files.length == 0) {
            throw new IllegalStateException("IO exception occurred while searching for appropriate files or no matches found");
        }
    }

}
