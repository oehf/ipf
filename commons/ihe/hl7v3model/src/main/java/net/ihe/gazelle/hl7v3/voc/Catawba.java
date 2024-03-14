/**
 * Catawba.java
 *
 * File generated from the voc::Catawba uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration Catawba.
 *
 */

@XmlType(name = "Catawba")
@XmlEnum
@XmlRootElement(name = "Catawba")
public enum Catawba {
	@XmlEnumValue("1076-9")
	_10769("1076-9"),
	@XmlEnumValue("1741-8")
	_17418("1741-8"),
	@XmlEnumValue("1742-6")
	_17426("1742-6"),
	@XmlEnumValue("1743-4")
	_17434("1743-4"),
	@XmlEnumValue("1744-2")
	_17442("1744-2"),
	@XmlEnumValue("1745-9")
	_17459("1745-9"),
	@XmlEnumValue("1746-7")
	_17467("1746-7"),
	@XmlEnumValue("1747-5")
	_17475("1747-5"),
	@XmlEnumValue("1748-3")
	_17483("1748-3"),
	@XmlEnumValue("1749-1")
	_17491("1749-1"),
	@XmlEnumValue("1750-9")
	_17509("1750-9"),
	@XmlEnumValue("1751-7")
	_17517("1751-7"),
	@XmlEnumValue("1752-5")
	_17525("1752-5"),
	@XmlEnumValue("1753-3")
	_17533("1753-3"),
	@XmlEnumValue("1754-1")
	_17541("1754-1"),
	@XmlEnumValue("1755-8")
	_17558("1755-8"),
	@XmlEnumValue("1756-6")
	_17566("1756-6"),
	@XmlEnumValue("1757-4")
	_17574("1757-4"),
	@XmlEnumValue("1758-2")
	_17582("1758-2"),
	@XmlEnumValue("1759-0")
	_17590("1759-0"),
	@XmlEnumValue("1760-8")
	_17608("1760-8"),
	@XmlEnumValue("1761-6")
	_17616("1761-6"),
	@XmlEnumValue("1762-4")
	_17624("1762-4"),
	@XmlEnumValue("1763-2")
	_17632("1763-2"),
	@XmlEnumValue("1764-0")
	_17640("1764-0"),
	@XmlEnumValue("1765-7")
	_17657("1765-7"),
	@XmlEnumValue("1766-5")
	_17665("1766-5"),
	@XmlEnumValue("1767-3")
	_17673("1767-3"),
	@XmlEnumValue("1768-1")
	_17681("1768-1"),
	@XmlEnumValue("1769-9")
	_17699("1769-9"),
	@XmlEnumValue("1770-7")
	_17707("1770-7"),
	@XmlEnumValue("1771-5")
	_17715("1771-5"),
	@XmlEnumValue("1772-3")
	_17723("1772-3"),
	@XmlEnumValue("1773-1")
	_17731("1773-1"),
	@XmlEnumValue("1774-9")
	_17749("1774-9"),
	@XmlEnumValue("1775-6")
	_17756("1775-6"),
	@XmlEnumValue("1776-4")
	_17764("1776-4"),
	@XmlEnumValue("1777-2")
	_17772("1777-2"),
	@XmlEnumValue("1778-0")
	_17780("1778-0"),
	@XmlEnumValue("1779-8")
	_17798("1779-8"),
	@XmlEnumValue("1780-6")
	_17806("1780-6"),
	@XmlEnumValue("1781-4")
	_17814("1781-4"),
	@XmlEnumValue("1782-2")
	_17822("1782-2"),
	@XmlEnumValue("1783-0")
	_17830("1783-0"),
	@XmlEnumValue("1784-8")
	_17848("1784-8"),
	@XmlEnumValue("1785-5")
	_17855("1785-5"),
	@XmlEnumValue("1786-3")
	_17863("1786-3"),
	@XmlEnumValue("1787-1")
	_17871("1787-1"),
	@XmlEnumValue("1788-9")
	_17889("1788-9"),
	@XmlEnumValue("1789-7")
	_17897("1789-7"),
	@XmlEnumValue("1790-5")
	_17905("1790-5"),
	@XmlEnumValue("1791-3")
	_17913("1791-3"),
	@XmlEnumValue("1792-1")
	_17921("1792-1"),
	@XmlEnumValue("1793-9")
	_17939("1793-9"),
	@XmlEnumValue("1794-7")
	_17947("1794-7"),
	@XmlEnumValue("1795-4")
	_17954("1795-4"),
	@XmlEnumValue("1796-2")
	_17962("1796-2"),
	@XmlEnumValue("1797-0")
	_17970("1797-0"),
	@XmlEnumValue("1798-8")
	_17988("1798-8"),
	@XmlEnumValue("1799-6")
	_17996("1799-6"),
	@XmlEnumValue("1800-2")
	_18002("1800-2"),
	@XmlEnumValue("1801-0")
	_18010("1801-0"),
	@XmlEnumValue("1802-8")
	_18028("1802-8"),
	@XmlEnumValue("1803-6")
	_18036("1803-6"),
	@XmlEnumValue("1804-4")
	_18044("1804-4"),
	@XmlEnumValue("1805-1")
	_18051("1805-1"),
	@XmlEnumValue("1806-9")
	_18069("1806-9"),
	@XmlEnumValue("1807-7")
	_18077("1807-7"),
	@XmlEnumValue("1808-5")
	_18085("1808-5"),
	@XmlEnumValue("1809-3")
	_18093("1809-3");
	
	private final String value;

    Catawba(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static Catawba fromValue(String v) {
        for (Catawba c: Catawba.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}