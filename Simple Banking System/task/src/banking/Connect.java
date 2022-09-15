package banking;

import org.sqlite.SQLiteDataSource;

import java.sql.*;

public class Connect {
    SQLiteDataSource dataSource = new SQLiteDataSource();

    String url = "jdbc:sqlite:card.s3db";
    private Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public void createDB() {
        this.dataSource.setUrl(this.url);
    }

    //creates table in database
    public void createTable() {
        String cmd = "CREATE TABLE IF NOT EXISTS card(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "number VARCHAR(16) NOT NULL UNIQUE, " +
                "pin VARCHAR(4) NOT NULL, " +
                "balance INTEGER DEFAULT 0)";

        try (Connection con = dataSource.getConnection()) {
            try (Statement statement = con.createStatement()) {
                statement.execute(cmd);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // checks if accNum exists in database
    public boolean checkExistence(String accNum) {
        String sql = "SELECT * "
                + "FROM card WHERE number LIKE " + accNum;
        try (Connection con = dataSource.getConnection()) {
            try (Statement statement = con.createStatement()) {
                ResultSet rs = statement.executeQuery(sql);
                if(rs.next()){
                    return true;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void addInfoDB(Account acc) {
        try (Connection con = dataSource.getConnection()) {
            try (Statement statement = con.createStatement()) {
                String sql = "INSERT INTO card (number,pin,balance) VALUES " + String.format("('%s', '%s', %d)",
                        acc.getAccountID(), acc.getPinCode(), acc.getBalance());
                statement.executeUpdate(sql);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateBalance(Account acc) {
        String sql = "UPDATE card SET balance = ? "
                + "WHERE number = ?";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // set the corresponding param
            pstmt.setLong(1, acc.getBalance());
            pstmt.setString(2, acc.getAccountID());
            // update
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public void transferBalance(String accNum, long addBalance) {
        long balance = connectToAcc(accNum);

        String sql = "UPDATE card SET balance = ? "
                + "WHERE number = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {


            // set the corresponding param
            pstmt.setLong(1, balance + addBalance);
            pstmt.setString(2, accNum);
            // update
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void selectAll(){
        String sql = "SELECT number, pin, balance FROM card";
        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            // loop through the result set
            while (rs.next()) {
                System.out.println(rs.getString("number") +  "\t" +
                        rs.getString("pin") + "\t" +
                        rs.getLong("balance"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public Account connectToAcc(String cardNum, Account acc){
        String sql = "SELECT * "
                + "FROM card WHERE number LIKE " + cardNum;

        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            while(rs.next()) {
                String accNum = rs.getString("number");
                String pinCode = rs.getString("pin");
                long balance = rs.getLong("balance");

                acc.setAcc(accNum);
                acc.setPinCode(pinCode);
                acc.setBalance(balance);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return acc;
    }
    public long connectToAcc(String cardNum){
        String sql = "SELECT * "
                + "FROM card WHERE number LIKE " + cardNum;
        long balance = 0;

        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            while(rs.next()) {
                balance = rs.getLong("balance");

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return balance;
    }
    public boolean checkData(String cardNum, int pinCode){
        String sql = "SELECT pin "
                + "FROM card WHERE number LIKE " + cardNum;

        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            while (rs.next()) {
                if (pinCode == rs.getInt("pin")) {
                    return true;
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public void deleteAcc(String cardNum) {
        String sql = "DELETE FROM card WHERE number = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // set the corresponding param
            pstmt.setString(1, cardNum);
            // execute the delete statement
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void clearDB() {
        try (Connection con = dataSource.getConnection()) {
            try (Statement statement = con.createStatement()) {
                String clearTable = "DROP TABLE IF EXISTS card";
                statement.executeUpdate(clearTable);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}