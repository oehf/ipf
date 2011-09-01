/*
 * Copyright 2008 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *     
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.modules.hl7.message

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormatter
import org.joda.time.format.ISODateTimeFormat
import org.openehealth.ipf.modules.hl7.AbstractHL7v2Exception
import org.openehealth.ipf.modules.hl7.AckTypeCode
import org.openehealth.ipf.modules.hl7.HL7v2Exception

import ca.uhn.hl7v2.HL7Exception
import ca.uhn.hl7v2.model.*
import ca.uhn.hl7v2.parser.*
import ca.uhn.hl7v2.util.DeepCopy
import ca.uhn.hl7v2.util.MessageIDGenerator
import ca.uhn.hl7v2.util.Terser

/**
 * This is a utility class that offers convenience methods for
 * accessing and creating HL7 messages. It's primarily used by
 * the HapiModelExtension to dynamically add the convenience
 * methods directly to the affected classes.
 * 
 * @author Christian Ohr
 * @author Marek Václavík
 * @author Dmytro Rud
 */
class MessageUtils {
    
    private static DateTimeFormatter FMT = ISODateTimeFormat.basicDateTimeNoMillis()
    private static int INDENT_SIZE = 3
    private static defaultFactory = new DefaultModelClassFactory();

    public static final List<String> HL7V2_VERSIONS = [
            '2.1',
            '2.2',
            '2.3',
            '2.3.1',
            '2.4',
            '2.5',
            '2.5.1',
            '2.6',
            '2.7',
    ]

    /**
     * Returns <code>true</code> when the HL7 version of the given message
     * is not smaller than the given one.
     */
    static boolean atLeastVersion(Message msg, String targetVersion) {
        return atLeastVersion(msg.version, targetVersion)
    }


    /**
     * Returns <code>true</code> when the given actual HL7 version
     * is not smaller than the target one.
     */
    static boolean atLeastVersion(String actualVersion, String targetVersion) {
        int actualIndex = HL7V2_VERSIONS.indexOf(actualVersion)
        int targetIndex = HL7V2_VERSIONS.indexOf(targetVersion)
        if ((actualIndex < 0) || (targetIndex < 0)) {
            throw new IllegalArgumentException('unknown HL7 version')
        }
        return (actualIndex >= targetIndex)
    }

    
    /**
     * @return Returns current time in HL7 format
     */
    static def String hl7Now() {
        FMT.print(new DateTime())[0..7, 9..14]
    }
    
    /**
     * @return Encodes a string using HL7 encoding definition taken
     * 	from the passed message
     */
    static def encodeHL7String(String s, Message msg) {
        Escape.escape(s, encodingCharacters(msg))
    }
    
    /**
     * @return ER7-formatted representation of the type
     */
    static def pipeEncode(Type t) {
        PipeParser.encode(t, encodingCharacters(t.message))
    }
    
    /**
     * @return ER7-formatted representation of the segment
     */
    static def pipeEncode(Segment s) {
        PipeParser.encode(s, encodingCharacters(s.message))
    }
    
    /**
     * @return event type of the message, e.g. 'ADT'
     */
    static String eventType(Message msg) {
        Terser.get(msg.MSH, 9, 0, 1, 1)
    }
    
    /**
     * @return trigger event of the message, e.g. 'A01'
     */
    static String triggerEvent(Message msg) {
        Terser.get(msg.MSH, 9, 0, 2, 1)
    }
    
    /**
     * @return structure of the message, e.g. 'ADT_A01'
     */
    static String messageStructure(Message msg) {
        def structName = eventType(msg) + '_' + triggerEvent(msg)
        if (atLeastVersion(msg, '2.4')) {
            structName = Parser.getMessageStructureForEvent(structName, msg.version)
        }
        structName
    }
    
    /*
     * @deprecated. Use {@link #ack(ModelClassFactory, Message)}.
     */
    @Deprecated
    static Message ack(Message msg) {
        ack(defaultFactory, msg)
    }
    
    /** 
     *  @return a positive ACK response message
     */
    static Message ack(ModelClassFactory factory, Message msg) {
        ack(factory, msg, AckTypeCode.AA)
    }
    
    /**
     *  @return a positive ACK response message
     */
    static Message ack(ModelClassFactory factory, Message msg, AckTypeCode ackType ) {
        def ack = response(factory, msg, 'ACK', triggerEvent(msg))
        Terser terser = new Terser(ack)
        terser.set("MSA-1", ackType.name());
        ack
    }
    
    /*
     * @deprecated. Use {@link #nak(ModelClassFactory, Message, String, AckTypeCode)}.
     */
    @Deprecated
    static Message nak(Message msg, String cause, AckTypeCode ackType) {
        nak(defaultFactory, msg, cause, ackType)
    }
    
    /** 
     *  @return a negative ACK response message using a String cause
     */
    static Message nak(ModelClassFactory factory, Message msg, String cause, AckTypeCode ackType) {
        HL7v2Exception e = new HL7v2Exception(cause)
        nak(factory, msg, e, ackType)
    }
    
    /*
     * @deprecated. Use {@link #nak(ModelClassFactory, Message, Exception, AckTypeCode)}.
     */
    @Deprecated
    static Message nak(Message msg, AbstractHL7v2Exception e, AckTypeCode ackType) {
        nak(defaultFactory, msg, e, ackType)
    }
    
    /** 
     *  @return a negative ACK response message using an Exception cause
     */
    static Message nak(ModelClassFactory factory, Message msg, AbstractHL7v2Exception e, AckTypeCode ackType) {
        def ack = ack(factory, msg)
        e.populateMessage(ack, ackType)
        ack
    }
    
    /** 
     *  @return a negative ACK response message constructed from scratch
     */
    static Message defaultNak(
            AbstractHL7v2Exception e,
            AckTypeCode ackType,
            String version,
            String sendingApplication,
            String sendingFacility,
            String msh9)
    {
        def cause = encodeHL7String(e.message, null)
        def now = hl7Now()
        
        def cannedNak = "MSH|^~\\&|${sendingApplication}|${sendingFacility}|unknown|unknown|$now||${msh9}|unknown|T|$version|\r" +
                "MSA|AE|MsgIdUnknown|$cause|\r"
        def nak = new GenericParser().parse(cannedNak)
        e.populateMessage(nak, ackType)
        nak
    }
    
    /** 
     *  @return a negative ACK response message constructed from scratch
     */
    static Message defaultNak(AbstractHL7v2Exception e, AckTypeCode ackType, String version) {
        defaultNak(e, ackType, version, 'unknown', 'unknown', 'ACK')
    }
    
    /** 
     *  @return a new message of the given event and version args[0]
     */
    static Message newMessage(ModelClassFactory factory, String event, String version) {
        if (version) {
            def list = event.tokenize('_')
            def eventType = list[0]
            def triggerEvent = list[1]
            return makeMessage(factory, eventType, triggerEvent, version)
        } else {
            throw new HL7v2Exception("Must have valid version to create message")
        }
    }
    
    /*
     * @deprecated
     */
    @Deprecated
    static Message response(Message msg, String eventType, String triggerEvent) {
        response(defaultFactory, msg, eventType, triggerEvent)
    }
    
    /** 
     *  @return a response message with the basic MSH fields already populated
     */
    static Message response(ModelClassFactory factory, Message msg, String eventType, String triggerEvent) {
        
        // make message of correct version
        def version = msg.version
        if (! triggerEvent) {
            triggerEvent = Terser.get(msg.MSH, 9, 0, 2, 1)
        }
        Message out = makeMessage(factory, eventType, triggerEvent, version)
        
        //populate outbound MSH using data from inbound message ...
        Terser outTerser = new Terser(out)
        def msh = msg.MSH
        outTerser.set('MSH-1', Terser.get(msh, 1, 0, 1, 1))
        outTerser.set('MSH-2', Terser.get(msh, 2, 0, 1, 1))
        
        outTerser.set('MSH-3-1', Terser.get(msh, 5, 0, 1, 1))
        outTerser.set('MSH-3-2', Terser.get(msh, 5, 0, 2, 1))
        outTerser.set('MSH-3-3', Terser.get(msh, 5, 0, 3, 1))
        
        outTerser.set('MSH-4-1', Terser.get(msh, 6, 0, 1, 1))
        outTerser.set('MSH-4-2', Terser.get(msh, 6, 0, 2, 1))
        outTerser.set('MSH-4-3', Terser.get(msh, 6, 0, 3, 1))
        
        outTerser.set('MSH-5-1', Terser.get(msh, 3, 0, 1, 1))
        outTerser.set('MSH-5-2', Terser.get(msh, 3, 0, 2, 1))
        outTerser.set('MSH-5-3', Terser.get(msh, 3, 0, 3, 1))
        
        outTerser.set('MSH-6-1', Terser.get(msh, 4, 0, 1, 1))
        outTerser.set('MSH-6-2', Terser.get(msh, 4, 0, 2, 1))
        outTerser.set('MSH-6-3', Terser.get(msh, 4, 0, 3, 1))
        
        outTerser.set('MSH-11', Terser.get(msh, 11, 0, 1, 1))

        if (out.get("MSA") != null) {
            outTerser.set('MSA-2', Terser.get(msh, 10, 0, 1, 1))
        }
        
        out
    }
    
    public static Message makeMessage(ModelClassFactory factory, String eventType, String triggerEvent, String version) {
        def structName
        Message result;

        if (eventType == 'ACK') {
            structName = 'ACK'
        } else {
            structName = "${eventType}_${triggerEvent}"
            if (atLeastVersion(version, '2.4')) {
                structName = Parser.getMessageStructureForEvent(structName, version)
            }
        }
        
        Class c = factory.getMessageClass(structName, version, true);
        if (!c) {
            throw new HL7v2Exception("Can't instantiate message ${structName}",
                HL7Exception.UNSUPPORTED_MESSAGE_TYPE)
        }
        Message msg = c.newInstance([factory]as Object[])
        Terser terser = new Terser(msg)
        terser.set('MSH-1', '|')
        terser.set('MSH-2', '^~\\&')
        terser.set('MSH-7', hl7Now())
        terser.set("MSH-9-1", eventType)
        terser.set("MSH-9-2", triggerEvent)
        if (atLeastVersion(version, '2.4')) {
            terser.set("MSH-9-3", structName)
        }
        terser.set('MSH-10', MessageIDGenerator.getInstance().getNewID())
        terser.set('MSH-11-1', 'P')
        terser.set('MSH-11-2', 'T')
        terser.set('MSH-12', version)
        msg
    }
    
    static Segment newSegment(ModelClassFactory factory, String name, Message message) {
        Class c = factory.getSegmentClass(name, message?.version);
        if (!c) {
            throw new HL7v2Exception("Can't instantiate Segment $name")
        }
        Segment segment = c.newInstance([message, factory]as Object[])
        segment
    }
    
    static Group newGroup(ModelClassFactory factory, String name, Message message) {
        Class c = factory.getGroupClass(name, message?.version);
        if (!c) {
            throw new HL7v2Exception("Can't instantiate Group $name")
        }
        Group group = c.newInstance([message, factory]as Object[])
        group
    }
    
    static Primitive newPrimitive(ModelClassFactory factory, String name, Message message, String value) {
        Class c = factory.getTypeClass(name, message?.version);
        if (!c) {
            throw new HL7v2Exception("Can't instantiate Type $name")
        }
        AbstractPrimitive primitive = c.newInstance([message]as Object[])
        if (value) {
            primitive.setValue(value)
        }
        primitive
    }
    
    static Composite newComposite(ModelClassFactory factory, String name, Message message, Map map) {
        Class c = factory.getTypeClass(name, message?.version);
        if (!c) {
            throw new HL7v2Exception("Can't instantiate Type $name")
        }
        Composite composite = c.newInstance([message]as Object[])
        map?.each { k, v ->
            Type type = composite."$k"
            if ((type instanceof Primitive) && (v instanceof String)) {
                type.setValue(v)
            } else {
                DeepCopy.copy(v, type)
            }
        }
        composite
    }
    
    /** 
     *  @return a hierarchical dump of the message
     */
    static String dump(Message msg) {
        def version = msg.version
        StringBuffer buf = new StringBuffer("${msg.class.simpleName} Version $version\n")
        dumpStructure(msg, buf, INDENT_SIZE).toString()
    }
    
    
    private static def dumpStructure(Group group, StringBuffer buf, int indent) {
        group.names.each { name ->
            if (group.isRepeating(name))  {
                group.getAll(name).eachWithIndex { structure, i ->
                    buf << ' ' * indent << "${structure.class.simpleName}($i)\n"
                    dumpStructure(structure, buf, indent + INDENT_SIZE)
                }
            } else {
                def structure = group.get(name)
                buf << ' ' * indent << "${structure.class.simpleName}\n"
                dumpStructure(structure, buf, indent + INDENT_SIZE)
            }
        }
        buf
    }
    
    private static def dumpStructure(Segment segment, StringBuffer buf, int indent) {
        if (segment.numFields() > 0) {
            (1..segment.numFields()).each { i ->
                if (segment.getMaxCardinality(i) > 1) {
                    segment.getField(i).eachWithIndex { type, j ->
                        String encoded = pipeEncode(type)
                        buf << ' ' * indent << "${segment.name}[$i]($j) ${type.class.simpleName} \"$encoded\"\n"
                        dumpType(type, true, buf, indent + INDENT_SIZE)
                    }
                } else {
                    Type type = segment.getField(i, 0)
                    String encoded = pipeEncode(type)
                    buf << ' ' * indent << "${segment.name}[$i] ${type.class.simpleName} \"$encoded\"\n"
                    dumpType(type, false, buf, indent + INDENT_SIZE)
                }
            }
        }
        buf
    }
    
    
    private static def dumpType(Type type, boolean isRepeating, StringBuffer buf, int indent) {
        if (type instanceof Varies) {
            def varies = (Varies) type
            buf << ' ' * indent << "${varies.data.class.simpleName}\n"
            dumpType(type.data, isRepeating, buf, indent + INDENT_SIZE);
        } else if (type instanceof Composite) {
            type.components.eachWithIndex { it, i ->
                buf << ' ' * indent << "${type.name}[$i] (${it.class.simpleName})\n"
                dumpType(it, isRepeating, buf, indent + INDENT_SIZE)
            }
        } else {
            buf << ' ' * indent << "${type.value}\n"
        }
        
        /*            if (type.extraComponents.numComponents() > 0) {
         DefaultMutableTreeNode newNode = new DefaultMutableTreeNode("ExtraComponents");
         treeParent.insert(newNode, treeParent.getChildCount());
         for (int i = 0; i < messParent.getExtraComponents().numComponents(); i++) {
         DefaultMutableTreeNode variesNode = new DefaultMutableTreeNode("Varies");
         newNode.insert(variesNode, i);
         addChildren(messParent.getExtraComponents().getComponent(i), variesNode);
         }
         }
         */        
        buf
    }
    
    
    private static EncodingCharacters encodingCharacters(Message msg) {
        if (msg != null) {
            String fieldSepString = msg.MSH.fieldSeparator
            char fieldSep = fieldSepString ? fieldSepString as char : '|'
            String encCharString = msg.MSH.encodingCharacters
            return new EncodingCharacters(fieldSep, encCharString);
        } else {
            return new EncodingCharacters('|' as char, '^~\\&')
        }
    }
}


