(: declare namespaces :)
declare namespace ns = "urn:default:ns";
declare namespace new = "urn:ns:2011";
declare namespace other = "urn:ns:1999";

<isbn-13>{
	for $isbn in //ns:book/new:isbn
	return <isbn type="{$isbn/@type/data(.)}">{$isbn/@ns:number/data(.)}</isbn>
}</isbn-13>
