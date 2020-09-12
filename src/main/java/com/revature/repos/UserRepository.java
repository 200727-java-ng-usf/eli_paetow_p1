package com.revature.repos;

import com.revature.models.Role;
import com.revature.models.User;
import com.revature.util.ConnectionFactory;

import java.sql.*;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class UserRepository {

    //TODO check the id's change to userId ?
    public UserRepository(){

    }
    //re use query
    private String baseQuery = "SELECT * FROM project1.ers_users eu " +
            "JOIN project1.ers_user_roles er " +
            "ON eu.user_role_id = er.role_id ";



    public Optional<User> findUserById(int id) {

        Optional<User> _user = Optional.empty();

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {
            String sql = baseQuery + "WHERE eu.ers_user_id = ?";
            //check id name in db
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            Set<User> result = mapResultSet(pstmt.executeQuery());
            if (!result.isEmpty()) {
                _user = result.stream().findFirst();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return _user;
    }
    public Set<User> findAllUsers() {

        Set<User> users = new HashSet<>();

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            String sql = baseQuery;
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            users = mapResultSet(rs);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return users;

    }
    public Optional<User> findUserByUsername(String username) {

        Optional<User> _user = Optional.empty();

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            // you can control whether or not JDBC automatically commits DML statements
//            conn.setAutoCommit(false);

            String sql = baseQuery + "WHERE username = ?";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);

            ResultSet rs = pstmt.executeQuery();
            _user = mapResultSet(rs).stream().findFirst();

            // if you want to manually control the transaction
//            conn.commit();
//            conn.rollback();
//            conn.setSavepoint();

        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

        return _user;

    }

    public Optional<User> findUserByCredentials(String username, String password) {

        Optional<User> _user = Optional.empty();

        /**
         * Try with resources; the resource is the JDB
         */
        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            String sql = baseQuery + "WHERE username = ? AND password = ?";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery(); // assign the SQL query to a ResultSet object

            /**
             * Map the result set of the query to the _user Optional
             */
            _user = mapResultSet(rs).stream().findFirst();

        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

        return _user;
    }

    public Optional<User> findUserByEmail(String email) {

        Optional<User> _user = Optional.empty();

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            String sql = baseQuery + "WHERE email = ?";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, email);

            ResultSet rs = pstmt.executeQuery();
            _user = mapResultSet(rs).stream().findFirst();

        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

        return _user;

    }


    //do we need to return an optional???
    public void save(User newUser) {
        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            String sql = "INSERT INTO project1.ers_users (username, password, first_name, last_name, email, user_role_id) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";

            /**
             * Prepared Statement uses user generated values, denoted with ?s
             */
            // second parameter here is used to indicate column names that will have generated values
            PreparedStatement pstmt = conn.prepareStatement(sql, new String[] {"ers_user_id"});
            pstmt.setString(1, newUser.getUsername());
            pstmt.setString(2, newUser.getPassword());
            pstmt.setString(3, newUser.getFirstName());
            pstmt.setString(4, newUser.getLastName());
            pstmt.setString(5, newUser.getEmail());
            pstmt.setInt(6, newUser.getUserRole().ordinal() + 1);

            int rowsInserted = pstmt.executeUpdate(); // returns an int that represents the #rows inserted

            if (rowsInserted != 0) {

                ResultSet rs = pstmt.getGeneratedKeys();

                rs.next();
                newUser.setId(rs.getInt(1));

            }

        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

    }
    public boolean update(User ersUser) {
        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            String sql = "UPDATE project1.ers_users "
                    + "SET email = '" + ersUser.getEmail() + "', "
                    + "username = '" + ersUser.getUsername() + "', "
                    + "password = '" + ersUser.getPassword() + "', "
                    + "first_name = '" + ersUser.getFirstName() + "', "
                    + "last_name = '" + ersUser.getLastName() + "' "
                    + "role_id = " + ersUser.getUserRole() + "', "
                    + "WHERE ers_user_id = " + ersUser.getId();

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.executeUpdate(); //
            pstmt.close();

        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

        return true;

    }



    private Set<User> mapResultSet(ResultSet rs) throws SQLException {

        Set<User> users = new HashSet<>();

        /**
         * Extract results, set the temporary AppUser fields, and add the temp AppUser to the Set.
         */
        while (rs.next()) {
            User temp = new User();
            //id or ersuserid
            temp.setId(rs.getInt("ers_user_id"));
            temp.setUsername(rs.getString("username"));
            temp.setPassword(rs.getString("password"));
            temp.setFirstName(rs.getString("first_name"));
            temp.setLastName(rs.getString("last_name"));
            temp.setEmail(rs.getString("email"));
            temp.setUserRole(Role.getByName(rs.getString("user_role_id")));
            System.out.println(temp);
            users.add(temp);
        }

        return users;

    }
}