import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;

import org.apache.derby.jdbc.EmbeddedConnectionPoolDataSource;
import org.apache.derby.jdbc.EmbeddedDataSource;

public class CreateDB {
    public static void main(String[] args) {
        EmbeddedDataSource dataSource = new EmbeddedConnectionPoolDataSource();
        dataSource.setDatabaseName("ehills_users");
        dataSource.setCreateDatabase("create");
        Database db = new Database(dataSource);

        if (!Files.isDirectory(Paths.get(System.getProperty("user.dir") + "/" + "ehills_users"))) {
            System.out.println("No database found, creating a new database.");
            try {
                db.initialize();
                System.out.println("Database initialized successfully.");
            } catch (SQLException sqle) {
                System.out.println("Database initialization failed.");
                sqle.printStackTrace();
            }
        } else {
            System.out.println("Database already exists.");
        }
        
        ServerUtils.initialize(db, dataSource);
        ServerUtils.signUp("Ishan Shah", "ishan0102@utexas.edu", "password");
        ServerUtils.signUp("Michael Chen", "user@utexas.edu", "password");
        ServerUtils.signUp("Yang Zhang", "user2@utexas.edu", "password");
        ServerUtils.signUp("Rishi Pona", "user5@utexas.edu", "password");
        ServerUtils.signUp("Brian Lee", "user6@utexas.edu", "password");

        ServerUtils.addItem("ishan0102@utexas.edu", "MacBook Pro 13", "Apple computer", 0.00, 1000.00);
        ServerUtils.addItem("ishan0102@utexas.edu", "Rolex Yachtmaster", "High end watch", 0.00, 15000.00);
        ServerUtils.addItem("ishan0102@utexas.edu", "Comme des Garcons Sweater", "Streetwear clothing", 0.00, 400.00);
        ServerUtils.addItem("ishan0102@utexas.edu", "iPhone 12", "Latest Apple phone", 0.00, 800.00);
    }
}
