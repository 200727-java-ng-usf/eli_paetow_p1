package com.revature.util;

import javax.servlet.http.HttpServletRequest;

public class RequestViewHelper {
    public String process(HttpServletRequest req) {

        switch (req.getRequestURI()) {

            case "/login.view":
            case "/project1/login.view":
                return "partials/login.html";

            case "/reimbs.view":
            case "/project1/reimbursements.view":
                return "partials/reimbursements.html";

            case "/register.view":
            case "/project1/register.view":
                return "partials/register.html";

            case "/users.view":
            case "/project1/users.view":
                return "partials/users.html";

            case "/update.view":
            case "/project1/update.view":
                return "partials/update.html";


            case "/home.view":
            case "/project1/home.view":
                String principal = (String) req.getSession().getAttribute("principal");
                if (principal == null || principal.equals("")) {
                    return "partials/login.html";
                }

                return "partials/home.html";

            default:
                return null;

        }

    }
}