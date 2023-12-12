package org.jax.mgi.mgd.api;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;

@QuarkusMain
public class Main {
	public static void main(String[] args) throws Exception {
		System.out.println("Running main method of quarkus");
		Quarkus.run(args);
	}
}
