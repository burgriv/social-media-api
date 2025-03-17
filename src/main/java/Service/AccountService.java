package Service;

import Model.Account;
import DAO.SocialMediaDAO;

public class AccountService {

    SocialMediaDAO socialMediaDAO;

    public AccountService(){
        socialMediaDAO = new SocialMediaDAO();
    }

    public Account addAccount(Account account){
        if (account.getUsername().length() == 0) return null;
        if (account.getPassword().length() < 4) return null;
        if (socialMediaDAO.usernameExists(account.getUsername())) return null;

        return socialMediaDAO.addAccount(account);
    }

    public Account verifyAccount(Account account){
        return socialMediaDAO.verifyAccount(account);
    }
}
