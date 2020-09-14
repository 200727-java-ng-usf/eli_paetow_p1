package com.revature.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.revature.dtos.ErrorResponse;
import com.revature.dtos.Principal;
import com.revature.exceptions.InvalidRequestException;
import com.revature.exceptions.ResourceNotFoundException;
import com.revature.models.User;
import com.revature.services.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

@WebServlet("/users/*")
public class UserServlet extends HttpServlet {
    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

//        PrintWriter respWriter = resp.getWriter();
//        ObjectMapper mapper = new ObjectMapper();
//        resp.setContentType("application/json");
//
//
//        String principalJson = (String) req.getSession().getAttribute("principal");
//        Principal principal = mapper.readValue(principalJson, Principal.class);
//
//        //check here for id problem
//        System.out.println(req.getParameter("id"));
//        System.out.println("why null??");
//
//        if(principalJson == null){
//
//            ErrorResponse err = new ErrorResponse(401, "No principal found");
//            respWriter.write(mapper.writeValueAsString(err));
//            resp.setStatus(401);
//            return;
//        }
//        if (!principal.getRole().equalsIgnoreCase("Admin")){
//            ErrorResponse err = new ErrorResponse(403, "forbidden role");
//            respWriter.write(mapper.writeValueAsString(err));
//            resp.setStatus(403);
//            return;
//
//        }
//
//        System.out.println(req.getParameter("id"));
//
//
//        try {
//            String idParam = req.getParameter("ers_user_id");
//            if (idParam != null) {
//                int id = Integer.parseInt(idParam);
//                User user = userService.getUserById(id);
//                String usersJSON = mapper.writeValueAsString(user);
//                respWriter.write(usersJSON);
//
//
//            } else {
//                System.out.println("Retrieving all users...");
//                Set<User> users = userService.getAllUsers();
//                String usersJSON = mapper.writeValueAsString(users);
//                respWriter.write(usersJSON);
//                resp.setStatus(200);
//
//            }
//
//        } catch (ResourceNotFoundException rnfe) {
//            resp.setStatus(404);
//            ErrorResponse err = new ErrorResponse(404, rnfe.getMessage());
//            respWriter.write(mapper.writeValueAsString(err));
//            String errJson = mapper.writeValueAsString(err);
//            respWriter.write(errJson);
//        }
//
//        catch (NumberFormatException | InvalidRequestException nfe) {
//
//            resp.setStatus(400);
//            ErrorResponse err = new ErrorResponse(400, "bad user id provided");
//            String errJson = mapper.writeValueAsString(err);
//            respWriter.write(errJson);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            resp.setStatus(500); // 500 = INTERNAL SERVER ERROR
//            ErrorResponse err = new ErrorResponse(500, "its not you its us");
//            respWriter.write(mapper.writeValueAsString(err));
//
//        }

        ObjectMapper mapper = new ObjectMapper();
        PrintWriter respWriter = resp.getWriter();
        resp.setContentType("application/json");

        System.out.println(req.getParameter("ers_user_id"));

        try {

            String idParam = req.getParameter("ers_user_id");

            if (idParam != null) {

                int id = Integer.parseInt(idParam);
                User user = userService.getUserById(id);
                String userJSON = mapper.writeValueAsString(user);
                respWriter.write(userJSON);

            } else {

                Set<User> users = userService.getAllUsers();

                String usersJSON = mapper.writeValueAsString(users);
                respWriter.write(usersJSON);

                resp.setStatus(200); // 200 = OK
                System.out.println(resp.getStatus());

            }




        } catch (ResourceNotFoundException rnfe) {

            resp.setStatus(404);

            ErrorResponse err = new ErrorResponse(404, rnfe.getMessage());
            respWriter.write(mapper.writeValueAsString(err));

        } catch(NumberFormatException | InvalidRequestException e) {
            resp.setStatus(400); // 400 Bad Request
            ErrorResponse err = new ErrorResponse(400, "Malformed user id parameter value provided.");
            String errJSON = mapper.writeValueAsString(err);
            respWriter.write(errJSON);
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(500); // 500 = INTERNAL SERVER ERROR
            ErrorResponse err = new ErrorResponse(500, "It's not you, it's us. Our bad");
            respWriter.write(mapper.writeValueAsString(err));
        }

    }

    /**
     * Used to handle incoming requests to register new users for the application.
     *
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        ObjectMapper mapper = new ObjectMapper();
        PrintWriter respWriter = resp.getWriter();
        try {
            User newUser = mapper.readValue(req.getInputStream(), User.class);
            userService.register(newUser);
            System.out.println(newUser);
            String newUserJSON = mapper.writeValueAsString(newUser);
            respWriter.write(newUserJSON);
            resp.setStatus(201); // 201 = CREATED
        } catch (MismatchedInputException mie) {
            resp.setStatus(400);

            ErrorResponse err = new ErrorResponse(400, "Bad Request: malformed user in request body");

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