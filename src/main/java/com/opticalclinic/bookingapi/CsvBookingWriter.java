package com.opticalclinic.bookingapi;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CsvBookingWriter {

    public static void writeBookingsToCsv(List<Booking> bookings, String filePath) {
        try (FileWriter writer = new FileWriter(filePath, true)) {
            for (Booking booking : bookings) {
                writer.append(booking.getFullName()).append(",")
                      .append(booking.getAge()).append(",")
                      .append(booking.getContact()).append(",")
                      .append(booking.getDoctor()).append(",")
                      .append(booking.getTime()).append(",")
                      .append(booking.getDate()).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
