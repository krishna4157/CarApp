package com.vasilkoff.easyvpnfree;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class Tls12SocketFactory extends SSLSocketFactory {
    private final SSLSocketFactory delegate;

    public Tls12SocketFactory(SSLSocketFactory base) {
        this.delegate = base;
    }

    @Override
    public String[] getDefaultCipherSuites() {
        return delegate.getDefaultCipherSuites();
    }

    @Override
    public String[] getSupportedCipherSuites() {
        return delegate.getSupportedCipherSuites();
    }

    @Override public Socket createSocket(Socket s, String host, int port, boolean autoClose) throws IOException {
        Socket sock = delegate.createSocket(s, host, port, autoClose);
        enableTLS12(sock); return sock;
    }

    @Override public Socket createSocket(String host, int port) throws IOException {
        Socket sock = delegate.createSocket(host, port);
        enableTLS12(sock); return sock;
    }

    private void enableTLS12(Socket socket) {
        if (socket instanceof SSLSocket) {
            ((SSLSocket)socket).setEnabledProtocols(new String[]{"TLSv1.2"});
        }
    }

    @Override
    public Socket createSocket(String s, int i, InetAddress inetAddress, int i1) throws IOException, UnknownHostException {
        return null;
    }

    @Override
    public Socket createSocket(InetAddress inetAddress, int i) throws IOException {
        return null;
    }

    @Override
    public Socket createSocket(InetAddress inetAddress, int i, InetAddress inetAddress1, int i1) throws IOException {
        return null;
    }
}
