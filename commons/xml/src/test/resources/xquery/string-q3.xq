(: Use Cases: STRING :)
(: return all bulgarian books where the title contains the author name:)

declare variable $language as xs:string external;
declare variable $author_name as xs:string external;

<books filter-lang="{$language }" filter-name="{$author_name }">{
    for $book in //book
    for $titles in //book[language = $language]/title
    where
        some $author in $book/author satisfies (contains($author/text(), $author_name) 
            and (some $title in $titles satisfies contains($title/text(), $author/text())))
    return
        $book
}</books>
