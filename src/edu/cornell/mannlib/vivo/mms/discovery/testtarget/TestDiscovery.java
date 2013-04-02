package edu.cornell.mannlib.vivo.mms.discovery.testtarget;

import java.util.Arrays;

import edu.cornell.mannlib.vivo.mms.discovery.DiscoveryWorker;

public class TestDiscovery implements DiscoveryWorker {
	@Override
	public Iterable<String> getUrisForSite(String siteUrl) {
		return Arrays.asList(
				"http://vivo.cornell.edu/individual/individual19589",
				"http://vivo.cornell.edu/individual/JamesBlake",
				"http://vivo.cornell.edu/individual/individual24416",
				"http://vivo.cornell.edu/individual/individual5320",
				"http://vivo.cornell.edu/individual/individual6059");
	}

}
