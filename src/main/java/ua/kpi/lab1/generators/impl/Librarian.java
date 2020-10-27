package ua.kpi.lab1.generators.impl;

import ua.kpi.lab1.generators.Generator;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Librarian implements Generator {

    private  String filePath = "files/book_clean.txt";

    public Librarian(String filePath) {
        this.filePath = filePath;
    }
    public Librarian(){

    }

    @Override
    public int[] generate(int byteLength) {

        int[] bytes = new int[byteLength];


        try (InputStream inputStream = new FileInputStream(filePath)) {
            if (inputStream.available() < byteLength) {
                throw new RuntimeException("file is too short: only " + inputStream.available() + "bytes");
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

