/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import junit.framework.Assert;
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
public class TrackingPeriodTest {
    
    TrackingPeriod trackingPeriod;
    
    public TrackingPeriodTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        trackingPeriod = new TrackingPeriod(1L);
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void serialNumberTest() {
        assertEquals((long)trackingPeriod.getSerialNumber(), 1L);
    }
    
    @Test
    public void setPositionsTest() {
        trackingPeriod.getPositions().add(new Position(50.0, 50.0));
        trackingPeriod.getPositions().add(new Position(50.0, 50.0));
        assertEquals(trackingPeriod.getPositions().size(), 2);
    }
}
