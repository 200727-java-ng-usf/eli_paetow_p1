package com.revature.servlet;

import com.revature.util.ValidationHelper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("*.validate")
public class ValidationServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        boolean isValid = new ValidationHelper().process(req);

        if (isValid) {
            resp.setStatus(204);
        } else {
            resp.setStatus(409);
        }

    }
}