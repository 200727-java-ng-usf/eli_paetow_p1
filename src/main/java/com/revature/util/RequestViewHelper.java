package com.revature.util;

import javax.servlet.http.HttpServletRequest;

public class RequestViewHelper {
    public String process(HttpServletRequest req) {

        switch (req.getRequestURI()) {


            case "/login.view": // for AWS deployment
            case "/project1/login.view": // for local deployment
                return "partials/login.html";

            case "/register.view":
            case "/project1/register.view":
                return "partials/register.html";

            case "/home.view":
            case "/project1/home.view":

                String principal = String.valueOf(req.getSession().getAttribute("principal"));
                if (principal == null || principal.equals("")) {
                    return "partials/login.html";
                }

                return "partials/home.html";
            case "/users.view":
            case "/project1/users.view":
                return "partials/users.html";

            case "/reimbs.view":
            case "/project1/reimbs.view":
                return "partials/reimbs.html";

            case "/submit.view":
            case "/project1/submit.view":
                return "partials/submit.html";

            case "/updateuser.view":
            case "/project1/updateuser.view":
                return "partials/updateuser.html";

            case "/updatereimb.view":
            case "/project1/updatereimb.view":
                return "partials/updatereimb.html";

            case "/profile.view":
            case "/project1/profile.view":
                return "partials/profile.html";

            default:
                return null;

        }

    }
}