package org.openehealth.ipf.commons.audit.unmarshal.dicom;

import org.custommonkey.xmlunit.DetailedDiff;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Test;
import org.openehealth.ipf.commons.audit.marshal.dicom.Current;
import org.openehealth.ipf.commons.audit.model.AuditMessage;
import org.openehealth.ipf.commons.audit.unmarshal.AuditParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

public class DICOMAuditParserTest {

    private static final Logger LOG  = LoggerFactory.getLogger(DICOMAuditParser.class);
    @Test
    public void roundtrip() {

        XMLUnit.setCompareUnmatched(true);
        XMLUnit.setIgnoreAttributeOrder(true);
        XMLUnit.setIgnoreComments(true);
        XMLUnit.setIgnoreWhitespace(true);
                
        AuditParser parser = new DICOMAuditParser();
        try {
            List<Path> files = getAllFilesFromResource("audit");
            for (Path file : files) {
                LOG.debug("Parsing {}", file);
                String read = Files.readString(file, StandardCharsets.UTF_8);
                AuditMessage auditMessage = parser.parse(read, true);
                String written = Current.INSTANCE.marshal(auditMessage, true);
                Diff diff = new Diff(read, written);
                DetailedDiff detDiff = new DetailedDiff(diff);
                assertEquals(0, detDiff.getAllDifferences().size());
            }
        } catch (URISyntaxException | IOException | SAXException e) {
            e.printStackTrace();
        }
    }

    private List<Path> getAllFilesFromResource(String folder) throws URISyntaxException, IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource(folder);
        List<Path> collect = Files.walk(Paths.get(resource.toURI()))
                .filter(Files::isRegularFile)
                .collect(Collectors.toList());

        return collect;
    }
}
