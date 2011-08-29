(: Use Cases: STRING :)
(: uses the classpath uri resolver on compile time :)

declare variable $static_context as document-node() := doc("xquery/string.xml");

declare function local:books() as element()*{
    let $book  := $static_context//book
    return $book
};

declare function local:main() as element()*{
    let $books as element()* := local:books()
    return <books>{$books}</books>
};

local:main()