package services;

import models.Contact;

import java.sql.*;
import java.util.ArrayList;

public class DBManager {

    private static DBManager dbManager;

    // Connection and statements.
    private Connection connection;
    private Statement statement;
    private PreparedStatement preparedStatement;

    /**
     * Checks whether table already exists.
     * @param conn - connection to the database.
     * @return - true if table exists otherwise false.
     */
    private boolean checkTable(Connection conn, String table) {
        try {
            Statement s = conn.createStatement();
            s.execute("select * from " + table);
        } catch (SQLException e) {
            String theError = e.getSQLState();
            if (theError.equals("42X05"))
                return false;
        }
        return true;
    }

    /**
     * Gets contact from the result set.
     * @param result - result set with correct pointer.
     * @return - contact from the result set.
     */
    private Contact getContact(ResultSet result) {
        Contact contact = null;
        try {
            contact = new Contact();
            contact.setID(result.getInt("ID"));
            contact.setFirstName(result.getString("FIRST_NAME"));
            contact.setLastName(result.getString("LAST_NAME"));
            contact.setMiddleName(result.getString("MIDDLE_NAME"));
            contact.setMobilePhone(result.getString("MOBILE_PHONE"));
            contact.setHomePhone(result.getString("HOME_PHONE"));
            contact.setAddress(result.getString("ADDRESS"));
            contact.setDateOfBirth(result.getDate("BIRTHDAY") != null ? result.getDate("BIRTHDAY").toLocalDate() : null);
            contact.setNotes(result.getString("NOTES"));
        } catch (SQLException e) {
            System.out.println("Error!");
        }
        return contact;
    }

    /**
     * Gets contact with appropriated properties.
     * @param firstName - first name of the contact we search.
     * @param lastName - last name of the contact we search.
     * @param middleName - patronymic of the contact we search.
     * @return - appropriate contact.
     */
    public Contact getContact(String firstName, String lastName, String middleName, String tableName) {
        Contact contact = null;
        try {
            var result = statement.executeQuery("SELECT * FROM " + tableName +
                    " WHERE FIRST_NAME = '" + firstName + "' AND LAST_NAME = '" + lastName + "' AND MIDDLE_NAME = '" + middleName + "'");
            result.next();
            contact = getContact(result);
        } catch (SQLException e) {
            System.out.println("Error with database!");
        }
        return contact;
    }

    /**
     * Prepare statement to execute.
     * @param preparedStatement - statement.
     * @param contact - contact to fill.
     */
    private void prepareStatement(PreparedStatement preparedStatement, Contact contact) {
        try {
            preparedStatement.setString(1, contact.getFirstName());
            preparedStatement.setString(2, contact.getLastName());
            preparedStatement.setString(3, contact.getMiddleName());
            preparedStatement.setString(4, contact.getMobilePhone());
            preparedStatement.setString(5, contact.getHomePhone());
            preparedStatement.setString(6, contact.getAddress());
            preparedStatement.setDate(7, contact.getDateOfBirth() != null ? java.sql.Date.valueOf(contact.getDateOfBirth()) : null);
            preparedStatement.setString(8, contact.getNotes());
        } catch (SQLException e) {
            System.out.println("Something went wrong!");
        }
    }

    private DBManager(String nameDB) {
        // Define the Derby connection URL to use.
        String connectionURL = "jdbc:derby:" + nameDB + ";create=true";

        try {
            // Create (if needed) and connect to the database.
            // The driver is loaded automatically.
            connection = DriverManager.getConnection(connectionURL);
            statement = connection.createStatement();

        } catch (SQLException e) {
            System.out.println("Database connection error!");
        }
    }

    /**
     * Creates a new table in current database.
     * @param tableName - name of the table.
     */
    public void createTable(String tableName) {
        String createString = "CREATE TABLE " + tableName + "( "
                + "ID INT NOT NULL GENERATED ALWAYS AS IDENTITY, "
                + "FIRST_NAME VARCHAR(255) NOT NULL, "
                + "LAST_NAME VARCHAR(255) NOT NULL, "
                + "MIDDLE_NAME VARCHAR(255), "
                + "MOBILE_PHONE VARCHAR(255), "
                + "HOME_PHONE VARCHAR(255), "
                + "ADDRESS VARCHAR(255), "
                + "BIRTHDAY DATE, "
                + "NOTES VARCHAR(255)) ";

        try {
            if (!checkTable(connection, tableName))
                statement.execute(createString);
        } catch (SQLException ignored) {}
    }

    /**
     * Select all contacts from database.
     * @return - list of contacts.
     */
    public ArrayList<Contact> getContacts(String tableName) {
        var contacts = new ArrayList<Contact>();
        try {
            var result = statement.executeQuery("SELECT * FROM " + tableName);
            while (result.next())
                contacts.add(getContact(result));

        } catch (SQLException e) {
            System.out.println("Error!");
        }
        return contacts;
    }

    /**
     * Inserts new contact to the database.
     * @param contact - new contact.
     * @return - appropriate contact from database if there are correct query, null otherwise.
     */
    public Contact addContact(Contact contact, String tableName) {
        Contact result = null;
        try {
            preparedStatement = connection.prepareStatement("insert into " + tableName +
                    "(FIRST_NAME, LAST_NAME, MIDDLE_NAME, MOBILE_PHONE, HOME_PHONE, ADDRESS, BIRTHDAY, NOTES)" +
                    " values (?, ?, ?, ?, ?, ?, ?, ?)");
            prepareStatement(preparedStatement, contact);
            preparedStatement.executeUpdate();
            result = getContact(contact.getFirstName(), contact.getLastName(), contact.getMiddleName(), tableName);

        } catch (SQLException e) {
            System.out.println("Inserting error");
        }
        return result;
    }

    /**
     * Removes contact from database.
     * @param contact - target contact.
     * @return - true if contact was deleted, otherwise - false.
     */
    public boolean removeContact(Contact contact, String tableName) {
        int result = 0;
        try {
            preparedStatement = connection.prepareStatement("DELETE FROM " + tableName + " WHERE ID = ?");
            preparedStatement.setInt(1, contact.getID());
            result = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Removing contact error");
        }
        return result == 1;
    }

    /**
     * Updates appropriate contact.
     * @param contact - updated contact.
     * @return - true if contact was updated, otherwise - false.
     */
    public boolean editContact(Contact contact, String tableName) {
        int result = 0;

        try {
            preparedStatement = connection.prepareStatement("UPDATE " + tableName + " SET FIRST_NAME = ?, LAST_NAME = ?, MIDDLE_NAME = ?, " +
                    "MOBILE_PHONE = ?, HOME_PHONE = ?, ADDRESS = ?, BIRTHDAY = ?, NOTES = ? WHERE ID = ?");
            prepareStatement(preparedStatement, contact);
            preparedStatement.setInt(9, contact.getID());
            result = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error!");
        }

        return result == 1;
    }

    /**
     * Searches contacts which first name, last name or patronymic contains query.
     * @param query - search query.
     * @return - id's of contacts.
     */
    public ArrayList<Integer> search(String query, String tableName) {
        var result = new ArrayList<Integer>();

        try {
            var set = statement.executeQuery("SELECT ID FROM " + tableName + " WHERE FIRST_NAME LIKE '%"
                    + query + "%' OR LAST_NAME LIKE '%" + query + "%' OR MIDDLE_NAME LIKE '%" + query + "%'");
            while (set.next())
                result.add(set.getInt("ID"));
        } catch (SQLException e) {
            System.out.println("Error");
        }

        return result;
    }

    public static DBManager getInstance(String nameDB) {
        if (dbManager == null)
            dbManager = new DBManager(nameDB);
        return dbManager;
    }

    /**
     * Closes all connections.
     */
    public void releaseResources() {
        try {
            if (connection != null)
                connection.close();
            if (statement != null)
                statement.close();
            if (preparedStatement != null)
                preparedStatement.close();
        } catch (SQLException ignored) { }
    }
}
