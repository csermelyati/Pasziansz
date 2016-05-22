/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myproyect.pasziansz.Model;

import java.util.List;
import javafx.scene.image.Image;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author csermely
 */
public class PakliTest {
    
    public PakliTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testGetKartyak() {
        System.out.println("getKartyak");
        Pakli instance = new Pakli();
        int expResult = 52;
        List<Kartya> result = instance.getKartyak();
        assertEquals(expResult, result.size());
    }

    @Test
    public void testAthelyezHitelesites() {
        System.out.println("athelyezHitelesites");
        int index = 1; //2_of_clubs
        int hely = 7; //1. oszlop
        Pakli instance = new Pakli();
        boolean expResult = true;
        boolean result = instance.athelyezHitelesites(index, hely);
        assertEquals(expResult, result);
    }
    @Test
    public void testAthelyezHitelesites2() {
        System.out.println("athelyezHitelesites");
        int index = 1; //2_of_clubs
        int hely = 3; //1. halom
        Pakli instance = new Pakli();
        boolean expResult = false;
        boolean result = instance.athelyezHitelesites(index, hely);
        assertEquals(expResult, result);
    }

    @Test
    public void testAthelyezKartya() {
        System.out.println("athelyezKartya");
        Integer index = 1;
        int hely = 7;
        Pakli instance = new Pakli();
        int expResult1 = 7;
        boolean expResult2 = true;
        boolean result = instance.athelyezKartya(index, hely);
        assertEquals((long)expResult1, (long)instance.getKartyak().get(index).getPlaceID());
        assertEquals(expResult2, result);
        
    }

    @Test
    public void testCsereKratya() {
        System.out.println("csereKratya");
        Pakli instance = new Pakli();
        boolean expResult = true;
        boolean result = instance.csereKratya();
        assertEquals(expResult, result);
        
        instance.getKartyak().forEach(item->{
            item.setPlaceID(7);
        });
        
        expResult = false;
        result = instance.csereKratya();
        assertEquals(expResult, result);
        
    }

    @Test
    public void testVisszaRendezesOszlop() {
        System.out.println("visszaRendezesOszlop");
        int i = 7;
        Pakli instance = new Pakli();
        instance.getKartyak().get(1).setPlaceID(7);  //2_of_clubs
        instance.getKartyak().get(1).setStackNumber(0);
        instance.getKartyak().get(36).setPlaceID(7); //ace_of_hearts
        instance.getKartyak().get(36).setStackNumber(4);
        
        instance.visszaRendezesOszlop(i-7);
        
        assertEquals(1, (long)instance.getKartyak().get(36).getStackNumber());
        
    }

    @Test
    public void testSetKovetkezoLathato() {
        System.out.println("setKovetkezoLathato");
        int index = 1;
        Pakli instance = new Pakli();
        boolean expResult = true;
        boolean result = instance.setKovetkezoLathato(index);
        assertEquals(expResult, result);
    }

    @Test
    public void testJatekVege() {
        System.out.println("jatekVege");
        Pakli instance = new Pakli();
        
        boolean expResult = false;
        boolean result = instance.jatekVege();
        assertEquals(expResult, result);
        
        instance.getKartyak().forEach(item->{
            item.setPlaceID(3);
        });
        
        expResult = true;
        result = instance.jatekVege();
        assertEquals(expResult, result);
    }
    
}
