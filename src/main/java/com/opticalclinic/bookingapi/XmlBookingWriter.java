package com.opticalclinic.bookingapi;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class XmlBookingWriter {

    public static void writeBookingsToXml(List<Booking> bookings, String filePath) {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            writer.write("<bookings>\n");

            for (Booking booking : bookings) {
                writer.write("  <booking>\n");
                writer.write("    <fullName>" + booking.getFullName() + "</fullName>\n");
                writer.write("    <age>" + booking.getAge() + "</age>\n");
                writer.write("    <contact>" + booking.getContact() + "</contact>\n");
                writer.write("    <doctor>" + booking.getDoctor() + "</doctor>\n");
                writer.write("    <time>" + booking.getTime() + "</time>\n");
                writer.write("    <date>" + booking.getDate() + "</date>\n");
                writer.write("  </booking>\n");
            }

            writer.write("</bookings>");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
