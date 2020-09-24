package ua.kpi.generators;

import ua.kpi.generators.util.Util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.stream.Stream;

public class Librarian implements Generator {

    private final String filePath = "input/book.txt";

    @Override
    public String generate(int bitLength) {

        int byteLength = bitLength / 8 + (bitLength % 8 != 0 ? 1 : 0);

        byte[] arr = new byte[bitLength];
        StringBuilder sb = new StringBuilder();

        try (InputStream inputStream = new FileInputStream(filePath)) {
            if (inputStream.read(arr, 0, byteLength) < 0) {
                throw new RuntimeException("file is too short");
            }
            for (byte b : arr) {
                sb.append(Util.makeBinaryString(b, 8));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sb.substring(0, bitLength);
    }

}
