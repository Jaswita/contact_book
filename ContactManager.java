package dbms;

import java.sql.*;
import java.util.Scanner;

public class ContactManager {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/contact_book";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "Password";

    private static Connection conn;

    public static void main(String[] args) {
        try {
            // Load JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Connect to MySQL
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            System.out.println("✅ Connected to MySQL.");

            // Create table if not exists
            createTable();

            // Menu
            Scanner sc = new Scanner(System.in);
            while (true) {
                System.out.println("\n==== Contact Book ====");
                System.out.println("1. Add Contact");
                System.out.println("2. View Contacts");
                System.out.println("3. Update Contact");
                System.out.println("4. Delete Contact");
                System.out.println("5. Exit");
                System.out.print("Enter choice: ");
                String choice = sc.nextLine();

                switch (choice) {
                    case "1":
                        addContact(sc);
                        break;
                    case "2":
                        viewContacts();
                        break;
                    case "3":
                        updateContact(sc);
                        break;
                    case "4":
                        deleteContact(sc);
                        break;
                    case "5":
                        conn.close();
                        System.out.println("Connection closed. Exiting.");
                        return;
                    default:
                        System.out.println("Invalid choice.");
                }
            }

        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void createTable() throws SQLException {
        String createQuery = """
            CREATE TABLE IF NOT EXISTS contacts (
                id INT AUTO_INCREMENT PRIMARY KEY,
                name VARCHAR(255) NOT NULL,
                phone VARCHAR(20) NOT NULL,
                email VARCHAR(255)
            );
        """;
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(createQuery);
        }
    }

    private static void addContact(Scanner sc) throws SQLException {
        System.out.print("Name: ");
        String name = sc.nextLine();
        System.out.print("Phone: ");
        String phone = sc.nextLine();
        System.out.print("Email: ");
        String email = sc.nextLine();

        String insertQuery = "INSERT INTO contacts (name, phone, email) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(insertQuery)) {
            ps.setString(1, name);
            ps.setString(2, phone);
            ps.setString(3, email);
            ps.executeUpdate();
            System.out.println("✅ Contact added.");
        }
    }

    private static void viewContacts() throws SQLException {
        String selectQuery = "SELECT * FROM contacts";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(selectQuery)) {

            System.out.println("\n--- Contacts ---");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id"));
                System.out.println("Name: " + rs.getString("name"));
                System.out.println("Phone: " + rs.getString("phone"));
                System.out.println("Email: " + rs.getString("email"));
                System.out.println("------------------");
            }
        }
    }

    private static void updateContact(Scanner sc) throws SQLException {
        System.out.print("Enter contact ID to update: ");
        int id = Integer.parseInt(sc.nextLine());
        System.out.print("New Name: ");
        String name = sc.nextLine();
        System.out.print("New Phone: ");
        String phone = sc.nextLine();
        System.out.print("New Email: ");
        String email = sc.nextLine();

        String updateQuery = "UPDATE contacts SET name=?, phone=?, email=? WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(updateQuery)) {
            ps.setString(1, name);
            ps.setString(2, phone);
            ps.setString(3, email);
            ps.setInt(4, id);
            int rows = ps.executeUpdate();
            if (rows > 0)
                System.out.println("✅ Contact updated.");
            else
                System.out.println("❌ Contact ID not found.");
        }
    }

    private static void deleteContact(Scanner sc) throws SQLException {
        System.out.print("Enter contact ID to delete: ");
        int id = Integer.parseInt(sc.nextLine());

        String deleteQuery = "DELETE FROM contacts WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(deleteQuery)) {
            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            if (rows > 0)
                System.out.println("🗑️ Contact deleted.");
            else
                System.out.println("❌ Contact ID not found.");
        }
    }
}

