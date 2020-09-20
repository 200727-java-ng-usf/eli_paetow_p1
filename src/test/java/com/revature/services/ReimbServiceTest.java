package com.revature.services;

import com.revature.exceptions.InvalidRequestException;
import com.revature.models.ErsReimbursement;


import com.revature.models.Status;
import com.revature.models.Type;
import com.revature.repos.ReimbRepository;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

public class ReimbServiceTest {

    private ReimbService sut;
    private ReimbRepository mockReimbRepo = Mockito.mock(ReimbRepository.class);
    Set<ErsReimbursement> mockReimbs = new HashSet<>();


    //setup
    @Before
    public void setup() {
        sut = new ReimbService();
        Timestamp timestamp = new Timestamp(2030, 6, 12, 2, 2, 20, 5);
        mockReimbs.add(new ErsReimbursement(9, 100.00, timestamp, timestamp, "Description", 1, 2, Status.PENDING, Type.LODGING));
        mockReimbs.add(new ErsReimbursement(1, 100.00, timestamp, timestamp, "Description", 1, 2, Status.PENDING, Type.TRAVEL));
        mockReimbs.add(new ErsReimbursement(2, 100.00, timestamp, timestamp, "Description", 1, 2, Status.PENDING, Type.FOOD));
        mockReimbs.add(new ErsReimbursement(3, 100.00, timestamp, timestamp, "Description", 1, 2, Status.PENDING, Type.OTHER));
    }



    //tests
    @Test(expected = InvalidRequestException.class)
    public void getReimbByIdBad() { sut.getReimbById(-1); }


    @Test (expected = InvalidRequestException.class)
    public void registerWithNullBad() {
        sut.register(null);
    }



    @Test
    public void isReimbValidBad() {
        ErsReimbursement mockReimb = null;

        Assert.assertEquals(false, sut.isReimbValid(mockReimb));
    }

    //teardown
    @After
    public void tearDown() {
        sut = null;
        mockReimbs.removeAll(mockReimbs);
    }


}
