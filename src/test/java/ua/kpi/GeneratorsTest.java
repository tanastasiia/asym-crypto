package ua.kpi;

import org.junit.Test;
import ua.kpi.generators.Geffe;
import ua.kpi.generators.Generator;
import ua.kpi.generators.L20;
import ua.kpi.generators.LehmerHigh;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigInteger;

public class GeneratorsTest {

    @Test
    public void testGeffe(){
        Generator geffe = new Geffe();
        //any random number you want
        int length = 113;
        String seq = geffe.generate(length);
        assertEquals(length, seq.length());
        System.out.println(seq);
    }

    @Test
    public void testL20(){
        L20 l20 = new L20();
        //any random number you want
        int length = 113;
        String seq = l20.generate(length);
        assertEquals(length, seq.length());
        System.out.println(seq);
    }

    @Test
    public void testLehmerHigh(){
        LehmerHigh lehmerHigh = new LehmerHigh();
        //any random number you want
        int length = 113;
        String seq = lehmerHigh.generate(length);
        assertEquals(length, seq.length());
        System.out.println(seq);
    }
    //TODO tests for all generators

}
