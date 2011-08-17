(: calculates the number of ordered and payed books. :)

declare variable $orders := doc("xquery/r.xml")//order[status="PAID"];
declare variable $books := doc("xquery/string.xml")//book;

<report>{
	for $isbn in distinct-values($orders//book/@isbn)
	let $book := $books[@isbn = $isbn]
	order by $book/title descending
	return 
		<item isbn="{$isbn}" type="{node-name($book)}" count="{sum($orders/item[@type="book"][book/@isbn = $isbn]/amount)}">
			{$book/title}
		</item>
}</report>