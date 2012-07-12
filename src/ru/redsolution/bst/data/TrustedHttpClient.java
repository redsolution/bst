package ru.redsolution.bst.data;

import java.io.InputStream;
import java.security.KeyStore;

import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import ru.redsolution.bst.R;
import android.content.Context;

public class TrustedHttpClient extends DefaultHttpClient {

	private static final int CONNECTION_TIMEOUT = 10000;
	private static final int SO_TIMEOUT = 120000;

	final Context context;

	public TrustedHttpClient(Context context) {
		super(createParams());
		this.context = context;
	}

	private static HttpParams createParams() {
		HttpParams httpParameters = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParameters,
				CONNECTION_TIMEOUT);
		HttpConnectionParams.setSoTimeout(httpParameters, SO_TIMEOUT);
		return httpParameters;
	}

	@Override
	protected ClientConnectionManager createClientConnectionManager() {
		SchemeRegistry registry = new SchemeRegistry();
		registry.register(new Scheme("http", PlainSocketFactory
				.getSocketFactory(), 80));
		registry.register(new Scheme("https", newSslSocketFactory(), 443));
		return new ThreadSafeClientConnManager(getParams(), registry);
	}

	private SSLSocketFactory newSslSocketFactory() {
		try {
			KeyStore trusted = KeyStore.getInstance("BKS");
			InputStream in = context.getResources()
					.openRawResource(R.raw.store);
			try {
				trusted.load(in, "".toCharArray());
			} finally {
				in.close();
			}
			return new SSLSocketFactory(trusted);
		} catch (Exception e) {
			throw new AssertionError(e);
		}
	}
}
