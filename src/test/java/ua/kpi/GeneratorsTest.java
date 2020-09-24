package ua.kpi;

import org.junit.Test;
import ua.kpi.generators.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class GeneratorsTest {

    private void testGenerator(Generator gen) {
        int length = 1_000_000;
        String seq = gen.generate(length);
        System.out.println(seq.substring(0, 150));
        assertEquals(length, seq.length());
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
        //TODO
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
