/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import java.util.Date;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author maikel
 */
public class PositionTest {
    
    Position position;
    
    public PositionTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        position = new Position(10.0, 10.0);
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void getLatitude() {
        assertEquals(10.0, position.getLatitude(), 0);
    }
    
    @Test
    public void setDate() {
        Date d = new Date();
        position.setDate(d);
        assertEquals(d, position.getDate());
    }
}
