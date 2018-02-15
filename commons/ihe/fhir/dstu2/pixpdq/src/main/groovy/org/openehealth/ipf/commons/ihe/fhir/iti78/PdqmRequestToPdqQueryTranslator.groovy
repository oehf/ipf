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
import ca.uhn.hl7v2.model.Message
import org.apache.commons.lang3.time.FastDateFormat
import org.hl7.fhir.instance.model.Enumerations
import org.hl7.fhir.instance.model.IdType
import org.hl7.fhir.instance.model.api.IBaseResource
import org.openehealth.ipf.commons.ihe.fhir.Constants
import org.openehealth.ipf.commons.ihe.fhir.Utils
import org.openehealth.ipf.commons.ihe.fhir.translation.FhirTranslationException
import org.openehealth.ipf.commons.ihe.fhir.translation.FhirTranslator
import org.openehealth.ipf.commons.ihe.fhir.translation.UnmappableUriException
import org.openehealth.ipf.commons.ihe.fhir.translation.UriMapper
import org.openehealth.ipf.commons.ihe.hl7v2.PDQ
import org.openehealth.ipf.commons.ihe.hl7v2.definitions.pdq.v25.message.QBP_Q21
import org.openehealth.ipf.modules.hl7.dsl.Repeatable

import static java.util.Objects.requireNonNull

/**
 * Translates a {@link IBaseResource} into a HL7v2 PDQ Query message. For the time being, paging is left in
 * the responsibility of the underlying FHIR framework, so that the PDQ v2 message does not contain any
 * paging parameters.
 *
 * Note that this translator implements search and (v)read requests, where the latter just translates into
 * a search using the _id parameter.
 *
 * @author Christian Ohr
 * @since 3.1
 */
class PdqmRequestToPdqQueryTranslator implements FhirTranslator<Message> {

    /**
     * Predefined fix value of QPD-1 (as String)
     */

    String queryName = 'IHE PDQ Query'
    String senderDeviceName = 'unknown'
    String senderFacilityName = 'unknown'
    String receiverDeviceName = 'unknown'
    String receiverFacilityName = 'unknown'

    String pdqSupplierResourceIdentifierUri
    private String pdqSupplierResourceIdentifierOid

    private final UriMapper uriMapper

    /**
     * @param uriMapper mapping for translating FHIR URIs into OIDs
     */
    PdqmRequestToPdqQueryTranslator(UriMapper uriMapper) {
        requireNonNull(uriMapper, "URI Mapper must not be null")
        this.uriMapper = uriMapper
    }

    /**
     * @param pdqSupplierResourceIdentifierUri the URI of the resource identifier system
     */
    void setPdqSupplierResourceIdentifierUri(String pdqSupplierResourceIdentifierUri) {
        requireNonNull(pdqSupplierResourceIdentifierUri, "Resource Identifier URI must not be null")
        this.pdqSupplierResourceIdentifierUri = pdqSupplierResourceIdentifierUri
        this.pdqSupplierResourceIdentifierOid = uriMapper.uriToOid(pdqSupplierResourceIdentifierUri)
            .orElseThrow({new UnmappableUriException(pdqSupplierResourceIdentifierUri)})
    }

    /**
     * Translate PDQm query map into a PDQ QBP^Q22 request message
     *
     * @param request object, only used for READing a dedicated resource
     * @param parameters query parameters, only used for SEARCHing patients
     * @return QBP^Q22 request message
     */
    @Override
    QBP_Q21 translateFhir(Object request, Map<String, Object> parameters) {
        if (request == null && parameters != null && parameters.containsKey(Constants.FHIR_REQUEST_PARAMETERS)) {
            return translateFhirSearchToHL7v2(parameters.get(Constants.FHIR_REQUEST_PARAMETERS))
        } else if (request != null && request instanceof IdType) {
            return translateFhirReadToHL7v2((IdType) request)
        } else {
            throw new FhirTranslationException("Expected either PDQ parameters or an Patient ID")
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
    protected QBP_Q21 translateFhirReadToHL7v2(IdType resourceId) {
        Iti78SearchParameters parameters = Iti78SearchParameters.builder()
                ._id(new TokenParam(pdqSupplierResourceIdentifierUri, resourceId.idPart))
                .build()
        translateFhirSearchToHL7v2(parameters)
    }

    /**
     * Translate PDQm query map into a PDQ QBP^Q22 request message
     *
     * @param queryTagPrefix prefix for the query tag to identify the type of query (search vs. get)
     * @param parameters query parameters
     * @return QBP^Q22 request message
     */
    protected QBP_Q21 translateFhirSearchToHL7v2(Iti78SearchParameters searchParameters) {
        QBP_Q21 qry = PDQ.Interactions.ITI_21.hl7v2TransactionConfiguration.request()

        qry.MSH[3] = senderDeviceName
        qry.MSH[4] = senderFacilityName
        qry.MSH[5] = receiverDeviceName
        qry.MSH[6] = receiverFacilityName

        qry.MSH[7] = Utils.hl7Timestamp()
        qry.MSH[10] = UUID.randomUUID().toString()

        qry.QPD[1] = this.queryName
        qry.QPD[2] = UUID.randomUUID().toString()


        // Handle identifiers
        List<String> requestedDomainOids
        Optional<CompositeIdentifier> searchIdentifier = Optional.empty()

        TokenParam resourceIdParam = searchParameters._id
        if (resourceIdParam) {
            resourceIdParam.system = pdqSupplierResourceIdentifierUri
            searchIdentifier = searchToken(resourceIdParam)
        }

        TokenAndListParam identifierParam = searchParameters.identifiers
        if (identifierParam) {
            List<Optional<CompositeIdentifier>> identifiers = searchTokenList(identifierParam)
            // Patient identifier has identifier value
            searchIdentifier = identifiers?.find {
                it.isPresent() && it.get().hasSystem() && !it.get().id.empty
            }
            if (searchIdentifier == null) searchIdentifier = Optional.empty()

            // Requested domains have no identifier value. If the resource identifier system is not included here,
            // add it because otherwise we don't know the resource ID in the response.
            requestedDomainOids = identifiers?.findAll { it.isPresent() && it.get().hasOnlySystem() }
                    .collect { it.get().oid }
        }

        // If requestedDomains is set but the pdqSupplierResourceIdentifierOid is not part of it, add it.
        // Otherwise no MPI patient ID is returned and we cannot generate a resource ID from it!
        if (requestedDomainOids && (!requestedDomainOids.contains(pdqSupplierResourceIdentifierOid)))
            requestedDomainOids.add(pdqSupplierResourceIdentifierOid)


        // Properly convert gender code
        TokenParam genderParam = searchParameters.gender
        String genderString = genderParam ?
                Enumerations.AdministrativeGender.fromCode(genderParam.value).toCode().mapReverse('hl7v2fhir-patient-administrativeGender') :
                null

        Map<String, Object> searchParams = [
                // PDQ only allows
                '@PID.3.1'  : searchIdentifier.map({ it.id }).orElse(null),
                '@PID.3.4.1': searchIdentifier.map({ it.namespace }).orElse(null),
                '@PID.3.4.2': searchIdentifier.map({ it.oid }).orElse(null),
                '@PID.3.4.3': searchIdentifier.map({ it.oid ? 'ISO' : null }).orElse(null),
                '@PID.5.1'  : firstOrNull(searchStringList(searchParameters.family, false)),
                '@PID.5.2'  : firstOrNull(searchStringList(searchParameters.given, false)),
                '@PID.7'    : convertBirthDate(searchParameters.birthDate),
                '@PID.8'    : genderString,
                '@PID.11.1' : searchString(searchParameters.address, true),
                '@PID.11.3' : searchString(searchParameters.city, true),
                '@PID.11.4' : searchString(searchParameters.state, true),
                '@PID.11.5' : searchString(searchParameters.postalCode, true),
                '@PID.11.6' : searchString(searchParameters.country, true),

                '@PID.6.1'  : firstOrNull(searchStringList(searchParameters.mothersMaidenNameFamily, false)),
                '@PID.6.2'  : firstOrNull(searchStringList(searchParameters.mothersMaidenNameGiven, false)),
                '@PID.13.1' : searchString(searchParameters.telecom, true),
                '@PID.25'   : searchNumber(searchParameters.multipleBirthNumber)
        ]

        fillSearchParameters(searchParams, qry.QPD[3])

        requestedDomainOids?.each {
            Utils.populateIdentifier(Utils.nextRepetition(qry.QPD[8]), it)
        }

        qry.RCP[1] = 'I'
        return qry
    }

    protected String convertBirthDate(DateAndListParam birthDateParam) {
        Date birthDate = firstOrNull(searchDateList(birthDateParam))
        return birthDate ? FastDateFormat.getInstance('yyyyMMdd').format(birthDate) : null
    }

    protected Date searchDate(DateParam param) {
        if (param == null || param.empty) return null
        param.value
    }

    protected String searchString(StringParam param, boolean forceExactSearch) {
        if (param == null || param.empty) return null
        forceExactSearch || param.exact ? param.value : param.value + "*"
    }

    protected List<String> searchStringList(StringAndListParam param, boolean forceExactSearch) {
        param?.getValuesAsQueryTokens().collect { searchString(it.valuesAsQueryTokens.find(), forceExactSearch) }
    }

    protected List<String> searchDateList(DateAndListParam param) {
        param?.getValuesAsQueryTokens().collect { searchDate(it.valuesAsQueryTokens.find()) }
    }

    protected String searchNumber(NumberParam param) {
        if (param == null) return null
        param?.value?.toString()
    }

    protected Optional<CompositeIdentifier> searchToken(TokenParam identifierParam) {
        if (identifierParam) {
            CompositeIdentifier cx = new CompositeIdentifier(
                    id : identifierParam.value,
                    namespace : uriMapper.uriToNamespace(identifierParam.system).orElse(null),
                    oid : uriMapper.uriToOid(identifierParam.system).orElse(null))
            if (!cx.hasSystem()) {
                throw identifierParam.value ?
                        Utils.unknownPatientDomain(identifierParam.system) :
                        Utils.unknownTargetDomainValue(identifierParam.system)
            }
            return Optional.of(cx)
        }
        return Optional.empty()
    }

    protected List<Optional<CompositeIdentifier>> searchTokenList(TokenAndListParam param) {
        param?.getValuesAsQueryTokens().collect { searchToken(it?.valuesAsQueryTokens?.find()) }
    }

    protected def firstOrNull(List<?> list) {
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

    }

    static class CompositeIdentifier {
        String id
        String namespace
        String oid

        boolean hasSystem() {
            namespace || oid
        }

        boolean hasOnlySystem() {
            (namespace || oid) && !id
        }

        boolean hasAll() {
            namespace && oid && id
        }
    }
}