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
package org.openehealth.ipf.platform.manager.connection;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

/**
 * @author Mitko Kolev
 */
public class ConnectionConfigurationImplTest extends TestCase {

    public void testEqualsAndHashCode() {
        List<IConnectionConfiguration> added = new ArrayList<IConnectionConfiguration>();

        for (int t = 0; t < 20; t++) {
            IConnectionConfiguration connectionConfiguration = new ConnectionConfigurationImpl(
                    "connection" + t, "localhost", t + 123);
            // tests the equals
            assertFalse(added.contains(connectionConfiguration));
            assertTrue(connectionConfiguration.equals(connectionConfiguration));
            IConnectionConfiguration connectionConfiguration2 = new ConnectionConfigurationImpl(
                    "connection" + t, "localhost", t + 123);

            assertTrue(connectionConfiguration.equals(connectionConfiguration2));
            assertTrue(connectionConfiguration2.equals(connectionConfiguration));
            assertTrue(connectionConfiguration2.hashCode() == connectionConfiguration
                    .hashCode());

            added.add(connectionConfiguration);
        }

    }

    public void testConstructor1() {
        for (int t = 0; t < 20; t++) {
            IConnectionConfiguration connectionConfiguration = new ConnectionConfigurationImpl(
                    "connection" + t, "localhost", t + 123);
            assertTrue(connectionConfiguration.getHost().equals("localhost"));
            assertTrue(connectionConfiguration.getPort() == t + 123);
            assertTrue(connectionConfiguration.getName().equals(
                    "connection" + t));
        }
    }

    public void testConstructor2() {
        for (int t = 0; t < 20; t++) {
            IConnectionConfiguration connectionConfiguration = new ConnectionConfigurationImpl(
                    "connection" + t, "localhost", t + 123, "user" + t,
                    "password" + t);
            assertTrue(connectionConfiguration.getHost().equals("localhost"));
            assertTrue(connectionConfiguration.getPort() == t + 123);
            assertTrue(connectionConfiguration.getName().equals(
                    "connection" + t));
            assertTrue(connectionConfiguration.getAuthenticationCredentials()
                    .getUserName().equals("user" + t));
            assertTrue(connectionConfiguration.getAuthenticationCredentials()
                    .getPassword().equals("password" + t));
            assertTrue(connectionConfiguration.getAuthenticationCredentials()
                    .isValid());
            assertTrue(connectionConfiguration.getAuthenticationCredentials()
                    .toStringArray() != null);
            String[] array = connectionConfiguration
                    .getAuthenticationCredentials().toStringArray();
            assertTrue(array[0].equals("user" + t));
            assertTrue(array[1].equals("password" + t));

        }

    }

    public void testAuthenticationCredentialsHashCodeAndEquals() {
        AuthenticationCredentials credenatials = new AuthenticationCredentials(
                "user", "password");
        AuthenticationCredentials credenatials2 = new AuthenticationCredentials(
                "user", "password");

        assertTrue(credenatials.equals(credenatials2));
        assertTrue(credenatials2.equals(credenatials));
        assertTrue(credenatials2.hashCode() == credenatials.hashCode());

    }

    public void testAuthenticationCredentialsValid() {
        AuthenticationCredentials credenatials = new AuthenticationCredentials(
                "user", "password");

        credenatials.setUserName(null);
        assertFalse(credenatials.isValid());

        credenatials.setUserName("user");
        credenatials.setPassword(null);
        assertFalse(credenatials.isValid());

        credenatials.setUserName("");
        credenatials.setPassword("");
        assertFalse(credenatials.isValid());

        credenatials.setUserName("1");
        credenatials.setPassword("1");
        assertTrue(credenatials.isValid());

    }
}
