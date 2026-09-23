# IPF Mapping Service

A mapping is a named translation table between two code systems — HL7v2 table 0001 to
`http://hl7.org/fhir/administrative-gender`, an ATNA code system name to a FHIR code system URI,
and so on. The mapping service holds them, looks codes up in either direction, and answers for
codes it has no entry for.

There is one model. The file format a mapping is written in is a decision per file, made by
whichever loader claims its extension, so formats mix freely inside one `Mappings` instance. Where
a name says nothing about its format, name the format instead — see
[Selecting the format explicitly](#selecting-the-format-explicitly).

| Module                         | Format                                                 | Format id       | Extension                                   | Validated by                               |
|--------------------------------|--------------------------------------------------------|-----------------|---------------------------------------------|--------------------------------------------|
| `ipf-commons-map-core`         | — the model, the API and the loader SPI                |                 |                                             |                                            |
| `ipf-commons-map-xml`          | IPF XML — **recommended default**                      | `xml`           | `.mapping.xml`                              | XSD, as the file is read                   |
| `ipf-commons-map-yaml`         | IPF YAML                                               | `yaml`          | `.mapping.yaml`, `.mapping.yml`             | strict binding; JSON Schema in the editor  |
| `ipf-commons-ihe-fhir-r4-core` | FHIR ConceptMap, R4                                    | `conceptmap-r4-json`, `conceptmap-r4-xml` | `.conceptmap.r4.json`, `.conceptmap.r4.xml` | HAPI, and the FHIR validator if you run it |
| `ipf-commons-map-groovy`       | legacy Groovy DSL — **deprecated, removed in IPF 7.0** | `groovy`        | `.map`                                      | nothing                                    |

## Looking mappings up

```java
var mappings = Mappings.builder()
        .function("oidUri", code -> Character.isDigit(code.charAt(0)) ? "urn:oid:" + code : code)
        .load("classpath:/META-INF/map/atna2fhir.mapping.xml")
        .load("classpath:/my-own.mapping.yaml")
        .build();

Optional<String> gender = mappings.map("hl7v2fhir-patient-administrativeGender", "M");   // "male"
Optional<String> back    = mappings.mapReverse("hl7v2fhir-patient-administrativeGender", "male");  // "M"
```

A mapping can also be found by the code systems it translates between, rather than by name — which
is how a caller holding a code would actually reach it, and the only way to reach one read from a
source that names itself, such as a ConceptMap:

```java
var mapping = mappings.mappingFor("2.16.840.1.113883.12.1",
                                  "http://hl7.org/fhir/administrative-gender").orElseThrow();
mappings.map(mapping.name(), "M");
```

Either side may be `null` for "any". `mappingFor` fails if several mappings translate between the
same pair — picking one silently would be arbitrary — so use `mappingsFor` to see them all. Both
scan the registered mappings, so resolve the mapping once rather than per message.

`Mappings` is `String`-typed and returns `Optional`. An unknown *mapping name* is a programming
error and raises `IllegalArgumentException`; an unknown *code* within a known mapping yields an
empty `Optional`.

`MappingService`, `BidiMappingService` and `SpringBidiMappingService` are **deprecated as of 6.0
and go away in 7.0**. They still work for existing applications and answer what `Mappings`
answers, with `null` where it returns an empty `Optional`. The two string conventions of IPF 5.x —
an empty value counting as no value, and composite `~` values — are gone; see
[What changed in IPF 6.0](#what-changed-in-ipf-60).

Migrating needs no big-bang: construct the untyped service over the mappings you already have, and
both front doors see the same content.

```java
var mappings = new SpringMappings();                          // or DefaultMappings
var mappingService = new SpringBidiMappingService(mappings);   // deprecated, but same content
```

Everything inside IPF has moved: `DefaultUriMapper` and `AuditRecordTranslator` take `Mappings`,
the Groovy and Kotlin DSL extensions resolve `Mappings` from the registry, and every Spring context
IPF ships declares a `SpringMappings` bean. The untyped service remains only as the compatibility
adapter, and the Boot starter still exposes one so that application code can migrate a call site at
a time.

`BidiMappingService.getMappings()` goes the other way, for a call site that has the old service and
wants the typed API.

## What a mapping declares

* **Entries** — key/value pairs. Both sides are strings; the empty string is a legal code. An
  entry may carry a `keyDisplay` and a `valueDisplay`: the human-readable name of either code.
  They are informative — nothing is looked up by its display — and exist so a mapping read from a
  source that carries them keeps them, and so a caller building a `Coding` out of a translation
  has one to put in it.
* **Key system and value system** — formal identifiers, usually OIDs or URIs.
* **An equivalence per entry** — `equal`, `equivalent`, `wider`, `narrower`, `inexact` or
  `disjoint`, borrowed from FHIR ConceptMap. Only `equal` and `equivalent` entries build the
  reverse index, which is how several keys may share a value and the value still has exactly one
  inverse. Two invertible entries sharing a value is a load-time error.

  A `disjoint` entry asserts that its key and value are explicitly **not** equivalent, so it does
  not translate in either direction — answering with its value would state the opposite of what
  the mapping says. It is still read and kept, so it round-trips and documents the decision, but it
  is not among the mapping's `keys()` and the mapping's fallback applies to its key as it does to
  any other key the mapping does not translate. FHIR draws the same line: the `result` of a
  `$translate` cannot be true for a `disjoint` match.
* **An unmatched fallback, per direction** — one of five modes:

  | Mode                    | Answers with                                                |
  |-------------------------|-------------------------------------------------------------|
  | `absent` (the default)  | nothing                                                     |
  | `provided` / `identity` | the key itself                                              |
  | `fixed`                 | a constant                                                  |
  | `function`              | the result of a named Java function, which receives the key |
  | `delegate`              | whatever another mapping answers                            |
  | `fail`                  | it raises an error                                          |

  `delegate` is how a mapping specialises another without restating it: declare the codes that
  differ and hand the rest over. The named mapping is asked **in full** — its entries first, then
  its own fallback — so it is the last mapping in the chain that finally decides the outcome, and
  a chain ends at the first mapping whose fallback answers on its own. `lookup()` does not follow
  a delegation (it reports what a mapping *declares*) and neither do `keys()`/`values()`.

  The delegate must **already be loaded**, which is what makes a cycle impossible to declare —
  reaching one would need a forward reference — and turns a typo into a startup error. Each
  direction delegates separately: handing over `unmatched` says nothing about `reverse`.

* **`reversible="false"`** — no reverse index at all, so several keys may share a value without
  declaring an equivalence.
* **`override="true"`** — this declaration replaces a mapping of the same name loaded earlier.
  Without it, a duplicate name is a load-time error.

## The formats

The same mapping in all four:

### IPF XML — `.mapping.xml`

```xml
<mappings xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:noNamespaceSchemaLocation="http://openehealth.org/schema/ipf-commons-map.xsd">
    <mapping name="hl7v2fhir-patient-administrativeGender"
             keySystem="2.16.840.1.113883.12.1"
             valueSystem="http://hl7.org/fhir/administrative-gender">
        <entry key="M" value="male"/>
        <entry key="F" value="female"/>
        <entry key="A" value="other" equivalence="narrower"/>   <!-- not invertible -->
        <entry key="O" value="other"/>                          <!-- "other" reverses to O -->
        <entry key="U" value="unknown" keyDisplay="Unknown"/>   <!-- displays are optional -->
        <unmatched mode="fixed" value="other"/>
        <reverse>
            <unmatched mode="fixed" value="O"/>
        </reverse>
    </mapping>
</mappings>
```

The schema has no target namespace, so `xsi:noNamespaceSchemaLocation` is all an IDE needs to
offer completion and mark errors inline — no plugin, no network access. The loader always
validates against the copy shipped inside `ipf-commons-map-xml` at `META-INF/ipf/mapping.xsd`,
never against the location the document names. A misspelled attribute is an error at startup with
a line and a column.

### IPF YAML — `.mapping.yaml`

```yaml
# yaml-language-server: $schema=http://openehealth.org/schema/ipf-commons-map.json
mappings:

  hl7v2fhir-patient-administrativeGender:
    keySystem:   "2.16.840.1.113883.12.1"
    valueSystem: http://hl7.org/fhir/administrative-gender
    entries:
      M: male
      F: female
      A: {value: other, equivalence: narrower}
      O: other
      U: {value: unknown, keyDisplay: Unknown}
    unmatched: {fixed: other}
    reverse:
      unmatched: {fixed: O}
```

Shorter than the XML, and comments survive — which matters for mapping files that record the HL7
table each code came from next to it. Property names are bound strictly, so `keysystem` for
`keySystem` is a startup error with a line and a column. The JSON Schema at
`META-INF/ipf/mapping-schema.json` states the same rules for editors; it is not used at runtime.

Values are strings, and a YAML scalar that parses as a number is **rejected rather than coerced**:
write `AGN: "1004"`, not `AGN: 1004`.

### FHIR ConceptMap, R4 — `.conceptmap.r4.json`, `.conceptmap.r4.xml`

```json
{
  "resourceType": "ConceptMap",
  "name": "hl7v2fhir-patient-administrativeGender",
  "status": "active",
  "group": [{
    "source": "http://terminology.hl7.org/CodeSystem/v2-0001",
    "target": "http://hl7.org/fhir/administrative-gender",
    "element": [
      {"code": "M", "target": [{"code": "male",    "equivalence": "equal"}]},
      {"code": "A", "target": [{"code": "other",   "equivalence": "narrower"}]},
      {"code": "O", "target": [{"code": "other",   "equivalence": "equal"}]},
      {"code": "U", "target": [{"code": "unknown", "equivalence": "equal"}]}
    ],
    "unmapped": {"mode": "fixed", "code": "other"},
    "extension": [{
      "url": "http://openehealth.org/ipf/StructureDefinition/reverse-unmapped",
      "extension": [{"url": "mode", "valueCode": "fixed"},
                    {"url": "code", "valueCode": "O"}]
    }]
  }]
}
```

For a consumer who already governs their terminology in ConceptMap — authored in Simplifier or
Forge, shipped in an Implementation Guide, validated by the FHIR validator — this lets IPF read
that artifact directly instead of duplicating it as an internal table. HAPI does the parsing, so
both wire formats and every structural quirk of the resource come for free.

It is offered as a loader, not as IPF's own format. The mapping above is 9 lines of YAML, 13 of
IPF XML and 24 of ConceptMap JSON; comments have nowhere natural to go; and the parts of the model
ConceptMap has no field for need IPF extensions, so an IPF-flavored ConceptMap is not fully
portable either:

| Extension URL                               | On               | Value                              | For                                    |
|---------------------------------------------|------------------|------------------------------------|----------------------------------------|
| `.../StructureDefinition/mapping-name`      | `group`          | `valueString`                      | the name to register the mapping under |
| `.../StructureDefinition/reversible`        | `group`          | `valueBoolean`                     | `reversible="false"`                   |
| `.../StructureDefinition/reverse-unmapped`  | `group`          | nested `mode`, `code` / `function` | the reverse direction's fallback       |
| `.../StructureDefinition/unmapped-function` | `group.unmapped` | `valueString`                      | a computed fallback                    |

All four are prefixed `http://openehealth.org/ipf`, and `ConceptMapExtensions` defines their URLs
and shapes once for both the loader and the writer. They are plain extensions rather than a HAPI
custom resource class because every one of them sits on a **backbone element** — `group` or
`group.unmapped` — and typing those would mean redeclaring `ConceptMap`'s own `group` child with a
different component type.

Without `mapping-name`, a mapping is named after the resource's `name`, `id` or the last segment
of its `url`; groups after the first get `#<index>` appended.

A delegating fallback is native: `unmapped.mode = other-map` with a `url`, whose last path segment
names the mapping to delegate to — the same rule this loader uses to name a resource that carries no
name of its own.

`ConceptMapWriter` goes the other way, so a mapping maintained as a plain table can be published as
the standard resource. Two limits are enforced rather than silently applied: `unmapped.mode =
other-map` is rejected on read, and a mapping using the **empty string as a code** cannot be
written at all — a FHIR primitive is never empty, and HAPI would drop it. Two of the mappings IPF
ships do exactly that, and stay in the XML format.

The extension is FHIR-version specific on purpose. A ConceptMap is not version-independent, so a
loader for another version can claim `.conceptmap.r5.json` and sit on the same classpath. R5's only
semantic difference is the rename of `target.equivalence` to `target.relationship`, which collapses
onto the same `Equivalence`.

ConceptMap has more than the model uses (several targets per element, `relatedto`, `subsumes`,
`specializes`); the loader folds what it can and logs or rejects the rest rather than dropping it
silently. R4 only — R5's rename of `target.equivalence` to `target.relationship` is the only
semantic difference to absorb if an R5 loader lands later.

### Legacy Groovy DSL — `.map`

Deprecated in IPF 6.0 and removed in 7.0. A `.map` file is a Groovy *script*, evaluated rather
than parsed, so nothing about it is checked before it runs:

```groovy
mappings = {
    'hl7v2fhir-patient-administrativeGender'(['2.16.840.1.113883.12.1',
                                              'http://hl7.org/fhir/administrative-gender'],
        'M' : 'male',
        'F' : 'female',
        'A' : 'other',
        'O' : 'other',
        'U' : 'unknown',
        (ELSE)    : 'other',
        ({'O'})   : (ELSE)
    )
}
```

`ipf-commons-map-groovy` also carries the Groovy metaclass extensions — `'M'.map('…gender')`,
`'male'.mapReverse('…gender')` and the dynamic `mapAdministrativeGender()` form. They work against
any format, not just `.map` files, and resolve `Mappings` from the registry, so a Spring context
needs a `Mappings` bean (`SpringMappings`) rather than a `MappingService` one. The HL7v2 DSL's
`map`/`mapReverse` on HAPI `Type`s, in both `ipf-modules-hl7` and `ipf-modules-hl7-kotlin`, resolve
it the same way.

All of them return the string the mapping declares, exactly as it is: an entry mapping to `''`
yields `''`, and a value containing `~` is one string, not a `List`.

## Selecting the format explicitly

Dispatch by extension covers the normal case and needs no configuration. It cannot cover every
case, though: a ConceptMap fetched from a terminology server has no extension at all, a file may
be called `gender.xml` for reasons outside IPF's control, and two loaders may be able to read one
and the same extension. Name the **format id** and the extension stops mattering:

```java
var mappings = Mappings.builder()
        .load("classpath:/gender.mapping.xml")                                   // by extension
        .load("https://tx.example.org/ConceptMap/gender", "conceptmap-r4-json")  // by format id
        .load(url, new MyOwnLoader())                                            // or the loader itself
        .build();
```

A named format bypasses `supports(URI)` entirely and is compared case-insensitively; a format that
nothing on the classpath implements fails by name, listing what could be read instead.

An id names one loader, so a format with several wire formats has one id per encoding —
`conceptmap-r4-json` and `conceptmap-r4-xml`. A source whose name says nothing leaves no other way
to know which encoding to expect, so the content is checked against the id that was named: a
ConceptMap read as `conceptmap-r4-json` that turns out to start with `<` is rejected saying to read
it as `conceptmap-r4-xml`, rather than failing as a parser error about an unexpected token.

`MappingLoaders.formats()` and `MappingWriters.formats()` list what the current classpath can read
and write.

## Untrusted mapping sources

A mapping file is data, and a loader reads whatever location it is given — a jar on the classpath,
but also a file in a configuration directory or a ConceptMap fetched from a terminology server. The
declarative loaders are built for that:

| | XML | YAML | ConceptMap R4 |
|---|---|---|---|
| DOCTYPE / entity expansion ("billion laughs") | rejected outright | n/a | ignored |
| External entities, external DTDs, `xsi:schemaLocation` hints | never resolved | n/a | never resolved |
| Type tags naming a class to instantiate | n/a | inert — read as text | n/a |
| Aliases / anchors | n/a | never expanded into collections | n/a |
| Nesting | bounded | bounded | bounded (1000) |
| Duplicate keys | schema-rejected | rejected with line and column | last wins (FHIR's own rule) |

Two limits apply to every format, wherever the source came from: a mapping source is read with a
connect and read timeout, so a location that never answers cannot hold up startup, and no more than
64 MiB of it is read at all. The YAML parser stops earlier, at 8 MiB — scanning one enormous scalar
costs quadratic time, and that ceiling is what bounds the work a hostile document can ask for. All
of this is pinned by tests (`*SecurityTest`), because the guarantee is in the parser configuration,
not in the format.

**The Groovy `.map` loader is the exception, and it is not a bug: a `.map` file is a script, and
loading one runs it.** Anything the process can do, the file can do. It is deprecated and goes away
in IPF 7.0; until then, point it only at files you would equally well put on the classpath, and
convert anything else with `MappingConverter`.

## Spring

`SpringMappings` is `Mappings` configured with Spring `Resource`s, and the bean to wire. Resources
are dispatched by file extension, so one list may mix formats:

```xml
<bean id="mappings" class="org.openehealth.ipf.commons.spring.map.SpringMappings">
    <property name="mappingFunctions">
        <map><entry key="first4"><bean class="com.example.First4"/></entry></map>
    </property>
</bean>

<bean class="org.openehealth.ipf.commons.spring.map.config.CustomMappings">
    <property name="mappingResources">
        <list>
            <value>classpath:gender.mapping.xml</value>
            <value>classpath:encounter.mapping.yaml</value>
            <value>classpath:kdl-ihe-typecode.conceptmap.r4.json</value>
            <value>classpath:legacy.map</value>
        </list>
    </property>
</bean>

<!-- resources whose names do not identify a format: name it for all of them -->
<bean class="org.openehealth.ipf.commons.spring.map.config.CustomMappings">
    <property name="mappingFormat" value="conceptmap-r4-json"/>
    <property name="mappingResources">
        <list><value>https://tx.example.org/ConceptMap/gender</value></list>
    </property>
</bean>
```

`SpringMappings` collects every `CustomMappings` bean of the application context itself — no
configurer and no post processor — which is how an application layers its own mappings over the
ones IPF ships. Collecting happens before the `ContextRefreshedEvent`, so mappings already resolve
while beans are being initialized, and contributions can be ordered relative to each other with
Spring's `@Order` / `Ordered` on the `CustomMappings` bean. Two ways to layer:

```xml
<!-- replace it: the later mapping wins wholesale, so it must restate everything it keeps -->
<mapping name="hl7v2fhir-patient-genderIdentity" override="true"> ... </mapping>

<!-- extend it: declare only what differs, and delegate the rest -->
<mapping name="genderIdentity-custom">
    <entry key="CUSTOM" value="non-binary"/>
    <unmatched mode="delegate" ref="hl7v2fhir-patient-genderIdentity"/>
</mapping>
```

`override` is the smaller change to make and the easier one to get wrong — anything the replaced
mapping declared and the replacement does not is simply gone. Delegating keeps the base mapping
intact and needs only the codes that differ, at the cost of a second name.
 `mappingFunctions` is a property of
`SpringMappings` rather than of a holder, so it is applied while the bean is created — before any
`CustomMappings` bean contributes a resource that names one of them.

Unlike the deprecated `SpringBidiMappingService`, `SpringMappings` holds the model's constraints: a
duplicate mapping name is an error unless the later declaration says `override`, and a value with
two inverses is an error. Both can be relaxed with `allowOverride` and `allowReverseCollisions` for
a source that cannot state the intent — which is what the legacy `.map` loader does for itself, so
script files keep working under the strict defaults.

The Spring Boot starter exposes both, over one set of mappings:

```java
@Autowired SpringMappings mappings;         // or just Mappings
@Autowired SpringBidiMappingService svc;    // deprecated, same content
```

## Computed fallbacks

The one thing the DSL's arbitrary `(ELSE)` closures were genuinely needed for is a fallback that
derives its answer from the key. That is `mode="function"`, resolved against a named
`Function<String,String>`:

```java
var mappings = Mappings.builder()
        .function("first4", key -> key.length() <= 4 ? key : key.substring(0, 4))
        .load("classpath:/devices.mapping.xml")
        .build();
```

Three ways to register one, in increasing order of reach:

* `Mappings.builder().function(name, fn)` — for a `Mappings` you build yourself.
* `BidiMappingService.setMappingFunctions(Map)` — a bean property, so it is applied before any
  configurer adds mapping resources.
* a `MappingFunctionProvider` service — a module that ships a mapping file with a computed
  fallback also ships the function it names, so the file works wherever it is loaded from. This is
  how `ipf-commons-ihe-fhir-r4-audit` provides `oidUri` for `atna2fhir.mapping.xml`.

A mapping source naming a function that is not registered is rejected as it is read.

## Migrating from `.map`

`MappingConverter` reads any format a loader on the classpath can read and writes any format a
writer can produce:

```
java -cp ... org.openehealth.ipf.commons.map.MappingConverter [-f <format>] [--from <format>] <output-dir|-> <file-or-dir>...
```

`-f` names the target format, by its id (`yaml`) or by the extension it produces
(`.mapping.yaml`), and defaults to `.mapping.xml`. `--from` names the format the sources are read
in, for files whose extension does not identify one; without it each file is dispatched by its
extension.

The output is a starting point, not a finished file, and it says so — every judgement the
converter had to make comes back as a warning naming the mapping:

* **Fallback closures.** It probes them: one that returns its argument becomes `mode="provided"`,
  one that returns a constant becomes `mode="fixed"`, anything else becomes
  `mode="function" ref="TODO-<mapping>"` with a warning. The probe is a heuristic, which is why
  the output is meant to be read before it is committed.
* **Reverse collisions.** Where several keys map onto one value, the Groovy service silently made
  the last one the inverse. The converter reproduces that and marks the others `narrower`, and
  warns so the canonical inverse can be chosen deliberately.
* **Composite values.** The `~` convention is gone. Such values are written verbatim, and the
  `.map` loader flags each mapping using them; the mapping wants splitting by hand.
* **Typed mappings.** A script may map objects — enum constants, numbers — where the model holds
  strings, so only their `toString()` form is converted. Code that relied on getting the objects
  back does not get them any more, and no format can express them, so each such mapping is flagged
  for rewriting in Java; see [What changed in IPF 6.0](#what-changed-in-ipf-60).
* **Comments.** A `.map` file is evaluated, not parsed, so its comments never reach the model.
  Several of them record the code system a code came from and are worth copying over.

Nothing has to be migrated at once. Loaders dispatch per file, so a `.map` file keeps working next
to converted ones until `ipf-commons-map-groovy` goes away in IPF 7.0.

## What changed in IPF 6.0

The whole mapping service was reimplemented. Nothing has to be migrated at once — the old API and
the old file format both still work — but nothing inside IPF uses either any more.

**Module split.** `ipf-commons-map` is now a parent POM. Depend on **`ipf-commons-map-core`**
instead, plus the module for each format you read: `ipf-commons-map-xml`, `ipf-commons-map-yaml`,
or `ipf-commons-map-groovy` for `.map` files and the Groovy mapping DSL. A mapping source whose
format has no loader on the classpath fails at load time naming the module to add.

**Deprecated, removed in 7.0.** `MappingService`, `BidiMappingService`,
`SpringBidiMappingService`, `SpringBidiMappingServiceConfigurer`, and the Groovy `.map` DSL
(`GroovyMappingLoader`, `MappingsBuilder`).

**Spring.** A context needs a **`Mappings` bean** where it previously needed a `MappingService`
one, because the DSL extensions resolve `Mappings` from the registry. Replace
`SpringBidiMappingService` with `SpringMappings`; wrap the former over the latter if some of your
code still needs the untyped service. Every context IPF ships does this.

**Typed mappings must be rewritten.** The Groovy service held whatever objects a `.map` script
evaluated to and returned them; the model holds strings only. A script mapping enum constants or
numbers still loads, but as the `toString()` form of every key, value and fallback result, so
`(AvailabilityStatus) mappingService.get(...)` now fails with a `ClassCastException`. No mapping
format can express such a mapping; rewrite it in Java, e.g. as a `switch` over the enum. The
`.map` loader logs a warning for each one it loads, and `MappingConverter` reports them.

**Composite values are gone.** The `~` convention of IPF 5.x — a `Collection` used as a key was
joined with `~`, and a value containing `~` came back as a `List` — no longer exists, neither in
the Groovy and Kotlin DSLs nor in `BidiMappingService`. A value containing `~` is now returned as
the one string it is, `map`/`mapReverse` on a `Collection` no longer exist, and
`BidiMappingService` rejects a `Collection` key with an `IllegalArgumentException`. Split such a
mapping into one mapping per component, or join and split the parts in the calling code:

    // IPF 5.x: a List ['2.16.840.1.113883.3.37.4.1.1.2', '411']
    [app, facility].map('device_OID')
    // IPF 6.0
    "${app}~${facility}".map('device_OID')?.split('~')

The `.map` loader logs a warning for each mapping with composite keys, values or fallbacks, and
`MappingConverter` reports them.

**Empty values are literal.** An entry mapping to `''` now always yields `''`; it never falls back
to the mapping's `ELSE` clause, and a default passed in applies only where there is no value at
all. The Groovy service reached a mapping's fallback through the `?:` operator, which treats `''`
as no value, so such an entry was never returned: with `'N' : ''` and `(ELSE) : 'IMP'`,
`'N'.map(…)` gave `'IMP'`, and without an `ELSE` it gave `null`; likewise, an empty result — also
an empty `ELSE` — gave way to the default passed to `map(mapping, default)` or
`get(mapping, key, default)`. This applies everywhere: to `Mappings`, the Groovy and Kotlin DSLs,
and the deprecated `BidiMappingService`. Check mappings with empty values whose callers relied on
the old result; where an entry was meant to fall back, remove it.

**Constructors.** `DefaultUriMapper` and `AuditRecordTranslator` take `Mappings`. Their
`MappingService` constructors remain, deprecated.

**`BidiMappingService` internals are gone**: the `map` and `reverseMap` properties and the
`protected retrieve`/`retrieveElse`/`updateReverseMap`/`checkMappingKey`/`joinKey`/`splitKey`
methods, and the configurable separator along with its constructors.

**The mappings IPF ships** are now XML — `hl7-v2-v3-translation.mapping.xml`,
`fhir-hl7v2-translation.mapping.xml`, `atna2fhir.mapping.xml` — with the `.map` originals kept
beside them for reference. Where several keys mapped onto one value the entries now declare an
`equivalence`, keeping the inverse the Groovy service happened to pick.

**One mapping was renamed.** `hl7v2v3-interactionId-eventStructure` had composite values
(`A01~ADT_A01`) and is split into `hl7v2v3-interactionId-triggerEvent` and
`hl7v2v3-interactionId-messageStructure`. `map('…eventStructure')[0]` becomes
`map('…triggerEvent')`.

**A bug went away.** A reverse fallback declared in one mapping file used to be destroyed as soon
as a second file was loaded — which happened to every shipped mapping whenever an application
registered a custom one. Indices are now built once per mapping, when it is registered.

## Writing a loader for another format

Implement `MappingLoader`, claim a file extension in `supports(URI)`, return a short format id
from `format()`, and list the class in
`META-INF/services/org.openehealth.ipf.commons.map.MappingLoader`. The id is what a caller names
to select the loader regardless of the source's name, so it has to be unique on the classpath —
qualify it the way `conceptmap-r4-json` does when a format has versions or encodings that a second
loader might read: one loader per id, one id per loader. `MappingWriter` is the counterpart, selected by its own `format()` or by the
extension it produces, which is what lets `MappingConverter` target formats it knows nothing
about.

Test it with the matchers the other loaders use, so a format's tests say what a mapping does rather
than which method they called to find out:

```xml
<dependency>
    <groupId>org.openehealth.ipf.commons</groupId>
    <artifactId>ipf-commons-map-core</artifactId>
    <version>${project.version}</version>
    <type>test-jar</type>
    <scope>test</scope>
</dependency>
```

```java
assertThat(mappings, translates("gender", "M").to("male"));
assertThat(mappings, doesNotTranslate("gender", "X"));
assertThat(mappings, translatesBack("gender", "male").to("M"));
assertThat(mappings, mapsBetween("gender", "2.16.840.1.113883.12.1", "http://hl7.org/fhir/administrative-gender"));
assertThat(mapping, hasUnmatched(Unmatched.fixed("UNK")));
assertThat(mapping.entries().get(2), hasEquivalence(Equivalence.NARROWER));
```

`MappingMatchers` tells the three ways a translation can fail apart — the mapping is not registered,
it has no entry for the key, or it answered something else — which is what an assertion on the
`Optional` alone cannot report. `OptionalMatchers` (`hasValue`, `hasNoValue`) lives one layer down in
the `ipf-commons-core` test jar, since nothing about it is specific to mappings.
