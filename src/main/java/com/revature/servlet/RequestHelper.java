package com.revature.servlet;

import javax.servlet.http.HttpServletRequest;

public class RequestHelper {

    public static String process(HttpServletRequest req){

        System.out.println("THIS IS THE CURRENT URI ACTIVE " + req.getRequestURI());

        switch (req.getRequestURI()){
            case "/project1/api/login":
                System.out.println("in login case");
                //not modularized
                //return "/html/home.html";

                //modularized
                return LoginController.login(req);
            case "/project1/api/home":
                System.out.println("in home case");
                //not modularized
                //return "/html/login.html";

                //modularized
               // return HomeController.home(req);
            case "/project1/api/create_account":
                System.out.println("in create account case");


            default:
                System.out.println("in login case");
                return "/html/badlogin.html";
        }

    }
}
