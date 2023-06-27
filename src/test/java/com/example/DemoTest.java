package com.example;

import java.io.File;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.micronaut.runtime.EmbeddedApplication;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.restassured.specification.RequestSpecification;
import jakarta.inject.Inject;

@MicronautTest
class DemoTest {

	@Inject
	EmbeddedApplication<?> application;

	@Test
	void testItWorks() {
		Assertions.assertTrue(application.isRunning());
	}

	@Test
	void testUploadWithEmptyJobName(RequestSpecification spec) {
		//This test hangs!
		spec.given().multiPart("job-name", "").log().all().post("/submit").then().statusCode(200);
	}

	@Test
	void testUploadWithJobName(RequestSpecification spec) {
		spec.given().multiPart("job-name", "job1").log().all().post("/submit").then().statusCode(200);
	}

	@Test
	void testUploadWithEmptyJobNameAndFile(RequestSpecification spec) {
		spec.given().multiPart("job-name", "").and().multiPart("file-1", new File("micronaut-cli.yml")).log().all().post("/submit")
				.then().statusCode(200);
	}

}
