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

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Martin Krasser
 */
@ExtendWith(SpringExtension.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class})
@ContextConfiguration(locations = { "/config/context-transport2.xml" })
public class Transport2Test {

    private static final Logger log = LoggerFactory.getLogger(Transport2Test.class);

    @Test
    public void testMessage01() throws Exception {
        var message = inputMessage("message/msg-01.hl7");

        var socket = new Socket("localhost", 8889);
        var out = new BufferedOutputStream(new DataOutputStream(socket.getOutputStream()));
        final var in = new BufferedInputStream(new DataInputStream(socket.getInputStream()));

        var messageCount = 100;
        var latch = new CountDownLatch(messageCount);

        var t = new Thread(() -> {
            int response;
            var s = new StringBuilder();
            try {
                while ((response = in.read()) >= 0) {
                    if (response == 28) {
                        response = in.read(); // read second end byte
                        if (response == 13) {
                            log.debug("Received response");
                            log.debug(s.toString().replace('\r', '\n'));
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

        for (var i = 0; i < messageCount; i++) {
            var msg = message.replace("123456", String.valueOf(i));
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

        var success = latch.await(20, TimeUnit.SECONDS);

        out.close();
        in.close();
        socket.close();

        assertTrue(success);
    }

    private static String inputMessage(String resource) {
        return new Scanner(Transport2Test.class.getResourceAsStream("/" + resource)).useDelimiter("\\A").next();
    }
    
}
