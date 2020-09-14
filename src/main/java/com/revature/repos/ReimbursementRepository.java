package com.revature.repos;

import com.revature.models.Reimbursement;
import com.revature.models.ReimbursementStatus;
import com.revature.models.ReimbursementType;
import com.revature.util.ConnectionFactory;

import java.sql.*;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class ReimbursementRepository {

    //findby reimbursement type
    //status
    //user
    //save
    //map results
    //find all
    //delete

private String baseQuery = "SELECT * FROM project1.ers_reimbursements er " +
        "JOIN project1.ers_reimbursement_types ert " +
        "ON er.reimb_type_id = ert.reimb_type_id " +
        "JOIN project1.ers_reimbursement_statuses ers " +
        "ON er.reimb_status_id = ers.reimb_status_id ";


public ReimbursementRepository(){
    super();
}

    public Set<Reimbursement> findAllReimbursements() {

        Set<Reimbursement> reimbursements = new HashSet<>();

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            String sql = baseQuery;

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            reimbursements = mapResultSet(rs);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return reimbursements;

    }

    private Set<Reimbursement> mapResultSet(ResultSet rs) throws SQLException {

        Set<Reimbursement> reimbs = new HashSet<>();

        while(rs.next()) {
            Reimbursement temp = new Reimbursement();
            temp.setId(rs.getInt("reimb_id"));
            temp.setAmount(rs.getDouble("amount"));
            temp.setSubmitted(rs.getTimestamp("submitted"));
            temp.setResolved(rs.getTimestamp("resolved"));
            temp.setDescription(rs.getString("description"));
            temp.setAuthorId(rs.getInt("author_id"));
            temp.setResolverId(rs.getInt("resolver_id"));
            temp.setReimbursementType(ReimbursementType.getByName(rs.getString("reimb_type")));
            temp.setReimbursementStatus(ReimbursementStatus.getByName(rs.getString("reimb_status")));
            reimbs.add(temp);
        }

        return reimbs;

    }

    public Optional<Reimbursement> findReimbursementById(int id) {

        Optional<Reimbursement> reimbursement = Optional.empty();

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            String sql = baseQuery + "WHERE er.reimb_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);

            Set<Reimbursement> result = mapResultSet(pstmt.executeQuery());

            if(!result.isEmpty()) {
                reimbursement = result.stream().findFirst();
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return reimbursement;
    }



    public void save(Reimbursement newReimbursement) {

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            String sql = "INSERT INTO project1.ers_reimbursements (amount, submitted, description, author_id, reimb_status_id, reimb_type_id) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";

            PreparedStatement pstmt = conn.prepareStatement(sql, new String[] {"reimb_id"});
            pstmt.setDouble(1, newReimbursement.getAmount());
            pstmt.setTimestamp(2, newReimbursement.getSubmitted());
            pstmt.setString(3, newReimbursement.getDescription());
            pstmt.setInt(4, newReimbursement.getAuthorId());
            pstmt.setInt(6, newReimbursement.getReimbursementStatus().ordinal() + 1);
            pstmt.setInt(6, newReimbursement.getReimbursementType().ordinal() + 1);

            int rowsInserted = pstmt.executeUpdate();

            if (rowsInserted != 0) {

                ResultSet rs = pstmt.getGeneratedKeys(); // use second parameter of prepare statement

                rs.next(); // for each reimbursement saved
                newReimbursement.setId(rs.getInt(1)); // get the ID and set it in the service layer

            }

        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

    }


}
