/*
 * Copyright 2018 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.openehealth.ipf.commons.audit.protocol;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.openehealth.ipf.commons.audit.AuditContext;
import org.openehealth.ipf.commons.audit.protocol.TLSSyslogSenderImpl.SocketTestPolicy;

public class TLSSyslogSenderImplTest {

	private static final String SENDING_HOST = "blah";
	private static final String SENDING_PROCESS = "blub";
	private static final String AUDIT_MESSAGE = "Quot erat demonstrandum!";
	private static final Integer AUDIT_REPO_PORT = 9999;

	@Mock
	private SSLSocketFactory socketFactory;
	@Mock
	private SSLSocket socket;
	@Mock
	private OutputStream os;
	@Mock
	private InputStream is;
	@Mock
	private AuditContext auditContext;
	@Mock
	private InetAddress inetAddress;

	private TLSSyslogSenderImpl tssi;

	@Rule
	public MockitoRule rule = MockitoJUnit.rule();

	@Before
	public void setup() throws IOException {
		when(auditContext.getSendingApplication()).thenReturn(SENDING_PROCESS);
		when(auditContext.getAuditRepositoryAddress()).thenReturn(inetAddress);
		when(auditContext.getAuditRepositoryPort()).thenReturn(AUDIT_REPO_PORT);

		when(socketFactory.createSocket(inetAddress, AUDIT_REPO_PORT)).thenReturn(socket);

		when(socket.getOutputStream()).thenReturn(os);
	}

	@Test
	public void sendDontTestSocketConnectionTest() throws Exception {
		ArgumentCaptor<byte[]> streamWriteCaptor = ArgumentCaptor.forClass(byte[].class);

		tssi = new TLSSyslogSenderImpl(SENDING_HOST, SENDING_PROCESS, socketFactory, SocketTestPolicy.DONT_TEST_POLICY);
		tssi.send(auditContext, AUDIT_MESSAGE);

		verify(socketFactory, times(1)).createSocket(any(InetAddress.class), any(Integer.class));
		verify(socket, never()).startHandshake();
		verify(socket, never()).setSoTimeout(any(Integer.class));
		verify(socket, never()).getSoTimeout();
		verify(socket, times(1)).setKeepAlive(true);
		// write #1: syslog frame metadata
		// write #2: audit message
		verify(os, times(2)).write(streamWriteCaptor.capture());
		final String auditMessageWithPreamble =  new String(streamWriteCaptor.getAllValues().get(1), StandardCharsets.UTF_8);
		assertTrue(auditMessageWithPreamble.endsWith(AUDIT_MESSAGE));
		// This is what counts: The DONT_TEST_POLICY shall not trigger any reads from the inputstream
		verify(is, never()).read();
	}

	@Test
	public void sendReadBeforeAndAfterSockeConnectionOKTest() throws Exception {
		ArgumentCaptor<byte[]> streamWriteCaptor = ArgumentCaptor.forClass(byte[].class);
		when(socket.getSoTimeout()).thenReturn(1);
		when(socket.getInputStream()).thenReturn(is);
		// Socket read timeout is used as signal that socket connection is alive and well.
		when(is.read()).thenThrow(new SocketTimeoutException());

		tssi = new TLSSyslogSenderImpl(SENDING_HOST, SENDING_PROCESS, socketFactory, SocketTestPolicy.TEST_BEFORE_AND_AFTER_WRITE);
		tssi.send(auditContext, AUDIT_MESSAGE);

		verify(socketFactory, times(1)).createSocket(any(InetAddress.class), any(Integer.class));
		verify(socket, times(1)).startHandshake();
		verify(socket, times(1)).setSoTimeout(1);
		verify(socket, times(1)).setKeepAlive(true);
		InOrder handshakeBeforeSoTimeout = inOrder(socket);
		handshakeBeforeSoTimeout.verify(socket).startHandshake();
		handshakeBeforeSoTimeout.verify(socket).setSoTimeout(any(Integer.class));
		// write #1: syslog frame metadata
		// write #2: audit message
		verify(os, times(2)).write(streamWriteCaptor.capture());
		final String auditMessageWithPreamble =  new String(streamWriteCaptor.getAllValues().get(1), StandardCharsets.UTF_8);
		assertTrue(auditMessageWithPreamble.endsWith(AUDIT_MESSAGE));
		// This is what counts: The TEST_BEFORE_AND_AFTER_WRITE shall trigger a read before and after the write operation
		verify(is, times(2)).read();
	}

	@Test
	public void sendReadBeforeAndAfterSockeConnectionClosedTest() throws Exception {
		ArgumentCaptor<byte[]> streamWriteCaptor = ArgumentCaptor.forClass(byte[].class);
		when(socket.getSoTimeout()).thenReturn(1);
		when(socket.getInputStream()).thenReturn(is);
		// Reading -1 from socket signals connection close -> new socket
		// Socket read timeout is used as signal that socket connection is alive and well.
		when(is.read()).thenReturn(-1).thenThrow(new SocketTimeoutException());

		tssi = new TLSSyslogSenderImpl(SENDING_HOST, SENDING_PROCESS, socketFactory, SocketTestPolicy.TEST_BEFORE_AND_AFTER_WRITE);
		tssi.send(auditContext, AUDIT_MESSAGE);

		// Because we simulate a closed socket connection we open two sockets in total.
		verify(socketFactory, times(2)).createSocket(any(InetAddress.class), any(Integer.class));
		verify(socket, times(2)).setSoTimeout(1);
		verify(socket, times(2)).setKeepAlive(true);
		InOrder handshakeBeforeSoTimeout = inOrder(socket);
		handshakeBeforeSoTimeout.verify(socket).startHandshake();
		handshakeBeforeSoTimeout.verify(socket).setSoTimeout(any(Integer.class));
		handshakeBeforeSoTimeout.verify(socket).startHandshake();
		handshakeBeforeSoTimeout.verify(socket).setSoTimeout(any(Integer.class));
		// write #1: syslog frame metadata
		// write #2: audit message
		verify(os, times(2)).write(streamWriteCaptor.capture());
		final String auditMessageWithPreamble =  new String(streamWriteCaptor.getAllValues().get(1), StandardCharsets.UTF_8);
		assertTrue(auditMessageWithPreamble.endsWith(AUDIT_MESSAGE));
		// On the first read/test we discover the connection has been closed -> triggers fall back loop, new socket connection
		// is opened and then we succeed on the second attempt with the normal flow of one test before and one after the write
		// operation.
		verify(is, times(3)).read();
	}

	@Test
	public void sendReadBeforeSockeConnectionOKTest() throws Exception {
		ArgumentCaptor<byte[]> streamWriteCaptor = ArgumentCaptor.forClass(byte[].class);
		when(socket.getSoTimeout()).thenReturn(1);
		when(socket.getInputStream()).thenReturn(is);
		// Socket read timeout is used as signal that socket connection is alive and well.
		when(is.read()).thenThrow(new SocketTimeoutException());

		tssi = new TLSSyslogSenderImpl(SENDING_HOST, SENDING_PROCESS, socketFactory, SocketTestPolicy.TEST_BEFORE_WRITE);
		tssi.send(auditContext, AUDIT_MESSAGE);

		verify(socketFactory, times(1)).createSocket(any(InetAddress.class), any(Integer.class));
		verify(socket, times(1)).setSoTimeout(1);
		verify(socket, times(1)).setKeepAlive(true);
		InOrder handshakeBeforeSoTimeout = inOrder(socket);
		handshakeBeforeSoTimeout.verify(socket).startHandshake();
		handshakeBeforeSoTimeout.verify(socket).setSoTimeout(any(Integer.class));
		// write #1: syslog frame metadata
		// write #2: audit message
		verify(os, times(2)).write(streamWriteCaptor.capture());
		final String auditMessageWithPreamble =  new String(streamWriteCaptor.getAllValues().get(1), StandardCharsets.UTF_8);
		assertTrue(auditMessageWithPreamble.endsWith(AUDIT_MESSAGE));
		// This is what counts: The TEST_BEFORE_WRITE shall trigger a read before the write operation.
		// Matter of fact, we cannot tell when the read happened. We just have to assume at this point.
		verify(is, times(1)).read();
	}

	@Test
	public void sendReadBeforeAndAfterSockeConnectionDeadTest() throws Exception {
		ArgumentCaptor<byte[]> streamWriteCaptor = ArgumentCaptor.forClass(byte[].class);
		when(socket.getSoTimeout()).thenReturn(1);
		when(socket.getInputStream()).thenReturn(is);
		// On first read we throw an IOException to signal a broken socket connection -> new socket
		// Socket read timeout is used as signal that socket connection is alive and well.
		when(is.read()).thenThrow(new IOException()).thenThrow(new SocketTimeoutException());

		tssi = new TLSSyslogSenderImpl(SENDING_HOST, SENDING_PROCESS, socketFactory, SocketTestPolicy.TEST_BEFORE_AND_AFTER_WRITE);
		tssi.send(auditContext, AUDIT_MESSAGE);

		// Because we simulate a closed socket connection we open two sockets in total.
		verify(socketFactory, times(2)).createSocket(any(InetAddress.class), any(Integer.class));
		verify(socket, times(2)).setSoTimeout(1);
		verify(socket, times(2)).setKeepAlive(true);
		InOrder handshakeBeforeSoTimeout = inOrder(socket);
		handshakeBeforeSoTimeout.verify(socket).startHandshake();
		handshakeBeforeSoTimeout.verify(socket).setSoTimeout(any(Integer.class));
		handshakeBeforeSoTimeout.verify(socket).startHandshake();
		handshakeBeforeSoTimeout.verify(socket).setSoTimeout(any(Integer.class));
		// write #1: syslog frame metadata
		// write #2: audit message
		verify(os, times(2)).write(streamWriteCaptor.capture());
		final String auditMessageWithPreamble =  new String(streamWriteCaptor.getAllValues().get(1), StandardCharsets.UTF_8);
		assertTrue(auditMessageWithPreamble.endsWith(AUDIT_MESSAGE));
		// On the first read/test we discover the connection has been closed -> triggers fall back loop, new socket connection
		// is opened and then we succeed on the second attempt with the normal flow of one test before and one after the write
		// operation.
		verify(is, times(3)).read();
	}

	@Test
    public void sendDonTestPolicyWithSocketOptionOverrideTest() throws Exception {
        ArgumentCaptor<byte[]> streamWriteCaptor = ArgumentCaptor.forClass(byte[].class);
        tssi = new SocketOptionOverrideTLSSyslogSenderImpl(SENDING_HOST, SENDING_PROCESS, socketFactory, SocketTestPolicy.DONT_TEST_POLICY);
        tssi.send(auditContext, AUDIT_MESSAGE);

        verify(socketFactory, times(1)).createSocket(any(InetAddress.class), any(Integer.class));
        verify(socket, never()).startHandshake();
        verify(socket, never()).setSoTimeout(any(Integer.class));
        verify(socket, never()).getSoTimeout();
        // The test sub-class calls the super implementation, so we should still see the call
        verify(socket, times(1)).setKeepAlive(true);
        // Call is done in our sub-class
        verify(socket, times(1)).setReceiveBufferSize(5);
        // write #1: syslog frame metadata
        // write #2: audit message
        verify(os, times(2)).write(streamWriteCaptor.capture());
        final String auditMessageWithPreamble =  new String(streamWriteCaptor.getAllValues().get(1), StandardCharsets.UTF_8);
        assertTrue(auditMessageWithPreamble.endsWith(AUDIT_MESSAGE));
        // This is what counts: The DONT_TEST_POLICY shall not trigger any reads from the inputstream
        verify(is, never()).read();
    }

	private class SocketOptionOverrideTLSSyslogSenderImpl extends TLSSyslogSenderImpl {

        public SocketOptionOverrideTLSSyslogSenderImpl(String sendingHost, String sendingProcess,
                SSLSocketFactory socketFactory, SocketTestPolicy socketTestPolicy) {
            super(sendingHost, sendingProcess, socketFactory, socketTestPolicy);
        }

        @Override
        protected void setSocketOptions(final Socket socket) throws SocketException {
            super.setSocketOptions(socket);
            socket.setReceiveBufferSize(5);
        }
	}
}
