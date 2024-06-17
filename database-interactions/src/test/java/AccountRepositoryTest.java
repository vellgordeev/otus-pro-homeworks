import org.junit.jupiter.api.*;
import ru.gordeev.Repository;
import ru.gordeev.Account;
import ru.gordeev.DataSource;
import ru.gordeev.DbMigrator;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AccountRepositoryTest {

    private DataSource dataSource;
    private Repository<Account> accountRepository;

    @BeforeEach
    public void setUp() {
        dataSource = new DataSource();
        dataSource.connect();
        DbMigrator dbMigrator = new DbMigrator(dataSource);
        dbMigrator.migrate();
        accountRepository = new Repository<>(dataSource, Account.class);
    }

    @AfterEach
    public void tearDown() {
        accountRepository.deleteAll();
        dataSource.disconnect();
    }


    @Test
    void testCreateAccount() {
        Account account = new Account(1000L, "savings", "active");
        accountRepository.create(account);

        List<Account> accounts = accountRepository.findAll();
        assertEquals(1, accounts.size());
        assertEquals(1000L, accounts.get(0).getAmount());
        assertEquals("savings", accounts.get(0).getAccountType());
        assertEquals("active", accounts.get(0).getStatus());
    }

    @Test
    void testFindAccountById() {
        Account account = new Account(2000L, "current", "inactive");
        accountRepository.create(account);

        List<Account> accounts = accountRepository.findAll();
        Account foundAccount = accountRepository.findById(accounts.get(0).getId());

        assertNotNull(foundAccount);
        assertEquals(2000L, foundAccount.getAmount());
        assertEquals("current", foundAccount.getAccountType());
        assertEquals("inactive", foundAccount.getStatus());
    }

    @Test
    void testUpdateAccountType() {
        Account account = new Account(3000L, "savings", "active");
        accountRepository.create(account);

        List<Account> accounts = accountRepository.findAll();
        accountRepository.update(accounts.get(0).getId(), "account_type", "current");

        Account updatedAccount = accountRepository.findById(accounts.get(0).getId());
        assertEquals("current", updatedAccount.getAccountType());
    }

    @Test
    void testUpdateAllAccountFields() {
        Account account = new Account(4000L, "current", "inactive");
        accountRepository.create(account);

        List<Account> accounts = accountRepository.findAll();
        accountRepository.updateAllFields(accounts.get(0).getId(), "5000", "savings", "active");

        Account updatedAccount = accountRepository.findById(accounts.get(0).getId());
        assertEquals(5000L, updatedAccount.getAmount());
        assertEquals("savings", updatedAccount.getAccountType());
        assertEquals("active", updatedAccount.getStatus());
    }

    @Test
    void testDeleteAccountById() {
        Account account = new Account(6000L, "current", "inactive");
        accountRepository.create(account);

        List<Account> accounts = accountRepository.findAll();
        accountRepository.deleteById(accounts.get(0).getId());

        accounts = accountRepository.findAll();
        assertTrue(accounts.isEmpty());
    }

    @Test
    void testDeleteAllAccounts() {
        Account account1 = new Account(7000L, "current", "inactive");
        Account account2 = new Account(8000L, "savings", "active");
        accountRepository.create(account1);
        accountRepository.create(account2);

        accountRepository.deleteAll();

        List<Account> accounts = accountRepository.findAll();
        assertTrue(accounts.isEmpty());
    }
}
