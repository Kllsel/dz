package org.example;
import java.sql.*;
import java.util.Scanner;

public class Main {

    private static final String URL = "jdbc:postgresql://ep-aged-base-a57gh1yy.us-east-2.aws.neon.tech:5432/pixej76488db";
    private static final String USER = "pixej76488db_owner";
    private static final String PASSWORD = "Q4kwmoFzi1Xb";

    // Підключення до БД
    private static Connection connection;

    public static void main(String[] args) {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            createTable();

            Scanner scanner = new Scanner(System.in);

            label:
            while (true) {
                System.out.println("Menu");
                System.out.println("Choose the option: ");
                System.out.println("1) Add abonent");
                System.out.println("2) Delete abonent");
                System.out.println("3) Search abonent");
                System.out.println("4) Exit");
                String option = scanner.nextLine();

                if (option.equals("1")) {
                    addAbonent();
                } else if (option.equals("2")) {
                    deleteAbonent();
                } else if (option.equals("3")) {
                    searchAbonent();
                } else if (option.equals("4")) {
                    System.out.println("Exiting program...");
                    break;
                } else {
                    System.out.println("Invalid option, please try again.");
                }
            }
        } catch (Exception e) {
            System.out.println("Something went wrong... " + e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                    System.out.println("Connection closed.");
                }
            } catch (SQLException e) {
                System.out.println("Failed to close the connection...");
            }
        }
    }

    private static void searchAbonent() {
        System.out.println("Enter the ID of the abonent you want to search:");
        Scanner scanner = new Scanner(System.in);
        int id = Integer.parseInt(scanner.nextLine());

        String searchSQL = "SELECT * FROM Phonebook WHERE ID = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(searchSQL)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                // Виведення інформації про абонента
                System.out.println("ID: " + resultSet.getInt("ID"));
                System.out.println("Name: " + resultSet.getString("Name"));
                System.out.println("Phone Number: " + resultSet.getString("PhoneNumber"));
                System.out.println("Email: " + resultSet.getString("Email"));
                System.out.println("Address: " + resultSet.getString("Address"));
            } else {
                System.out.println("No abonent found with ID " + id);
            }
        } catch (SQLException e) {
            System.out.println("Error occurred while trying to search the abonent: " + e.getMessage());
        }
    }

    private static void deleteAbonent() {
        System.out.println("Enter the ID of the abonent you want to delete:");
        Scanner scanner = new Scanner(System.in);
        int id = Integer.parseInt(scanner.nextLine());
        String deleteSQL = "DELETE FROM Phonebook WHERE ID = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL)) {
            // Установка значення ID у запит
            preparedStatement.setInt(1, id);

            // Виконання запиту
            int rowsAffected = preparedStatement.executeUpdate();

            // Перевірка результату виконання
            if (rowsAffected > 0) {
                System.out.println("Abonent with ID " + id + " was successfully deleted.");
            } else {
                System.out.println("No abonent found with ID " + id);
            }
        } catch (SQLException e) {
            System.out.println("Error occurred while trying to delete the abonent: " + e.getMessage());
        }
    }

    private static void createTable() throws SQLException {
        try {
            // SQL-запит для створення таблиці
            String createTableSQL = "CREATE TABLE IF NOT EXISTS Phonebook (" +
                    "ID SERIAL PRIMARY KEY, " +
                    "Name VARCHAR(100) NOT NULL, " +
                    "PhoneNumber VARCHAR(15) NOT NULL, " +
                    "Email VARCHAR(100), " +
                    "Address VARCHAR(255)" +
                    ");";
            Statement command = connection.createStatement();

            // Виконання SQL-запиту на створення таблиці
            command.execute(createTableSQL);
            System.out.println("Успішно створено таблицю Phonebook :)");
        } catch (SQLException e) {
            System.out.println("Error with connecting to db: " + e.getMessage());
        }
    }

    private static void addAbonent() {
        try (Scanner scanner = new Scanner(System.in)) {
            Abonent abonent = new Abonent();

            System.out.print("Enter name: ");
            abonent.setName(scanner.nextLine());

            System.out.print("Enter phone number: ");
            abonent.setPhoneNumber(scanner.nextLine());

            System.out.print("Enter address: ");
            abonent.setAddress(scanner.nextLine());

            System.out.print("Enter email: ");
            abonent.setEmail(scanner.nextLine());

            abonent.saveToDatabase(connection);
        } catch (Exception e) {
            System.out.println("Something went wrong... " + e.getMessage());
        }
    }
}