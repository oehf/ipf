/*
 * Copyright 2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.commons.ihe.ech

import jakarta.xml.bind.JAXBContext
import org.openehealth.ipf.commons.ihe.ech.stub.ech_0006._2.ObjectFactory as Ech0006ObjectFactory
import org.openehealth.ipf.commons.ihe.ech.stub.ech_0007._5.ObjectFactory as Ech0007ObjectFactory
import org.openehealth.ipf.commons.ihe.ech.stub.ech_0008._3.ObjectFactory as Ech0008ObjectFactory
import org.openehealth.ipf.commons.ihe.ech.stub.ech_0010._5.ObjectFactory as Ech0010ObjectFactory
import org.openehealth.ipf.commons.ihe.ech.stub.ech_0011._8.ObjectFactory as Ech0011ObjectFactory
import org.openehealth.ipf.commons.ihe.ech.stub.ech_0021._7.ObjectFactory as Ech0021ObjectFactory
import org.openehealth.ipf.commons.ihe.ech.stub.ech_0044._4.ObjectFactory as Ech0044ObjectFactory
import org.openehealth.ipf.commons.ihe.ech.stub.ech_0058._5.HeaderType
import org.openehealth.ipf.commons.ihe.ech.stub.ech_0058._5.ObjectFactory as Ech0058ObjectFactory
import org.openehealth.ipf.commons.ihe.ech.stub.ech_0058._5.SendingApplicationType
import org.openehealth.ipf.commons.ihe.ech.stub.ech_0135._1.ObjectFactory as Ech0135ObjectFactory
import org.openehealth.ipf.commons.ihe.ech.stub.ech_0213._1.ObjectFactory as Ech0213ObjectFactory
import org.openehealth.ipf.commons.ihe.ech.stub.ech_0213_commons._1.ObjectFactory as Ech0213CommonsObjectFactory
import org.openehealth.ipf.commons.ihe.ech.stub.ech_0214._2.ObjectFactory as Ech0214ObjectFactory
import org.openehealth.ipf.commons.ihe.ech.stub.ech_0215._2.ObjectFactory as Ech0215ObjectFactory

import javax.xml.datatype.DatatypeFactory
import javax.xml.datatype.XMLGregorianCalendar

class EchUtils {

    private static final DatatypeFactory DT_FACTORY = DatatypeFactory.newInstance()

    static final JAXBContext JAXB_CONTEXT = JAXBContext.newInstance(
        Ech0006ObjectFactory.class,
        Ech0007ObjectFactory.class,
        Ech0008ObjectFactory.class,
        Ech0010ObjectFactory.class,
        Ech0011ObjectFactory.class,
        Ech0021ObjectFactory.class,
        Ech0044ObjectFactory.class,
        Ech0058ObjectFactory.class,
        Ech0135ObjectFactory.class,
        Ech0213ObjectFactory.class,
        Ech0213CommonsObjectFactory.class,
        Ech0214ObjectFactory.class,
        Ech0215ObjectFactory.class,
    )

    static final String EPR_SPID_ID_CATEGORY = 'EPD-ID.BAG.ADMIN.CH'

    final String senderId
    final String manufacturerName
    final String productName
    final String productVersion
    final boolean testDeliveryFlag
    final String responseLanguage

    EchUtils(String senderId, String manufacturerName, String productName, String productVersion, boolean testDeliveryFlag, String responseLanguage) {
        this.senderId = senderId
        this.manufacturerName = manufacturerName
        this.productName = productName
        this.productVersion = productVersion
        this.testDeliveryFlag = testDeliveryFlag
        this.responseLanguage = responseLanguage
    }

    static XMLGregorianCalendar now() {
        def calendar = new GregorianCalendar(TimeZone.getTimeZone('UTC'))
        calendar.add(Calendar.MINUTE, -1)
        return DT_FACTORY.newXMLGregorianCalendar(calendar)
    }

    HeaderType createHeader(String messageType, String action, HeaderType requestHeader = null) {
        def header = new HeaderType(
            senderId: 'sedex://' + senderId,
            messageId: UUID.randomUUID().toString(),
            messageType: messageType,
            sendingApplication: new SendingApplicationType(
                manufacturer: manufacturerName,
                product: productName,
                productVersion: productVersion,
            ),
            messageDate: now(),
            action: action,
            testDeliveryFlag: testDeliveryFlag,
        )

        if (requestHeader) {
            header.recipientId << requestHeader.senderId
            header.referenceMessageId = requestHeader.messageId
        }

        return header
    }

}
