package bgu.spl.mics;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class FutureTest {
    private Future<String> future;

    @Before
    public void setUp() throws Exception {
        future = new Future<String>();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void get() throws Exception {
        this.future.resolve("fine");
        assertEquals("fine", future.get());
    }

    @Test
    public void resolve() throws Exception {
        this.future.resolve("fine");
        assertEquals("fine", future.get());
    }

    //there are two options in isDone: resolved or unresolved
    @Test
    public void isDone1() throws Exception {
        this.future.resolve("fine");
        assertTrue("true", future.isDone());
    }
    public void isDone2() throws Exception {
        assertFalse("false", future.isDone());
    }

    @Test
    public void get1() throws Exception { //Dont need to check THE METARGEL SAID SO
    }

}