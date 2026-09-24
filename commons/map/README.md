# IPF Mapping Service 2.0

A mapping is a named translation table between two code systems. Examples are HL7v2 table 0001 to
`http://hl7.org/fhir/administrative-gender`, or an ATNA code system name to a FHIR code system URI.
The mapping service holds these mappings and looks codes up in either direction. It can also answer
for codes that have no entry.

In contrary to the now deprecated `MappingService`, there is one model, but there are several file formats. 
The format is decided per file: the loader that claims the file's extension reads it. 
You can therefore mix formats in one `Mappings` instance.
If a file name does not tell the format, you can name the format explicitly. See
[Selecting the format explicitly](#selecting-the-format-explicitly).

| Module                         | Format                                  | Format id                                 | Extension                                   | Validated by                               |
|--------------------------------|-----------------------------------------|-------------------------------------------|---------------------------------------------|--------------------------------------------|
| `ipf-commons-map-core`         | — the model, the API and the loader SPI |                                           |                                             |                                            |
| `ipf-commons-map-xml`          | IPF XML — **recommended default**       | `xml`                                     | `.mapping.xml`                              | XSD, as the file is read                   |
| `ipf-commons-map-yaml`         | IPF YAML                                | `yaml`                                    | `.mapping.yaml`, `.mapping.yml`             | strict binding; JSON Schema in the editor  |
| `ipf-commons-ihe-fhir-r4-core` | FHIR ConceptMap, R4                     | `conceptmap-r4-json`, `conceptmap-r4-xml` | `.conceptmap.r4.json`, `.conceptmap.r4.xml` | HAPI, and the FHIR validator if you run it |
| `ipf-commons-map-groovy`       | legacy Groovy DSL — **deprecated**      | `groovy`                                  | `.map`                                      | nothing                                    |

## Looking up mappings

```java
var mappings = Mappings.builder()
        .function("oidUri", code -> Character.isDigit(code.charAt(0)) ? "urn:oid:" + code : code)
        .load("classpath:/META-INF/map/atna2fhir.mapping.xml")
        .load("classpath:/my-own.mapping.yaml")
        .build();

Optional<String> gender = mappings.map("hl7v2fhir-patient-administrativeGender", "M");   // "male"
Optional<String> back   = mappings.mapReverse("hl7v2fhir-patient-administrativeGender", "male");  // "M"
```

You can also find a mapping by the code systems it translates between. A caller that holds a coded
value usually knows its code system, but not the name of a mapping. Some sources, such as a
ConceptMap, name their mappings themselves. For those, the code systems are the only practical way
to find a mapping.

```java
var mapping = mappings.mappingFor("2.16.840.1.113883.12.1",
                                  "http://hl7.org/fhir/administrative-gender").orElseThrow();
mappings.map(mapping.name(), "M");
```

Either code system may be `null`, which means "any". If several mappings translate between the
same pair, `mappingFor` fails, because picking one of them would be arbitrary. Use `mappingsFor` to
get all of them. Both methods scan all registered mappings. Resolve the mapping once and reuse it,
instead of resolving it for every message.

`Mappings` works with strings and returns `Optional`. An unknown *mapping name* is a programming
error and raises an `IllegalArgumentException`. An unknown *code* in a known mapping returns an
empty `Optional`.

Sometimes the caller builds a coded value from the answer. In that case, use `translate`. It
returns a `Translation` that holds the code, its code system and its display:

```java
mappings.translate("kdl-ihe-typecode|2025", "UB140101")
// Translation[code=UNK, system=http://terminology.hl7.org/CodeSystem/v3-NullFlavor, display=Unbekannt]
```

The system is the one of the mapping that actually answered. This can be a part of a composite
mapping, the target of a delegating fallback, or the key system for an identity fallback. So the
system can differ from the `valueSystem` of the mapping you asked. `translateReverse` does the same
in the other direction.

### SimpleMapping and CompositeMapping

`Mapping` is a sealed interface with two kinds:

* A `SimpleMapping` is a table. It has entries and a fallback for each direction.
* A `CompositeMapping` has no entries of its own. It asks other mappings, its `parts()`, one after
  the other. The first part that declares the code answers. If no part declares it, the first part
  whose fallback answers is used.

Lookups by name work the same for both kinds. So does `entries(name)`, which lists the entries of
all parts for a composite. The ConceptMap loader creates a composite for a resource with several
groups. No file format declares a composite, and no writer writes one. Write its parts instead. The
parts must be simple mappings, and they must be registered before the composite.

Both kinds have the same accessors, so code that inspects a mapping does not need to know its kind.
Some accessors only make sense for a table: `entries()`, `unmatched()`, `reverseUnmatched()` and
`reversible()`. A composite throws an `UnsupportedOperationException` for them. For a simple
mapping, `parts()` is empty.

### MappingService deprecation and changes

`MappingService`, `BidiMappingService` and `SpringBidiMappingService` are **deprecated as of 6.0
and may be removed in 7.0**. They still work for existing applications. They return the same
answers as `Mappings`, but return `null` where `Mappings` returns an empty `Optional`. 

IPF 5.x had two string conventions: an empty value counted as no value, and values could be composed with `~`.
Both conventions are gone. In addition, only mappings between Strings are supported now.
See [What changed in IPF 6.0](#what-changed-in-ipf-60).

You do not have to migrate everything at once. Create the untyped service on top of your existing
mappings. Both APIs then see the same content.

```java
var mappings = new SpringMappings();                          // or DefaultMappings
var mappingService = new SpringBidiMappingService(mappings);  // deprecated, but same content
```

### Usage of mappings within IPF itself

IPF itself has migrated mappings completely:

* `DefaultUriMapper` and `AuditRecordTranslator` take `Mappings`.
* The Groovy and Kotlin DSL extensions look up `Mappings` in the registry.
* Every Spring context that IPF ships declares a `SpringMappings` bean.

The untyped service remains only as a compatibility adapter. The Spring Boot starter still exposes
one, so that you can migrate your application one call site at a time.

`BidiMappingService.getMappings()` goes the other way. Use it where a call site has the old service
but needs the typed API.

## Model: What a `Mapping` declares

* **Entries.** An entry is a key/value pair. Both sides are strings, and the empty string is a
  valid code. An entry may also have a `keyDisplay` and a `valueDisplay`. These are the
  human-readable names of the codes. They are for information only, and nothing is looked up by
  its display. They exist for two reasons: 
 
  * a mapping read from a source that has displays keeps them. 
  * a caller that builds a `Coding` from a translation has a display to put in it.

* **Key system and value system.** These are formal identifiers, usually OIDs or URIs.

* **An equivalence per entry.** It is one of `equal`, `equivalent`, `wider`, `narrower`, `inexact`
  or `disjoint`. The values and their meaning are those of FHIR ConceptMap. As in FHIR, the
  equivalence says how the **value** (the target) relates to the **key** (the source). For example,
  `wider` means that the value is the wider concept: `other` is wider than `A` (adoption). A
  ConceptMap writes this entry as `wider` too.

  Only `equal` and `equivalent` entries are added to the reverse index. This allows several keys to
  share a value while the value still has exactly one inverse. If two invertible entries share a
  value, loading fails.

  A `disjoint` entry states that its key and value are explicitly **not** equivalent. Therefore it
  does not translate in either direction. Answering with its value would state the opposite of what
  the mapping says. The entry is still read and kept. This way it survives a round trip and
  documents the decision. However, its key is not among the mapping's `keys()`. For this key, the
  mapping's fallback applies, just as for any other key the mapping does not translate. FHIR draws
  the same line: the `result` of a `$translate` cannot be true for a `disjoint` match.

* **An `unmatched` fallback for each direction.** It applies if the source code is not defined 
  in the Mapping. It is one of six modes:

  | Mode                    | Answers with                                                |
  |-------------------------|-------------------------------------------------------------|
  | `absent` (the default)  | nothing                                                     |
  | `provided` / `identity` | the key itself                                              |
  | `fixed`                 | a constant                                                  |
  | `function`              | the result of a named Java function, which receives the key |
  | `delegate`              | whatever another mapping answers                            |
  | `fail`                  | it raises an error                                          |

  With `delegate`, a mapping can specialize another mapping without repeating it. Declare only the
  codes that differ, and hand over the rest. The other mapping is asked **in full**: first its
  entries, then its own fallback. So the last mapping in the chain decides the outcome. The chain
  ends at the first mapping whose fallback answers on its own.

  The `lookup()` method does not follow a delegation, because it reports what a mapping *declares*. Neither do
  `keys()` and `values()`.

  The mapping you delegate to must **already be loaded**. This makes it impossible to declare a
  cycle, because a cycle would need a forward reference. It also turns a typo into an error at
  startup. Each direction delegates separately. Delegating `unmatched` has no effect on `reverse`.

* **`reversible="false"`.** The mapping has no reverse index at all. Several keys may then share a
  value without declaring an equivalence.

* **`override="true"`.** This declaration replaces a mapping with the same name that was loaded
  earlier. Without it, a duplicate name is an error at load time.

## The Mapping formats

Each of the following sections shows the same mapping in a different format.

### IPF XML — `.mapping.xml`

```xml
<mappings xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:noNamespaceSchemaLocation="http://openehealth.org/schema/ipf-commons-map.xsd">
    <mapping name="hl7v2fhir-patient-administrativeGender"
             keySystem="2.16.840.1.113883.12.1"
             valueSystem="http://hl7.org/fhir/administrative-gender">
        <entry key="M" value="male"/>
        <entry key="F" value="female"/>
        <entry key="A" value="other" equivalence="wider"/>      <!-- not invertible -->
        <entry key="O" value="other"/>                          <!-- "other" reverses to O -->
        <entry key="U" value="unknown" keyDisplay="Unknown"/>   <!-- displays are optional -->
        <unmatched mode="fixed" value="other"/>
        <reverse>
            <unmatched mode="fixed" value="O"/>
        </reverse>
    </mapping>
</mappings>
```

The schema has no target namespace. An IDE therefore only needs `xsi:noNamespaceSchemaLocation` to
offer completion and to mark errors. It needs no plugin and no network access. The loader always
validates against its own copy of the schema, at `META-INF/ipf/mapping.xsd` in
`ipf-commons-map-xml`. It never uses the location that the document names. A misspelled attribute
causes an error at startup, with line and column.

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
      A: {value: other, equivalence: wider}
      O: other
      U: {value: unknown, keyDisplay: Unknown}
    unmatched: {fixed: other}
    reverse:
      unmatched: {fixed: O}
```

YAML is shorter than XML, and you can write comments next to the entries. This is useful for
mapping files that note the HL7 table each code comes from. Property names are checked strictly.
Writing `keysystem` instead of `keySystem` causes an error at startup, with line and column. The
JSON Schema at `META-INF/ipf/mapping-schema.json` describes the same rules for editors. It is not
used at runtime.

All values are strings. A YAML value that looks like a number is **rejected, not converted**.
Write `AGN: "1004"`, not `AGN: 1004`.

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
      {"code": "A", "target": [{"code": "other",   "equivalence": "wider"}]},
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

Some users already manage their terminology as FHIR ConceptMaps. They author them in Simplifier or
Forge, ship them in an Implementation Guide, and check them with the FHIR validator. IPF can read
these ConceptMaps directly, so they do not have to be copied into an internal table. HAPI does the
parsing. Both wire formats and all structural details of the resource are therefore supported.

ConceptMap is offered as a loader, but it is not IPF's own format. There are three reasons:

* It is verbose. The mapping above takes 9 lines of YAML, 13 lines of IPF XML and 24 lines of
  ConceptMap JSON.
* Comments have no natural place.
* ConceptMap has no fields for some parts of the model. These parts need FHIR extensions, so a
  ConceptMap that uses them is not fully portable either.

These are the extensions:

| Extension URL                               | On               | Value                                           | For                                    |
|---------------------------------------------|------------------|-------------------------------------------------|----------------------------------------|
| `.../StructureDefinition/mapping-name`      | `group`          | `valueString`                                   | the name to register the mapping under |
| `.../StructureDefinition/reversible`        | `group`          | `valueBoolean`                                  | `reversible="false"`                   |
| `.../StructureDefinition/reverse-unmapped`  | `group`          | nested `mode`, `code` / `function` / `delegate` | the reverse direction's fallback       |
| `.../StructureDefinition/unmapped-function` | `group.unmapped` | `valueString`                                   | a computed fallback                    |
| `.../StructureDefinition/unmapped-fail`     | `group.unmapped` | `valueBoolean`                                  | an unmapped code is an error           |
| `.../StructureDefinition/override`          | `group`          | `valueBoolean`                                  | `override="true"`                      |

All six URLs start with `http://openehealth.org/ipf`. `ConceptMapExtensions` defines their URLs and
structure once, for both the loader and the writer. They are plain extensions, not a HAPI custom
resource class.

ConceptMaps are loaded into `CompositeMapping`s, where each `group` in the ConceptMap represents one `part`
in the `CompositeMapping`.

A delegating fallback needs no extension. ConceptMap supports it natively as `unmapped.mode =
other-map` with a `url`. The last path segment of the `url` names the mapping to delegate to.

#### How mappings from a ConceptMap are named

If a `group` has no `mapping-name` extension, its mapping is named after the resource's canonical
URL. The `name` of the resource is not used, because publishers tend to change it between
releases. The rules are:

* The base name is the last segment of `url`. If there is no `url`, the loader uses `name`, and
  then `id`.
* If the resource has a `version`, it is appended as `|version`, as in a FHIR canonical reference.
  Without a version, the name has no trailing `|`.
* A resource with a single `group` becomes one mapping, named by the base name.
* In a resource with several groups, each `group` is named by the base name, followed by `#` and the
  last segment of its `target`. The base name itself names a composite of all groups, in
  declaration order.

No group is preferred over another, because FHIR gives their order no meaning. A caller that uses
a third-party ConceptMap does not need to know which groups it has, or which group holds a code.
The composite answers like `$translate` without a target system. `translate` tells you which
group's system the answer belongs to.

If all groups of a resource carry `mapping-name`, the resource holds exactly the mappings that its
groups name. No composite is added. `ConceptMapWriter` writes resources in this form.

With these rules, you can load several versions of one resource side by side. For example, the KDL
ConceptMap is published every year under the same canonical URL. It is loaded as
`kdl-ihe-typecode|2024` and `kdl-ihe-typecode|2025`. Each of them answers for both of its groups.
The groups, such as `kdl-ihe-typecode|2025#IHEXDStypeCode` and
`kdl-ihe-typecode|2025#v3-NullFlavor`, can also be asked on their own.

An `other-map` fallback refers to the canonical URL, optionally as `url|version`. It resolves to the
resource as a whole. A canonical URL without a version finds the only version that is registered.
For example, `…/kdl-ihe-typecode` finds `kdl-ihe-typecode|2025` if no other version is loaded. If
several versions are loaded, the reference is ambiguous and is rejected. The target is fixed when
the delegating mapping is registered. Loading another version later does not change it.

#### Writing ConceptMaps

`ConceptMapWriter` works in the other direction. You can maintain a mapping as a plain table and
publish it as a standard FHIR resource. Some mappings cannot be expressed as a ConceptMap. The
writer rejects them with an error instead of silently changing them:

* A mapping that uses the **empty string as a code**. A FHIR code is never empty, and HAPI would
  drop it. Two of the mappings that IPF ships have empty codes. They stay in XML format.
* A code with leading or trailing whitespace. FHIR trims it.
* A fallback that delegates to a mapping whose name contains `/`. The name must be the last path
  segment of a URL.

#### FHIR versions

The file extension names the FHIR version on purpose. A ConceptMap is not version-independent. A
loader for another FHIR version can claim `.conceptmap.r5.json` and share the classpath with the R4
loader. Currently only R4 is supported. R5 renamed `target.equivalence` to `target.relationship`
and changed its codes. For example, R5's `source-is-narrower-than-target` is R4's `wider`. Both map
onto the same `Equivalence`.

#### What the ConceptMap loader does not keep

ConceptMap can express more than the model. The loader converts what it can. It reports the rest
with a warning instead of dropping it silently:

* `relatedto` becomes `inexact`. `subsumes` becomes `wider`, and `specializes` becomes `narrower`.
* An element may have several targets. The loader picks the first `equal` or `equivalent` target.
  If there is none, it picks the first target that translates at all. Disjoint targets are kept as
  disjoint entries. Any further translating target is left out with a warning.
* A target with `dependsOn` or `product` is only valid under a condition that the model cannot
  express. It is also left out with a warning.

#### Example

The KDL ConceptMap of the DVMD translates German KDL document classes into IHE XDS type codes. It
has two groups with the same source. The first group maps most KDL codes onto an IHE type code. The
second group maps the KDL codes that have no suitable type code onto the null flavor `UNK`. This is
an excerpt with one or two elements per group:

```json
{
  "resourceType": "ConceptMap",
  "url": "http://dvmd.de/fhir/ConceptMap/kdl-ihe-typecode",
  "version": "2025",
  "status": "active",
  "group": [{
    "source": "http://dvmd.de/fhir/CodeSystem/kdl",
    "target": "http://ihe-d.de/CodeSystems/IHEXDStypeCode",
    "element": [
      {"code": "AD010101", "display": "Ärztliche Stellungnahme",
       "target": [{"code": "BERI", "display": "Arztberichte", "equivalence": "wider"}]},
      {"code": "AD010102", "display": "Durchgangsarztbericht",
       "target": [{"code": "BERI", "display": "Arztberichte", "equivalence": "wider"}]}
    ]
  }, {
    "source": "http://dvmd.de/fhir/CodeSystem/kdl",
    "target": "http://terminology.hl7.org/CodeSystem/v3-NullFlavor",
    "element": [
      {"code": "UB140101", "display": "Behördliche Genehmigung",
       "target": [{"code": "UNK", "display": "Unbekannt", "equivalence": "wider"}]}
    ]
  }]
}
```

The loader registers three mappings:

* `kdl-ihe-typecode|2025#IHEXDStypeCode` for the first group,
* `kdl-ihe-typecode|2025#v3-NullFlavor` for the second group,
* `kdl-ihe-typecode|2025`, the composite of both groups.

A caller asks the composite and does not need to know which group holds a code:

```java
mappings.translate("kdl-ihe-typecode|2025", "AD010101")
// Translation[code=BERI, system=http://ihe-d.de/CodeSystems/IHEXDStypeCode, display=Arztberichte]
mappings.translate("kdl-ihe-typecode|2025", "UB140101")
// Translation[code=UNK, system=http://terminology.hl7.org/CodeSystem/v3-NullFlavor, display=Unbekannt]
```

The system of each translation tells which group answered. Every target is `wider` than its KDL
code, so no entry is invertible. The reverse direction therefore answers nothing: `BERI` and `UNK`
do not translate back to a KDL code.

### Legacy Groovy DSL — `.map`

This format is deprecated in IPF 6.0 and may be removed in 7.0. A `.map` file is a Groovy
*script*. It is executed, not parsed, so nothing in it is checked before it runs:

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

`ipf-commons-map-groovy` also contains the Groovy metaclass extensions: `'M'.map('…gender')`,
`'male'.mapReverse('…gender')` and the dynamic form `mapAdministrativeGender()`. They work with
mappings in any format, not just `.map` files. They look up `Mappings` in the registry. A Spring
context therefore needs a `Mappings` bean (`SpringMappings`) instead of a `MappingService` bean.
The HL7v2 DSL in `ipf-modules-hl7` and `ipf-modules-hl7-kotlin` offers `map` and `mapReverse` on
HAPI `Type`s. These look up `Mappings` in the same way.

All of them return the string that the mapping declares, without changes. An entry that maps to
`''` returns `''`. A value that contains `~` is returned as one string, not as a `List` as in IPF 5.x.

## Selecting the format explicitly

Usually the loader is selected by the file extension, and no configuration is needed. This does not
work in every case:

* A ConceptMap fetched from a terminology server has no extension at all.
* A file may be called `gender.xml` for reasons outside IPF's control.
* Two loaders may be able to read the same extension.

In these cases, name the **format id**. The extension then no longer matters:

```java
var mappings = Mappings.builder()
        .load("classpath:/gender.mapping.xml")                                   // by extension
        .load("https://tx.example.org/ConceptMap/gender", "conceptmap-r4-json")  // by format id
        .load(url, new MyOwnLoader())                                            // or the loader itself
        .build();
```

If you name a format, `supports(URI)` is not called at all. The format id is not case-sensitive. If
no loader on the classpath implements the format, loading fails. The error message lists the
formats that are available.

Each id names exactly one loader. A format with several wire formats therefore has one id per
encoding, for example `conceptmap-r4-json` and `conceptmap-r4-xml`. If the name of a source tells
nothing, only the id says which encoding to expect. The loader therefore checks the content against
the id. Suppose a ConceptMap is read as `conceptmap-r4-json`, but it starts with `<`. The loader
rejects it and tells you to read it as `conceptmap-r4-xml`. You do not get a confusing parser error
about an unexpected token.

`MappingLoaders.formats()` lists the formats that the current classpath can read.
`MappingWriters.formats()` lists the formats it can write.

## Untrusted mapping sources

A mapping file is data. A loader reads from whatever location it is given. This may be a jar on the
classpath, but also a file in a configuration directory, or a ConceptMap from a terminology server.
The declarative loaders are designed to handle untrusted sources:

|                                                              | XML               | YAML                            | ConceptMap R4               |
|--------------------------------------------------------------|-------------------|---------------------------------|-----------------------------|
| DOCTYPE / entity expansion ("billion laughs")                | rejected outright | n/a                             | ignored                     |
| External entities, external DTDs, `xsi:schemaLocation` hints | never resolved    | n/a                             | never resolved              |
| Type tags naming a class to instantiate                      | n/a               | inert — read as text            | n/a                         |
| Aliases / anchors                                            | n/a               | never expanded into collections | n/a                         |
| Nesting                                                      | bounded           | bounded                         | bounded (1000)              |
| Duplicate keys                                               | schema-rejected   | rejected with line and column   | last wins (FHIR's own rule) |

Two limits apply to every format and every source:

* A mapping source is read with a connect timeout and a read timeout. A location that never answers
  therefore cannot block startup.
* At most 64 MiB of a source are read.

The YAML parser stops earlier, at 8 MiB. Scanning one very large scalar takes quadratic time. This
limit bounds the work that a hostile document can cause. All of this is covered by tests
(`*SecurityTest`). The tests are needed because the protection comes from the parser
configuration, not from the format itself.

**The Groovy `.map` loader is the exception. This is not a bug: a `.map` file is a script, and
loading it runs it.** The file can do anything the process can do. The loader is deprecated and
may be removed in IPF 7.0. Until then, only load files with it that you would also put on the
classpath. Convert all other files with `MappingConverter`.

## Spring

`SpringMappings` is a `Mappings` implementation that is configured with Spring `Resource`s. It is
the bean to wire. Each resource is dispatched by its file extension, so one list may contain
several formats:

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

`SpringMappings` itself collects every `CustomMappings` bean in the application context. You need
no configurer and no post processor. This is how an application adds its own mappings to the ones
IPF ships.

The beans are collected after all singletons have been created, and before the
`ContextRefreshedEvent`. The mappings are therefore complete when Camel builds its routes. They are
not yet complete inside a `@PostConstruct` method.

You can order several `CustomMappings` beans with Spring's `@Order` or `Ordered`. IPF's own
mappings are always read first. An application's mappings can therefore override them or delegate
to them. There are two ways to do this:

```xml
<!-- replace it: the later mapping wins wholesale, so it must restate everything it keeps -->
<mapping name="hl7v2fhir-patient-genderIdentity" override="true"> ... </mapping>

<!-- extend it: declare only what differs, and delegate the rest -->
<mapping name="genderIdentity-custom">
    <entry key="CUSTOM" value="non-binary"/>
    <unmatched mode="delegate" ref="hl7v2fhir-patient-genderIdentity"/>
</mapping>
```

`override` is the smaller change, but it is also easier to get wrong. Anything that the replaced
mapping declared and the replacement does not declare is lost. Delegation keeps the base mapping
unchanged and only needs the codes that differ. The cost is a second mapping name.

`mappingFunctions` is a property of `SpringMappings`, not of a `CustomMappings` bean. It is
therefore applied when the `SpringMappings` bean is created. This happens before any
`CustomMappings` bean adds a resource that refers to one of the functions.

`SpringMappings` enforces the constraints of the model. So does the deprecated
`SpringBidiMappingService`, which reads into a `SpringMappings`:

* A duplicate mapping name is an error, unless the later declaration says `override`.
* A value with two inverses is an error.

You can relax both rules with `allowOverride` and `allowReverseCollisions`. This is meant for
sources that cannot state their intent. The legacy `.map` loader relaxes the rules for its own
files. Script files therefore keep working under the strict defaults.

The Spring Boot starter exposes both beans. They share one set of mappings:

```java
@Autowired SpringMappings mappings;         // or just Mappings
@Autowired SpringBidiMappingService svc;    // deprecated, same content
```

## Computed fallbacks

The Groovy DSL allowed arbitrary `(ELSE)` closures. The only real need for them was a fallback that
computes its answer from the key. This is now `mode="function"`. The mode refers to a named
`Function<String,String>`:

```java
var mappings = Mappings.builder()
        .function("first4", key -> key.length() <= 4 ? key : key.substring(0, 4))
        .load("classpath:/devices.mapping.xml")
        .build();
```

There are three ways to register a function. They are listed from the narrowest to the widest
reach:

* `Mappings.builder().function(name, fn)` registers a function for a `Mappings` instance that you
  build yourself.
* `setMappingFunctions(Map)` is a bean property of `SpringMappings` and of the deprecated
  `BidiMappingService`. It is applied before any mapping resources are added.
* A `MappingFunctionProvider` service is shipped by a module together with its mapping file. The
  file then works wherever it is loaded. For example, `ipf-commons-ihe-fhir-r4-audit` provides the
  `oidUri` function for `atna2fhir.mapping.xml` in this way.

A provider implements one method. It adds its functions to the registry that it receives:

```java
package com.example.devices;

public class DeviceMappingFunctions implements MappingFunctionProvider {

    public static final String FIRST4 = "com.example.devices.first4";

    @Override
    public void register(MappingFunctionRegistry registry) {
        registry.register(FIRST4, DeviceMappingFunctions::first4);
    }

    // a function must tolerate a null argument
    static String first4(String key) {
        return key == null || key.length() <= 4 ? key : key.substring(0, 4);
    }
}
```

List the provider in `META-INF/services/org.openehealth.ipf.commons.map.MappingFunctionProvider`
in the same jar:

```
com.example.devices.DeviceMappingFunctions
```

Then refer to the function by name in the mapping file that the jar ships:

```xml
<mapping name="device-type">
    <entry key="PUMP-01" value="infusion-pump"/>
    <unmatched mode="function" ref="com.example.devices.first4"/>
</mapping>
```

You do not need to configure anything else. Every `Mappings` instance discovers the providers
before it reads its first mapping source. This applies to instances built with `Mappings.builder()`
and to `SpringMappings` beans. Discovery uses `ServiceLoader` with the thread context class loader.

All functions of a `Mappings` instance share one namespace. This is why the example uses a
qualified name. A function that you register explicitly, with `Mappings.builder().function(name,
fn)` or with `mappingFunctions`, replaces a provided function with the same name. You can use this
to replace a provided function, for example in a test.

If a mapping source refers to a function that is not registered, the source is rejected when it is
read.

## Migrating from `.map`

`MappingConverter` reads any format that a loader on the classpath can read. It writes any format
that a writer can produce:

```
java -cp ... org.openehealth.ipf.commons.map.MappingConverter [-f <format>] [--from <format>] <output-dir|-> <file-or-dir>...
```

* `-f` names the target format. You can give its id (`yaml`) or the extension it produces
  (`.mapping.yaml`). The default is `.mapping.xml`.
* `--from` names the format of the source files. Use it for files whose extension does not identify
  a format. Without it, each file is dispatched by its extension.

The output is a starting point, not a finished file. The converter makes this clear: every decision
it had to make is reported as a warning that names the mapping. Read the output before you commit
it. The warnings cover these cases:

* **Fallback closures.** The converter tries each closure with a test value. A closure that returns
  its argument becomes `mode="provided"`. A closure that returns a constant becomes `mode="fixed"`.
  Any other closure becomes `mode="function" ref="TODO-<mapping>"` with a warning. This test is a
  heuristic.
* **Reverse collisions.** Several keys may map onto one value. The Groovy service silently used the
  last of these keys as the inverse. The converter keeps this behaviour and marks the other entries
  as `wider`. It warns, so that you can choose the inverse deliberately.
* **Composite values.** The `~` convention no longer exists. Such values are written unchanged, and
  the `.map` loader reports each mapping that uses them. You need to split these mappings by hand.
* **Typed mappings.** A script may map objects, such as enum constants or numbers. The model only
  holds strings, so only their `toString()` form is converted. Code that expects to get the objects
  back no longer gets them. No format can express such objects. Each of these mappings is therefore
  reported, so that you can rewrite it in Java. See [What changed in IPF 6.0](#what-changed-in-ipf-60).
* **Comments.** A `.map` file is executed, not parsed, so its comments never reach the model. Some
  comments note the code system a code comes from. These are worth copying by hand.

You do not have to migrate everything at once. The loader is selected per file. A `.map` file
therefore keeps working next to converted files, until `ipf-commons-map-groovy` is removed.

## What changed in IPF 6.0

The mapping service was completely reimplemented. You do not have to migrate at once, because the
old API and the old file format both still work. However, IPF itself no longer uses either of them.

**Module split.** `ipf-commons-map` is now a parent POM. Depend on **`ipf-commons-map-core`**
instead. Also add the module for each format you read: `ipf-commons-map-xml`,
`ipf-commons-map-yaml`, or `ipf-commons-map-groovy` for `.map` files and the Groovy mapping DSL. If
no loader on the classpath can read a mapping source, loading fails. The error message names the
module to add.

**Deprecated, potentially removed in 7.0.** `MappingService`, `BidiMappingService`,
`SpringBidiMappingService`, `SpringBidiMappingServiceConfigurer`, and the Groovy `.map` DSL
(`GroovyMappingLoader`, `MappingsBuilder`).

**Spring.** A context now needs a **`Mappings` bean** instead of a `MappingService` bean. The
reason is that the DSL extensions look up `Mappings` in the registry. Replace
`SpringBidiMappingService` with `SpringMappings`. If some of your code still needs the untyped
service, create a `SpringBidiMappingService` on top of the `SpringMappings`. Every context that IPF
ships does this.

**Typed mappings must be rewritten.** The Groovy service kept the objects that a `.map` script
produced, and returned them. The model only holds strings. A script that maps enum constants or
numbers still loads. However, every key, value and fallback result becomes its `toString()` form.
As a result, `(AvailabilityStatus) mappingService.get(...)` now fails with a
`ClassCastException`. No mapping format can express such a mapping. Rewrite it in Java, for example
as a `switch` over the enum. The `.map` loader logs a warning for each such mapping, and
`MappingConverter` reports them.

**Composite values are gone.** IPF 5.x had a `~` convention. A `Collection` used as a key was joined
with `~`, and a value that contained `~` was returned as a `List`. This convention no longer exists,
neither in the Groovy and Kotlin DSLs nor in `BidiMappingService`:

* A value that contains `~` is now returned as a single string.
* `map` and `mapReverse` on a `Collection` no longer exist.
* `BidiMappingService` rejects a `Collection` key with an `IllegalArgumentException`.

Split such a mapping into one mapping per component. Alternatively, join and split the parts in the
calling code:

    // IPF 5.x: a List ['2.16.840.1.113883.3.37.4.1.1.2', '411']
    [app, facility].map('device_OID')
    // IPF 6.0
    "${app}~${facility}".map('device_OID')?.split('~')

The `.map` loader logs a warning for each mapping with composite keys, values or fallbacks.
`MappingConverter` reports them as well.

**Empty values are literal.** An entry that maps to `''` now always returns `''`. It never falls
back to the mapping's `ELSE` clause. A default value that you pass in only applies if there is no
value at all.

In IPF 5.x, the Groovy service reached a mapping's fallback through the `?:` operator. This operator
treats `''` as no value, so an entry with an empty value was never returned. For example, with
`'N' : ''` and `(ELSE) : 'IMP'`, `'N'.map(…)` returned `'IMP'`. Without an `ELSE`, it returned
`null`. In the same way, an empty result, including an empty `ELSE`, was replaced by the default
passed to `map(mapping, default)` or `get(mapping, key, default)`.

The new behavior applies everywhere: to `Mappings`, to the Groovy and Kotlin DSLs, and to the
deprecated `BidiMappingService`. Check your mappings with empty values, if callers rely on the old
result. If an entry was meant to fall back, remove it.

**Constructors.** `DefaultUriMapper` and `AuditRecordTranslator` take `Mappings`. Their
constructors that take a `MappingService` still exist, but are deprecated.

**`BidiMappingService` internals are gone.** This affects the `map` and `reverseMap` properties,
the protected methods `retrieve`, `retrieveElse`, `updateReverseMap`, `checkMappingKey`, `joinKey`
and `splitKey`, and the configurable separator together with its constructors.

**The mappings IPF ships are now XML.** These are `hl7-v2-v3-translation.mapping.xml`,
`fhir-hl7v2-translation.mapping.xml` and `atna2fhir.mapping.xml`. The `.map` originals are kept next
to them for reference. Where several keys map onto one value, the entries now declare an
`equivalence`. This keeps the inverse that the Groovy service happened to choose.

**One mapping was renamed.** `hl7v2v3-interactionId-eventStructure` had composite values such as
`A01~ADT_A01`. It is now split into `hl7v2v3-interactionId-triggerEvent` and
`hl7v2v3-interactionId-messageStructure`. Replace `map('…eventStructure')[0]` with
`map('…triggerEvent')`.

**A bug was fixed.** In IPF 5.x, loading a second mapping file destroyed the reverse fallbacks
declared in the first file. This happened to every shipped mapping as soon as an application
registered a custom one. Indices are now built once per mapping, when the mapping is registered.

## Writing a loader for another format

To add a loader for another format:

1. Implement `MappingLoader`.
2. Claim a file extension in `supports(URI)`.
3. Return a short format id from `format()`.
4. List the class in `META-INF/services/org.openehealth.ipf.commons.map.MappingLoader`.

Callers use the format id to select the loader, regardless of the source's name. The id must
therefore be unique on the classpath. Each id belongs to exactly one loader, and each loader has
exactly one id. If a format has versions or encodings that a second loader might read, qualify the
id, as `conceptmap-r4-json` does.

`MappingWriter` is the counterpart for writing. A writer is selected by its own `format()` or by the
extension it produces. This lets `MappingConverter` write formats it knows nothing about.

Test your loader with the same matchers as the other loaders. The tests then describe what a
mapping does, not which method they called to find out. Add this dependency:

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
assertThat(mapping.entries().get(2), hasEquivalence(Equivalence.WIDER));
```

A translation can fail in three ways. The mapping may not be registered, it may have no entry for
the key, or it may answer something else. `MappingMatchers` tells these cases apart. An assertion
on the `Optional` alone cannot do this. `OptionalMatchers` (`hasValue`, `hasNoValue`) is in the
`ipf-commons-core` test jar, one layer down, because nothing about it is specific to mappings.
