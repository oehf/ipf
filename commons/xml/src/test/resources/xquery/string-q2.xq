(: Use Cases: STRING :)
(: final all english books using local function:)

declare variable $language as xs:string external;

declare function local:books($ctx as document-node(), $lang as xs:string) as element()*{
    let $book  := $ctx//book[language = $lang]
    return $book/title
};

<titles>{
local:books(., $language)
}</titles>