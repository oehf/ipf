# XDS metadata model

`commons/ihe/xds` is the largest and most intricate module. Three representations, transformed in sequence:

1. **JAXB stubs** (`core/stub/`) — generated-style classes for the ebXML 3.0 / ebRIM wire format.
2. **`EbXML*` abstraction** (`core/ebxml/`, impl in `ebxml30/`) — a version-neutral facade over the stubs.
3. **Simplified request/response model** (`core/requests/`, `core/responses/`, `core/metadata/`) — what route
   authors actually see: `ProvideAndRegisterDocumentSet`, `QueryRegistry`, `RetrieveDocumentSet`, `DocumentEntry`, …

`core/transform/` maps between (2) and (3): `requests/` and `responses/` transformers, with `hl7/` helpers for
HL7v2 datatypes embedded in XDS slots.

**Stored queries use a visitor pattern.** Adding a query type means touching, at minimum:
`QueryType`, the new `Query` subclass, `Query.Visitor` (both `visit` overloads and all implementors),
`ToEbXMLVisitor`, `FromEbXMLVisitor`, `QueryParameter`, `QuerySlotHelper`, and the query-parameter validations in
`core/validate/query/` wired into `AdhocQueryRequestValidator`. The compiler catches the visitor implementors;
the `QueryParameter` entries and `AdhocQueryRequestValidator`'s static registries (`ALLOWED_QUERY_TYPES` — which
query types each interaction accepts — and `ALLOWED_MULTIPLE_SLOTS`) it does not, so a missing entry surfaces
only as a runtime validation failure.

Validation lives in `core/validate/`, keyed by `ValidationProfile` (interaction id, profile, query-vs-not),
and is exposed to routes as Camel `Processor`s via `XdsCamelValidators`
(`iti18RequestValidator()`, `iti41ResponseValidator()`, …).
