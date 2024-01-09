package com.baqterya.muzukanji;

import com.baqterya.muzukanji.model.Kanji;
import com.baqterya.muzukanji.repository.KanjiRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasSize;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MuzukanjiApplicationTests {

	@LocalServerPort
	private Integer port;

	static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:alpine");

	@BeforeAll
	static void beforeAll() {
		postgres.start();
	}

	@AfterAll
	static void afterAll() {
		postgres.stop();
	}

	@DynamicPropertySource
	static void configureProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url", postgres::getJdbcUrl);
		registry.add("spring.datasource.username", postgres::getUsername);
		registry.add("spring.datasource.password", postgres::getPassword);
	}

	@Autowired
	KanjiRepository kanjiRepository;

	@BeforeEach
	void setUp() {
		RestAssured.baseURI = "http://localhost:" + port;
		kanjiRepository.deleteAll();
	}

	@Test
	void kanjiListLoads() {
		List<Kanji> testData = List.of(
				Kanji.builder()
						.kanji("人")
						.meanings(List.of("person"))
						.kunyomi(List.of("ひと", "-り", "-と"))
						.kunyomiRomaji(List.of("hito", "-ri", "-to"))
						.onyomi(List.of("ジン", "ニン"))
						.onyomiRomaji(List.of("jin", "nin"))
						.strokes(2)
						.jlptLevel("N5")
						.jyoyoGradeTaught(1)
						.mostUsedInNewspapers(5)
						.build(),
				Kanji.builder()
						.kanji("空")
						.meanings(List.of("empty", "sky", "void", "vacant", "vacuum"))
						.kunyomi(List.of("そら", "あ.く", "あ.き", "あ.ける", "から", "す.く", "す.かす", "むな.しい"))
						.kunyomiRomaji(List.of("sora", "a.ku", "a.ki", "a.keru", "kara", "su.ku", "su.kasu", "muna.shii"))
						.onyomi(List.of("クウ"))
						.onyomiRomaji(List.of("kuu"))
						.strokes(8)
						.jlptLevel("N4")
						.jyoyoGradeTaught(1)
						.mostUsedInNewspapers(304)
						.build()
		);
		kanjiRepository.saveAll(testData);

		given()
				.auth()
				.basic("admin", "password")
				.contentType(ContentType.JSON)
				.when()
				.get("api/v1/kanji")
				.then()
				.statusCode(200)
				.body(".", hasSize(2));
	}

}
