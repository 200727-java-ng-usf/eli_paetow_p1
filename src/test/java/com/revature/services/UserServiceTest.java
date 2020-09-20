package com.revature.services;

import com.revature.exceptions.InvalidRequestException;
import com.revature.exceptions.ResourceNotFoundException;
import com.revature.models.ErsUser;
import com.revature.models.Role;
import com.revature.repos.UserRepository;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.HashSet;
import java.util.Set;

public class UserServiceTest {

    private UserService sut;
    private UserRepository mockUserRepo = Mockito.mock(UserRepository.class);
    Set<ErsUser> mockUsers = new HashSet<>();


    //setup
    @Before
    public void setup(){
        sut = new UserService();
        mockUsers.add(new ErsUser(1, "Adam", "Inn", "admin", "secret", "admin@app.com", Role.ADMIN));
        mockUsers.add(new ErsUser(2, "Manny", "Gerr", "manager", "manage", "manager@app.com", Role.FINANCE_MANAGER));
        mockUsers.add(new ErsUser(3, "Alice", "Anderson", "aanderson", "password", "admin@app.com", Role.EMPLOYEE));
        mockUsers.add(new ErsUser(4, "Bob", "Bailey", "bbailey", "dev", "dev@app.com", Role.EMPLOYEE));

    }

    //tests
    @Test(expected = InvalidRequestException.class)
    public void getInvalidUserBad() {
        sut.getUserById(-1); // there is no user with this ID
    }



    @Test(expected = InvalidRequestException.class)
    public void authenticateWithEmptyBad() {

        sut.authenticate("", "");
    }

    @Test (expected = ResourceNotFoundException.class)
    public void getUserByIdTooHighBad() {
        sut.getUserById(999);
    }

    @Test (expected = RuntimeException.class)
    public void registerUserThatExists() {
        ErsUser validUser = new ErsUser("eli5", "eli", "eli5", "password", "@Email");
        sut.register(validUser);

        sut.register(validUser);
    }

    @Test
    public void isUsernameAvailableReturnsTrue() {
        String nullString = null;
        boolean actual = sut.isUsernameAvailable(nullString);
        Assert.assertEquals(true, actual);
    }

    @Test
    public void isEmailAvailableReturnsTrue() {
        String nullString = null;
        boolean actual = sut.isEmailAvailable(nullString);
        Assert.assertEquals(true, actual);
    }




    //teardown
    @After
    public void tearDown(){
        sut = null;
        mockUsers.removeAll(mockUsers);
    }
}
