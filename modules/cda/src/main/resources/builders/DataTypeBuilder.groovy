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
package builders

import groovytools.builder.*

adxp(schema:'_any', factory:'ADXP') {
	properties { 
		partType()                      
	}
}
ad(schema:'_any', factory:'AD') {
	properties {
		isNotOrdered()
	}
	collections {
		// use()
		delimiters(collection:'delimiter') {
			delimiter(schema:'adxp', factory:'ADXP_DELIMITER')
		}
		countries(collection: 'country') {
			country(schema:'adxp', factory:'ADXP_COUNTRY')
		}
		states(collection: 'state') {
			state(schema:'adxp', factory:'ADXP_STATE')
		}
		counties(collection:'county') {
			county(schema:'adxp', factory:'ADXP_COUNTY')
		}
		cities(collection:'city') {
			city(schema:'adxp', factory:'ADXP_CITY')
		}
		postalCodes(collection:'postalCode') {
			postalCode(schema:'adxp', factory:'ADXP_POSTAL_CODE')					    
		}
		streetAddressLines(collection:'streetAddressLine')  {
			streetAddressLine(schema:'adxp', factory:'ADXP_STREET_ADDRESS_LINE')
		}
		houseNumberNumerics(collection:'houseNumberNumeric') {
			houseNumberNumeric(schema:'adxp', factory:'ADXP_HOUSE_NUMBER_NUMERIC')
		}
		directions(collection:'direction') {
			direction(schema:'adxp', factory:'ADXP_DIRECTION')
		}
		streetNames(collection:'streetName') {
			streetName(schema:'adxp', factory:'ADXP_STREET_NAME')
		}
		streetNameBases(collection:'streetNameBase') {
			streetNameBase(schema:'adxp', factory:'ADXP_STREET_NAME_BASE')
		}
		streetNameTypes(collection:'streetNameType') {
			streetNameType(schema:'adxp', factory:'ADXP_STREET_NAME_TYPE')
		}
		additionalLocators(collection:'additionalLocator') {
			additionalLocator(schema:'adxp', factory:'ADXP_ADDITIONAL_LOCATOR')
		}
		unitIDs(collection:'unitID') {
			unitID(schema:'adxp', factory:'ADXP_UNIT_ID')
		}
		unitTypes(collection:'unitType') {
			unitType(schema:'adxp', factory:'ADXP_UNIT_TYPE')
		}
		careOfs(collection:'careOf') {
			careOf(schema:'adxp', factory:'ADXP_CARE_OF')
		}
		censusTracts(collection:'censusTract') {
			censusTract(schema:'adxp', factory:'ADXP_CENSUS_TRACT')
		}
		deliveryAddressLines(collection:'deliveryAddressLine') {
			deliveryAddressLine(schema:'adxp', factory:'ADXP_DELIVERY_ADDRESS_LINE')
		}
		deliveryInstallationTypes(collection:'deliveryInstallationType') {
			deliveryInstallationType(schema:'adxp', factory:'ADXP_DELIVERY_INSTALLATION_TYPE')
		}
		deliveryInstallationAreas(collection:'deliveryInstallationArea') {
			deliveryInstallationArea(schema:'adxp', factory:'ADXP_DELIVERY_INSTALLATION_AREA')
		}
		deliveryInstallationQualifiers(collection:'deliveryInstallationQualifier') {
			deliveryInstallationQualifier(schema:'adxp', factory:'ADXP_DELIVERY_INSTALLATION_QUALIFIER')
		}
		deliveryModes(collection:'deliveryMode') {
			deliveryMode(schema:'adxp', factory:'ADXP_DELIVERY_MODE')
		}
		deliveryModeIdentifiers(collection:'deliveryModeIdentifier') {
			deliveryModeIdentifier(schema:'adxp', factory:'ADXP_DELIVERY_MODE_IDENTIFIER')
		}
		buildingNumberSuffixes(collection:'buildingNumberSuffix') {
			buildingNumberSuffix(schema:'adxp', factory:'ADXP_BUILDING_NUMBER_SUFFIX')
		}
		postBoxes(collection:'postBox') {
			postBox(schema:'adxp', factory:'ADXP_PRECINCT')
		}
		usablePeriod() // TODO   
		
	}
}
bin(schema:'_any', factory:'BIN1') {
	properties {
		representation(factory:'BINARY_DATA_ENCODING')
	}
}
bl(schema:'_any', factory:'BL1'){
    properties{
        value()
    }
}
ce(schema:'cv', factory:'CE') {
	collections { 
		translations(collection:'translation') {
			translation(schema:'cd') 
		}
	}
}
cd(schema:'ce', factory:'CD') {
	collections { 
		qualifiers(collection:'qualifier') {
			qualifier(schema:'cr')  
		}
	}
}
cr(schema:'_any', factory:'CR') {
	properties {
		name(schema:'cv')
		value(schema:'cd')
		inverted()
	}
}
cs(schema:'_any', factory:'CS1') {
	properties { 
		code()
	}
}
cv(schema:'cs', factory:'CV') {
	properties {
		// TODO: enforce OID or UUID format for codeSystem
		codeSystem()
		codeSystemName()
		codeSystemVersion()
		displayName()
		originalText(schema:'ed')
	}
	
}
ed(schema:'bin', factory:'ED') {
	properties {
		compression()
		integrityCheck()
		integrityCheckAlgorithm()
		language()
		mediaType()
		reference(schema:'tel')
		thumbnail() //TODO
	}	    
}	
enxp(schema:'_any', factory:'ENXP') {
	properties { 
		partType() 
	}
	collections {
		qualifiers(collection:'qualifier') {
			qualifier(schema:'cs') 
		}
	}
}
en(schema:'_any', factory:'EN') {
	properties { 
	    validTime(schema:'ivlts') 
	}
	collections {
		uses(collection:'use') {
			_use()
		}
		delimiters(collection:'delimiter') {
			delimiter(schema:'enxp', factory:'EN_DELIMITER')
		}
		families(collection:'family') {
			family(schema:'enxp', factory:'EN_FAMILY')
		}
		givens(collection:'given') {
			given(schema:'enxp', factory:'EN_GIVEN')
		}
		prefixes(collection:'prefix') {
			prefix(schema:'enxp', factory:'EN_PREFIX')
		}
		suffixes(collection:'suffix') {
			suffix(schema:'enxp', factory:'EN_SUFFIX')
		}
	}
}
ii(schema:'_any', factory:'II') {
	properties {
		// TODO: enforce OID or UUID format for root
		root()
		extension()
		assigningAuthorityName()
		isDisplayable()
	}
}
_int(schema:'_any', factory:'INT1') {
	properties { 
		value() 
	}
}
ivxbts(schema:'ts', factory:'IVXBTS') {
	properties {
		inclusive()
	}
}
ivlts(schema:'_any', factory:'IVLTS') {
	properties {
        value(schema:'ts')
		center(schema:'ts')
		low(schema:'ivxbts')
		width(schema:'pq')
		high(schema:'ivxbts')	        
	}
}
ivlint(schema:'sxcmint', factory:'IVLINT'){//TODO
    properties{
        low(schema:'ivxbint', def:null, req:true)
        /* TODO choice width|high optional*/
        high(schema:'ivxbint', def:null, req:true)
        /* choice width | high */
        width(schema:'_int', min:1)
        //high(schema:'ivxbint')
        /*choice center | width */
        center(schema:'_int',min:1)
    }
    
}
ivxbint(schema:'_int',factory:'IVXBINT'){
    properties{
        value()
        inclusive(def:true)
    }
}
ivxbpq(schema:'pq', factory:'IVXBPQ'){
    properties{
        inclusive(def:true)
    }
}
ivlpq(schema:'sxcmpq',factory:'IVLPQ'){//TODO complicated choice rools
    properties{
        low(schema:'ivxbpq')
        high(schema:'ivxbpq')
        width(schema:'pq')
        /* TODO optional high */
        center(schema:'pq')
        /* TODO optional width */
    }
}
on(schema:'en', factory:'ON') {
	// TODO: no family/given names allowed
}
pn(schema:'en', factory:'PN') {
	// TODO: only person ENXP qualifiers allowed
}
pq(schema:'_any', factory:'PQ') {
	properties {
		unit(def:1)
		value()
	}
	collections {
		translations(collection:'translation') {
			translation(schema:'pqr')
		}
	}
}
pqr(schema:'cv', factory:'PQR'){
    properties{
        value()
    }
}
rtopqpq(schema:'_any', factory:'RTOPQPQ'){
    properties{
        numerator(schema:'pq', min:1, max:1)
        denominator(schema:'pq', min:1, max:1)
    }
}
sc(schema:'st', factory:'SC') {	
    properties {
        code()
        codeSystem()
        codeSystemName()
        codeSystemVersion()
        displayName()
    }
}
st(schema:'_any', factory:'ST1') {	    
}
sxcmts(schema:'ts', factory:'SXCMTS'){
    properties{
        operator(factory:'SET_OPERATOR')
    }
}
sxcmint(schema:'_int',factory:'SXCMINT'){
    properties{
        operator(factory:'SET_OPERATOR')
    }
}
sxcmpq(schema:'pq',factory:'SXCMPQ'){
    properties{
        operator(factory:'SET_OPERATOR')
    }
}
tel(schema:'url', factory:'TEL') {
    properties { 
        usablePeriod() // TODO                     
    }
    collections {
        uses(collection:'use') {
            _use() 
        }
    }
}
ts(schema:'_any', factory:'TS1') {
	properties { // TODO: enforce time string format
		value() }
}
url(schema:'_any', factory:'URL') {
	properties { 
	    value()
	}
}

