package org.openehealth.ipf.commons.ihe.xacml20.stub.hl7v3;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class EqualityCheckTest {

    @Test
    public void testCeEquality() {
        var ce1 = new CE("code1", "1.2.3.4.5", "codeSystemName1", "displayName1");
        var ce2 = new CE("code1", "1.2.3.4.5", "codeSystemName2", "displayName2");
        var ce3 = new CE("XXXXX", "1.2.3.4.5", "codeSystemName1", "displayName1");
        var ce4 = new CE("code1", "2.3.4.5.6", "codeSystemName1", "displayName1");
        assertEquals(ce1, ce2);
        assertNotEquals(ce1, ce3);
        assertNotEquals(ce1, ce4);
    }

}
