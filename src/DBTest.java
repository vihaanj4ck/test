
    import utils.DBConnector;

import java.sql.Connection;

    public class DBTest {
        public static void main(String[] args) {
            try {
                Connection conn = DBConnector.getConnection();
                if (conn != null) {
                    System.out.println("✅ Connected to MySQL database successfully!");
                    conn.close();
                } else {
                    System.out.println("❌ Connection failed.");
                }
            } catch (Exception e) {
                System.out.println("❌ Error connecting to database:");
                e.printStackTrace();
            }
        }
    }
