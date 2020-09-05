package com.revature.repos;

public class UserRepository {

    //empty constructor
    public UserRepository(){

    }
    //re use query
    private String baseQuery = "SELECT * FROM project1.ers_users eu " +
            "JOIN project1.ers_user_roles er " +
            "ON eu.user_role_id = er.role_id ";

}
