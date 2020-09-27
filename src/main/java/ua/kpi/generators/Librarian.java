package ua.kpi.generators;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Librarian implements Generator {

    private final String filePath = "files/book.txt";

    @Override
    public int[] generate(int byteLength) {

        int[] bytes = new int[byteLength];

        try (InputStream inputStream = new FileInputStream(filePath)) {
            if (inputStream.available() < byteLength) {
                throw new RuntimeException("file is too short");
            }
            for (int i = 0; i < byteLength; i++) {
                bytes[i] = inputStream.read();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bytes;
    }

}

