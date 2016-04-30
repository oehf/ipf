/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
 package org.openehealth.ipf.commons.ihe.hl7v2.definitions.pix.v231.message;

import java.util.List;
import java.util.Map;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Structure;
import ca.uhn.hl7v2.model.v231.segment.*;
import ca.uhn.hl7v2.parser.ModelClassFactory;
import org.openehealth.ipf.commons.ihe.hl7v2.definitions.pix.v231.group.ADT_A01_INSURANCE;
import org.openehealth.ipf.commons.ihe.hl7v2.definitions.pix.v231.group.ADT_A01_PROCEDURE;
import org.openehealth.ipf.modules.hl7.model.AbstractMessage;

/**
 * ADT_A01 message with renamed groups PROCEDURE and INSURANCE.
 */
public class ADT_A01 extends AbstractMessage {

    public ADT_A01() {
        super();
    }

    public ADT_A01(ModelClassFactory factory) {
        super(factory);
    }

    @Override
    protected Map<Class<? extends Structure>, Cardinality> structures(Map<Class<? extends Structure>, Cardinality> s) {
        s.put(MSH.class, Cardinality.REQUIRED);
        s.put(EVN.class, Cardinality.REQUIRED);
        s.put(PID.class, Cardinality.REQUIRED);
        s.put(PD1.class, Cardinality.OPTIONAL);
        s.put(PD1.class, Cardinality.OPTIONAL);
        s.put(NK1.class, Cardinality.OPTIONAL_REPEATING);
        s.put(PV1.class, Cardinality.REQUIRED);
        s.put(PV2.class, Cardinality.OPTIONAL);
        s.put(DB1.class, Cardinality.OPTIONAL_REPEATING);
        s.put(OBX.class, Cardinality.OPTIONAL_REPEATING);
        s.put(AL1.class, Cardinality.OPTIONAL_REPEATING);
        s.put(DG1.class, Cardinality.OPTIONAL_REPEATING);
        s.put(DRG.class, Cardinality.OPTIONAL);
        s.put(ADT_A01_PROCEDURE.class, Cardinality.OPTIONAL_REPEATING);
        s.put(GT1.class, Cardinality.OPTIONAL_REPEATING);
        s.put(ADT_A01_INSURANCE.class, Cardinality.OPTIONAL);
        s.put(ACC.class, Cardinality.OPTIONAL);
        s.put(UB1.class, Cardinality.OPTIONAL);
        s.put(UB2.class, Cardinality.OPTIONAL);
        return s;
    }

    public String getVersion() {
        return "2.3.1";
    }

    public MSH getMSH() {
        return getTyped(MSH.class);
    }

    public EVN getEVN() {
        return getTyped(EVN.class);
    }

    public PID getPID() {
        return getTyped(PID.class);
    }

    public PD1 getPD1() {
        return getTyped(PD1.class);
    }

    public NK1 getNK1() {
        return getTyped(NK1.class);
    }

    public NK1 getNK1(int rep) {
        return getTyped(rep, NK1.class);
    }

    public int getNK1Reps() {
        return getReps("NK1");
    }

    public List<NK1> getNK1All() throws HL7Exception {
        return getAllAsList(NK1.class);
    }

    public void insertNK1(NK1 structure, int rep) throws HL7Exception {
        insertRepetition("NK1", structure, rep);
    }

    public NK1 insertNK1(int rep) throws HL7Exception {
        return insertRep("NK1", rep);
    }

    public NK1 removeNK1(int rep) throws HL7Exception {
        return removeRep("NK1", rep);
    }

    public PV1 getPV1() {
        return getTyped(PV1.class);
    }

    public PV2 getPV2() {
        return getTyped(PV2.class);
    }

    public DB1 getDB1() {
        return getTyped(DB1.class);
    }

    public DB1 getDB1(int rep) {
        return getTyped(rep, DB1.class);
    }

    public int getDB1Reps() {
        return getReps("DB1");
    }

    public List<DB1> getDB1All() throws HL7Exception {
        return getAllAsList(DB1.class);
    }

    public void insertDB1(DB1 structure, int rep) throws HL7Exception {
        insertRepetition("DB1", structure, rep);
    }

    public DB1 insertDB1(int rep) throws HL7Exception {
        return insertRep("DB1", rep);
    }

    public DB1 removeDB1(int rep) throws HL7Exception {
        return removeRep("DB1", rep);
    }

    public OBX getOBX() {
        return getTyped(OBX.class);
    }

    public OBX getOBX(int rep) {
        return getTyped(rep, OBX.class);
    }

    public int getOBXReps() {
        return getReps("OBX");
    }

    public List<OBX> getOBXAll() throws HL7Exception {
        return getAllAsList(OBX.class);
    }

    public void insertOBX(OBX structure, int rep) throws HL7Exception {
        insertRepetition("OBX", structure, rep);
    }

    public OBX insertOBX(int rep) throws HL7Exception {
        return insertRep("OBX", rep);
    }

    public OBX removeOBX(int rep) throws HL7Exception {
        return removeRep("OBX", rep);
    }

    public AL1 getAL1() {
        return getTyped(AL1.class);
    }

    public AL1 getAL1(int rep) {
        return getTyped(rep, AL1.class);
    }

    public int getAL1Reps() {
        return getReps("AL1");
    }

    public List<AL1> getAL1All() throws HL7Exception {
        return getAllAsList(AL1.class);
    }

    public void insertAL1(AL1 structure, int rep) throws HL7Exception {
        insertRepetition("AL1", structure, rep);
    }

    public AL1 insertAL1(int rep) throws HL7Exception {
        return insertRep("AL1", rep);
    }

    public AL1 removeAL1(int rep) throws HL7Exception {
        return removeRep("AL1", rep);
    }

    public DG1 getDG1() {
        return getTyped(DG1.class);
    }

    public DG1 getDG1(int rep) {
        return getTyped(rep, DG1.class);
    }

    public int getDG1Reps() {
        return getReps("DG1");
    }

    public List<DG1> getDG1All() throws HL7Exception {
        return getAllAsList(DG1.class);
    }

    public void insertDG1(DG1 structure, int rep) throws HL7Exception {
        insertRepetition("DG1", structure, rep);
    }

    public DG1 insertDG1(int rep) throws HL7Exception {
        return insertRep("DG1", rep);
    }

    public DG1 removeDG1(int rep) throws HL7Exception {
        return removeRep("DG1", rep);
    }

    public DRG getDRG() {
        return getTyped(DRG.class);
    }

    public ADT_A01_PROCEDURE getPROCEDURE() {
        return getTyped("PROCEDURE", ADT_A01_PROCEDURE.class);
    }

    public ADT_A01_PROCEDURE getPROCEDURE(int rep) {
        return getTyped("PROCEDURE", rep, ADT_A01_PROCEDURE.class);
    }

    public int getPROCEDUREReps() {
        return getReps("PROCEDURE");
    }

    public List<ADT_A01_PROCEDURE> getPROCEDUREAll() throws HL7Exception {
        return getAllAsList("PROCEDURE", ADT_A01_PROCEDURE.class);
    }

    public void insertPROCEDURE(ADT_A01_PROCEDURE structure, int rep) throws HL7Exception {
        insertRepetition("PROCEDURE", structure, rep);
    }

    public ADT_A01_PROCEDURE insertPROCEDURE(int rep) throws HL7Exception {
        return insertRep("PROCEDURE", rep);
    }

    public ADT_A01_PROCEDURE removePROCEDURE(int rep) throws HL7Exception {
        return removeRep("PROCEDURE", rep);
    }

    public GT1 getGT1() {
        return getTyped(GT1.class);
    }

    public GT1 getGT1(int rep) {
        return getTyped(rep, GT1.class);
    }

    public int getGT1Reps() {
        return getReps("GT1");
    }

    public List<GT1> getGT1All() throws HL7Exception {
        return getAllAsList(GT1.class);
    }

    public void insertGT1(GT1 structure, int rep) throws HL7Exception {
        insertRepetition("GT1", structure, rep);
    }

    public GT1 insertGT1(int rep) throws HL7Exception {
        return insertRep("GT1", rep);
    }

    public GT1 removeGT1(int rep) throws HL7Exception {
        return removeRep("GT1", rep);
    }

    public ADT_A01_INSURANCE getINSURANCE() {
        return getTyped("INSURANCE", ADT_A01_INSURANCE.class);
    }

    public ADT_A01_INSURANCE getINSURANCE(int rep) {
        return getTyped("INSURANCE", rep, ADT_A01_INSURANCE.class);
    }

    public int getIN1IN2IN3Reps() {
        return getReps("INSURANCE");
    }

    public List<ADT_A01_INSURANCE> getINSURANCEAll() throws HL7Exception {
        return getAllAsList("INSURANCE", ADT_A01_INSURANCE.class);
    }

    public void insertINSURANCE(ADT_A01_INSURANCE structure, int rep) throws HL7Exception {
        insertRepetition("INSURANCE", structure, rep);
    }

    public ADT_A01_INSURANCE insertINSURANCE(int rep) throws HL7Exception {
        return insertRep("INSURANCE", rep);
    }

    public ADT_A01_INSURANCE removeINSURANCE(int rep) throws HL7Exception {
        return removeRep("INSURANCE", rep);
    }

    public ACC getACC() {
        return getTyped(ACC.class);
    }

    public UB1 getUB1() {
        return getTyped(UB1.class);
    }

    public UB2 getUB2() {
        return getTyped(UB2.class);
    }

    private <T extends Structure> T insertRep(String name, int rep) throws HL7Exception {
        return (T)insertRepetition(name, rep);
    }

    private <T extends Structure> T removeRep(String name, int rep) throws HL7Exception {
        return (T)removeRepetition(name, rep);
    }

    private <T extends Structure> T getTyped(Class<T> clazz) {
        return getTyped(clazz.getSimpleName(), clazz);
    }

    private <T extends Structure> T getTyped(int rep, Class<T> clazz) {
        return getTyped(clazz.getSimpleName(), rep, clazz);
    }

    private <T extends Structure> List<T> getAllAsList(Class<T> clazz) throws HL7Exception {
        return getAllAsList(clazz.getSimpleName(), clazz);
    }
}
