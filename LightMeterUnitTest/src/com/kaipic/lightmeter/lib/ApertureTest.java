package com.kaipic.lightmeter.lib;

import static org.junit.Assert.*;

import org.junit.Test;

public class ApertureTest {

	@Test
	public void shouldCreateFromString() throws Exception {
		assertApertureEquals(new Aperture(4f), Aperture.fromString("4.0"));
	}

	public static void assertApertureEquals(Aperture expected, Aperture actual) {
		assertEquals(expected.getValue(), actual.getValue(), 0.0001f);
	}
}
