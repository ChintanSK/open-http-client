package com.ma.open.http.client.request.ssl;

import java.io.File;
import java.security.KeyStore;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

public class SSLConfig {
	private SSLContext sslContext;

	private KeyManager keyManager;
	private KeyStore keyStore;
	private File keyStoreFile;
	private String keyStoreFilePath;
	private char[] keyStorePassphrase;

	private TrustManager trustManager;
	private KeyStore trustStore;
	private File trustStoreFile;
	private String trustStoreFilePath;
	private char[] trustStorePassphrase;

	private HostnameVerifier hostnameVerifier;

	private String algorithm;

	private String securityDomain;

	public static SSLConfigurator newSSLConfig() {
		return new SSLConfigurator();
	}

	SSLConfig(SSLConfigurator sslConfigurator) {
		this.sslContext = sslConfigurator.getSslContext();

		this.keyManager = sslConfigurator.getKeyManager();
		this.keyStore = sslConfigurator.getKeyStore();
		this.keyStoreFile = sslConfigurator.getKeyStoreFile();
		this.keyStoreFilePath = sslConfigurator.getKeyStoreFilePath();
		this.keyStorePassphrase = sslConfigurator.getKeyStorePassphrase();

		this.trustManager = sslConfigurator.getTrustManager();
		this.trustStore = sslConfigurator.getTrustStore();
		this.trustStoreFile = sslConfigurator.getTrustStoreFile();
		this.trustStoreFilePath = sslConfigurator.getTrustStoreFilePath();
		this.trustStorePassphrase = sslConfigurator.getTrustStorePassphrase();

		this.hostnameVerifier = sslConfigurator.getHostnameVerifier();

		this.algorithm = sslConfigurator.getAlgorithm();

		this.securityDomain = sslConfigurator.getSecurityDomain();
	}

	public SSLContext getSslContext() {
		return sslContext;
	}

	public KeyManager getKeyManager() {
		return keyManager;
	}

	public KeyStore getKeyStore() {
		return keyStore;
	}

	public File getKeyStoreFile() {
		return keyStoreFile;
	}

	public String getKeyStoreFilePath() {
		return keyStoreFilePath;
	}

	public char[] getKeyStorePassphrase() {
		return keyStorePassphrase;
	}

	public TrustManager getTrustManager() {
		return trustManager;
	}

	public KeyStore getTrustStore() {
		return trustStore;
	}

	public File getTrustStoreFile() {
		return trustStoreFile;
	}

	public String getTrustStoreFilePath() {
		return trustStoreFilePath;
	}

	public char[] getTrustStorePassphrase() {
		return trustStorePassphrase;
	}

	public HostnameVerifier getHostnameVerifier() {
		return hostnameVerifier;
	}

	public String getAlgorithm() {
		return algorithm;
	}

	public String getSecurityDomain() {
		return securityDomain;
	}

}
