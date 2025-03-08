package Service;

import Model.Account;
import DAO.AccountDAO;


public class AccountService {
    private final AccountDAO accountDAO;
    // Instance of AccountDAO for database interactions

    // Constructor
    public AccountService() {
        this.accountDAO = new AccountDAO();
    }

    // Constructor 2
    public AccountService(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    // Method to authenticate a user by username and password
    public Account login(Account account) {
        if (account == null || account.getUsername() == null || account.getPassword() == null) {
            throw new IllegalArgumentException("Error: Please check username and password");
        }

        Account existingAccount = accountDAO.searchByUser(account.getUsername());
        if (existingAccount != null && existingAccount.getPassword().equals(account.getPassword())) {
            return existingAccount;
        } else {
            throw new IllegalArgumentException("Error: Please check username and password");
        }
    }

    // Method to register a new user
    public Account register(Account account) {
        verifyLogin(account);
        return accountDAO.addUser(account);
    }

    // Private method to validate the login credentials for registration
    private void verifyLogin(Account account) {
        if (account.getUsername() == null || account.getUsername().isBlank()) {
            throw new IllegalArgumentException("Error: The username field is empty");
        }
        if (account.getPassword() == null || account.getPassword().length() < 4) {
            throw new IllegalArgumentException("Error: Password must be at least 4 characters long");
        }
        if (accountDAO.searchByUser(account.getUsername()) != null) {
            throw new IllegalArgumentException("Error: username already exists in database");
        }
    }

    // Method to search for an account by user ID
    public Account searchById(int userId) {
        return accountDAO.searchById(userId);
    }
}
