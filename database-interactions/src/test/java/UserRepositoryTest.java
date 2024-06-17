import org.junit.jupiter.api.*;
import ru.gordeev.Repository;
import ru.gordeev.DataSource;
import ru.gordeev.DbMigrator;
import ru.gordeev.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserRepositoryTest {

    private DataSource dataSource;
    private Repository<User> userRepository;

    @BeforeEach
    public void setUp() {
        dataSource = new DataSource();
        dataSource.connect();
        DbMigrator dbMigrator = new DbMigrator(dataSource);
        dbMigrator.migrate();
        userRepository = new Repository<>(dataSource, User.class);
    }

    @AfterEach
    public void tearDown() {
        userRepository.deleteAll();
        dataSource.disconnect();
    }

    @Test
    void testCreate() {
        User user = new User("login1", "password1", "nickname1");
        userRepository.create(user);

        List<User> users = userRepository.findAll();
        assertEquals(1, users.size());
        assertEquals("login1", users.get(0).getLogin());
        assertEquals("password1", users.get(0).getPassword());
        assertEquals("nickname1", users.get(0).getNickname());
    }

    @Test
    void testFindById() {
        User user = new User("login2", "password2", "nickname2");
        userRepository.create(user);

        List<User> users = userRepository.findAll();
        User foundUser = userRepository.findById(users.get(0).getId());

        assertNotNull(foundUser);
        assertEquals("login2", foundUser.getLogin());
        assertEquals("password2", foundUser.getPassword());
        assertEquals("nickname2", foundUser.getNickname());
    }

    @Test
    void testUpdate() {
        User user = new User("login3", "password3", "nickname3");
        userRepository.create(user);

        List<User> users = userRepository.findAll();
        userRepository.update(users.get(0).getId(), "nickname", "newNickname");

        User updatedUser = userRepository.findById(users.get(0).getId());
        assertEquals("newNickname", updatedUser.getNickname());
    }

    @Test
    void testUpdateAllFields() {
        User user = new User("login4", "password4", "nickname4");
        userRepository.create(user);

        List<User> users = userRepository.findAll();
        userRepository.updateAllFields(users.get(0).getId(), "newLogin", "newPassword", "newNickname");

        User updatedUser = userRepository.findById(users.get(0).getId());
        assertEquals("newLogin", updatedUser.getLogin());
        assertEquals("newPassword", updatedUser.getPassword());
        assertEquals("newNickname", updatedUser.getNickname());
    }

    @Test
    void testDeleteById() {
        User user = new User("login5", "password5", "nickname5");
        userRepository.create(user);

        List<User> users = userRepository.findAll();
        userRepository.deleteById(users.get(0).getId());

        users = userRepository.findAll();
        assertTrue(users.isEmpty());
    }

    @Test
    void testDeleteAll() {
        User user1 = new User("login6", "password6", "nickname6");
        User user2 = new User("login7", "password7", "nickname7");
        userRepository.create(user1);
        userRepository.create(user2);

        userRepository.deleteAll();

        List<User> users = userRepository.findAll();
        assertTrue(users.isEmpty());
    }
}
