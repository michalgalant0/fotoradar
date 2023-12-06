package com.example.fotoradar.summaryGenerator;

import com.example.fotoradar.databaseOperations.DatabaseConnection;
import com.example.fotoradar.models.Collection;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType0Font;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SummaryGenerator {
    private final Connection connection;
    private final Collection collection;
    private final PDType0Font font;

    private final PDDocument document;
    private PDPage page;
    private PDPageContentStream contentStream;

    private int lineCount = 0;
    private final DecimalFormat df = new DecimalFormat("0.00");

    public SummaryGenerator(Collection collection) throws SQLException {
        this.collection = collection;
        connection = DatabaseConnection.getInstance().getConnection();
        document = new PDDocument();
        page = new PDPage(PDRectangle.A4);
        try {
            font = PDType0Font.load(document, getClass().getResourceAsStream("/fonts/Helvetica.ttf"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        generateReport();
    }

    private void increaseLineCountByValue(int value) {
        lineCount += value;
        if (lineCount >= 50) {
            try {
                generatePage();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void generatePage() throws IOException {
        document.addPage(new PDPage(PDRectangle.A4));
        if (contentStream != null) {
            contentStream.endText(); // Zakończenie tekstu na starej stronie
            contentStream.close();   // Zamknięcie strumienia na starej stronie
        }

        // Rozpoczęcie tekstu na nowej stronie
        page = document.getPage(document.getNumberOfPages() - 1);
        contentStream = new PDPageContentStream(document, page);
        contentStream.setFont(font, 12);
        contentStream.beginText();
        contentStream.newLineAtOffset(30, 800);
        lineCount = 0;
    }

    private void generateReport() {
        String query = getCollectionQuery();

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            generatePage();

            try {
                while (resultSet.next()) {
                    String collectionTitle = resultSet.getString("collection_title");
                    String collectionStartDate = resultSet.getString("collection_start_date");
                    String collectionFinishDate = resultSet.getString("collection_finish_date");
                    String collectionDescription = resultSet.getString("collection_description");
                    int totalCollectibles = resultSet.getInt("total_collectibles");
                    double collectionSize = resultSet.getDouble("collection_size");

                    contentStream.showText("Podsumowanie kolekcji " + collectionTitle);
                    contentStream.newLineAtOffset(0, -20);
                    contentStream.showText("Data rozpoczęcia: " + collectionStartDate);
                    contentStream.newLineAtOffset(0, -15);
                    contentStream.showText("Data zakończenia: " + collectionFinishDate);
                    contentStream.newLineAtOffset(0, -15);
                    contentStream.showText("Opis: " + collectionDescription);
                    contentStream.newLineAtOffset(0, -15);
                    contentStream.showText("Ilość obiektów: " + totalCollectibles);
                    contentStream.newLineAtOffset(0, -15);
                    contentStream.showText("Rozmiar kolekcji: " + df.format(collectionSize) + " MB");
                    contentStream.newLineAtOffset(0, -20);

                    increaseLineCountByValue(6);

                    generateCollectiblesInfo(collection.getId());
                }

                contentStream.endText();
                contentStream.close();
            } catch (IOException | SQLException e) {
                throw new RuntimeException(e);
            }

            String fileName = Paths.get(
                    "RAPORTY",
                    String.format(
                            "raport_%s_%s.pdf",
                            collection.getTitle(),
                            new SimpleDateFormat("dd-MM-yyyy").format(new Date())
                    )
            ).toString();
            try (FileOutputStream fileOutputStream = new FileOutputStream(fileName)) {
                document.save(fileOutputStream);
            }

            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void generateCollectiblesInfo(int collectionId) throws SQLException {
        String collectiblesQuery = getCollectiblesQuery(collectionId);

        try (Statement collectiblesStatement = connection.createStatement();
             ResultSet collectiblesResultSet = collectiblesStatement.executeQuery(collectiblesQuery)) {

            while (collectiblesResultSet.next()) {
                int collectibleId = collectiblesResultSet.getInt("collectible_id");
                String collectibleTitle = collectiblesResultSet.getString("collectible_title");
                String collectibleStartDate = collectiblesResultSet.getString("collectible_start_date");
                String collectibleFinishDate = collectiblesResultSet.getString("collectible_finish_date");
                String collectibleDescription = collectiblesResultSet.getString("collectible_description");
                int totalSegments = collectiblesResultSet.getInt("total_segments");
                double collectibleSize = collectiblesResultSet.getDouble("collectible_size");

                contentStream.showText("    Obiekt: " + collectibleTitle);
                contentStream.newLineAtOffset(0, -15);
                contentStream.showText("    Data rozpoczęcia: " + collectibleStartDate);
                contentStream.newLineAtOffset(0, -12);
                contentStream.showText("    Data zakończenia: " + collectibleFinishDate);
                contentStream.newLineAtOffset(0, -12);
                contentStream.showText("    Opis: " + collectibleDescription);
                contentStream.newLineAtOffset(0, -12);
                contentStream.showText("    Ilość segmentów: " + totalSegments);
                contentStream.newLineAtOffset(0, -12);
                contentStream.showText("    Waga obiektu: " + df.format(collectibleSize) + " MB");
                contentStream.newLineAtOffset(0, -15);

                increaseLineCountByValue(6);

                generateSegmentsInfo(collectibleId);

                contentStream.newLineAtOffset(0, -15);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void generateSegmentsInfo(int collectibleId) throws SQLException {
        String segmentsQuery = getSegmentsQuery(collectibleId);

        try (Statement segmentsStatement = connection.createStatement();
             ResultSet segmentsResultSet = segmentsStatement.executeQuery(segmentsQuery)) {

            while (segmentsResultSet.next()) {
                int segmentId = segmentsResultSet.getInt("segment_id");
                String segmentTitle = segmentsResultSet.getString("segment_title");
                String segmentStartDate = segmentsResultSet.getString("segment_start_date");
                String segmentFinishDate = segmentsResultSet.getString("segment_finish_date");
                int totalVersions = segmentsResultSet.getInt("total_versions");
                double segmentSize = segmentsResultSet.getDouble("segment_size");

                contentStream.showText("        Segment: " + segmentTitle);
                contentStream.newLineAtOffset(0, -15);
                contentStream.showText("        Data rozpoczęcia: " + segmentStartDate);
                contentStream.newLineAtOffset(0, -12);
                contentStream.showText("        Data zakończenia: " + segmentFinishDate);
                contentStream.newLineAtOffset(0, -12);
                contentStream.showText("        Ilość wersji: " + totalVersions);
                contentStream.newLineAtOffset(0, -12);
                contentStream.showText("        Waga segmentu: " + df.format(segmentSize) + " MB");
                contentStream.newLineAtOffset(0, -15);

                increaseLineCountByValue(5);

                generateVersionsInfo(segmentId);

                contentStream.newLineAtOffset(-0, -15);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void generateVersionsInfo(int segmentId) throws SQLException {
        String versionsQuery = getVersionsQuery(segmentId);

        try (Statement versionsStatement = connection.createStatement();
             ResultSet versionsResultSet = versionsStatement.executeQuery(versionsQuery)) {

            while (versionsResultSet.next()) {
                int versionId = versionsResultSet.getInt("version_id");
                String versionName = versionsResultSet.getString("version_name");
                String startDateTime = versionsResultSet.getString("start_datetime");
                String finishDateTime = versionsResultSet.getString("finish_datetime");
                int totalPhotos = versionsResultSet.getInt("total_photos");
                double versionSize = versionsResultSet.getDouble("version_size");

                contentStream.showText("            Wersja: " + versionName);
                contentStream.newLineAtOffset(0, -15);
                contentStream.showText("            Data rozpoczęcia: " + startDateTime);
                contentStream.newLineAtOffset(0, -12);
                contentStream.showText("            Data zakończenia: " + finishDateTime);
                contentStream.newLineAtOffset(0, -12);
                contentStream.showText("            Ilość zdjęć: " + totalPhotos);
                contentStream.newLineAtOffset(0, -12);
                contentStream.showText("            Waga wersji: " + df.format(versionSize) + " MB");
                contentStream.newLineAtOffset(0, -15);

                increaseLineCountByValue(5);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String getCollectionQuery() {
        String collectionQuery = """
                SELECT
                    c.title AS collection_title,
                    c.start_date AS collection_start_date,
                    c.finish_date AS collection_finish_date,
                    c.description AS collection_description,
                    COUNT(DISTINCT cl.collectible_id) AS total_collectibles,
                    SUM(p.file_size) AS collection_size
                FROM
                    COLLECTION c
                    LEFT JOIN COLLECTIBLE cl ON c.collection_id = cl.collection_id
                    LEFT JOIN SEGMENT s ON cl.collectible_id = s.collectible_id
                    LEFT JOIN VERSION v ON s.segment_id = v.segment_id
                    LEFT JOIN PHOTO p ON v.version_id = p.version_id
                WHERE
                    c.collection_id = %d
                """;
        collectionQuery = String.format(collectionQuery, collection.getId());
        return collectionQuery;
    }

    private String getCollectiblesQuery(int collectionId) {
        String collectiblesQuery = """
                SELECT
                    cl.collectible_id,
                    cl.title AS collectible_title,
                    cl.start_date AS collectible_start_date,
                    cl.finish_date AS collectible_finish_date,
                    cl.description AS collectible_description,
                    COUNT(DISTINCT s.segment_id) AS total_segments,
                    SUM(p.file_size) AS collectible_size
                FROM
                    COLLECTIBLE cl
                    LEFT JOIN SEGMENT s ON cl.collectible_id = s.collectible_id
                    LEFT JOIN VERSION v ON s.segment_id = v.segment_id
                    LEFT JOIN PHOTO p ON v.version_id = p.version_id
                WHERE
                    cl.collection_id = %d
                GROUP BY
                    cl.collectible_id, cl.title, cl.start_date, cl.finish_date, cl.description
                """;

        collectiblesQuery = String.format(collectiblesQuery, collectionId);
        return collectiblesQuery;
    }

    private String getSegmentsQuery(int collectibleId) {
        String segmentsQuery = """
                SELECT
                    s.segment_id,
                    s.title AS segment_title,
                    s.start_datetime AS segment_start_date,
                    s.finish_datetime AS segment_finish_date,
                    COUNT(DISTINCT v.version_id) AS total_versions,
                    SUM(p.file_size) AS segment_size
                FROM
                    SEGMENT s
                    LEFT JOIN VERSION v ON s.segment_id = v.segment_id
                    LEFT JOIN PHOTO p ON v.version_id = p.version_id
                WHERE
                    s.collectible_id = %d
                GROUP BY
                    s.segment_id
                """;

        segmentsQuery = String.format(segmentsQuery, collectibleId);
        return segmentsQuery;
    }

    private String getVersionsQuery(int segmentId) {
        String versionsQuery = """
                SELECT
                    v.version_id,
                    v.name AS version_name,
                    v.start_datetime,
                    v.finish_datetime,
                    COUNT(DISTINCT p.photo_id) AS total_photos,
                    SUM(p.file_size) AS version_size
                FROM
                    VERSION v
                    LEFT JOIN PHOTO p ON v.version_id = p.version_id
                WHERE
                    v.segment_id = %d
                GROUP BY
                    v.version_id
                """;

        versionsQuery = String.format(versionsQuery, segmentId);
        return versionsQuery;
    }
}