/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.commons.ihe.fhir.iti78

import ca.uhn.fhir.rest.param.*
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException
import ca.uhn.hl7v2.HapiContext
import org.apache.commons.lang3.Validate
import org.apache.commons.lang3.time.FastDateFormat
import org.hl7.fhir.instance.model.Enumerations
import org.hl7.fhir.instance.model.IdType
import org.hl7.fhir.instance.model.Patient
import org.hl7.fhir.instance.model.api.IBaseResource
import org.openehealth.ipf.commons.ihe.fhir.Constants
import org.openehealth.ipf.commons.ihe.fhir.Utils
import org.openehealth.ipf.commons.ihe.fhir.translation.TranslatorFhirToHL7v2
import org.openehealth.ipf.commons.ihe.fhir.translation.UriMapper
import org.openehealth.ipf.commons.ihe.hl7v2.definitions.CustomModelClassUtils
import org.openehealth.ipf.commons.ihe.hl7v2.definitions.HapiContextFactory
import org.openehealth.ipf.commons.ihe.hl7v2.definitions.pdq.v25.message.QBP_Q21
import org.openehealth.ipf.gazelle.validation.profile.pixpdq.PixPdqTransactions
import org.openehealth.ipf.modules.hl7.dsl.Repeatable
import org.openehealth.ipf.modules.hl7.message.MessageUtils

/**
 * Translates a {@link IBaseResource} into a HL7v2 PDQ Query message. For the time being, paging is left in
 * the responsibility of the underlying FHIR framework, so that the PDQ v2 message does not contain any
 * paging parameters.
 *
 * @since 3.1
 */
class PdqmRequestToPdqQueryTranslator implements TranslatorFhirToHL7v2 {

    static final String SEARCH_TAG = "search"
    static final String GET_TAG = "get"

    /**
     * Predefined fix value of QPD-1 (as String)
     */
    String queryName = 'IHE PDQ Query'
    String senderDeviceName = 'unknown'
    String senderFacilityName = 'unknown'
    String receiverDeviceName = 'unknown'
    String receiverFacilityName = 'unknown'

    String pdqSupplierResourceIdentifierUri

    private final UriMapper uriMapper;

    private static final HapiContext PDQ_QUERY_CONTEXT = HapiContextFactory.createHapiContext(
            CustomModelClassUtils.createFactory("pdq", "2.5"),
            PixPdqTransactions.ITI21)

    /**
     * @param uriMapper mapping for translating FHIR URIs into OIDs
     */
    PdqmRequestToPdqQueryTranslator(UriMapper uriMapper) {
        Validate.notNull(uriMapper, "URI Mapper must not be null")
        this.uriMapper = uriMapper
    }

    /**
     * Translate PDQm query map into a PDQ QBP^Q22 request message
     *
     * @param request object, only used for READing a dedicated resource
     * @param parameters query parameters, only used for SEARCHing patients
     * @return QBP^Q22 request message
     */
    @Override
    QBP_Q21 translateFhirToHL7v2(Object request, Map<String, Object> parameters) {
        if (request == null && parameters != null && !parameters.isEmpty()) {
            return translateFhirSearchToHL7v2(SEARCH_TAG, parameters);
        } else {
            return translateFhirReadToHL7v2(GET_TAG, (IdType) request);
        }
    }

    /**
     * Translate reading a resource into a PDQ search using the {@link #pdqSupplierResourceIdentifierUri} as
     * system for the resource identifier.
     *
     * @param queryTagPrefix prefix for the query tag to identify the type of query (search vs. get)
     * @param resourceId
     * @return QBP^Q22 request message
     */
    protected QBP_Q21 translateFhirReadToHL7v2(String queryTagPrefix, IdType resourceId) {
        Map<String, Object> parameters = [
                (Constants.FHIR_REQUEST_PARAMETERS): [
                        (Constants.SP_RESOURCE_IDENTIFIER): new TokenParam(null, resourceId.value)
                ]
        ]
        translateFhirSearchToHL7v2(queryTagPrefix, parameters)
    }

    /**
     * Translate PDQm query map into a PDQ QBP^Q22 request message
     *
     * @param queryTagPrefix prefix for the query tag to identify the type of query (search vs. get)
     * @param parameters query parameters
     * @return QBP^Q22 request message
     */
    protected QBP_Q21 translateFhirSearchToHL7v2(String queryTagPrefix, Map<String, Object> parameters) {
        QBP_Q21 qry = MessageUtils.makeMessage(PDQ_QUERY_CONTEXT, 'QBP', 'Q22', '2.5')

        qry.MSH[3] = senderDeviceName
        qry.MSH[4] = senderFacilityName
        qry.MSH[5] = receiverDeviceName
        qry.MSH[6] = receiverFacilityName

        qry.MSH[7] = Utils.hl7Timestamp()
        qry.MSH[10] = UUID.randomUUID().toString()

        qry.QPD[1] = this.queryName
        qry.QPD[2] = "${queryTagPrefix}_" + UUID.randomUUID().toString()

        Map<String, Object> map = parameters.get(Constants.FHIR_REQUEST_PARAMETERS);
        if (!map) throw new InvalidRequestException("No request parameters found")

        // Handle identifiers
        List<String> requestedDomains
        String identifierNamespace, identifierOid, identifierValue

        TokenParam resourceIdParam = map.get(Constants.SP_RESOURCE_IDENTIFIER)
        if (resourceIdParam) {
            resourceIdParam.system = pdqSupplierResourceIdentifierUri
            (identifierNamespace, identifierOid, identifierValue) = searchToken(resourceIdParam)
        }

        TokenAndListParam identifierParam = map.get(Patient.SP_IDENTIFIER)
        if (identifierParam) {
            List<List<String>> identifiers = searchTokenList(identifierParam)
            // Patient identifier has identifier value
            (identifierNamespace, identifierOid, identifierValue) = identifiers?.find { it[2] }
            // Requested domains have no identifier value
            requestedDomains = identifiers?.findAll { !it[2] }.collect { it[1] }
        }

        // Properly convert birth date.
        DateParam birthDateParam = map.get(Patient.SP_BIRTHDATE)
        String birthDateString = birthDateParam ? FastDateFormat.getInstance('yyyyMMdd').format(birthDateParam.getValue()) : null

        // Properly convert gender code
        TokenParam genderParam = map.get(Patient.SP_GENDER)
        String genderString = genderParam ?
                Enumerations.AdministrativeGender.fromCode(genderParam.value).toCode().mapReverse('hl7v2fhir-patient-administrativeGender') :
                null

        Map<String, Object> searchParams = [
                // PDQ only allows
                '@PID.3.1'  : identifierValue,
                '@PID.3.4.1': identifierNamespace,
                '@PID.3.4.2': identifierOid,
                '@PID.3.4.3': identifierOid ? 'ISO' : null,
                '@PID.5.1'  : firstOrNull(searchStringList(map.get(Patient.SP_FAMILY), false)),
                '@PID.5.2'  : firstOrNull(searchStringList(map.get(Patient.SP_GIVEN), false)),
                '@PID.7'    : birthDateString,
                '@PID.8'    : genderString,
                '@PID.11.1' : searchString(map.get(Patient.SP_ADDRESS), true),
                '@PID.11.3' : searchString(map.get(Patient.SP_ADDRESSCITY), true),
                '@PID.11.4' : searchString(map.get(Patient.SP_ADDRESSSTATE), true),
                '@PID.11.5' : searchString(map.get(Patient.SP_ADDRESSPOSTALCODE), true),
                '@PID.11.6' : searchString(map.get(Patient.SP_ADDRESSCOUNTRY), true),

                '@PID.6.1'  : firstOrNull(searchStringList(map.get(Constants.SP_MOTHERS_MAIDEN_NAME_FAMILY), false)),
                '@PID.6.2'  : firstOrNull(searchStringList(map.get(Constants.SP_MOTHERS_MAIDEN_NAME_GIVEN), false)),
                '@PID.13.1' : searchString(map.get(Patient.SP_TELECOM), true),
                '@PID.25'   : searchNumber(map.get(Constants.SP_MULTIPLE_BIRTH_ORDER_NUMBER))
        ]

        fillSearchParameters(searchParams, qry.QPD[3])

        requestedDomains?.each {
            Utils.populateIdentifier(Utils.nextRepetition(qry.QPD[8]), it)
        }

        qry.RCP[1] = 'I'
        return qry
    }

    // FIXME: exact search does not work yet
    private String searchString(StringParam param, boolean forceExactSearch) {
        if (param == null || param.empty) return null;
        forceExactSearch || param.exact ? param.value : param.value + "*"
    }

    private List<String> searchStringList(StringAndListParam param, boolean forceExactSearch) {
        param?.getValuesAsQueryTokens().collect { searchString(it.valuesAsQueryTokens.find(), forceExactSearch) }
    }

    private String searchNumber(NumberParam param) {
        if (param == null) return null;
        param?.value.toString()
    }

    private List<String> searchToken(TokenParam identifierParam) {
        String namespace, oid
        if (identifierParam) {
            namespace = uriMapper.uriToNamespace(identifierParam.system)
            oid = uriMapper.uriToOid(identifierParam.system)
            if (!(namespace || oid)) {
                throw identifierParam.value ? Utils.unknownPatientDomain() : Utils.unknownTargetDomain()
            }
        }
        [namespace, oid, identifierParam?.value]
    }

    private List<List<String>> searchTokenList(TokenAndListParam param) {
        param?.getValuesAsQueryTokens().collect { searchToken(it?.valuesAsQueryTokens?.find()) }
    }

    private def firstOrNull(List<?> list) {
        list?.empty ? null : list[0]
    }

    /**
     * Creates in the given target HL7 v2 data structure a set of repeatable fields
     * which correspond to the items of the given source map.
     * <p>
     * E.g. the source is <code>['abc' : '123', 'cde' : '456']</code> and the
     * target is <code>msg.QPD[3]</code>.  A call to this function will lead to
     * <code>QPD|...|...|abc^123~cde^456|...</code>.
     */
    static void fillSearchParameters(Map<String, Object> parameters, Repeatable target) {
        parameters
                .findAll { it.value }
                .each {
            def field = Utils.nextRepetition(target)
            field[1].value = it.key
            field[2].value = it.value
        }

/*
        for (parameter in parameters.findAll { it.value }) {
            parameter.value.each {
                def field = Utils.nextRepetition(target)
                field[1].value = parameter.key
                field[2].value = it
            }
        }
*/
    }

}