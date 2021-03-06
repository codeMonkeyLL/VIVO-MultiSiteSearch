/* $This file is distributed under the terms of the license in /doc/license.txt$ */

package edu.cornell.mannlib.vivo.mss.utils.http;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;

/**
 * Create an HttpClient with the favored settings.
 */
public final class HttpClientFactory {
	
	public static HttpClient standardClient() {
		HttpClient http = new DefaultHttpClient();
		
		// Set the socket timeout to 60 seconds.
		http.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 60000);

		return http;
	}

	private HttpClientFactory() {
		// nothing to instantiate.
	}
}
