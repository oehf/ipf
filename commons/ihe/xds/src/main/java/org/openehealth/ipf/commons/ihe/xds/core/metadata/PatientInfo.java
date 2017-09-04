/*
 * Copyright 2009 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.xds.core.metadata;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.*;

/**
 * Represents additional info about a patient.
 * <p>
 * This class contains a subset of the HL7v2 PID segment. The XDS profile prohibits
 * the use of PID-2, PID-4, PID-12 and PID-19.
 * <p>
 * All non-list members of this class are allowed to be <code>null</code>. When
 * transforming to HL7 this indicates that the values are empty. Trailing empty
 * values are removed from the HL7 string.
 * <p>
 * Lists are pre-created and can therefore never be <code>null</code>.
 * @author Jens Riemschneider
 */
@EqualsAndHashCode(doNotUseGetters = true)
@ToString(doNotUseGetters = true, exclude = {"stringFields", "pojoFields"})
public class PatientInfo implements Serializable {
    private static final long serialVersionUID = 7202574584233259959L;

    private List<Identifiable> ids = new ArrayList<>();
    private List<Name> names = new ArrayList<>();
    private Timestamp dateOfBirth;
    private String gender;
    private List<Address> addresses = new ArrayList<>();

    /**
     * Map from HL7 field ID to a list of field repetitions.
     */
    private final Map<String, List<String>> stringFields = new HashMap<>();

    /**
     * Map from HL7 field ID to a list of XDS metadata objects (for a standardized subset of fields).
     * This list of XDS metadata objects will be <code>null</code> when the field is not repeatable.
     */
    private final Map<String, List<? extends Hl7v2Based>> pojoFields;

    public PatientInfo() {
        this.pojoFields = new HashMap<>();
        this.pojoFields.put("PID-3", ids);
        this.pojoFields.put("PID-5", names);
        this.pojoFields.put("PID-7", null);
        this.pojoFields.put("PID-8", null);
        this.pojoFields.put("PID-11", addresses);
    }

    private List<String> getStrings(String fieldId) {
        return stringFields.computeIfAbsent(fieldId, dummy -> new ArrayList<>());
    }

    /**
     * @return IDs of HL7 fields which are currently present in this SourcePatientInfo instance
     *         (note: there can be present fields without any content)
     */
    public Set<String> getFieldIds() {
        return stringFields.keySet();
    }

    /**
     * @return IDs of HL7 fields which are currently present in this SourcePatientInfo instance
     *         and are not backed up by XDS metadata POJOs
     *         (note: there can be present fields without any content)
     */
    public Set<String> getOtherFieldIds() {
        Set<String> set = stringFields.keySet();
        set.removeAll(pojoFields.keySet());
        return set;
    }

    /**
     * @param fieldId HL7 field ID, e.g. "PID-3"
     * @return iterator over a list of raw HL7 strings representing repetitions of the given HL7 field
     */
    public ListIterator<String> getHl7FieldIterator(String fieldId) {
        ListIterator<String> stringsIterator = getStrings(fieldId).listIterator();
        if (!pojoFields.containsKey(fieldId)) {
            return stringsIterator;
        };

        List<? extends Hl7v2Based> xdsFields = pojoFields.get(fieldId);
        ListIterator xdsIterator = (xdsFields != null) ? xdsFields.listIterator() : null;

        return new SynchronizingListIterator<String, Hl7v2Based>(stringsIterator, xdsIterator) {
            private void handleDateOfBirth(String s) {
                if (previousIndex() == 0) {
                    int pos = s.indexOf('^');
                    dateOfBirth = Timestamp.fromHL7((pos > 0) ? s.substring(0, pos) : s);
                }
            }

            private void handleGender(String s) {
                if (previousIndex() == 0) {
                    gender = StringUtils.trimToNull(s);
                }
            }

            private void validateParameter(String s) {
                if ((s != null) && s.contains("~")) {
                    throw new RuntimeException("Repetitions shall be handled by multiple calls to .add()/.set(), and not by the tilde in " + s);
                }
            }

            @Override
            public void set(String s) {
                s = StringUtils.trimToEmpty(s);
                validateParameter(s);
                getIterator().set(s);
                switch (fieldId) {
                    case "PID-3":
                        getOtherIterator().set(Hl7v2Based.parse(s, Identifiable.class));
                        break;
                    case "PID-5":
                        getOtherIterator().set(Hl7v2Based.parse(s, XpnName.class));
                        break;
                    case "PID-7":
                        handleDateOfBirth(s);
                        break;
                    case "PID-8":
                        handleGender(s);
                        break;
                    case "PID-11":
                        getOtherIterator().set(Hl7v2Based.parse(s, Address.class));
                        break;
                    default:
                        throw new RuntimeException("This line shall be not reachable, please report a bug");
                }
            }

            @Override
            public void add(String s) {
                s = StringUtils.trimToEmpty(s);
                validateParameter(s);
                getIterator().add(s);
                switch (fieldId) {
                    case "PID-3":
                        getOtherIterator().add(Hl7v2Based.parse(s, Identifiable.class));
                        break;
                    case "PID-5":
                        getOtherIterator().add(Hl7v2Based.parse(s, XpnName.class));
                        break;
                    case "PID-7":
                        handleDateOfBirth(s);
                        break;
                    case "PID-8":
                        handleGender(s);
                        break;
                    case "PID-11":
                        getOtherIterator().add(Hl7v2Based.parse(s, Address.class));
                        break;
                    default:
                        throw new RuntimeException("This line shall be not reachable, please report a bug");
                }
            }
        };
    }

    private <T extends Hl7v2Based> ListIterator<T> getXdsFieldIterator(String fieldId) {
        if (pojoFields.get(fieldId) == null) {
            throw new IllegalArgumentException(fieldId + " is not a known repeatable SourcePatientInfo element");
        }

        ListIterator<T> xdsIterator = (ListIterator<T>) pojoFields.get(fieldId).listIterator();
        ListIterator<String> stringsIterator = getStrings(fieldId).listIterator();

        return new SynchronizingListIterator<T, String>(xdsIterator, stringsIterator) {
            private T prepareValue(T xdsObject) {
                if ("PID-5".equals(fieldId) && (xdsObject != null) && (xdsObject instanceof XcnName)) {
                    Name name = new XpnName();
                    try {
                        BeanUtils.copyProperties(name, xdsObject);
                        return (T) name;
                    } catch (Exception e) {
                        throw new RuntimeException("Could not copy properties");
                    }
                }
                return xdsObject;
            }

            @Override
            public void set(T xdsObject) {
                xdsObject = prepareValue(xdsObject);
                getOtherIterator().set(StringUtils.trimToEmpty(Hl7v2Based.render(xdsObject)));
                getIterator().set(xdsObject);
            }

            @Override
            public void add(T xdsObject) {
                xdsObject = prepareValue(xdsObject);
                getOtherIterator().add(StringUtils.trimToEmpty(Hl7v2Based.render(xdsObject)));
                getIterator().add(xdsObject);
            }
        };
    }

    /**
     * @return iterator over the IDs of the patient (PID-3).
     */
    public ListIterator<Identifiable> getIds() {
        return getXdsFieldIterator("PID-3");
    }

    /**
     * @return iterator over the names of the patient (PID-5).
     */
    public ListIterator<Name> getNames() {
        return getXdsFieldIterator("PID-5");
    }

    /**
     * @return the date of birth of the patient (PID-7).
     */
    public Timestamp getDateOfBirth() {
        return dateOfBirth;
    }

    /**
     * @param date the date of birth of the patient (PID-7).
     */
    public void setDateOfBirth(Timestamp date) {
        this.dateOfBirth = date;
        setDateOfBirthString(Timestamp.toHL7(date));
    }

    /**
     * @param date the date of birth of the patient (PID-7).
     */
    public void setDateOfBirth(String date) {
        this.dateOfBirth = Timestamp.fromHL7(date);
        setDateOfBirthString(date);
    }

    private void setDateOfBirthString(String date) {
        List<String> strings = getStrings("PID-7");
        strings.clear();
        if ((date != null) && (date.length() > 8)) {
            date = date.substring(0, 8);
        }
        strings.add(date);
    }

    /**
     * @return the gender of the patient (PID-8).
     */
    public String getGender() {
        return gender;
    }

    /**
     * @param gender the gender of the patient (PID-8).
     */
    public void setGender(String gender) {
        this.gender = gender;
        List<String> strings = getStrings("PID-8");
        strings.clear();
        if (gender != null) {
            strings.add(gender);
        }
    }
    
    /**
     * @return iterator over thr addresses of the patient (PID-11).
     */
    public ListIterator<Address> getAddresses() {
        return getXdsFieldIterator("PID-11");
    }
    
    abstract private static class SynchronizingListIterator<T, OtherT> implements ListIterator<T> {
        @Getter(AccessLevel.PROTECTED) private final ListIterator<T> iterator;
        @Getter(AccessLevel.PROTECTED) private final ListIterator<OtherT> otherIterator;

        SynchronizingListIterator(ListIterator<T> iterator, ListIterator<OtherT> otherIterator) {
            this.iterator = iterator;
            this.otherIterator = otherIterator;
        }

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public T next() {
            if (otherIterator != null) {
                otherIterator.next();
            }
            return iterator.next();
        }

        @Override
        public boolean hasPrevious() {
            return iterator.hasPrevious();
        }

        @Override
        public T previous() {
            if (otherIterator != null) {
                otherIterator.previous();
            }
            return iterator.previous();
        }

        @Override
        public int nextIndex() {
            return iterator.nextIndex();
        }

        @Override
        public int previousIndex() {
            return iterator.previousIndex();
        }

        @Override
        public void remove() {
            if (otherIterator != null) {
                otherIterator.remove();
            }
            iterator.remove();
        }
    }

}
