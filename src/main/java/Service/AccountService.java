package Service;

import Model.Account;

import DAO.SocialMediaDAO;

public class AccountService {

    SocialMediaDAO socialMediaDAO;

    public AccountService(){
        socialMediaDAO = new SocialMediaDAO();
    }

    /**
     * Validates the account's fields, then persists it to the database.
     * An account is valid if the username is not blank, the password is at least four characters, and the username is not taken.
     * @param account The Account object to be added to the database.
     * @return The Account object persisted to the database.
     */
    public Account addAccount(Account account){
        if (account.getUsername().length() == 0) return null;
        if (account.getPassword().length() < 4) return null;
        if (socialMediaDAO.usernameExists(account.getUsername())) return null;

        return socialMediaDAO.addAccount(account);
    }

    /**
     * Calls the DAO to verify the Account's information in the database.
     * @param account The Account object to verify.
     * @return The verified Account object. Returns null on failure.
     */
    public Account verifyAccount(Account account){
        return socialMediaDAO.verifyAccount(account);
    }
}
