
## Caching and Paging FHIR endpoint parameters

FHIR endpoints requesting a potentially big bundle of results are enabled to support paging _out of the box_, providing different modes
regarding _eager_ or _lazy_ fetching of result subsets.

#### Parameters

| Parameter name       | Type       | Default value | Short description                                                                    |
|:---------------------|:-----------|:--------------|:-------------------------------------------------------------------------------------|
| `lazyLoadBundles`    | Boolean    | false         | whether page request parameters are delegated to the consumer route
| `cacheBundles`       | Boolean    | false         | whether result pages are cached (only effective if `lazyLoadBundles` is true)

By default, the [FHIR servlet] is configured to return in pages containing 20 resources. The client may demand for smaller or larger pages
by specifying the `_count` parameter, but the maximum is by default 100.

If `lazyLoadBundles` is false, the request must be treated as if no paging is in effect, i.e. the service returns all matching results.
IPF cares about caching the results as well as the result size and delivers them back to the caller in the requested chunks. Repeating
requests are served from this cache rather than being forwarded into the consumer route.

This behavior can get inefficient if the overall result set is usually much bigger compared to the requested result subset, particularly
when the backend service has no upper result limit. In this case, `lazyLoadBundles` can be set to true, which causes IPF to delegate
paging into the consumer route. The route is now responsible of handling the following two cases:

* `size` request: IPF adds a message header called `FhirRequestSizeOnly` to the exchange, which indicates that the route must only return 
the overall result size as integer value.
* `subset` request: IPF adds two message headers called `FhirFromIndex` and `FhirToIndex` to the exchange, containing the lower and upper
bound of the request result subset. The route is expected to return the corresponding list of results.

By default, the results are _not_ cached unless `cacheBundles` is set to true. In this case already returned results are cached and reused
on repeating requests, if possible.


In the example below, only two entries per page were requested. As the total result size is three, the response bundle contains a link to the
next page. Note that the URL is specific to the underlying FHIR library and must be treated as atomic unit.

```xml
    <Bundle xmlns="http://hl7.org/fhir">
       <id value="baa11115-c300-4043-8214-e72c97fe2984"/>
       <meta>
          <lastUpdated value="2016-04-19T13:46:49.159+02:00"/>
       </meta>
       <type value="searchset"/>
       <total value="3"/>
       <link>
          <relation value="self"/>
          <url value="http://localhost:8999/Patient?family=Test&amp;_count=2"/>
       </link>
       <link>
          <relation value="next"/>
          <url value="http://localhost:8999?_getpages=4fa95271-bb38-416b-a844-b83345280fbd&amp;_getpagesoffset=2&amp;_count=2&amp;_bundletype=searchset"/>
       </link>
       <entry>
          <resource>
             <Patient xmlns="http://hl7.org/fhir">
                <id value="4711"/>
                 ...
             </Patient>
          </resource>
       </entry>
       <entry>
          <resource>
             <Patient xmlns="http://hl7.org/fhir">
                <id value="0815"/>
                ...
             </Patient>
          </resource>
       </entry>
    </Bundle>
```

After calling directly the _next_ URL, the second page is returned, containing the remaining patient entry together with a link to the previous page:

```xml
    <Bundle xmlns="http://hl7.org/fhir">
       <id value="0de9dd8b-cb98-406e-a1f6-3bb6e2a5eaa5"/>
       <meta>
          <lastUpdated value="2016-04-19T14:10:17.109+02:00"/>
       </meta>
       <type value="searchset"/>
       <total value="3"/>
       <link>
          <relation value="self"/>
          <url value="http://localhost:8999?_getpages=e8d02c62-6a96-48cc-904b-9740a7eac51f&amp;_getpagesoffset=2&amp;_count=2&amp;_bundletype=searchset"/>
       </link>
       <link>
          <relation value="previous"/>
          <url value="http://localhost:8999?_getpages=e8d02c62-6a96-48cc-904b-9740a7eac51f&amp;_getpagesoffset=0&amp;_count=2&amp;_format=xml&amp;_bundletype=searchset"/>
       </link>
       <entry>
          <resource>
             <Patient xmlns="http://hl7.org/fhir">
                <id value="9999"/>
                ...
             </Patient>
          </resource>
       </entry>
    </Bundle>
```

[FHIR Servlet]: deployment.html