/* $This file is distributed under the terms of the license in /doc/license.txt$ */

package edu.cornell.mannlib.vivo.mms.discovery.smallvivosite;

import java.util.Collection;
import java.util.Collections;

import org.junit.Test;

import edu.cornell.mannlib.vivo.mms.AbstractTestClass;
import edu.cornell.mannlib.vivo.mms.discovery.DiscoverUrisContext;
import edu.cornell.mannlib.vivo.mms.utils.http.BasicHttpWorker;
import edu.cornell.mannlib.vivo.mms.utils.http.BasicHttpWorkerRequest;
import edu.cornell.mannlib.vivo.mms.utils.http.HttpWorker;

/**
 * TODO
 */
public class DiscoverUrisUsingListrdfTest extends AbstractTestClass {
	/**
	 * 
	 */
	private static final String HARDCODED_RDF = "<rdf:RDF\n"
			+ "    xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\"\n"
			+ "    xmlns:j.0=\"http://xmlns.com/foaf/0.1/\" > \n"
			+ "  <rdf:Description rdf:about=\"http://vivo.mydomain.edu/individual/n5638\">\n"
			+ "    <rdf:type rdf:resource=\"http://xmlns.com/foaf/0.1/Person\"/>\n"
			+ "  </rdf:Description>\n" + "</rdf:RDF>\n";

	private DiscoverUrisUsingListrdf urlFinder;
	private DiscoverUrisContext duContext;

	@Test
	public void parseHardcodedResult() {
		duContext = new DiscoverUrisContext() {

			@Override
			public HttpWorker getHttpWorker() {
				return new DummyHttpWorker(HARDCODED_RDF);
			}

			@Override
			public Collection<String> getClassUris(String siteUrl) {
				return Collections.singleton("junk");
			}
		};

		urlFinder = new DiscoverUrisUsingListrdf();

		assertUnorderedActualIterable("URIs",
				urlFinder.getUrisForSite("http://BOGUS", duContext),
				"http://vivo.mydomain.edu/individual/n5638");
	}

	private static class DummyHttpWorker extends BasicHttpWorker {
		private final String responseString;

		public DummyHttpWorker(String responseString) {
			this.responseString = responseString;
		}

		@Override
		protected String executeRequest(BasicHttpWorkerRequest<?> request)
				throws HttpWorkerException {
			return responseString;
		}
	}
}
