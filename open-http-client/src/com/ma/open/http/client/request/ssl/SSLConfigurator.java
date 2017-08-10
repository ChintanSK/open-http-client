package com.ma.open.http.client.request.ssl;

import java.io.File;
import java.security.KeyStore;
import java.util.Arrays;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

public class SSLConfigurator {
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

	public SSLConfig getSSLConfig() {
		// TODO: validate if there is enough information available for building the
		// SSLContext
		return new SSLConfig(this);
	}

	public SSLConfigurator sslContext(SSLContext sslContext) {
		this.sslContext = sslContext;
		return this;
	}

	public SSLConfigurator keyManager(KeyManager keyManager) {
		this.keyManager = keyManager;
		return this;
	}

	public SSLConfigurator keyStore(KeyStore keyStore) {
		this.keyStore = keyStore;
		return this;
	}

	public SSLConfigurator keyStoreFile(File keyStoreFile) {
		this.keyStoreFile = keyStoreFile;
		return this;
	}

	public SSLConfigurator keyStoreFilePath(String keyStoreFilePath) {
		this.keyStoreFilePath = keyStoreFilePath;
		return this;
	}

	public SSLConfigurator keyStorePassphrase(char[] keyStorePassphrase) {
		this.keyStorePassphrase = Arrays.copyOf(keyStorePassphrase, keyStorePassphrase.length);
		return this;
	}

	public SSLConfigurator trustManager(TrustManager trustManager) {
		this.trustManager = trustManager;
		return this;
	}

	public SSLConfigurator trustStore(KeyStore trustStore) {
		this.trustStore = trustStore;
		return this;
	}

	public SSLConfigurator trustStoreFile(File trustStoreFile) {
		this.trustStoreFile = trustStoreFile;
		return this;
	}

	public SSLConfigurator trustStoreFilePath(String trustStoreFilePath) {
		this.trustStoreFilePath = trustStoreFilePath;
		return this;
	}

	public SSLConfigurator trustStorePassphrase(char[] trustStorePassphrase) {
		this.trustStorePassphrase = Arrays.copyOf(trustStorePassphrase, trustStorePassphrase.length);
		return this;
	}

	public SSLConfigurator hostnameVerifier(HostnameVerifier hostnameVerifier) {
		this.hostnameVerifier = hostnameVerifier;
		return this;
	}

	public SSLConfigurator algorithm(String algorithm) {
		this.algorithm = algorithm;
		return this;
	}

	public SSLConfigurator securityDomain(String securityDomain) {
		this.securityDomain = securityDomain;
		return this;
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
