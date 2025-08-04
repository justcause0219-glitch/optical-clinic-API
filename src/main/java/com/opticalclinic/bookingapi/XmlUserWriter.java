package com.opticalclinic.bookingapi;

import java.io.File;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class XmlUserWriter {

    public static void writeUsersToXml(List<User> users, String filePath) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();

            // Root element
            Element rootElement = doc.createElement("Users");
            doc.appendChild(rootElement);

            for (User user : users) {
                Element userElement = doc.createElement("User");
                rootElement.appendChild(userElement);

                Element username = doc.createElement("Username");
                username.appendChild(doc.createTextNode(user.getUsername()));
                userElement.appendChild(username);

                Element password = doc.createElement("Password");
                password.appendChild(doc.createTextNode(user.getPassword()));
                userElement.appendChild(password);

                Element fullName = doc.createElement("FullName");
                fullName.appendChild(doc.createTextNode(user.getFullName()));
                userElement.appendChild(fullName);

                Element phoneNumber = doc.createElement("PhoneNumber");
                phoneNumber.appendChild(doc.createTextNode(user.getPhoneNumber()));
                userElement.appendChild(phoneNumber);

                Element birthdate = doc.createElement("Birthdate");
                birthdate.appendChild(doc.createTextNode(user.getBirthdate()));
                userElement.appendChild(birthdate);
            }

            // Write to XML file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(filePath));
            transformer.transform(source, result);

            System.out.println("User data saved to XML: " + filePath);

        } catch (ParserConfigurationException | TransformerException e) {
            e.printStackTrace();
        }
    }
}
