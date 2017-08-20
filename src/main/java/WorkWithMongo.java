import com.mongodb.*;
import com.mongodb.event.ServerHeartbeatFailedEvent;
import com.mongodb.event.ServerHeartbeatStartedEvent;
import com.mongodb.event.ServerHeartbeatSucceededEvent;
import com.mongodb.event.ServerMonitorListener;

import java.net.UnknownHostException;
import java.util.Properties;

public class WorkWithMongo implements ServerMonitorListener {
    // это клиент который обеспечит подключение к БД
    private MongoClient mongoClient;
    private MongoCredential credential;

    // В нашем случае, этот класс дает
    // возможность аутентифицироваться в MongoDB
    private DB db;

    // тут мы будем хранить состояние подключения к БД
    private boolean authenticate;

    // И класс который обеспечит возможность работать
// с коллекциями / таблицами MongoDB
    private DBCollection table;

    public WorkWithMongo(Properties prop) {
        // Создаем подключение
        mongoClient = new MongoClient(prop.getProperty("host"), Integer.valueOf(prop.getProperty("port")));

        // Выбираем БД для дальнейшей работы
        db = mongoClient.getDB(prop.getProperty("dbname"));

        // Входим под созданным логином и паролем
        credential = MongoCredential.createCredential(
                prop.getProperty("login"),
                prop.getProperty("dbname"),
                prop.getProperty("password").toCharArray());

        //authenticate = db.authenticate(prop.getProperty("login"), prop.getProperty("password").toCharArray());

        // Выбираем коллекцию/таблицу для дальнейшей работы
        table = db.getCollection(prop.getProperty("table"));
    }

    public void add(User user) {
        BasicDBObject document = new BasicDBObject();

        // указываем поле с объекта User
        // это поле будет записываться в коллекцию/таблицу
        document.put("login", user.getLogin());

        // записываем данные в коллекцию/таблицу
        table.insert(document);
    }

    public User getByDataName(String name) {
        BasicDBObject query = new BasicDBObject();

        // задаем поле и значение поля по которому будем искать
        query.put("name", name);

        // осуществляем поиск
        DBObject result = table.findOne(query);

        // Заполняем сущность полученными данными с коллекции
        User user = new User();
        user.setName(String.valueOf(result.get("name")));
        user.setId(String.valueOf(result.get("_id")));

        // возвращаем полученного пользователя
        return user;
    }

    // login - это старый логин пользователя
// newLogin - это новый логин который мы хотим задать
    public void updateByLogin(String login, String newLogin) {
        BasicDBObject newData = new BasicDBObject();

        // задаем новый логин
        newData.put("login", newLogin);

        // указываем обновляемое поле и текущее его значение
        BasicDBObject searchQuery = new BasicDBObject().append("login", login);

        // обновляем запись
        table.update(searchQuery, newData);
    }

    public void deleteByLogin(String login) {
        BasicDBObject query = new BasicDBObject();

        // указываем какую запись будем удалять с коллекции
        // задав поле и его текущее значение
        query.put("login", login);

        // удаляем запись с коллекции/таблицы
        table.remove(query);
    }

    public void close() {
        mongoClient.close();
    }

    public void serverConnection() {
        try {
            MongoClientOptions clientOptions = new MongoClientOptions.Builder()
                    .addServerMonitorListener(this)
                    .build();

            mongoClient = new MongoClient(new ServerAddress("localhost", 27017), clientOptions);
        } catch (Exception ex) {
            System.out.println("Mongo is down");
            mongoClient.close();
            return;
        }
    }

    public void dbStats() {
        db.getStats();
    }

    @Override
    public void serverHearbeatStarted(ServerHeartbeatStartedEvent event) {

    }

    @Override
    public void serverHeartbeatSucceeded(ServerHeartbeatSucceededEvent event) {

    }

    @Override
    public void serverHeartbeatFailed(ServerHeartbeatFailedEvent event) {

    }
}
