package org.openehealth.ipf.commons.audit.unmarshal.dicom;

import org.junit.jupiter.api.Test;
import org.openehealth.ipf.commons.audit.marshal.dicom.Current;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.builder.Input;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class DICOMAuditParserTest {

    private static final Logger log = LoggerFactory.getLogger(DICOMAuditParserTest.class);

    @Test
    public void roundtrip() throws IOException, URISyntaxException {

        var parser = new DICOMAuditParser();
        var files = getAllFilesFromResource("audit");
        for (var file : files) {
            log.debug("Parsing {}", file);
            var read = Files.readString(file, StandardCharsets.UTF_8);
            var auditMessage = parser.parse(read, true);
            var written = Current.INSTANCE.marshal(auditMessage, true);

            var diff = DiffBuilder.compare(Input.fromString(read))
                    .withTest(written)
                    .ignoreComments()
                    .ignoreWhitespace()
                    .checkForSimilar()
                    .build();
            assertFalse(diff.hasDifferences());
        }
    }

    private List<Path> getAllFilesFromResource(String folder) throws URISyntaxException, IOException {
        var classLoader = getClass().getClassLoader();
        var resource = classLoader.getResource(folder);

        return Files.walk(Paths.get(resource.toURI()))
                .filter(Files::isRegularFile)
                .collect(Collectors.toList());
    }
}
