package ua.kpi;

import org.junit.Test;
import ua.kpi.generators.Geffe;
import ua.kpi.generators.Generator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigInteger;

public class GeneratorsTest {

    @Test
    public void testGeffe(){
        Generator geffe = new Geffe();
        //any random number you want
        int length = 113;
        BigInteger seq = geffe.generate(length);
        System.out.println(seq.toString(2));
    }
    //TODO tests for all generators

}
