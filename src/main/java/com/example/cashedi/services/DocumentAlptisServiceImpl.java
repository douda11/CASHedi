package com.example.cashedi.services;

import com.example.cashedi.models.DocumentInfo;
import com.example.cashedi.repositories.ProjetRepository;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class DocumentAlptisServiceImpl implements DocumentAlptisService {

    private final ProjetRepository projetRepository;

    @Autowired
    public DocumentAlptisServiceImpl(ProjetRepository projetRepository) {
        this.projetRepository = projetRepository;
    }

    @Override
    public Optional<List<DocumentInfo>> getDocumentsForProject(String projetId) {
        if (!projetRepository.existsById(projetId)) {
            return Optional.empty();
        }
        List<DocumentInfo> documents = Arrays.asList(
                new DocumentInfo("ETUDE_PERSONNALISEE", "Étude personnalisée", "Étude personnalisée Alptis", "/projets/" + projetId + "/documents/etude-personnalisee"),
                new DocumentInfo("NOTICE", "Notices et IPID", "Notice d'information Alptis", "/documents-statiques?code=NOTICE")
        );
        return Optional.of(documents);
    }

    @Override
    public Optional<byte[]> generateEtudePersonnalisee(String projetId) {
        if (!projetRepository.existsById(projetId)) {
            return Optional.empty();
        }

        try (PDDocument document = new PDDocument(); ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            PDPage page = new PDPage();
            document.addPage(page);

            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            contentStream.beginText();
            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12);
            contentStream.setLeading(14.5f);
            contentStream.newLineAtOffset(25, 750);
            
            String text = "Ceci est une étude personnalisée pour le projet " + projetId;
            contentStream.showText(text);
            contentStream.endText();
            contentStream.close();

            document.save(baos);
            return Optional.of(baos.toByteArray());

        } catch (IOException e) {
            // In a real app, you'd log this error properly.
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public Optional<byte[]> getStaticDocument(String code) {
        try {
            Resource resource = new ClassPathResource("static/documents/" + code + ".pdf");
            if (!resource.exists()) {
                return Optional.empty();
            }
            try (InputStream inputStream = resource.getInputStream()) {
                return Optional.of(inputStream.readAllBytes());
            }
        } catch (IOException e) {
            // In a real app, you'd log this error.
            return Optional.empty();
        }
    }
}
