package org.openehealth.ipf.commons.ihe.fhir.translation

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormatter
import org.joda.time.format.ISODateTimeFormat
import org.openehealth.ipf.commons.core.URN
import org.openehealth.ipf.modules.hl7.dsl.Repeatable

/**
 *
 */
class Utils {

    private static final DateTimeFormatter TIME_FORMAT = ISODateTimeFormat.basicDateTimeNoMillis()

    /**
     * Returns the next repetition of the given HL7 v2 field/segment/etc.
     */
    static def nextRepetition(Repeatable closure) {
        return closure(closure().size())
    }

    /**
     * Returns current timestamp in the format prescribed by HL7.
     */
    static String hl7Timestamp() {
        return TIME_FORMAT.print(new DateTime())[0..7, 9..14]
    }

}
