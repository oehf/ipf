declare namespace cda = "urn:hl7-org:v3";
declare namespace xsd = "http://www.w3.org/2001/XMLSchema";
declare namespace xsi = "http://www.w3.org/2001/XMLSchema-instance";
declare namespace saxon = "http://saxon.sf.net/";

declare variable $id as xs:string external;
declare variable $mapfile as xs:string external;
(: declare variable $map as document-node() external; :)

declare function local:normalize($value as xsd:string) as xsd:string{
	let $result := replace(normalize-unicode($value, 'NFD'), '[\p{M}]', '')
	return $result
};

declare function local:value($value as element()) as element()* {
		if ( $value/@xsi:type eq "PQ") then
			(<item name="value">{string($value/@value)}</item>,
			<item name="unit">{string($value/@unit)}</item>,
			<item name="refHigh"></item>,
			<item name="refLow"></item>)
		else if ($value/@xsi:type eq "IVL_PQ") then ()
		else ()
};

declare function local:observation($obs as element()) as element()* {
	(
	<item name="dos">{string($obs/cda:effectiveTime/cda:low/@value)}</item>,
	<item name="result">{string($obs/cda:code/@displayName)}</item>,
	<item name="loinc">{string($obs/cda:code[@codeSystem="2.16.840.1.113883.6.1"]/@code)}</item>
	)
};

declare function local:serviceprovider($ctx as document-node()) as xsd:string {
	let $servID  := $ctx/*/cda:custodian/cda:assignedCustodian/cda:representedCustodianOrganization
	let $servIDFallback := $ctx/*/cda:documentationOf/cda:serviceEvent/cda:performer/cda:assignedEntity/cda:representedOrganisation
	return if ( not(empty($servID/node())) ) then
		concat( $servID//cda:id/@root, '/', $servID/cda:id/@extension)
	else if ( not(empty($servIDFallback/node())) ) then
		concat($servIDFallback/cda:id/@root, '/', $servIDFallback/cda:id/@extension)
	else ''
};

declare function local:row($obs as element(), $ctx as document-node()) as element(){
	<row>
	    <item name="pid">{$id}</item>
		<item name="name">{local:normalize(string($ctx/*/cda:recordTarget/cda:patientRole/cda:patient/cda:name/cda:family/text()))}</item>
		<item name="family">{local:normalize($ctx/*/cda:recordTarget/cda:patientRole/cda:patient/cda:name/cda:given[1]/text())}</item>
		<item name="middle">{local:normalize(string($ctx/*/cda:recordTarget/cda:patientRole/cda:patient/cda:name/cda:given[2]/text()))}</item>
		<item name="dob">{$ctx/*/cda:recordTarget/cda:patientRole/cda:patient/cda:birthTime/@value/data(.)}</item>
		<item name="serviceProviderId">{local:serviceprovider($ctx)}</item>
		<item name="orderingProviderId"></item>
		{local:observation($obs)}
		{local:value($obs/cda:value)}
	</row>
};

declare function local:extract($ctx as document-node()) as element()*{
		let $map := doc($mapfile)
		for $obs in $ctx//cda:observation
		let $loincOid := $obs/cda:code/@code
		where $obs/@classCode='OBS' 
			and $obs/@moodCode='EVN' 
			and $obs/cda:statusCode/@code='completed'
			and $obs/cda:code/@code = $map//map[@group='lab']/@code/data(.)
		return local:row($obs, $ctx)
};

(: waht does the function returns xsd:string :)
declare function local:main ($ctx as document-node()) as element()*{
	let $rows as element()* := local:extract($ctx) return $rows
};

<result>{
local:main(.)
}</result>