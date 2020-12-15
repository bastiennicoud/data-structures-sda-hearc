import Entity.User;
import database.DataRepository;
import database.DatabaseConnexion;

import java.util.Collection;

public class Main {

    public static void main(String[] args){

        // initialize a connexion to the DB
        DatabaseConnexion conn = new DatabaseConnexion();

        // Create the repository for future DB call's
        DataRepository repo = new DataRepository(conn.getDbConnection());

        Collection<User> users = repo.query("SELECT * FROM users", User.class);

        conn.closeConnexion();

        users.forEach(System.out::println);

    }
}
