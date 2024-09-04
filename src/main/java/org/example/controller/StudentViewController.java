package org.example.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.example.model.StudentModel;
import org.example.db.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentViewController {

    @FXML
    private TextField studentIdField;

    @FXML
    private TextField nameField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField courseField;

    @FXML
    private Button saveButton;

    @FXML
    private Button updateButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Label messageLabel;

    private Connection connection;

    public StudentViewController() {
        this.connection = DBConnection.getInstance().getConnection();
    }

    @FXML
    private void initialize() {
        // Any initialization code can go here
    }

    @FXML
    private void handleSave() {
        String name = nameField.getText();
        String email = emailField.getText();
        String course = courseField.getText();

        String query = "INSERT INTO students (name, email, course) VALUES (?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setString(3, course);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                messageLabel.setText("Student saved successfully!");
            } else {
                messageLabel.setText("Error saving student.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            messageLabel.setText("SQL Error: " + e.getMessage());
        }
    }

    @FXML
    private void handleUpdate() {
        try {
            int studentId = Integer.parseInt(studentIdField.getText());
            StudentModel student = getStudent(studentId);

            if (student != null) {
                student.setName(nameField.getText());
                student.setEmail(emailField.getText());
                student.setCourse(courseField.getText());

                if (updateStudent(student)) {
                    messageLabel.setText("Student updated successfully!");
                } else {
                    messageLabel.setText("Error updating student.");
                }
            } else {
                messageLabel.setText("Student not found.");
            }
        } catch (NumberFormatException e) {
            messageLabel.setText("Invalid Student ID.");
        }
    }

    @FXML
    private void handleDelete() {
        try {
            int studentId = Integer.parseInt(studentIdField.getText());
            if (deleteStudent(studentId)) {
                messageLabel.setText("Student deleted successfully!");
                clearFields();
            } else {
                messageLabel.setText("Error deleting student.");
            }
        } catch (NumberFormatException e) {
            messageLabel.setText("Invalid Student ID.");
        }
    }

    private StudentModel getStudent(int studentId) {
        String query = "SELECT * FROM students WHERE studentID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, studentId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new StudentModel(
                        rs.getInt("studentID"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("course")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean updateStudent(StudentModel student) {
        String query = "UPDATE students SET name = ?, email = ?, course = ? WHERE studentID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, student.getName());
            pstmt.setString(2, student.getEmail());
            pstmt.setString(3, student.getCourse());
            pstmt.setInt(4, student.getStudentID());
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean deleteStudent(int studentId) {
        String query = "DELETE FROM students WHERE studentID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, studentId);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void clearFields() {
        studentIdField.clear();
        nameField.clear();
        emailField.clear();
        courseField.clear();
    }
}
