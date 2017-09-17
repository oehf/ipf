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
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents additional information about a patient, as an HL7v2 PID segment
 * with the following specialities:
 * <ul>
 *     <li>Fields PID-3 (patient IDs), PID-5 (patient names), PID-7 (birth date), and
 *     PID-8 (gender) can be manipulated both as HL7 strings and as XDS metadata objects.
 *     <li>Fields PID-2, PID-4, PID-12 and PID-19 are prohibited by the XDS standard.</li>
 * </ul>
 * <b>IMPORTANT NOTE:</b> in-place modifications of the XDS metadata objects contained in the lists
 * returned by {@link #getIds()}, {@link #getNames()}, and {@link #getAddresses()} will be <b>not</b>
 * propagated to their HL7 string counterparts, therefore {@link ListIterator#set(Object)}
 * shall be used to properly update the both representations.  For example,
 * <pre>
 * ListIterator&lt;Name&gt; iter = patientInfo.getNames();
 * Name name = iter.next();
 * name.setFamilyName("Krusenstern");
 * iter.set(name);   // this statement is absolutely essential!
 * </pre>
 *
 * @author Jens Riemschneider
 * @author Dmytro Rud
 */
@EqualsAndHashCode(doNotUseGetters = true)
public class PatientInfo implements Serializable {
    private static final long serialVersionUID = 7202574584233259959L;

    /**
     * Map from HL7 field ID to a list of field repetitions.
     */
    private final Map<String, List<String>> stringFields = new HashMap<>();

    /**
     * A map covering a subset of fields with enhanced access mechanisms.
     * The key is the field index (e.g. "PID-5"), and the value is
     * <ul>
     *     <li>for repeatable fields -- list of XDS metadata objects,</li>
     *     <li>for non-repeatable fields -- always <code>null</code>.</li>
     * </ul>
     */
    private final Map<String, List<? extends Hl7v2Based>> pojoFields;

    public PatientInfo() {
        this.pojoFields = new HashMap<>();
        this.pojoFields.put("PID-3", new ArrayList<Identifiable>());
        this.pojoFields.put("PID-5", new ArrayList<Name>());
        this.pojoFields.put("PID-7", null);
        this.pojoFields.put("PID-8", null);
        this.pojoFields.put("PID-11", new ArrayList<Address>());
    }

    private List<String> getStrings(String fieldId) {
        return stringFields.computeIfAbsent(fieldId, dummy -> new ArrayList<>());
    }

    /**
     * @return IDs of HL7 fields which are currently present in this SourcePatientInfo instance
     *         (note: there can be present fields without any content)
     */
    public Set<String> getAllFieldIds() {
        Set<String> result = new HashSet<>();
        result.addAll(stringFields.keySet());
        return result;
    }

    /**
     * @return IDs of HL7 fields which are currently present in this SourcePatientInfo instance
     *         and are not backed up by XDS metadata POJOs
     *         (note: there can be present fields without any content)
     */
    public Set<String> getCustomFieldIds() {
        Set<String> set = getAllFieldIds();
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
            private void validateParameter(String s) {
                if ((s != null) && s.contains("~")) {
                    throw new RuntimeException("Repetitions shall be handled by multiple calls to .add()/.set(), and not by the tilde in " + s);
                }
            }

            @Override
            public void set(String s) {
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
                        break;
                    case "PID-8":
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
                        break;
                    case "PID-8":
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
                if ("PID-5".equals(fieldId) && (xdsObject != null) && !(xdsObject instanceof XpnName)) {
                    Name xpnName = new XpnName();
                    try {
                        BeanUtils.copyProperties(xpnName, xdsObject);
                        return (T) xpnName;
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
     * Returns a snapshot of list of patient IDs.  See the note in the class javadoc.
     * @return iterator over the IDs of the patient (PID-3).
     */
    public ListIterator<Identifiable> getIds() {
        return getXdsFieldIterator("PID-3");
    }

    /**
     * Returns a snapshot of the list of patient names.  See the note in the class javadoc.
     * @return iterator over the names of the patient (PID-5).
     */
    public ListIterator<Name> getNames() {
        return getXdsFieldIterator("PID-5");
    }

    private String getFirstStringValue(String fieldName) {
        List<String> list = stringFields.get(fieldName);
        return ((list != null) && !list.isEmpty()) ? list.get(0) : null;
    }

    /**
     * @return the date of birth of the patient (PID-7).
     */
    public Timestamp getDateOfBirth() {
        String s = getFirstStringValue("PID-7");
        if (s != null) {
            int pos = s.indexOf('^');
            return Timestamp.fromHL7((pos > 0) ? s.substring(0, pos) : s);
        }
        return null;
    }

    /**
     * @param date the date of birth of the patient (PID-7).
     */
    public void setDateOfBirth(Timestamp date) {
        setDateOfBirthString(Timestamp.toHL7(date));
    }

    /**
     * @param date the date of birth of the patient (PID-7).
     */
    public void setDateOfBirth(String date) {
        setDateOfBirthString(StringUtils.stripToNull(date));
    }

    private void setDateOfBirthString(String date) {
        List<String> strings = getStrings("PID-7");
        strings.clear();
        if (date != null) {
            strings.add(date);
        }
    }

    /**
     * @return the gender of the patient (PID-8).
     */
    public String getGender() {
        return StringUtils.stripToNull(getFirstStringValue("PID-8"));
    }

    /**
     * @param gender the gender of the patient (PID-8).
     */
    public void setGender(String gender) {
        List<String> strings = getStrings("PID-8");
        strings.clear();
        String s = StringUtils.stripToNull(gender);
        if (s != null) {
            strings.add(s);
        }
    }

    /**
     * Returns a snapshot of the list of patient addresses.  See the note in the class javadoc.
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

    public static class Hl7FieldIdComparator implements Comparator<String> {
        public static final Pattern FIELD_ID_PATTERN = Pattern.compile("([A-Z][A-Z][A-Z0-9])-(\\d\\d?)");

        @Override
        public int compare(String o1, String o2) {
            Matcher matcher1 = FIELD_ID_PATTERN.matcher(o1);
            Matcher matcher2 = FIELD_ID_PATTERN.matcher(o2);
            if (matcher1.matches() && matcher2.matches() && matcher1.group(1).equals(matcher2.group(1))) {
                return Integer.parseInt(matcher1.group(2)) - Integer.parseInt(matcher2.group(2));
            }
            return o1.compareTo(o2);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder()
                .append("PatientInfo(")
                .append("ids=")
                .append(pojoFields.get("PID-3"))
                .append(", names=")
                .append(pojoFields.get("PID-5"))
                .append(", birthDate=")
                .append(getDateOfBirth())
                .append(", gender=")
                .append(getGender())
                .append(", addresses=")
                .append(pojoFields.get("PID-11"));

        getCustomFieldIds().stream().sorted(new Hl7FieldIdComparator()).forEach(fieldId -> {
            List<String> values = stringFields.get(fieldId);
            if (!values.isEmpty()) {
                sb.append(", ").append(fieldId).append('=').append(values);
            }
        });

        return sb.append(')').toString();
    }
}
