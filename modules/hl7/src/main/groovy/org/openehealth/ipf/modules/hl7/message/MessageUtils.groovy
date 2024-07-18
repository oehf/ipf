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

import ca.uhn.hl7v2.*
import ca.uhn.hl7v2.model.*
import ca.uhn.hl7v2.parser.*
import ca.uhn.hl7v2.util.DeepCopy
import ca.uhn.hl7v2.util.ReflectionUtil
import ca.uhn.hl7v2.util.Terser
import org.openehealth.ipf.modules.hl7.HL7v2Exception

import java.lang.reflect.Constructor

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

/**
 * This is a utility class that offers convenience methods for
 * accessing and creating HL7 messages. It's primarily used by
 * the {@link org.openehealth.ipf.modules.hl7.extend.Hl7ExtensionModule} to dynamically add the convenience
 * methods directly to the affected classes.
 * 
 * @author Christian Ohr
 * @author Marek Vaclavik
 * @author Dmytro Rud
 */
class MessageUtils {
    
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyyMMddHHmmss")
    private static final Escaping ESCAPE = org.openehealth.ipf.modules.hl7.parser.DefaultEscaping.INSTANCE
    private static final Parser PARSER

    static {
        ParserConfiguration config = new ParserConfiguration()
        config.escaping = ESCAPE
        HapiContext context = new DefaultHapiContext()
        context.parserConfiguration = config
        PARSER = context.getGenericParser()
    }


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
        return atLeastVersion(Version.versionOf(actualVersion), Version.versionOf(targetVersion))
    }

    /**
     * Returns <code>true</code> when the given actual HL7 version
     * is not smaller than the target one.
     */
    static boolean atLeastVersion(Version actualVersion, Version targetVersion) {
        if (actualVersion == null || targetVersion == null) {
            throw new IllegalArgumentException('unknown HL7 version')
        }
        return !(targetVersion == actualVersion || targetVersion.isGreaterThan(actualVersion))
    }
    
    /**
     * @return Returns current time in HL7 format
     */
    static String hl7Now() {
        ZonedDateTime.now().format(FMT)
    }
    
    /**
     * @return Encodes a string using HL7 encoding definition taken
     * 	from the passed message
     */
    static String encodeHL7String(String s, Message msg) {
        Escaping escaping = msg?.parser?.parserConfiguration?.escaping ?: ESCAPE
        escaping.escape(s, encodingCharacters(msg))
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
    static String messageStructure(String messageType, String triggerEvent, String version, ModelClassFactory factory) {
        def structName = "${messageType}_${triggerEvent}"
        return factory.getMessageStructureForEvent(structName, Version.versionOf(version)) ?: structName
    }

    /**
     * @return structure of the message, e.g. 'ADT_A01'
     */
    static String messageStructure(Message msg) {
        return messageStructure(eventType(msg), triggerEvent(msg), msg.version, msg.parser.hapiContext.modelClassFactory)
    }

    /** 
     *  @return a negative ACK response message constructed from scratch
     */
    static Message defaultNak(
            HL7Exception e,
            AcknowledgmentCode ackCode,
            String version,
            String sendingApplication,
            String sendingFacility,
            String msh9) {
        def cause = encodeHL7String(e.message ?: e.class.simpleName, null)
        def now = hl7Now()
        def cannedNak = "MSH|^~\\&|${sendingApplication}|${sendingFacility}|unknown|unknown|$now||${msh9}|unknown|T|$version|\r" +
                "MSA|AE|MsgIdUnknown|$cause|\r" +
                "ERR|\r"

        def nak = PARSER.parse(cannedNak)
        e.populateResponse(nak, ackCode, 0)
        nak
    }
    
    /** 
     *  @return a negative ACK response message constructed from scratch
     */
    static Message defaultNak(HL7Exception e, AcknowledgmentCode ackCode, String version) {
        defaultNak(e, ackCode, version, 'unknown', 'unknown', 'ACK')
    }
    
    /** 
     *  @return a new message of the given event and version args[0]
     */
    static Message newMessage(HapiContext context, String event, String version) {
        if (version) {
            def list = event.tokenize('_')
            def eventType = list[0]
            def triggerEvent = list[1]
            return makeMessage(context, eventType, triggerEvent, version)
        } else {
            throw new HL7Exception("Must have valid version to create message")
        }
    }

    
    /** 
     *  @return a response message with the basic MSH fields already populated
     */
    static Message response(Message msg, String eventType, String triggerEvent) {
        
        // make message of correct version
        if (!triggerEvent) {
            triggerEvent = Terser.get(msg.MSH, 9, 0, 2, 1)
        }
        String v = msg.version
        Message out = makeMessage(msg.parser.hapiContext, eventType, triggerEvent, v)

        //populate outbound MSH using data from inbound message ...
        Segment mshIn = msg.MSH
        Segment mshOut = out.MSH

        // get MSH data from incoming message ...
        String fieldSep = Terser.get(mshIn, 1, 0, 1, 1)
        String encChars = Terser.get(mshIn, 2, 0, 1, 1)
        String procID = Terser.get(mshIn, 11, 0, 1, 1)

        // populate outbound MSH using data from inbound message ...
        Terser.set(mshOut, 1, 0, 1, 1, fieldSep)
        Terser.set(mshOut, 2, 0, 1, 1, encChars)
        Terser.set(mshOut, 11, 0, 1, 1, procID)

        // revert sender and receiver
        Terser.set(mshOut, 3, 0, 1, 1, Terser.get(mshIn, 5, 0, 1, 1))
        Terser.set(mshOut, 4, 0, 1, 1, Terser.get(mshIn, 6, 0, 1, 1))
        Terser.set(mshOut, 5, 0, 1, 1, Terser.get(mshIn, 3, 0, 1, 1))
        Terser.set(mshOut, 6, 0, 1, 1, Terser.get(mshIn, 4, 0, 1, 1))

        if ('MSA' in out.names) {
            Terser.set(out.MSA, 2, 0, 1, 1, Terser.get(msg.MSH, 10, 0, 1, 1))
        }
        out
    }

    static Message empty(Message message) {
        newInstance(message, message.parser.hapiContext.modelClassFactory)
    }

    /**
     * Copies the message by rendering and reparsing it. Consider using {@link AbstractMessage#copy()} for copying using reflection
     * @param source message
     * @return message copy
     */
    static Message copyMessage(Message source) {
        source.parser.parse(source.encode())
    }

    // Helpers

    private static Group newInstance(Group group, ModelClassFactory factory) {
        Constructor constructor = group?.class?.constructors?.find {
            it.parameterTypes?.size() == 1 && it.parameterTypes[0] == ModelClassFactory.class
        }
        (constructor && factory) ? constructor.newInstance(factory) : newInstance(group)
    }

    private static Group newInstance(Group group) {
        group.class.getConstructor().newInstance()
    }

    /**
     * Creates a message using the provided HapiContext and message type parameters. The MSH segment of
     * the message is already initialized
     *
     * @param context HapiContext
     * @param eventType event type, e.g. ADT
     * @param triggerEvent trigger event, e.g. A01
     * @param version version, e.g. 2.3.1
     * @return new message
     */
    static Message makeMessage(HapiContext context, String eventType, String triggerEvent, String version) {
        ModelClassFactory factory = context.modelClassFactory

        def structName = (eventType == 'ACK') ? 'ACK' :
                messageStructure(eventType, triggerEvent, version, factory)

        Class<? extends Message> c = factory.getMessageClass(structName, version, true)
        if (!c) {
            HL7Exception e = new HL7Exception("Can't instantiate message ${structName}", ErrorCode.UNSUPPORTED_MESSAGE_TYPE)
            throw new HL7v2Exception(e)
        }
        AbstractMessage msg = ReflectionUtil.instantiateMessage(c, factory)
        msg.setParser(context.getGenericParser())
        msg.initQuickstart(eventType, triggerEvent, 'P')
        Terser.set(msg.MSH, 11, 0, 2, 1, 'T')
        msg
    }

    /**
     * Creates a new segment for the provided message
     *
     * @param name segment name
     * @param message message for which the segment shall be created
     * @return new segment
     */
    static Segment newSegment(String name, Message message) {
        HapiContext context = message.getParser().getHapiContext()
        Class<? extends Segment> c = context.modelClassFactory.getSegmentClass(name, message?.version)
        if (!c) {
            throw new HL7Exception("Can't instantiate Segment $name")
        }
        ReflectionUtil.instantiateStructure(c, message, context.modelClassFactory)
    }

    /**
     * Creates a new group for the provided message
     *
     * @param name group name
     * @param message message for which the group shall be created
     * @return new group
     */
    static Group newGroup(String name, Message message) {
        HapiContext context = message.getParser().getHapiContext()
        Class<? extends Group> c = context.modelClassFactory.getGroupClass(name, message?.version)
        if (!c) {
            throw new HL7Exception("Can't instantiate Group $name")
        }
        ReflectionUtil.instantiateStructure(c, message, context.modelClassFactory)
    }

    /**
     * Creates a new primitive for the provided message. Example: newPrimitive('SI', msg, '1')
     *
     * @param name primitive name
     * @param message message for which the primitive shall be created
     * @param value primitive value
     * @return new primitive
     */
    static Primitive newPrimitive(String name, Message message, String value) {
        HapiContext context = message.getParser().getHapiContext()
        Class<? extends Type> c = context.modelClassFactory.getTypeClass(name, message?.version)
        if (!c) {
            throw new HL7Exception("Can't instantiate Type $name")
        }
        AbstractPrimitive primitive = c.newInstance([message]as Object[])
        if (value) {
            primitive.setValue(value)
        }
        primitive
    }

    /**
     * Creates a new composite for the provided message. The provided map contains
     * entries, which represent the component values, where the component name is the
     * key of an entry. Example: newComposite('CE', msg, [identifier:'BRO'])
     *
     * @param name composite name
     * @param message message for which the composite shall be created
     * @param map the map with the values for the composite
     * @return new composite
     */
    static Composite newComposite(String name, Message message, Map map) {
        HapiContext context = message.getParser().getHapiContext()
        Class c = context.modelClassFactory.getTypeClass(name, message?.version)
        if (!c) {
            throw new HL7Exception("Can't instantiate Type $name")
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

    // Some stuff rescued from MessageAdapters, creating HL7 messages from resources/streams

    static Message load(HapiContext context, String resource) {
        make(context, getClass().classLoader.getResource(resource)?.text)
    }

    static Message load(HapiContext context, String resource, String charset) {
        make(context, getClass().classLoader.getResource(resource)?.getText(charset))
    }

    static Message make(HapiContext context, InputStream stream) {
        make(context, stream?.text)
    }

    static Message make(HapiContext context, InputStream stream, String charset) {
        make(context, stream?.getText(charset))
    }

    static Message make(HapiContext context, String message) {
        if (!message) {
            return null
        }
        context.genericParser.parse(message)
    }


    private static EncodingCharacters encodingCharacters(Message msg) {
        if (msg != null) {
            return EncodingCharacters.getInstance(msg)
        } else {
            return new EncodingCharacters('|' as char, '^~\\&')
        }
    }


}


