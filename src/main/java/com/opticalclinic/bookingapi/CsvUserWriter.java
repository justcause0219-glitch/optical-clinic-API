package com.opticalclinic.bookingapi;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class CsvUserWriter {
   
     public static void writeUserToCsv(User user, String filePath) {
        File file = new File(filePath);
        boolean fileExists = file.exists();

        try (PrintWriter writer = new PrintWriter(new FileWriter(file, true))) {
            if (!fileExists) {
                // Write header if file doesn't exist
                writer.println("Username,Password,FullName,PhoneNumber,Birthdate");
            }

            writer.println(String.join(",", user.toCsvRow()));
            System.out.println("User appended to " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void writeUsersToCsv(List<User> users, String filePath) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            writer.println("Username,Password,FullName,PhoneNumber,Birthdate");

            for (User user : users) {
                writer.println(String.join(",", user.toCsvRow()));
            }

            System.out.println("User data saved to " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

