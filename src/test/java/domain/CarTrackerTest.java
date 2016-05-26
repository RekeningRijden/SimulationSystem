/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import simulation.SimulationInfo;

/**
 *
 * @author maikel
 */
public class CarTrackerTest {

    CarTracker carTracker;

    public CarTrackerTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        carTracker = new CarTracker();
    }

    @After
    public void tearDown() {
    }

    @Test
    public void setSimulationInfo() {
        SimulationInfo info = new SimulationInfo(1, 1, 1, 50.0, new Position(10.0, 10.0));
        carTracker.setInitialSimulationInfo(info);
        Assert.assertEquals(carTracker.getSimulationInfo(), info);
    }
    
    @Test
    public void setAuthorisationCode() {
        String code = "new code";
        carTracker.setAuthorisationCode(code);
        Assert.assertEquals(carTracker.getAuthorisationCode(), code);
    }
}
