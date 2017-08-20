import com.mongodb.ConnectionString;
import com.mongodb.MongoClient;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Properties;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class WorkWithMongoTest {
    private WorkWithMongo mongo;
    private ConnectToTest mongoTest;

    @BeforeMethod
    public void setUp() throws Exception {
        Properties prop = new Properties();
        prop.setProperty("host", "localhost");
        prop.setProperty("port", "27017");
        prop.setProperty("dbname", "java");
        prop.setProperty("login", "root");
        prop.setProperty("password", "root");
        prop.setProperty("table", "users");

        Properties properties = new Properties();
        properties.setProperty("host", "localhost");
        properties.setProperty("port", "27017");
        properties.setProperty("dbname", "unknown");
        properties.setProperty("login", "rot");
        properties.setProperty("password", "rot");
        properties.setProperty("table", "users");

        mongo = new WorkWithMongo(prop);
        mongoTest = new ConnectToTest(properties);
    }

    @AfterMethod
    public void tearDown() throws Exception {
        mongo.close();
    }

    @Test
    public void testConnection() throws Exception {
        mongo.serverConnection();
    }

    @Test
    public void testAddUser() throws Exception {
        mongo.add(new User("test"));
    }

    @Test
    public void testGet() throws Exception {
        User user = mongo.getByDataName("Dima");
        System.out.println(user);
    }

    @Test
    public void compare() throws Exception {
        User userJava;
        User userTest;
        userJava = mongo.getByDataName("Dima");
        userTest = mongoTest.getByName("Dim");
        assertEquals(userJava,userTest);
//        System.out.println(user);
    }

    @Test
    public void testDelete() throws Exception {
        mongo.deleteByLogin("test");
    }

    @Test
    public void testUpdate() throws Exception {
        mongo.updateByLogin("test", "DevColibri");
    }

}
