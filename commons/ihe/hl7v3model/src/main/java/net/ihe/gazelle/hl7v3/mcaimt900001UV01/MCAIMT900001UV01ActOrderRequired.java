/**
 * MCAIMT900001UV01ActOrderRequired.java
 * <p>
 * File generated from the mcaimt900001UV01::MCAIMT900001UV01ActOrderRequired uml Class
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.mcaimt900001UV01;

import net.ihe.gazelle.gen.common.ConstraintValidatorModule;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import jakarta.xml.bind.annotation.XmlType;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.Serial;
import java.util.ArrayList;
import java.util.List;


/**
 * Description of the class MCAIMT900001UV01ActOrderRequired.
 *
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MCAI_MT900001UV01.ActOrderRequired", propOrder = {
        "realmCode",
        "typeId",
        "templateId",
        "code",
        "effectiveTime",
        "subject",
        "classCode",
        "moodCode",
        "nullFlavor"
})
@XmlRootElement(name = "MCAI_MT900001UV01.ActOrderRequired")
public class MCAIMT900001UV01ActOrderRequired implements java.io.Serializable {

    /**
     *
     */
    @Serial
    private static final long serialVersionUID = 1L;


    @XmlElement(name = "realmCode", namespace = "urn:hl7-org:v3")
    public List<net.ihe.gazelle.hl7v3.datatypes.CS> realmCode;
    @XmlElement(name = "typeId", namespace = "urn:hl7-org:v3")
    public net.ihe.gazelle.hl7v3.datatypes.II typeId;
    @XmlElement(name = "templateId", namespace = "urn:hl7-org:v3")
    public List<net.ihe.gazelle.hl7v3.datatypes.II> templateId;
    @XmlElement(name = "code", namespace = "urn:hl7-org:v3")
    public net.ihe.gazelle.hl7v3.datatypes.CE code;
    @XmlElement(name = "effectiveTime", namespace = "urn:hl7-org:v3")
    public List<net.ihe.gazelle.hl7v3.datatypes.SXCMTS> effectiveTime;
    @XmlElement(name = "subject", namespace = "urn:hl7-org:v3")
    public List<net.ihe.gazelle.hl7v3.mcaimt900001UV01.MCAIMT900001UV01Subject> subject;
    @XmlAttribute(name = "classCode", required = true)
    public net.ihe.gazelle.hl7v3.voc.ActClassRoot classCode;
    @XmlAttribute(name = "moodCode", required = true)
    public net.ihe.gazelle.hl7v3.voc.ActMood moodCode;
    @XmlAttribute(name = "nullFlavor")
    public net.ihe.gazelle.hl7v3.voc.NullFlavor nullFlavor;

    /**
     * An attribute containing marshalled element node
     */
    @XmlTransient
    private org.w3c.dom.Node _xmlNodePresentation;


    /**
     * Return realmCode.
     * @return realmCode
     */
    public List<net.ihe.gazelle.hl7v3.datatypes.CS> getRealmCode() {
        if (realmCode == null) {
            realmCode = new ArrayList<>();
        }
        return realmCode;
    }

    /**
     * Set a value to attribute realmCode.
     */
    public void setRealmCode(List<net.ihe.gazelle.hl7v3.datatypes.CS> realmCode) {
        this.realmCode = realmCode;
    }


    /**
     * Add a realmCode to the realmCode collection.
     * @param realmCode_elt Element to add.
     */
    public void addRealmCode(net.ihe.gazelle.hl7v3.datatypes.CS realmCode_elt) {
        this.realmCode.add(realmCode_elt);
    }

    /**
     * Remove a realmCode to the realmCode collection.
     * @param realmCode_elt Element to remove
     */
    public void removeRealmCode(net.ihe.gazelle.hl7v3.datatypes.CS realmCode_elt) {
        this.realmCode.remove(realmCode_elt);
    }

    /**
     * Return typeId.
     * @return typeId
     */
    public net.ihe.gazelle.hl7v3.datatypes.II getTypeId() {
        return typeId;
    }

    /**
     * Set a value to attribute typeId.
     */
    public void setTypeId(net.ihe.gazelle.hl7v3.datatypes.II typeId) {
        this.typeId = typeId;
    }


    /**
     * Return templateId.
     * @return templateId
     */
    public List<net.ihe.gazelle.hl7v3.datatypes.II> getTemplateId() {
        if (templateId == null) {
            templateId = new ArrayList<>();
        }
        return templateId;
    }

    /**
     * Set a value to attribute templateId.
     */
    public void setTemplateId(List<net.ihe.gazelle.hl7v3.datatypes.II> templateId) {
        this.templateId = templateId;
    }


    /**
     * Add a templateId to the templateId collection.
     * @param templateId_elt Element to add.
     */
    public void addTemplateId(net.ihe.gazelle.hl7v3.datatypes.II templateId_elt) {
        this.templateId.add(templateId_elt);
    }

    /**
     * Remove a templateId to the templateId collection.
     * @param templateId_elt Element to remove
     */
    public void removeTemplateId(net.ihe.gazelle.hl7v3.datatypes.II templateId_elt) {
        this.templateId.remove(templateId_elt);
    }

    /**
     * Return code.
     * @return code
     */
    public net.ihe.gazelle.hl7v3.datatypes.CE getCode() {
        return code;
    }

    /**
     * Set a value to attribute code.
     */
    public void setCode(net.ihe.gazelle.hl7v3.datatypes.CE code) {
        this.code = code;
    }


    /**
     * Return effectiveTime.
     * @return effectiveTime
     */
    public List<net.ihe.gazelle.hl7v3.datatypes.SXCMTS> getEffectiveTime() {
        if (effectiveTime == null) {
            effectiveTime = new ArrayList<>();
        }
        return effectiveTime;
    }

    /**
     * Set a value to attribute effectiveTime.
     */
    public void setEffectiveTime(List<net.ihe.gazelle.hl7v3.datatypes.SXCMTS> effectiveTime) {
        this.effectiveTime = effectiveTime;
    }


    /**
     * Add a effectiveTime to the effectiveTime collection.
     * @param effectiveTime_elt Element to add.
     */
    public void addEffectiveTime(net.ihe.gazelle.hl7v3.datatypes.SXCMTS effectiveTime_elt) {
        this.effectiveTime.add(effectiveTime_elt);
    }

    /**
     * Remove a effectiveTime to the effectiveTime collection.
     * @param effectiveTime_elt Element to remove
     */
    public void removeEffectiveTime(net.ihe.gazelle.hl7v3.datatypes.SXCMTS effectiveTime_elt) {
        this.effectiveTime.remove(effectiveTime_elt);
    }

    /**
     * Return subject.
     * @return subject
     */
    public List<net.ihe.gazelle.hl7v3.mcaimt900001UV01.MCAIMT900001UV01Subject> getSubject() {
        if (subject == null) {
            subject = new ArrayList<>();
        }
        return subject;
    }

    /**
     * Set a value to attribute subject.
     */
    public void setSubject(List<net.ihe.gazelle.hl7v3.mcaimt900001UV01.MCAIMT900001UV01Subject> subject) {
        this.subject = subject;
    }


    /**
     * Add a subject to the subject collection.
     * @param subject_elt Element to add.
     */
    public void addSubject(net.ihe.gazelle.hl7v3.mcaimt900001UV01.MCAIMT900001UV01Subject subject_elt) {
        this.subject.add(subject_elt);
    }

    /**
     * Remove a subject to the subject collection.
     * @param subject_elt Element to remove
     */
    public void removeSubject(net.ihe.gazelle.hl7v3.mcaimt900001UV01.MCAIMT900001UV01Subject subject_elt) {
        this.subject.remove(subject_elt);
    }

    /**
     * Return classCode.
     * @return classCode
     */
    public net.ihe.gazelle.hl7v3.voc.ActClassRoot getClassCode() {
        return classCode;
    }

    /**
     * Set a value to attribute classCode.
     */
    public void setClassCode(net.ihe.gazelle.hl7v3.voc.ActClassRoot classCode) {
        this.classCode = classCode;
    }


    /**
     * Return moodCode.
     * @return moodCode
     */
    public net.ihe.gazelle.hl7v3.voc.ActMood getMoodCode() {
        return moodCode;
    }

    /**
     * Set a value to attribute moodCode.
     */
    public void setMoodCode(net.ihe.gazelle.hl7v3.voc.ActMood moodCode) {
        this.moodCode = moodCode;
    }


    /**
     * Return nullFlavor.
     * @return nullFlavor
     */
    public net.ihe.gazelle.hl7v3.voc.NullFlavor getNullFlavor() {
        return nullFlavor;
    }

    /**
     * Set a value to attribute nullFlavor.
     */
    public void setNullFlavor(net.ihe.gazelle.hl7v3.voc.NullFlavor nullFlavor) {
        this.nullFlavor = nullFlavor;
    }


    public Node get_xmlNodePresentation() {
        if (_xmlNodePresentation == null) {
            JAXBContext jc;
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            DocumentBuilder db = null;
            Document doc = null;
            try {
                db = dbf.newDocumentBuilder();
                doc = db.newDocument();
            } catch (ParserConfigurationException ignored) {
            }
            try {
                jc = JAXBContext.newInstance("net.ihe.gazelle.hl7v3.mcaimt900001UV01");
                Marshaller m = jc.createMarshaller();
                m.marshal(this, doc);
                _xmlNodePresentation = doc.getElementsByTagNameNS("urn:hl7-org:v3", "MCAI_MT900001UV01.ActOrderRequired").item(0);
            } catch (JAXBException e) {
                try {
                    db = dbf.newDocumentBuilder();
                    _xmlNodePresentation = db.newDocument();
                } catch (Exception ignored) {
                }
            }
        }
        return _xmlNodePresentation;
    }

    public void set_xmlNodePresentation(Node _xmlNodePresentation) {
        this._xmlNodePresentation = _xmlNodePresentation;
    }


    /**
     * validate by a module of validation
     *
     */
    public static void validateByModule(MCAIMT900001UV01ActOrderRequired mCAIMT900001UV01ActOrderRequired, String _location, ConstraintValidatorModule cvm, List<net.ihe.gazelle.validation.Notification> diagnostic) {
        if (mCAIMT900001UV01ActOrderRequired != null) {
            cvm.validate(mCAIMT900001UV01ActOrderRequired, _location, diagnostic);
            {
                int i = 0;
                for (net.ihe.gazelle.hl7v3.datatypes.CS realmCode : mCAIMT900001UV01ActOrderRequired.getRealmCode()) {
                    net.ihe.gazelle.hl7v3.datatypes.CS.validateByModule(realmCode, _location + "/realmCode[" + i + "]", cvm, diagnostic);
                    i++;
                }
            }

            net.ihe.gazelle.hl7v3.datatypes.II.validateByModule(mCAIMT900001UV01ActOrderRequired.getTypeId(), _location + "/typeId", cvm, diagnostic);
            {
                int i = 0;
                for (net.ihe.gazelle.hl7v3.datatypes.II templateId : mCAIMT900001UV01ActOrderRequired.getTemplateId()) {
                    net.ihe.gazelle.hl7v3.datatypes.II.validateByModule(templateId, _location + "/templateId[" + i + "]", cvm, diagnostic);
                    i++;
                }
            }

            net.ihe.gazelle.hl7v3.datatypes.CE.validateByModule(mCAIMT900001UV01ActOrderRequired.getCode(), _location + "/code", cvm, diagnostic);
            {
                int i = 0;
                for (net.ihe.gazelle.hl7v3.datatypes.SXCMTS effectiveTime : mCAIMT900001UV01ActOrderRequired.getEffectiveTime()) {
                    net.ihe.gazelle.hl7v3.datatypes.SXCMTS.validateByModule(effectiveTime, _location + "/effectiveTime[" + i + "]", cvm, diagnostic);
                    i++;
                }
            }

            {
                int i = 0;
                for (net.ihe.gazelle.hl7v3.mcaimt900001UV01.MCAIMT900001UV01Subject subject : mCAIMT900001UV01ActOrderRequired.getSubject()) {
                    net.ihe.gazelle.hl7v3.mcaimt900001UV01.MCAIMT900001UV01Subject.validateByModule(subject, _location + "/subject[" + i + "]", cvm, diagnostic);
                    i++;
                }
            }

        }
    }

}