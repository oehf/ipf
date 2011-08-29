(: Use Cases: STRING :)
(: use local functions for traversing :)

declare function local:books($ctx as document-node()) as element()*{
    let $book  := $ctx//book
    return $book
};

declare function local:main($ctx as document-node()) as element()*{
    let $books as element()* := local:books($ctx)
    return <books>{$books}</books>
};

local:main(.)