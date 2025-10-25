package com.vasilkoff.easyvpnfree;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class TLSSocketFactoryCompat extends SSLSocketFactory {

    private final SSLSocketFactory delegate;

    public TLSSocketFactoryCompat() throws Exception {
        SSLContext context = SSLContext.getInstance("TLSv1.2");
        context.init(null, null, null);
        delegate = context.getSocketFactory();
    }

    @Override
    public String[] getDefaultCipherSuites() {
        return delegate.getDefaultCipherSuites();
    }

    @Override
    public String[] getSupportedCipherSuites() {
        return delegate.getSupportedCipherSuites();
    }

    @Override
    public Socket createSocket(Socket s, String host, int port, boolean autoClose) throws IOException {
        Socket socket = delegate.createSocket(s, host, port, autoClose);
        enableTLSOnSocket(socket);
        return socket;
    }

    @Override
    public Socket createSocket(String host, int port) throws IOException {
        Socket socket = delegate.createSocket(host, port);
        enableTLSOnSocket(socket);
        return socket;
    }

    @Override
    public Socket createSocket(InetAddress host, int port) throws IOException {
        Socket socket = delegate.createSocket(host, port);
        enableTLSOnSocket(socket);
        return socket;
    }

    @Override
    public Socket createSocket(String host, int port, InetAddress localHost, int localPort) throws IOException {
        Socket socket = delegate.createSocket(host, port, localHost, localPort);
        enableTLSOnSocket(socket);
        return socket;
    }

    @Override
    public Socket createSocket(InetAddress address, int port, InetAddress localAddress, int localPort) throws IOException {
        Socket socket = delegate.createSocket(address, port, localAddress, localPort);
        enableTLSOnSocket(socket);
        return socket;
    }

    private void enableTLSOnSocket(Socket socket) {
        if (socket instanceof SSLSocket) {
            ((SSLSocket) socket).setEnabledProtocols(new String[]{"TLSv1.2"});
        }
    }
}

