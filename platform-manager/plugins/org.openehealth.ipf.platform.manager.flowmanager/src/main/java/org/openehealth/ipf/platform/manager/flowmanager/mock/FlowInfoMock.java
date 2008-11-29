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
package org.openehealth.ipf.platform.manager.flowmanager.mock;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openehealth.ipf.platform.manager.flowmanager.IFlowInfo;
import org.openehealth.ipf.platform.manager.flowmanager.IFlowPartInfo;

/**
 * @author Mitko Kolev
 */
public class FlowInfoMock implements IFlowInfo {

    private static long ids = 1;
    
    int ackCount;
    int ackCountExpected;
    String application;
    Date creationTime;
    Long id;
    int nakCount;
    int replayCount;
    Date replayTime;
    boolean replayable;
    private final List<IFlowPartInfo> partInfos;
    
    public FlowInfoMock(){
        this.ackCount = 0; 
        this.ackCountExpected = -1;
        this.application = "test";
        this.creationTime  = new Date();
        id = ids ++;
        this.nakCount = 0;
        this.replayCount = 0;
        this.replayTime = null;
        this.replayable = false;
        partInfos = new ArrayList<IFlowPartInfo>();
        
    }
    @Override
    public int getAckCount() {

        return ackCount;
    }

    @Override
    public int getAckCountExpected() {
        return ackCountExpected;
    }

    @Override
    public String getApplication() {
        return application;
    }

    @Override
    public Date getCreationTime() {
        return creationTime;
    }

    @Override
    public Long getIdentifier() {
        return id;
    }

    @Override
    public int getNakCount() {
        return nakCount;
    }

    @Override
    public List<IFlowPartInfo> getPartInfos() {
        return partInfos;
    }

    @Override
    public int getReplayCount() {
        return replayCount;
    }

    @Override
    public Date getReplayTime() {
        return replayTime;
    }

    @Override
    public String getStatus() {
       for (IFlowPartInfo info: partInfos){
           if (info.getStatus().equals("ERROR")){
               return "ERROR";
           }
       }
       return "CLEAN";
    }

    @Override
    public boolean isReplayable() {
        return replayable;
    }

    public Long getId() {
        return id;
    }

    public void setAckCount(int ackCount) {
        this.ackCount = ackCount;
    }

    public void setAckCountExpected(int ackCountExpected) {
        this.ackCountExpected = ackCountExpected;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public void setNakCount(int nakCount) {
        this.nakCount = nakCount;
    }

    public void setReplayCount(int replayCount) {
        this.replayCount = replayCount;
    }

    public void setReplayTime(Date replayTime) {
        this.replayTime = replayTime;
    }

    public void setReplayable(boolean replayable) {
        this.replayable = replayable;
    }

}
