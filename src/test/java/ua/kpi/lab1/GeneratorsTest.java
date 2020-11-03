package ua.kpi.lab1;

import ua.kpi.lab1.generators.*;
import ua.kpi.lab1.generators.impl.*;
import ua.kpi.lab1.generators.BBS;
import ua.kpi.lab1.generators.BM;

import java.util.Arrays;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class GeneratorsTest {


    private void testGenerator(Generator gen) {
        int length = 200;
        int[] bytes = gen.generate(length);
        System.out.println(Arrays.toString(bytes));
        assertEquals(length, bytes.length);
    }

    //1
    @Test
    public void testJavaRand() {
        JavaRand javaRand = new JavaRand();
        testGenerator(javaRand);
    }

    //2
    @Test
    public void testLehmerLow() {
        LehmerLow lehmerLow = new LehmerLow();
        testGenerator(lehmerLow);
    }

    //3
    @Test
    public void testLehmerHigh() {
        LehmerHigh lehmerHigh = new LehmerHigh();
        testGenerator(lehmerHigh);
    }

    //4
    @Test
    public void testL20() {
        L20 l20 = new L20();
        testGenerator(l20);
    }

    //5
    @Test
    public void testL89() {
        L89 l89 = new L89();
        testGenerator(l89);
    }

    //6
    @Test
    public void testGeffe() {
        Generator geffe = new Geffe();
        testGenerator(geffe);
    }

    //7
    @Test
    public void testLibrarian() {
        Librarian librarian = new Librarian();
        testGenerator(librarian);
    }

    //8
    @Test
    public void testWolfram() {
        Wolfram wolfram = new Wolfram();
        testGenerator(wolfram);
    }

    //9
    @Test
    public void testBMBit() {
        BM bm = new BMBit();
        testGenerator(bm);
    }

    //10
    @Test
    public void testBMByte() {
        BM bm = new BMByte();
        testGenerator(bm);
    }

    //11
    @Test
    public void testBBSBit() {
        BBS bbs = new BBSBit();
        testGenerator(bbs);
    }

    //12
    @Test
    public void testBBSByte() {
        BBS bbs = new BBSByte();
        testGenerator(bbs);
    }

}
