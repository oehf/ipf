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
package org.openehealth.ipf.commons.ihe.core.atna;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.junit.Assert;

/**
 * @author Martin Krasser
 */
public class UdpServer extends Thread {

    private static final Logger LOG = LoggerFactory.getLogger(UdpServer.class);

    private static final int BUFFER_SIZE = 65507;

    private static final int WAIT_TIMEOUT = 10; // seconds

    private final int port;

    private int expectedPacketCount;

    private DatagramSocket socket;

    private final List<String> packets;

    private CountDownLatch latch;

    public UdpServer(int port) {
        this.port = port;
        this.packets = Collections.synchronizedList(new ArrayList<String>());
    }

    public synchronized void expectedPacketCount(int expectedPacketCount) {
        this.expectedPacketCount = expectedPacketCount;
        this.latch = new CountDownLatch(expectedPacketCount);
    }

    public synchronized void assertIsSatisfied() throws InterruptedException {
        latch.await(WAIT_TIMEOUT, TimeUnit.SECONDS);
        Assert.assertEquals(expectedPacketCount, packets.size());
    }

    public synchronized void reset() {
        this.expectedPacketCount = 0;
        this.packets.clear();
        this.latch = null;
    }

    public String getPacket(int index) {
        return packets.get(index);
    }

    @Override
    public void run() {

        byte[] buffer = new byte[BUFFER_SIZE];

        try {
            socket = new DatagramSocket(port);
            LOG.debug("UDP server started on port " + port);
        } catch (SocketException e) {
            LOG.error("cannot open datagram socket", e);
            return;
        }

        try {
            while (true) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                packets.add(packetToString(packet));
                countDown();
            }
        } catch (SocketException e) {
            LOG.debug("socket closed");
        } catch (IOException e) {
            LOG.error(e);
        }

    }

    public void cancel() {
        socket.close();
    }

    private synchronized void countDown() {
        if (latch != null) {
            latch.countDown();
        }
    }

    private static String packetToString(DatagramPacket packet) {
        return new String(packet.getData(), packet.getOffset(), packet.getLength());
    }

}
