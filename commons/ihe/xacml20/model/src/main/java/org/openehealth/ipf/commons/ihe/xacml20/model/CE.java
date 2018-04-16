package org.openehealth.ipf.commons.ihe.xacml20.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * @author Dmytro Rud
 */
@AllArgsConstructor
@EqualsAndHashCode
public class CE {
    @Getter private String code;
    @Getter private String codeSystem;
    @Getter private String codeSystemName;
    @Getter private String displayName;

    @Override
    public String toString() {
        return "CE{" +
                "code='" + code + '\'' +
                ", codeSystem='" + codeSystem + '\'' +
                ", codeSystemName='" + codeSystemName + '\'' +
                ", displayName='" + displayName + '\'' +
                '}';
    }
}
