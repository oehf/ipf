
## HL7 v2 segment fragmentation in MLLP-based IPF IHE components

This feature corresponds to Section 2.10.2.1 (Segment fragmentation/continuation using the ADD segment) 
of the HL7 v2.5 specification, is supported for both incoming and outgoing messages on both producer and 
consumer sides, and controlled by the following endpoint URI parameters:

### Parameters

| Parameter name                  | Type       | Default value | Short description                                            |
|:--------------------------------|:-----------|:--------------|:-----------------
| `supportSegmentFragmentation`   | boolean    | false         | whether segment fragmentation should be supported by the given endpoint |
| `segmentFragmentationThreshold` | int | -1 | threshold (maximal count of characters  per segment) for segment fragmentation.  Values smaller than 5 lead to no segment fragmentation. |

As stated in the table above, segment fragmentation support can be activated by setting the URL parameter `supportSegmentFragmentation` 
of the corresponding endpoint to `true`.  For outgoing messages, the additional parameter `segmentFragmentationThreshold` should be set 
to an integer value greater on equal to 5. It denotes the maximal allowed length of segments in outgoing messages, without consideration 
of segment separators `'\r'`. 

### Example

When `segmentFragmentationThreshold` equals to 10, the message

```
MSH|^~\&|MESA_PD_CONSUMER|MESA_DEPARTMENT|MESA_PD_SUPPLIER|PIM|20081031112704||QBP^Q22|324406609|P|2.5|||ER
QPD|IHE PDQ Query|1402274727|@PID.3.1^12345678~@PID.3.2.1^BLABLA~'@PID.3.4.2^1.2.3.4~@PID.3.4.3^KRYSO|||||
RCP|I|10^RD|||||
```

will be sent as

```
MSH|^~\&|M
ADD|ESA_PD
ADD|_CONSU
ADD|MER|ME
ADD|SA_DEP
ADD|ARTMEN
ADD|T|MESA
ADD|_PD_SU
ADD|PPLIER
ADD||PIM|2
ADD|008103
ADD|111270
ADD|4||QBP
ADD|^Q22|3
ADD|244066
ADD|09|P|2
ADD|.5|||E
ADD|R
QPD|IHE PD
ADD|Q Quer
ADD|y|1402
.....
```

Note that segment fragmentation across messages (described in Section 2.10.2.3 of the HL7 v2.5 specification) is not supported yet.

### Combining segment fragmentation with other features

Segment fragmentation can be combined with both [unsolicited message fragmentation] and [interactive message continuation].  
Note that in case of outgoing request messages, unsolicited message fragmentation is performed *before* segment fragmentation, 
therefore the resulting count of segments can be actually greater than the value of the `unsolicitedFragmentationThreshold` parameter.




[unsolicited request fragmentation]: unsolicitedFragmentation.html
[interactive response continuation]: interactiveContinuation.html