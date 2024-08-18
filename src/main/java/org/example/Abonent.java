package org.example;

import lombok.Data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Data
public class Abonent {
    private int id;
    private String name;
    private String phoneNumber;
    private String email;
    private String address;

    public void setName(String name) {
        this.name = name;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public void saveToDatabase(Connection connection) throws SQLException {
        String insertSQL = "INSERT INTO Phonebook (Name, PhoneNumber, Email, Address) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
            preparedStatement.setString(1, this.name);
            preparedStatement.setString(2, this.phoneNumber);
            preparedStatement.setString(3, this.email);
            preparedStatement.setString(4, this.address);
            preparedStatement.executeUpdate();
            System.out.println("Abonent saved to database successfully.");
        }
    }
}
