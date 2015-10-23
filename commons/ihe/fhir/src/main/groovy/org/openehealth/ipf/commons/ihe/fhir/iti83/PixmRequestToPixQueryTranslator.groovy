package org.openehealth.ipf.commons.ihe.fhir.translation

import ca.uhn.fhir.model.api.IResource
import ca.uhn.hl7v2.HapiContext
import org.openehealth.ipf.commons.ihe.hl7v2.definitions.CustomModelClassUtils
import org.openehealth.ipf.commons.ihe.hl7v2.definitions.HapiContextFactory
import org.openehealth.ipf.commons.ihe.hl7v2.definitions.pix.v25.message.QBP_Q21
import org.openehealth.ipf.modules.hl7.message.MessageUtils

class PixmRequestToPixQueryTranslator implements TranslatorFhirToHL7v2<QBP_Q21> {

    /**
     * Predefined fix value of QPD-1 (as String)
     */
    String queryName = 'IHE PIX Query'

    private static final HapiContext PIX_QUERY_CONTEXT = HapiContextFactory.createHapiContext(
            CustomModelClassUtils.createFactory("pix", "2.5"),
            PixPdqTransactions.ITI9)

    @Override
    QBP_Q21 translateFhirToHL7v2(Map<String, IResource> resources) {
        QBP_Q21 qry = MessageUtils.makeMessage(PIX_QUERY_CONTEXT, 'QBP', 'Q23', '2.5')

        // Segment MSH
        fillMshFromSlurper(xml, qry, this.useSenderDeviceName, this.useReceiverDeviceName)

        // Segment QPD
        def queryByParameter = xml.controlActProcess.queryByParameter
        def params = queryByParameter.parameterList
        qry.QPD[1] = this.queryName
        qry.QPD[2] = idString(queryByParameter.queryId)

        fillCx(qry.QPD[3], params.patientIdentifier[0].value[0])
        for (source in params.dataSource) {
            def cx = nextRepetition(qry.QPD[4])
            cx[4][1] = source.value.@assigningAuthorityName.text()
            cx[4][2] = source.value.@root.text()
            cx[4][3] = 'ISO'
        }

        // Segment RCP
        qry.RCP[1] = 'I'

        return qry

    }

}