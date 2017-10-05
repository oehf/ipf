package org.openehealth.ipf.commons.ihe.fhir;

import org.hl7.fhir.exceptions.FHIRException;

/**
 * This has been dropped from STU3 due to the size of the code system.
 *
 * @author Christian Ohr
 */
public enum V3ReligiousAffiliation {

    _1001("1001", "Adventist", "Adventist"),
    _1002("1002", "African Religions", "African Religions"),
    _1003("1003", "Afro-Caribbean Religions", "Afro-Caribbean Religions"),
    _1004("1004", "Agnosticism", "Agnosticism"),
    _1005("1005", "Anglican", "Anglican"),
    _1006("1006", "Animism", "Animism"),
    _1007("1007", "Atheism", "Atheism"),
    _1008("1008", "Babi & Baha'I faiths", "Babi & Baha'I faiths"),
    _1009("1009", "Baptist", "Baptist"),
    _1010("1010", "Bon", "Bon"),
    _1011("1011", "Cao Dai", "Cao Dai"),
    _1012("1012", "Celticism", "Celticism"),
    _1013("1013", "Christian (non-Catholic, non-spec)", "Christian (non-Catholic, non-specific)"),
    _1014("1014", "Confucianism", "Confucianism"),
    _1015("1015", "Cyberculture Religions", "Cyberculture Religions"),
    _1016("1016", "Divination", "Divination"),
    _1017("1017", "Fourth Way", "Fourth Way"),
    _1018("1018", "Free Daism", "Free Daism"),
    _1019("1019", "Gnosis", "Gnosis"),
    _1020("1020", "Hinduism", "Hinduism"),
    _1021("1021", "Humanism", "Humanism"),
    _1022("1022", "Independent", "Independent"),
    _1023("1023", "Islam", "Islam"),
    _1024("1024", "Jainism", "Jainism"),
    _1025("1025", "Jehovah's Witnesses", "Jehovah's Witnesses"),
    _1026("1026", "Judaism", "Judaism"),
    _1027("1027", "Latter Day Saints", "Latter Day Saints"),
    _1028("1028", "Lutheran", "Lutheran"),
    _1029("1029", "Mahayana", "Mahayana"),
    _1030("1030", "Meditation", "Meditation"),
    _1031("1031", "Messianic Judaism", "Messianic Judaism"),
    _1032("1032", "Mitraism", "Mitraism"),
    _1033("1033", "New Age", "New Age"),
    _1034("1034", "non-Roman Catholic", "non-Roman Catholic"),
    _1035("1035", "Occult", "Occult"),
    _1036("1036", "Orthodox", "Orthodox"),
    _1037("1037", "Paganism", "Paganism"),
    _1038("1038", "Pentecostal", "Pentecostal"),
    _1039("1039", "Process, The", "Process, The"),
    _1040("1040", "Reformed/Presbyterian", "Reformed/Presbyterian"),
    _1041("1041", "Roman Catholic Church", "Roman Catholic Church"),
    _1042("1042", "Satanism", "Satanism"),
    _1043("1043", "Scientology", "Scientology"),
    _1044("1044", "Shamanism", "Shamanism"),
    _1045("1045", "Shiite (Islam)", "Shiite (Islam)"),
    _1046("1046", "Shinto", "Shinto"),
    _1047("1047", "Sikism", "Sikism"),
    _1048("1048", "Spiritualism", "Spiritualism"),
    _1049("1049", "Sunni (Islam)", "Sunni (Islam)"),
    _1050("1050", "Taoism", "Taoism"),
    _1051("1051", "Theravada", "Theravada"),
    _1052("1052", "Unitarian-Universalism", "Unitarian-Universalism"),
    _1053("1053", "Universal Life Church", "Universal Life Church"),
    _1054("1054", "Vajrayana (Tibetan)", "Vajrayana (Tibetan)"),
    _1055("1055", "Veda", "Veda"),
    _1056("1056", "Voodoo", "Voodoo"),
    _1057("1057", "Wicca", "Wicca"),
    _1058("1058", "Yaohushua", "Yaohushua"),
    _1059("1059", "Zen Buddhism", "Zen Buddhism"),
    _1060("1060", "Zoroastrianism", "Zoroastrianism"),
    _1061("1061", "Assembly of God", "Assembly of God"),
    _1062("1062", "Brethren", "Brethren"),
    _1063("1063", "Christian Scientist", "Christian Scientist"),
    _1064("1064", "Church of Christ", "Church of Christ"),
    _1065("1065", "Church of God", "Church of God"),
    _1066("1066", "Congregational", "Congregational"),
    _1067("1067", "Disciples of Christ", "Disciples of Christ"),
    _1068("1068", "Eastern Orthodox", "Eastern Orthodox"),
    _1069("1069", "Episcopalian", "Episcopalian"),
    _1070("1070", "Evangelical Covenant", "Evangelical Covenant"),
    _1071("1071", "Friends", "Friends"),
    _1072("1072", "Full Gospel", "Full Gospel"),
    _1073("1073", "Methodist", "Methodist"),
    _1074("1074", "Native American", "Native American"),
    _1075("1075", "Nazarene", "Nazarene"),
    _1076("1076", "Presbyterian", "Presbyterian"),
    _1077("1077", "Protestant", "Protestant"),
    _1078("1078", "Protestant, No Denomination", "Protestant, No Denomination"),
    _1079("1079", "Reformed", "Reformed"),
    _1080("1080", "Salvation Army", "Salvation Army"),
    _1081("1081", "Unitarian Universalist", "Unitarian Universalist"),
    _1082("1082", "United Church of Christ", "United Church of Christ"),
    NULL(null, "?", "?");


    private String code;
    private String definition;
    private String display;

    V3ReligiousAffiliation(String code, String definition, String display) {
        this.code = code;
        this.definition = definition;
        this.display = display;
    }

    public static V3ReligiousAffiliation fromCode(String codeString) throws FHIRException {
        if (codeString == null || "".equals(codeString))
            return null;

        for (V3ReligiousAffiliation v3ReligiousAffiliation : V3ReligiousAffiliation.values()) {
            if (v3ReligiousAffiliation.code.equals(codeString)) {
                return v3ReligiousAffiliation;
            }
        }

        throw new FHIRException("Unknown V3ReligiousAffiliation code '" + codeString + "'");
    }

    public String toCode() {
        return code;
    }


    public String getDefinition() {
        return definition;

    }

    public String getDisplay() {
        return display;
    }


    public String getSystem() {
        return "http://hl7.org/fhir/v3/ReligiousAffiliation";
    }


}

