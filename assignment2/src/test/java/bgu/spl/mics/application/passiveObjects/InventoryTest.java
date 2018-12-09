package bgu.spl.mics.application.passiveObjects;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.HashMap;
import static org.junit.Assert.*;

public class InventoryTest {
    private BookInventoryInfo book;
    private BookInventoryInfo[] array;
    private Inventory inv;
    private HashMap hashMap;

    @Before
    public void setUp() throws Exception {
        book = new BookInventoryInfo("sherlock", 6, 50);
        inv = new Inventory();
        hashMap=new HashMap();
    }

    @After
    public void tearDown() throws Exception {
    }

    //no need because there is only one instance
    @Test
    public void getInstance() throws Exception {
    }

    @Test
    public void load() throws Exception {
        array= new BookInventoryInfo[]{book};
        this.inv.load(array);
        assertTrue(this.inv.checkAvailabiltyAndGetPrice(book.getBookTitle()) == 50);
    }

    //there are two options in take : NOT_IN_STOCK or SUCCESSFULLY_TAKEN
    @Test
    public void take1() throws Exception {
        array= new BookInventoryInfo[]{book};
        this.inv.load(array);
        assertEquals(OrderResult.NOT_IN_STOCK, this.inv.take(book.getBookTitle()));
    }
    @Test
    public void take2() throws Exception {
        array= new BookInventoryInfo[]{book};
        this.inv.load(array);
        assertEquals(OrderResult.SUCCESSFULLY_TAKEN, this.inv.take(book.getBookTitle()));
    }

    //there are two options in checkAvilabiltyAndGetPrice: available or not
    @Test
    public void checkAvailabiltyAndGetPrice1() throws Exception {
        array= new BookInventoryInfo[]{book};
        this.inv.load(array);
        assertTrue(this.inv.checkAvailabiltyAndGetPrice(book.getBookTitle())==50);
    }

    @Test
    public void checkAvailabiltyAndGetPrice2() throws Exception {
        assertTrue(this.inv.checkAvailabiltyAndGetPrice(book.getBookTitle())==-1);
    }

    @Test
    public void printInventoryToFile() throws Exception {
        hashMap.put(book.getBookTitle(),book.getAmountInInventory());
        assertEquals(6 ,hashMap.get(book.getBookTitle()));
    }

}