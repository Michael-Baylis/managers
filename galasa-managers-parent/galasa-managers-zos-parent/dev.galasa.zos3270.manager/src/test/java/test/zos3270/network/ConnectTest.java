/*
 * Copyright (c) 2019 IBM Corporation.
 */
package test.zos3270.network;

import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;
import org.junit.internal.ArrayComparisonFailure;

import dev.galasa.zos3270.internal.comms.Network;
import dev.galasa.zos3270.spi.NetworkException;
import test.zos3270.util.DummySocket;
import test.zos3270.util.DummySocketImpl;

public class ConnectTest {
	
	@Test
	public void testGoldenPath() throws ArrayComparisonFailure, UnsupportedEncodingException, IOException, NetworkException {
		ByteArrayInputStream fromServer = new ByteArrayInputStream(NegotiationTest.getServerNegotiation().toByteArray());
		ByteArrayOutputStream toServer = new ByteArrayOutputStream();
		
		
		DummySocket dummySocket = new DummySocket(new DummySocketImpl(fromServer, toServer));
		
		Network network = new Network("dummy", 0) {
			@Override
			public Socket createSocket() throws UnknownHostException, IOException {
				return dummySocket;
			}
		};
		
		Assert.assertTrue("Network did not connect", network.connectClient());
		
		Assert.assertArrayEquals("Negotiation was not completed correctly via dummy socket", 
				NegotiationTest.getClientNegotiation().toByteArray(), 
				toServer.toByteArray());
		
	}
	
	@Test
	public void testCreateSocket() throws Exception {
		Network network = new Network("google.com", 80);
		try (Socket socket = network.createSocket()) {
			Assert.assertEquals("Invalid port was set", 80, socket.getPort());
		}
		
	}
	
	
	@Test
	public void testExceptionDuringConnect() throws SocketException, NetworkException {
		DummySocket dummySocket = new DummySocket(new DummySocketImpl(null, null));
		
		Network network = new Network("dummy", 0) {
			@Override
			public Socket createSocket() throws UnknownHostException, IOException {
				return dummySocket;
			}
		};
		
		try {
			network.connectClient();
			fail("Should have thrown a network exception");
		} catch(NetworkException e) {
			if (!e.getMessage().contains("Unable to connect")) {
				e.printStackTrace();
				fail("Did not contain 'Unable to Connect' message");
			}
		}
		
		Assert.assertTrue("Should have closed the Socket on exception", dummySocket.testClosed);

		return;
	}
	
	@Test
	public void testConnectTwice() throws UnsupportedEncodingException, IOException, NetworkException {
		ByteArrayInputStream fromServer = new ByteArrayInputStream(NegotiationTest.getServerNegotiation().toByteArray());
		ByteArrayOutputStream toServer = new ByteArrayOutputStream();
		
		DummySocketImpl dummySocketImpl = new DummySocketImpl(fromServer, toServer);
		DummySocket dummySocket = new DummySocket(dummySocketImpl);
		
		Network network = new Network("dummy", 0) {
			@Override
			public Socket createSocket() throws UnknownHostException, IOException {
				return dummySocket;
			}
		};
		
		Assert.assertTrue("Network did not connect", network.connectClient());
		Assert.assertTrue("Network did not connect", network.connectClient());
		Assert.assertEquals("Appears to have reopened a socket", 1, dummySocketImpl.getInputStreamCount);
		Assert.assertEquals("Appears to have closed a socket", 0, dummySocketImpl.closeCount);
	}

	@Test
	public void testConnectTwiceWithClose() throws UnsupportedEncodingException, IOException, NetworkException {
		ByteArrayInputStream fromServer1 = new ByteArrayInputStream(NegotiationTest.getServerNegotiation().toByteArray());
		ByteArrayInputStream fromServer2 = new ByteArrayInputStream(NegotiationTest.getServerNegotiation().toByteArray());
		ByteArrayOutputStream toServer = new ByteArrayOutputStream();
		
		DummySocketImpl dummySocketImpl1 = new DummySocketImpl(fromServer1, toServer);
		DummySocketImpl dummySocketImpl2 = new DummySocketImpl(fromServer2, toServer);
		ArrayList<DummySocket> sockets = new ArrayList<>();
		DummySocket dummySocket = new DummySocket(dummySocketImpl1);
		sockets.add(dummySocket);
		sockets.add(new DummySocket(dummySocketImpl2));
		
		Network network = new Network("dummy", 0) {
			@Override
			public Socket createSocket() throws UnknownHostException, IOException {
				return sockets.remove(0);
			}
		};
		
		Assert.assertTrue("Network did not connect", network.connectClient());
		dummySocket.connected = false;
		Assert.assertTrue("Network did not connect", network.connectClient());
		Assert.assertEquals("Appears to have not opened a socket", 1, dummySocketImpl1.getInputStreamCount);
		Assert.assertEquals("Appears to have not opened a socket", 1, dummySocketImpl2.getInputStreamCount);
		Assert.assertEquals("Appears to have not closed a socket", 1, dummySocketImpl1.closeCount);
		Assert.assertEquals("Appears to have closed a socket", 0, dummySocketImpl2.closeCount);
	}

}
