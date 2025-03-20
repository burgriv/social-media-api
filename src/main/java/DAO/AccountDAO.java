package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Model.Account;
import Util.ConnectionUtil;

public class AccountDAO {
    /**
     * Attempts to connect to the database and add account as a new record to the account table.
     * @param account The Account object to persist to the database.
     * @return The Account object after persisting it to the database. Returns null on failure.
     */
    public Account addAccount(Account account){
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "INSERT INTO account (username, password) VALUES (?, ?);";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getPassword());

            preparedStatement.executeUpdate();
            ResultSet rs = preparedStatement.getGeneratedKeys();
            if(rs.next()){
                int account_id = rs.getInt(1);
                return new Account(account_id, account.getUsername(), account.getPassword());
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Attempts to connect to the database and verify that account exists.
     * @param account The Account object to verify
     * @return The Account object after verifying it from the database. Returns null on failure.
     */
    public Account verifyAccount(Account account){
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT * FROM account WHERE username=? AND password=?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getPassword());

            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()){
                int account_id = rs.getInt(1);
                return new Account(account_id, account.getUsername(), account.getPassword());
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Attempts to connect to the database and check if username is present in the account table.
     * @param username The username to search for in the database.
     * @return true if username is currently being used by an Account, false otherwise.
     */
    public boolean usernameExists(String username){
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT * FROM account WHERE username = ?;" ;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, username);

            if (preparedStatement.executeQuery().next()) return true;
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    /**
     * Attempts to connect to the database and check if account_id is present in the account table.
     * @param account_id The account_id to search for in the database.
     * @return true if account_id is currently being used by an Account, false otherwise.
     */
    public boolean accountIDExists(int account_id){
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT * FROM account WHERE account_id = ?;" ;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, account_id);

            if (preparedStatement.executeQuery().next()) return true;
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
    
}
