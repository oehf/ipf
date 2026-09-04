---
name: ihe-transaction
description: How an IHE transaction is assembled across commons/ and platform-camel/ — port types, audit strategies, components, services, and the Camel endpoint scheme registration contract. Use when adding, changing, or tracing an IHE transaction (ITI-*, RAD-*, PHARM-*).
---

# How an IHE transaction is wired

Each transaction (identified by its IHE code, e.g. ITI-18) is assembled from parts spread across the two layers.
Use an existing transaction as a template — the file layout is highly regular.

**In `commons/ihe/<family>/`:**
- A profile class per IHE profile (`XDS.java`, `XCA.java`, `RMU.java`, `PHARM`/`CMPD.java`, …) implementing
  `XdsIntegrationProfile` / `IntegrationProfile`, with a nested `Interactions` enum whose constants are the
  `InteractionId`s (`XDS.Interactions.ITI_18`). Each constant carries a static
  `WsTransactionConfiguration` / `FhirTransactionConfiguration` / `Hl7v2TransactionConfiguration` describing
  service QName, port type, binding, WSDL location, audit strategies (client and server), and feature flags.
- A per-transaction package `iti18/` containing the JAX-WS `PortType` interface and `Iti18AuditStrategy`
  (or separate client/server strategies).
- Audit datasets in `core/audit/` extending the family's base dataset.

**In `platform-camel/ihe/<family>/`:**
- An `Iti18Component` extending the family component (`XdsComponent`), constructed with the `InteractionId`,
  creating an endpoint and a producer.
- An `Iti18Service` implementing the port type, delegating into the Camel route.
- A file in `src/main/resources/META-INF/services/org/apache/camel/component/<scheme>` registering the Camel
  endpoint scheme. **The scheme name is the contract**: `xds-iti18`, `xca-iti38`, `mhd-iti65`, `pdqm-iti78`,
  `pix-iti8`, `xdsi-rad69`, `cmpd-pharm1`, and `-async-response` variants for WS-Addressing async responses.
  A new transaction is not reachable from a route until this file exists.

`InterceptableComponent` / `InterceptableEndpoint` / `Interceptor` in `platform-camel/ihe/core` provide the
interceptor chain that all IHE endpoints share (audit, payload logging, validation hooks); interceptor order
matters and is managed through `InterceptorSupport` chain positions.
