/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package p06_14873443_21145466;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author wnd5547
 */
public class SeatsTest {
    Seats instance = new Seats();
    
    public SeatsTest() {
        
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

    /**
     * Test of loadSeats method, of class Seats.
     */
    @Test
    public void testLoadSeats() {
        boolean[][] expResult = new boolean[10][20];
        String option = "a";
        String date = "b";
        
        expResult[0][0] = true;
        boolean[][] result = instance.loadSeats(option, date);
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    

    /**
     * Test of saveSeats method, of class Seats.
     */
    @Test
    public void testSaveSeats() {
        boolean[][] expResult = new boolean[10][20];
        String option = "b";
        String date = "a";
        
        instance.saveSeats(expResult, option, date);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of chooseSeat method, of class Seats.
     */
    @Test
    public void testChooseSeat() {
        System.out.println("chooseSeat");
        boolean[][] toBook = null;
        int row = 0;
        int column = 0;
        Seats instance = new Seats();
        instance.chooseSeat(toBook, row, column);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of arrayString method, of class Seats.
     */
    @Test
    public void testArrayString() {
        System.out.println("arrayString");
        boolean[][] seatsArray = null;
        Seats instance = new Seats();
        String expResult = "";
        String result = instance.arrayString(seatsArray);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of printArray method, of class Seats.
     */
    @Test
    public void testPrintArray() {
        System.out.println("printArray");
        boolean[][] seatsArray = null;
        Seats instance = new Seats();
        instance.printArray(seatsArray);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
