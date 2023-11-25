package com.example.fotoradar.summaryGenerator;

import com.example.fotoradar.databaseOperations.DatabaseConnection;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType0Font;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SummaryGenerator {
    private final Connection connection;
    public SummaryGenerator() throws SQLException {
        connection = DatabaseConnection.getInstance().getConnection();
        generateReport();
    }

    private void generateReport() {
        String query = """
                SELECT
                	c.title AS collection_title, COUNT(p.photo_id) AS total_photos,
                	SUM(p.file_size) AS collection_size
                FROM
                	COLLECTION c
                	LEFT JOIN COLLECTIBLE cl ON c.collection_id = cl.collection_id
                	LEFT JOIN SEGMENT s ON cl.collectible_id = s.collectible_id
                	LEFT JOIN VERSION v ON s.segment_id = v.segment_id
                	LEFT JOIN PHOTO p ON v.version_id = p.version_id
                GROUP BY
                	c.title
                """;

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            // Tworzenie dokumentu PDF
            PDDocument document = new PDDocument();
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                PDType0Font font = PDType0Font.load(document, getClass().getResourceAsStream("/fonts/Roboto-Regular.ttf"));
                contentStream.setFont(font, 12);
                contentStream.beginText();
                contentStream.newLineAtOffset(20, 700);

                // Dodawanie nagłówka
                contentStream.showText("Podsumowanie kolekcji");
                contentStream.newLineAtOffset(0, -20);
                contentStream.showText("------------------");
                contentStream.newLineAtOffset(0, -20);

                // Dodawanie danych do raportu
                while (resultSet.next()) {
                    String collectionTitle = resultSet.getString("collection_title");
                    int totalPhotos = resultSet.getInt("total_photos");
                    double collectionSize = resultSet.getDouble("collection_size");

                    contentStream.showText("Kolekcja: " + collectionTitle);
                    contentStream.newLineAtOffset(0, -15);
                    contentStream.showText("Ilość zdjęć: " + totalPhotos);
                    contentStream.newLineAtOffset(0, -15);
                    contentStream.showText("Rozmiar kolekcji: " + collectionSize);
                    contentStream.newLineAtOffset(0, -15);
                    contentStream.showText("------------------");
                    contentStream.newLineAtOffset(0, -15);
                }

                contentStream.endText();
            }

            // Zapisywanie do pliku PDF
            try (FileOutputStream fileOutputStream = new FileOutputStream("CollectionReport.pdf")) {
                document.save(fileOutputStream);
            }

            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
