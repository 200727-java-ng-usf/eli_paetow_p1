package com.revature.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.revature.dtos.Credentials;
import com.revature.dtos.ErrorResponse;
import com.revature.exceptions.AuthenticationException;
import com.revature.exceptions.InvalidRequestException;
import com.revature.models.User;
import com.revature.services.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/auth")
public class AuthServlet extends HttpServlet {
    private final UserService userService = new UserService();


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getSession().invalidate();
        resp.setStatus(204);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        PrintWriter respWriter = resp.getWriter();
        resp.setContentType("application/json");


        try {
            Credentials creds = mapper.readValue(req.getInputStream(), Credentials.class);

            User authUser = userService.authenticate(creds.getUsername(), creds.getPassword());

            HttpSession session = req.getSession();
            session.setAttribute("user-role", authUser.getUserRole().toString());

        } catch (MismatchedInputException | InvalidRequestException mie) {
            resp.setStatus(400);

            ErrorResponse err = new ErrorResponse(400, "Bad Request: malformed object in request body");

            String errJson = mapper.writeValueAsString(err);
            respWriter.write(errJson);
        } catch (AuthenticationException ae) {
            resp.setStatus(401);

            ErrorResponse err = new ErrorResponse(400, ae.getMessage());

            String errJson = mapper.writeValueAsString(err);
            respWriter.write(errJson);
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(500); // 500 = INTERNAL SERVER ERROR
            ErrorResponse err = new ErrorResponse(500, "its not you its us");
            respWriter.write(mapper.writeValueAsString(err));

        }
    }
}
