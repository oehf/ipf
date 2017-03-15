/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.platform.camel.hl7.transport;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;

/**
 * @author Martin Krasser
 */
@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class})
@ContextConfiguration(locations = { "/config/context-transport2.xml" })
public class TransportTest2 {

    private static final Logger LOG = LoggerFactory.getLogger(TransportTest2.class);

    @Test
    public void testMessage01() throws Exception {
        String message = inputMessage("message/msg-01.hl7");

        Socket socket = new Socket("localhost", 8888);
        BufferedOutputStream out = new BufferedOutputStream(new DataOutputStream(socket.getOutputStream()));
        final BufferedInputStream in = new BufferedInputStream(new DataInputStream(socket.getInputStream()));

        int messageCount = 100;
        CountDownLatch latch = new CountDownLatch(messageCount);

        Thread t = new Thread(() -> {
            int response;
            StringBuilder s = new StringBuilder();
            try {
                while ((response = in.read()) >= 0) {
                    if (response == 28) {
                        response = in.read(); // read second end byte
                        if (response == 13) {
                            LOG.debug("Received response");
                            LOG.debug(s.toString().replace('\r', '\n'));
                            s.setLength(0);
                            latch.countDown();
                        }
                    } else {
                        s.append((char) response);
                    }
                }
            } catch (IOException ignored) {
            }
        });
        t.start();

        for (int i = 0; i < messageCount; i++) {
            String msg = message.replace("123456", String.valueOf(i));
            out.write(11);
            out.flush();
            // Some systems send end bytes in a separate frame
            Thread.sleep(10);
            out.write(msg.getBytes());
            out.flush();
            // Some systems send end bytes in a separate frame
            // Thread.sleep(10);
            out.write(28);
            out.write(13);
            out.flush();
            // Thread.sleep(10);
        }

        boolean success = latch.await(20, TimeUnit.SECONDS);

        out.close();
        in.close();
        socket.close();

        assertTrue(success);
    }

    private static String inputMessage(String resource) {
        return new Scanner(TransportTest2.class.getResourceAsStream("/" + resource)).useDelimiter("\\A").next();
    }
    
}
