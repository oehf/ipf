/*
 * Copyright 2009 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *     
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.commons.ihe.xds.core.validate.requests;

import org.openehealth.ipf.commons.core.modules.api.Validator;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLAdhocQueryRequest;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.QueryType;
import org.openehealth.ipf.commons.ihe.xds.core.validate.*;
import org.openehealth.ipf.commons.ihe.xds.core.validate.query.*;

import java.util.EnumMap;
import java.util.Map;

import static org.apache.commons.lang.Validate.notNull;
import static org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter.*;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage.*;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidatorAssertions.metaDataAssert;

/**
 * Validates an {@link EbXMLAdhocQueryRequest}.
 * @author Jens Riemschneider
 */
public class AdhocQueryRequestValidator implements Validator<EbXMLAdhocQueryRequest, ValidationProfile> {
    private static final String OBJECT_REF = "ObjectRef";
    private static final String LEAF_CLASS = "LeafClass";
    
    private static final Map<QueryType, QueryParameterValidation[]> validations;
    
    static {
        CXValidator cxValidator = new CXValidator();
        TimeValidator timeValidator = new TimeValidator();
        // OIDValidator oidValidator = new OIDValidator();
        NopValidator nopValidator = new NopValidator();  

        validations = new EnumMap<QueryType, QueryParameterValidation[]>(QueryType.class);
        validations.put(QueryType.FIND_DOCUMENTS, new QueryParameterValidation[] {
                new StringValidation(DOC_ENTRY_PATIENT_ID, cxValidator, false),
                new CodeValidation(DOC_ENTRY_CLASS_CODE),
                new CodeValidation(DOC_ENTRY_TYPE_CODE),
                new CodeValidation(DOC_ENTRY_PRACTICE_SETTING_CODE),
                new CodeValidation(DOC_ENTRY_HEALTHCARE_FACILITY_TYPE_CODE),
                new CodeValidation(DOC_ENTRY_FORMAT_CODE),
                new NumberValidation(DOC_ENTRY_CREATION_TIME_FROM, timeValidator),
                new NumberValidation(DOC_ENTRY_CREATION_TIME_TO, timeValidator),
                new NumberValidation(DOC_ENTRY_SERVICE_START_TIME_FROM, timeValidator),
                new NumberValidation(DOC_ENTRY_SERVICE_START_TIME_TO, timeValidator),
                new NumberValidation(DOC_ENTRY_SERVICE_STOP_TIME_FROM, timeValidator),
                new NumberValidation(DOC_ENTRY_SERVICE_STOP_TIME_TO, timeValidator),
                new QueryListCodeValidation(DOC_ENTRY_EVENT_CODE, DOC_ENTRY_EVENT_CODE_SCHEME),
                new QueryListCodeValidation(DOC_ENTRY_CONFIDENTIALITY_CODE, DOC_ENTRY_CONFIDENTIALITY_CODE_SCHEME),
                new StringListValidation(DOC_ENTRY_AUTHOR_PERSON, nopValidator),
                new StatusValidation(DOC_ENTRY_STATUS),
        });
        
        validations.put(QueryType.FIND_SUBMISSION_SETS, new QueryParameterValidation[] {
                new StringValidation(SUBMISSION_SET_PATIENT_ID, cxValidator, false),
                // Excluded to avoid validation errors for xdstest requests
                // new StringListValidation(SUBMISSION_SET_SOURCE_ID, oidValidator),
                new NumberValidation(SUBMISSION_SET_SUBMISSION_TIME_FROM, timeValidator),
                new NumberValidation(SUBMISSION_SET_SUBMISSION_TIME_TO, timeValidator),
                new StringValidation(SUBMISSION_SET_AUTHOR_PERSON, nopValidator, true),
                new CodeValidation(SUBMISSION_SET_CONTENT_TYPE_CODE),
                new StatusValidation(SUBMISSION_SET_STATUS)
        });
        
        validations.put(QueryType.FIND_FOLDERS, new QueryParameterValidation[] {
                new StringValidation(FOLDER_PATIENT_ID, cxValidator, false),
                new NumberValidation(FOLDER_LAST_UPDATE_TIME_FROM, timeValidator),
                new NumberValidation(FOLDER_LAST_UPDATE_TIME_TO, timeValidator),
                new QueryListCodeValidation(FOLDER_CODES, FOLDER_CODES_SCHEME),
                new StatusValidation(FOLDER_STATUS)
        });
        
        validations.put(QueryType.GET_ALL, new QueryParameterValidation[] {
                new StringValidation(PATIENT_ID, cxValidator, false),
                new StatusValidation(DOC_ENTRY_STATUS),
                new StatusValidation(SUBMISSION_SET_STATUS),
                new StatusValidation(FOLDER_STATUS),
                new QueryListCodeValidation(DOC_ENTRY_FORMAT_CODE, DOC_ENTRY_FORMAT_CODE_SCHEME)                
        });

        QueryParameterValidation[] getDocumentsValidations = new QueryParameterValidation[] {
                new ChoiceValidation(DOC_ENTRY_UUID, DOC_ENTRY_UNIQUE_ID),
                new StringListValidation(DOC_ENTRY_UUID, nopValidator),
                new StringListValidation(DOC_ENTRY_UNIQUE_ID, nopValidator),
                new StringValidation(HOME, nopValidator, true)                
        };
        
        validations.put(QueryType.GET_DOCUMENTS, getDocumentsValidations);
        validations.put(QueryType.GET_DOCUMENTS_AND_ASSOCIATIONS, getDocumentsValidations);
        validations.put(QueryType.GET_FOLDERS_FOR_DOCUMENT, new QueryParameterValidation[] {
                new ChoiceValidation(DOC_ENTRY_UUID, DOC_ENTRY_UNIQUE_ID),
                new StringValidation(DOC_ENTRY_UUID, nopValidator, true),
                new StringValidation(DOC_ENTRY_UNIQUE_ID, nopValidator, true),
                new StringValidation(HOME, nopValidator, true)                
        });
        
        validations.put(QueryType.GET_FOLDERS, new QueryParameterValidation[] {
                new ChoiceValidation(FOLDER_UUID, FOLDER_UNIQUE_ID),
                new StringListValidation(FOLDER_UUID, nopValidator),
                new StringListValidation(FOLDER_UNIQUE_ID, nopValidator),
                new StringValidation(HOME, nopValidator, true)                
        });
        
        QueryParameterValidation[] uuidAndHomValidations = new QueryParameterValidation[] {
                new StringListValidation(UUID, nopValidator),
                new StringValidation(HOME, nopValidator, true)                
        };
        
        validations.put(QueryType.GET_ASSOCIATIONS, uuidAndHomValidations);
        validations.put(QueryType.GET_SUBMISSION_SETS, uuidAndHomValidations);
        
        validations.put(QueryType.GET_SUBMISSION_SET_AND_CONTENTS, new QueryParameterValidation[] {
                new ChoiceValidation(SUBMISSION_SET_UUID, SUBMISSION_SET_UNIQUE_ID),
                new StringValidation(SUBMISSION_SET_UUID, nopValidator, true),
                new StringValidation(SUBMISSION_SET_UNIQUE_ID, nopValidator, true),
                new QueryListCodeValidation(DOC_ENTRY_CONFIDENTIALITY_CODE, DOC_ENTRY_CONFIDENTIALITY_CODE_SCHEME),
                new QueryListCodeValidation(DOC_ENTRY_FORMAT_CODE, DOC_ENTRY_FORMAT_CODE_SCHEME),                
                new StringValidation(HOME, nopValidator, true)                
        });

        validations.put(QueryType.GET_FOLDER_AND_CONTENTS, new QueryParameterValidation[] {
                new ChoiceValidation(FOLDER_UUID, FOLDER_UNIQUE_ID),
                new StringValidation(FOLDER_UUID, nopValidator, true),
                new StringValidation(FOLDER_UNIQUE_ID, nopValidator, true),
                new QueryListCodeValidation(DOC_ENTRY_CONFIDENTIALITY_CODE, DOC_ENTRY_CONFIDENTIALITY_CODE_SCHEME),
                new QueryListCodeValidation(DOC_ENTRY_FORMAT_CODE, DOC_ENTRY_FORMAT_CODE_SCHEME),                
                new StringValidation(HOME, nopValidator, true)                
        });
        
        validations.put(QueryType.GET_RELATED_DOCUMENTS, new QueryParameterValidation[] {
                new ChoiceValidation(DOC_ENTRY_UUID, DOC_ENTRY_UNIQUE_ID),
                new StringValidation(DOC_ENTRY_UUID, nopValidator, true),
                new StringValidation(DOC_ENTRY_UNIQUE_ID, nopValidator, true),
                new AssociationValidation(ASSOCIATION_TYPE),
                new StringValidation(HOME, nopValidator, true)                
        });
    }

    @Override
    public void validate(EbXMLAdhocQueryRequest request, ValidationProfile profile) {
        notNull(request, "request cannot be null");
        
        metaDataAssert(OBJECT_REF.equals(request.getReturnType()) || LEAF_CLASS.equals(request.getReturnType()), 
                UNKNOWN_RETURN_TYPE, request.getReturnType());

        QueryType queryType = QueryType.valueOfId(request.getId());
        metaDataAssert(queryType != null, UNKNOWN_QUERY_TYPE, request.getId());
        if (queryType == QueryType.SQL) {
            validateSqlQuery(request);
        }
        else {
            validateStoredQuery(request);
        }
    }

    private void validateStoredQuery(EbXMLAdhocQueryRequest request) throws XDSMetaDataException {
        QueryType queryType = QueryType.valueOfId(request.getId());        
        for (QueryParameterValidation validation : validations.get(queryType)) {
            validation.validate(request);
        }
    }

    private void validateSqlQuery(EbXMLAdhocQueryRequest request) throws XDSMetaDataException {
        metaDataAssert(request.getSql() != null, MISSING_SQL_QUERY_TEXT);
    }
}
