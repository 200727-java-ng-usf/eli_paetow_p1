package com.revature.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.revature.dtos.Credentials;
import com.revature.dtos.ErrorResponse;
import com.revature.dtos.Principal;
import com.revature.exceptions.InvalidRequestException;
import com.revature.exceptions.ResourceNotFoundException;
import com.revature.models.ErsReimbursement;
import com.revature.models.ErsUser;
import com.revature.models.Role;
import com.revature.services.ReimbService;
import com.revature.services.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

@WebServlet("/reimbs/*")
public class ReimbServlet extends HttpServlet {

    private final ReimbService reimbService = new ReimbService();
    private final UserService userService = new UserService();


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        System.out.println("in ReimbServlet doGet");

        // Set up objects to write back to the browser
        ObjectMapper mapper = new ObjectMapper();
        PrintWriter respWriter = resp.getWriter();
        resp.setContentType("application/json");


        System.out.println("below should be the reimb_id (may be null)");
        System.out.println(req.getParameter("reimb_id"));

        System.out.println("below should be the authUser ID");
        System.out.println(req.getSession().getAttribute("authorIdToFindReimbs"));

        System.out.println("below should be the user ID if they are a finance manager");
        System.out.println(req.getSession().getAttribute("userWhoIsDefinitelyAFinanceManager"));

        System.out.println("below should be the reimbursement if one was assigned");
        System.out.println(req.getSession().getAttribute("reimbursement"));

        try {

            // See which ID is present (one for employee or one for finance manager)
            if (req.getSession().getAttribute("authorIdToFindReimbs") != null) { // if the user is an employee...

                Object authorIdParam = req.getSession().getAttribute("authorIdToFindReimbs"); // assign this attribute to an object in the service layer
                System.out.println("User is an employee!" + authorIdParam.toString());

                // Now, we need to see if one reimbursement has been selected.
                if (req.getSession().getAttribute("reimbursement") != null) { // if a reimbursement has been selected...

                    // Get the reimbursement
                    Object reimbursement = req.getSession().getAttribute("reimbursement");
                    System.out.println("A specific reimbursement has been selected!");

                    ErsReimbursement ersReimbursement = (ErsReimbursement) reimbursement;

                    // write the JSON back to the browser and set the status
                    String ersReimbursementJSON = mapper.writeValueAsString(ersReimbursement);
                    respWriter.write(ersReimbursementJSON);
                    req.getSession().removeAttribute("reimbursement");
                    resp.setStatus(200);

                } else { // else, return all of the reimbursements by the employee

                    // convert information
                    String authorIdParamString = String.valueOf(authorIdParam);
                    Integer authorIdParamInteger = Integer.parseInt(authorIdParamString);

                    // get the reimbursements, write it back to the browser, and set the status
                    Set<ErsReimbursement> reimbsByAuthor = reimbService.getAllByAuthorId(authorIdParamInteger);
                    String principalJSON = mapper.writeValueAsString(reimbsByAuthor);
                    respWriter.write(principalJSON);
                    resp.setStatus(200); // 200 OK

                }

            } else {

                Object userWhoIsDefinitelyAFinanceManager = req.getSession().getAttribute("userWhoIsDefinitelyAFinanceManager");
                System.out.println("User is a finance manager!" + userWhoIsDefinitelyAFinanceManager.toString());

                // Now, we need to see if one reimbursement has been selected
                if (req.getSession().getAttribute("reimbursement") != null) { // if a reimbursement has been selected...

                    Object reimbursement = req.getSession().getAttribute("reimbursement");
                    System.out.println("A specific reimbursement has been selected!");

                    ErsReimbursement ersReimbursement = (ErsReimbursement) reimbursement;

                    String ersReimbursementJSON = mapper.writeValueAsString(ersReimbursement);

                    respWriter.write(ersReimbursementJSON);

                    reimbursement = null; // reimbursement to null. next time a user calls the doGet

                    resp.setStatus(200);

                } else { // else, return all reimbursements for the finance manager

                    Set<ErsReimbursement> reimbs = reimbService.getAllReimbs();

                    String reimbsJSON = mapper.writeValueAsString(reimbs);
                    respWriter.write(reimbsJSON);

                    resp.setStatus(200); // 200 = OK
                    System.out.println(resp.getStatus());
                    System.out.println(req.getRequestURI());

                }

            }


        } catch (ResourceNotFoundException rnfe) {

            resp.setStatus(404);

            ErrorResponse err = new ErrorResponse(404, rnfe.getMessage());
            respWriter.write(mapper.writeValueAsString(err));

        } catch (NumberFormatException | InvalidRequestException e) {
            resp.setStatus(400); // 400 Bad Request
            ErrorResponse err = new ErrorResponse(400, "Malformed reimb id parameter value provided.");
            String errJSON = mapper.writeValueAsString(err);
            respWriter.write(errJSON);
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(500); // 500 = INTERNAL SERVER ERROR
            ErrorResponse err = new ErrorResponse(500, "It's not you, it's us. Our bad");
            respWriter.write(mapper.writeValueAsString(err));
        }


    }




    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        System.out.println("in ReimbServlet doPost");

        resp.setContentType("application/json");
        ObjectMapper mapper = new ObjectMapper();
        PrintWriter respWriter = resp.getWriter();


        try {

            // Get data, register the user, write back the registered user, and set the status
            System.out.println("Starting submit!");
            ErsReimbursement newReimbursement = mapper.readValue(req.getInputStream(), ErsReimbursement.class);
            System.out.println("read the input stream: " + newReimbursement);
            reimbService.register(newReimbursement);
            System.out.println(newReimbursement);
            String newReimbursementJSON = mapper.writeValueAsString(newReimbursement);
            respWriter.write(newReimbursementJSON);
            resp.setStatus(201); // 201 = CREATED


        } catch (MismatchedInputException mie) {

            resp.setStatus(400); // 400 = BAD REQUEST

            ErrorResponse err = new ErrorResponse(400, "Bad Request: Malformed reimb object found in request body");
            String errJSON = mapper.writeValueAsString(err);
            respWriter.write(errJSON);

        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(500); // 500 = INTERNAL SERVER ERROR
            ErrorResponse err = new ErrorResponse(500, "It's not you, it's us. Our bad");
            respWriter.write(mapper.writeValueAsString(err));
        }
    }


    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        System.out.println("In doPut of ReimbServlet!");

        resp.setContentType("application/json");
        ObjectMapper mapper = new ObjectMapper();
        PrintWriter respWriter = resp.getWriter();

        try {
            if (req.getSession().getAttribute("userWhoIsDefinitelyAFinanceManager") != null) { // user is a finance manager


                // Find the reimbursement from the session
                Object reimbId = req.getSession().getAttribute("reimbIdToUpdate");
                System.out.println("This is the reimb ID: " + reimbId);

                // Convert
                String string = String.valueOf(reimbId);
                System.out.println(string);
                String cleanString = string.replaceAll("\\D+", "");
                System.out.println(cleanString);
                Integer integer = Integer.parseInt(cleanString);
                System.out.println(integer);

                // find the reimbursement with that ID
                ErsReimbursement reimbToUpdate = reimbService.getReimbById(integer);
                System.out.println("This is the reimbursement to update: " + reimbToUpdate);

                ErsReimbursement reimbursementWithResolvedInfo = mapper.readValue(req.getInputStream(), ErsReimbursement.class);

                System.out.println("This contains the updated information: " + reimbursementWithResolvedInfo);
                System.out.println("Note that ID may be null");

                // set original reimbursement to the resolve reimbursement
                reimbToUpdate.setResolverId(reimbursementWithResolvedInfo.getResolverId());
                reimbToUpdate.setResolved(reimbursementWithResolvedInfo.getResolved());
                reimbToUpdate.setReimbursementStatus(reimbursementWithResolvedInfo.getReimbursementStatus());

                // Update the DB
                reimbService.resolve(reimbToUpdate);

                // Session changes
                HttpSession session = req.getSession();
                session.setAttribute("reimbUpdated", reimbToUpdate);
                req.getSession().removeAttribute("reimbursement"); // resets so that managers can see all users again when this method is requested

                // Write back to the browser
                String reimbUpdatedJSON = mapper.writeValueAsString(reimbToUpdate);
                respWriter.write(reimbUpdatedJSON);
                resp.setStatus(201);


            } else if (req.getSession().getAttribute("authorIdToFindReimbs") != null) { // user is an employee

                System.out.println("User is an employee! Updating their reimb...");

                // Find the original reimbursement ID
                Object reimbId = req.getSession().getAttribute("reimbIdToUpdate");
                System.out.println("This is the reimb ID: " + reimbId);

                // Convert
                String string = String.valueOf(reimbId);
                System.out.println(string);
                String cleanString = string.replaceAll("\\D+", "");
                System.out.println(cleanString);
                Integer integer = Integer.parseInt(cleanString);
                System.out.println(integer);

                // find the reimbursement with that ID
                ErsReimbursement reimbToUpdate = reimbService.getReimbById(integer);
                System.out.println("This is the reimbursement to update: " + reimbToUpdate);

                // Map the input stream from the XMLHttpRequest to a reimbursement object with the new values
                ErsReimbursement ersReimbursement = mapper.readValue(req.getInputStream(), ErsReimbursement.class);
                System.out.println("ID: of this should be null: " + ersReimbursement);

                //set the new values
                reimbToUpdate.setAmount(ersReimbursement.getAmount());
                reimbToUpdate.setReimbursementType(ersReimbursement.getReimbursementType());
                reimbToUpdate.setDescription(ersReimbursement.getDescription());

                // update the reimbursement
                reimbService.update(reimbToUpdate);

                // Session changes
                HttpSession session = req.getSession();
                session.setAttribute("reimbUpdatedByEmployee", reimbToUpdate);
                req.getSession().removeAttribute("reimbursement");

                // Write back to JS and set status
                String reimbUpdatedJSON = mapper.writeValueAsString(reimbToUpdate);
                respWriter.write(reimbUpdatedJSON); // return the user (if found) to the response
                resp.setStatus(201);

            }
        } catch (InvalidRequestException ire) {
            resp.setStatus(400);
            ire.printStackTrace();
            ErrorResponse err = new ErrorResponse(400, "Reimbursement provided is null.");
            String errJSON = mapper.writeValueAsString(err);
            respWriter.write(errJSON);
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(500);
            ErrorResponse err = new ErrorResponse(500, "An Internal Service Error occured");
            respWriter.write(mapper.writeValueAsString(err));
        }


    }


    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


        resp.setContentType("application/json");

        try {

            if (req.getSession().getAttribute("reimbIdToUpdate") != null) {

                // find the original reimbursement ID
                Object reimbId = req.getSession().getAttribute("reimbIdToUpdate");
                System.out.println("This is the reimb ID: " + reimbId);

                String string = String.valueOf(reimbId);
                System.out.println(string);

                String cleanString = string.replaceAll("\\D+", "");
                System.out.println(cleanString);

                Integer integer = Integer.parseInt(cleanString);
                System.out.println(integer);

                // find the reimbursement with that ID
                ErsReimbursement reimbToDelete = reimbService.getReimbById(integer);
                System.out.println("This is the reimbursement to delete: " + reimbToDelete);

                reimbService.delete(reimbToDelete);

                req.getSession().removeAttribute("reimbursement"); // clear the session data for this

                resp.setStatus(200); // 200 = OK

            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

    }
}

