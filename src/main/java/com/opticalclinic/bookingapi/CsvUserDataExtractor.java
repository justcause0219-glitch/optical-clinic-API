package com.opticalclinic.bookingapi;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//import com.opticalclinic.bookingapi.User;

public class CsvUserDataExtractor {

    public static List<User> readUsersFromCsv(String filePath) {
        List<User> users = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isFirstLine = true;

            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false; // Skip header
                    continue;
                }

                String[] parts = line.split(",");

                if (parts.length < 5) continue; // Safety check

                String username = parts[0].trim();
                String password = parts[1].trim();
                String fullName = parts[2].trim();
                String phoneNumber = parts[3].trim();
                String birthdate = parts[4].trim();

                User user = new User(fullName, phoneNumber, username, password, birthdate);
                users.add(user);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return users;
    }
}
