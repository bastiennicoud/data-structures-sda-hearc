import com.github.javafaker.Faker;
import entities.SearchResult;
import database.DataRepository;
import database.DatabaseConnexion;
import database.exceptions.HydrationException;
import entities.User;

import java.sql.SQLException;
import java.util.Collection;

public class Main {

    public static void main(String[] args) {

        // initialize a connexion to the DB
        DatabaseConnexion conn = new DatabaseConnexion();

        // Create the repository for future DB call's
        DataRepository repo = new DataRepository(conn.getDbConnection());

        //try {

//            String[] tokens = "Bas".split(" ");
//            Collection<SearchResult> users = repo.textSearch(SearchResult.class, tokens);
//
//            users.forEach(f -> System.out.println("Type : " + f.type + " Titre : " + f.title));
//
//            User baba = repo.findById(User.class, 4);
//            System.out.println(baba.firstName);


            Faker faker = new Faker();
            int i = 0;
            while (i <= 100) {
                User usr = new User(
                        faker.name().firstName(),
                        faker.name().lastName()
                );
                usr = repo.insertNew(usr);
                i++;
            }

//        } catch (SQLException | HydrationException throwables) {
//
//            throwables.printStackTrace();
//
//        }

        conn.closeConnexion();

    }

}
