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
import ca.uhn.hl7v2.Version;
import ca.uhn.hl7v2.app.DefaultApplication;
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
 * @author Marek Vaclavic
 * @author Dmytro Rud
 */
class MessageUtils {
    
    private static DateTimeFormatter FMT = ISODateTimeFormat.basicDateTimeNoMillis()
    private static int INDENT_SIZE = 3
    private static defaultFactory = new DefaultModelClassFactory();


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
		Version actual = Version.versionOf(actualVersion)
		Version target = Version.versionOf(targetVersion)
		if (actual == null || target == null) {
			throw new IllegalArgumentException('unknown HL7 version')
		}
		return !target.isGreaterThan(actual);
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
     * 
     * @deprecated use {@link Type#encode()}
     */
    static def pipeEncode(Type t) {
		t.encode()
    }
    
    /**
     * @return ER7-formatted representation of the segment
     * 
     * @deprecated use {@link Segment#encode()}
     */
    static def pipeEncode(Segment s) {
        s.encode()
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
        Message ack = response(factory, msg, 'ACK', triggerEvent(msg))
        Terser terser = new Terser(ack)
        if (!('MSA' in ack.names)) ack.addNonstandardSegment('MSA', 1)
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
        Message ack = ack(factory, msg)
        if (!('ERR' in ack.names)) ack.addNonstandardSegment('ERR', 2)
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
        DefaultApplication.fillResponseHeader(msg.MSH, out.MSH)
        if ('MSA' in out.names) {
            Terser.set(out.MSA, 2, 0, 1, 1, Terser.get(msg.MSH, 10, 0, 1, 1))
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
        AbstractMessage msg = c.newInstance([factory]as Object[])
        msg.initQuickstart(eventType, triggerEvent, 'P')
        Terser.set(msg.MSH, 11, 0, 2, 1, 'T');
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
        msg.printStructure()
    }
    
    private static EncodingCharacters encodingCharacters(Message msg) {
        if (msg != null) {
            return EncodingCharacters.getInstance(msg)
        } else {
            return new EncodingCharacters('|' as char, '^~\\&')
        }
    }
}


