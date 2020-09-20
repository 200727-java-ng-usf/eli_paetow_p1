package com.revature.repos;

import com.revature.models.ErsReimbursement;
import com.revature.models.Status;
import com.revature.models.Type;
import com.revature.util.ConnectionFactory;

import java.sql.*;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;



public class ReimbRepository {

    // re useable
    private String baseQuery = "SELECT * FROM project1.ers_reimbursements er " +
            "JOIN project1.ers_reimbursement_types ert " +
            "ON er.reimb_type_id = ert.reimb_type_id " +
            "JOIN project1.ers_reimbursement_statuses ers " +
            "ON er.reimb_status_id = ers.reimb_status_id ";

    public ReimbRepository()  {
        System.out.println("[LOG] - Instantiating " + this.getClass().getName());
    }


    public void save(ErsReimbursement newReimbursement) {

        System.out.println("in reimbursement save method");

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            String sql = "INSERT INTO project1.ers_reimbursements (amount, submitted, description, author_id, reimb_status_id, reimb_type_id) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";

            // second paramter here is used to indicate column names that will have generated values
            PreparedStatement pstmt = conn.prepareStatement(sql, new String[] {"reimb_id"});
            pstmt.setDouble(1, newReimbursement.getAmount());
            pstmt.setTimestamp(2, newReimbursement.getSubmitted());
            pstmt.setString(3, newReimbursement.getDescription());
            pstmt.setInt(4, newReimbursement.getAuthorId());
            pstmt.setInt(5, 1); // all new reimbursements will be set to pending
            pstmt.setInt(6, (newReimbursement.getReimbursementType().ordinal()+1));

            int rowsInserted = pstmt.executeUpdate();

            if (rowsInserted != 0) {

                ResultSet rs = pstmt.getGeneratedKeys();
                rs.next();
                newReimbursement.setId(rs.getInt(1));

            }

        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

    }


    public Optional<ErsReimbursement> findReimbById(int id) {

        Optional<ErsReimbursement> _reimb = Optional.empty();

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            String sql = baseQuery + "WHERE er.reimb_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);

            Set<ErsReimbursement> result = mapResultSet(pstmt.executeQuery());

            if(!result.isEmpty()) {
                _reimb = result.stream().findFirst();
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return _reimb;
    }

    private Set<ErsReimbursement> mapResultSet(ResultSet rs) throws SQLException {

        Set<ErsReimbursement> reimbs = new HashSet<>();

        while(rs.next()) {
            ErsReimbursement temp = new ErsReimbursement();
            temp.setId(rs.getInt("reimb_id"));
            temp.setAmount(rs.getDouble("amount"));
            temp.setSubmitted(rs.getTimestamp("submitted"));
            temp.setResolved(rs.getTimestamp("resolved"));
            temp.setDescription(rs.getString("description"));
            temp.setAuthorId(rs.getInt("author_id"));
            temp.setResolverId(rs.getInt("resolver_id"));
            temp.setReimbursementType(Type.getByName(rs.getString("reimb_type")));
            temp.setReimbursementStatus(Status.getByName(rs.getString("reimb_status")));
            reimbs.add(temp);
        }

        return reimbs;

    }


    public Set<ErsReimbursement> findAllReimbs() {

        Set<ErsReimbursement> reimbs = new HashSet<>();

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            String sql = baseQuery + "ORDER BY er.reimb_status_id";

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            reimbs = mapResultSet(rs);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return reimbs;

    }


    public Set<ErsReimbursement> findAllReimbsByAuthorId(Integer authorId) {

        Set<ErsReimbursement> reimbs = new HashSet<>();

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            String sql = baseQuery + "WHERE author_id = ?";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, authorId);

            ResultSet rs = pstmt.executeQuery();
            reimbs = mapResultSet(rs);

        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

        return reimbs;

    }


    public Set<ErsReimbursement> findAllReimbsByStatus(Status status) {

        Set<ErsReimbursement> reimbs = new HashSet<>();
        Integer statusInt = status.ordinal() + 1; // convert the enum constant to an ordinal to find in the DB

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            String sql = baseQuery + "WHERE reimb_status_id = ?";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, statusInt);

            ResultSet rs = pstmt.executeQuery();
            reimbs = mapResultSet(rs);

        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

        return reimbs;

    }


    public Set<ErsReimbursement> findAllReimbsByType(Type type) {

        Set<ErsReimbursement> reimbs = new HashSet<>();
        Integer typeInt = type.ordinal() + 1; // convert the enum constant to an ordinal to find in the DB

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            String sql = baseQuery + "WHERE reimb_type_id = ?";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, typeInt);

            ResultSet rs = pstmt.executeQuery();
            reimbs = mapResultSet(rs);

        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

        return reimbs;

    }


    public boolean resolve(ErsReimbursement updatedReimb) {

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            System.out.println("This is the type ID: " + updatedReimb.getReimbursementType().ordinal());
            String sql = "UPDATE project1.ers_reimbursements "
                    + "SET resolved = '" + updatedReimb.getResolved() + "', "
                    + "resolver_id = '" + updatedReimb.getResolverId() + "', "
                    + "reimb_status_id = '" + (updatedReimb.getReimbursementStatus().ordinal() + 1) + "' "
                    + "WHERE reimb_id = '" + updatedReimb.getId() + "' ";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.executeUpdate();


            ResultSet rs = pstmt.getGeneratedKeys();

            rs.next();



        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

        return true;
    }


    public boolean update(ErsReimbursement updatedReimb) {

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            System.out.println("This is the type ID: " + updatedReimb.getReimbursementType().ordinal());
            String sql = "UPDATE project1.ers_reimbursements "
                    + "SET amount = '" + updatedReimb.getAmount() + "', "
                    + "reimb_type_id = '" + (updatedReimb.getReimbursementType().ordinal() + 1) + "', "
                    + "description = '" + updatedReimb.getDescription() + "' "
                    + "WHERE reimb_id = '" + updatedReimb.getId() + "' ";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();

            rs.next();



        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

        return true;

    }


    public void delete(ErsReimbursement reimb) {

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            String sql = "DELETE from project1.ers_reimbursements WHERE reimb_id = " + reimb.getId();

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();

            rs.next();
            System.out.println("reimbursement deleted in the DB");



        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

    }



}