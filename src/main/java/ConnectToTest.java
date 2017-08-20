import com.mongodb.*;
import com.mongodb.event.ServerHeartbeatFailedEvent;
import com.mongodb.event.ServerHeartbeatStartedEvent;
import com.mongodb.event.ServerHeartbeatSucceededEvent;
import com.mongodb.event.ServerMonitorListener;

import java.util.Properties;

public class ConnectToTest {
    // это клиент который обеспечит подключение к БД
    private MongoClient mongoClient;
    private MongoCredential credential;

    // В нашем случае, этот класс дает
    // возможность аутентифицироваться в MongoDB
    private DB db;

    // И класс который обеспечит возможность работать
// с коллекциями / таблицами MongoDB
    private DBCollection table;

    public ConnectToTest(Properties properties) {
        // Создаем подключение
        mongoClient = new MongoClient(properties.getProperty("host"), Integer.valueOf(properties.getProperty("port")));

        // Выбираем БД для дальнейшей работы
        db = mongoClient.getDB(properties.getProperty("dbname"));

        // Входим под созданным логином и паролем
        credential = MongoCredential.createCredential(
                properties.getProperty("login"),
                properties.getProperty("dbname"),
                properties.getProperty("password").toCharArray());

        // Выбираем коллекцию/таблицу для дальнейшей работы
        table = db.getCollection(properties.getProperty("table"));
    }

    public User getByName(String name) {
        db.getStats();
        BasicDBObject query = new BasicDBObject();

        // задаем поле и значение поля по которому будем искать
        query.put("name", name);

        // осуществляем поиск
        DBObject result = table.findOne(query);

        // Заполняем сущность полученными данными с коллекции
        User user = new User();
        user.setName(String.valueOf(result.get("name")));

        // возвращаем полученного пользователя
        return user;
    }
}
