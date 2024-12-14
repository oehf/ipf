/**
 * RaceAlaskanNativeAleut.java
 * <p>
 * File generated from the voc::RaceAlaskanNativeAleut uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration RaceAlaskanNativeAleut.
 *
 */

@XmlType(name = "RaceAlaskanNativeAleut")
@XmlEnum
@XmlRootElement(name = "RaceAlaskanNativeAleut")
public enum RaceAlaskanNativeAleut {
	@XmlEnumValue("1966-1")
	_19661("1966-1"),
	@XmlEnumValue("1968-7")
	_19687("1968-7"),
	@XmlEnumValue("1969-5")
	_19695("1969-5"),
	@XmlEnumValue("1970-3")
	_19703("1970-3"),
	@XmlEnumValue("1972-9")
	_19729("1972-9"),
	@XmlEnumValue("1973-7")
	_19737("1973-7"),
	@XmlEnumValue("1974-5")
	_19745("1974-5"),
	@XmlEnumValue("1975-2")
	_19752("1975-2"),
	@XmlEnumValue("1976-0")
	_19760("1976-0"),
	@XmlEnumValue("1977-8")
	_19778("1977-8"),
	@XmlEnumValue("1978-6")
	_19786("1978-6"),
	@XmlEnumValue("1979-4")
	_19794("1979-4"),
	@XmlEnumValue("1980-2")
	_19802("1980-2"),
	@XmlEnumValue("1981-0")
	_19810("1981-0"),
	@XmlEnumValue("1982-8")
	_19828("1982-8"),
	@XmlEnumValue("1984-4")
	_19844("1984-4"),
	@XmlEnumValue("1985-1")
	_19851("1985-1"),
	@XmlEnumValue("1986-9")
	_19869("1986-9"),
	@XmlEnumValue("1987-7")
	_19877("1987-7"),
	@XmlEnumValue("1988-5")
	_19885("1988-5"),
	@XmlEnumValue("1990-1")
	_19901("1990-1"),
	@XmlEnumValue("1992-7")
	_19927("1992-7"),
	@XmlEnumValue("1993-5")
	_19935("1993-5"),
	@XmlEnumValue("1994-3")
	_19943("1994-3"),
	@XmlEnumValue("1995-0")
	_19950("1995-0"),
	@XmlEnumValue("1996-8")
	_19968("1996-8"),
	@XmlEnumValue("1997-6")
	_19976("1997-6"),
	@XmlEnumValue("1998-4")
	_19984("1998-4"),
	@XmlEnumValue("1999-2")
	_19992("1999-2"),
	@XmlEnumValue("2000-8")
	_20008("2000-8"),
	@XmlEnumValue("2002-4")
	_20024("2002-4"),
	@XmlEnumValue("2004-0")
	_20040("2004-0"),
	@XmlEnumValue("2006-5")
	_20065("2006-5"),
	@XmlEnumValue("2007-3")
	_20073("2007-3"),
	@XmlEnumValue("2008-1")
	_20081("2008-1"),
	@XmlEnumValue("2009-9")
	_20099("2009-9"),
	@XmlEnumValue("2010-7")
	_20107("2010-7"),
	@XmlEnumValue("2011-5")
	_20115("2011-5"),
	@XmlEnumValue("2012-3")
	_20123("2012-3"),
	@XmlEnumValue("2013-1")
	_20131("2013-1"),
	@XmlEnumValue("2014-9")
	_20149("2014-9"),
	@XmlEnumValue("2015-6")
	_20156("2015-6"),
	@XmlEnumValue("2016-4")
	_20164("2016-4"),
	@XmlEnumValue("2017-2")
	_20172("2017-2"),
	@XmlEnumValue("2018-0")
	_20180("2018-0"),
	@XmlEnumValue("2019-8")
	_20198("2019-8"),
	@XmlEnumValue("2020-6")
	_20206("2020-6"),
	@XmlEnumValue("2021-4")
	_20214("2021-4"),
	@XmlEnumValue("2022-2")
	_20222("2022-2"),
	@XmlEnumValue("2023-0")
	_20230("2023-0"),
	@XmlEnumValue("2024-8")
	_20248("2024-8"),
	@XmlEnumValue("2025-5")
	_20255("2025-5"),
	@XmlEnumValue("2026-3")
	_20263("2026-3");
	
	private final String value;

    RaceAlaskanNativeAleut(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static RaceAlaskanNativeAleut fromValue(String v) {
        for (RaceAlaskanNativeAleut c: RaceAlaskanNativeAleut.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}