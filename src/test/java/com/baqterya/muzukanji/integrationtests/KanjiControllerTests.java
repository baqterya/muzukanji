package com.baqterya.muzukanji.integrationtests;


import com.baqterya.muzukanji.model.Kanji;
import com.baqterya.muzukanji.model.KanjiDto;
import com.baqterya.muzukanji.repository.KanjiRepository;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.apache.http.client.utils.URIBuilder;
import dasniko.testcontainers.keycloak.KeycloakContainer;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.shaded.com.google.common.collect.ImmutableList;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Stream;

import static com.baqterya.muzukanji.util.Const.*;
import static io.restassured.RestAssured.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class KanjiControllerTests {

    private static final String POSTGRES_IMAGE = "postgres:alpine";
    private static final  String KEYCLOAK_IMAGE = "quay.io/keycloak/keycloak:23.0.0";
    private static final  String REALM_IMPORT_FILE = "/keycloak/muzukanji-realm.json";
    private static final  String REALM_NAME = "/realms/muzukanji";
    private static final String KANJI_ENDPOINT = "api/v1/kanji";

    private Integer kanjiId = 1;

    @LocalServerPort
    private Integer port;

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(POSTGRES_IMAGE);
    static KeycloakContainer keycloak = new KeycloakContainer(KEYCLOAK_IMAGE).withRealmImportFile(REALM_IMPORT_FILE);

    @BeforeAll
    static void beforeAll() {
        postgres.start();
        keycloak.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
        keycloak.stop();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add(
            "spring.security.oauth2.resourceserver.jwt.issuer-uri",
            () -> keycloak.getAuthServerUrl() + REALM_NAME
        );
        registry.add(
            "spring.security.oauth2.resourceserver.jwt.jwk-set-uri",
            () -> keycloak.getAuthServerUrl() + "/realms/muzukanji/protocol/openid-connect/certs"
        );
    }

    @Autowired
    KanjiRepository kanjiRepository;

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost:" + port;
        kanjiRepository.deleteAll();
    }

    private void updateKanjiId() {
        List<Kanji> kanjiList = kanjiRepository.findAll();
        if (kanjiList.isEmpty()) throw new RuntimeException("Kanji database is empty");

        kanjiId = kanjiList.get(kanjiList.size() - 1).getId();
    }

    protected String getKeycloakBearerToken() throws URISyntaxException {
        URI authorizationURI = new URIBuilder(
                keycloak.getAuthServerUrl() + "/realms/muzukanji/protocol/openid-connect/token"
        ).build();
        WebClient webClient = WebClient.builder().build();
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.put("grant_type", Collections.singletonList("password"));
        formData.put("username", Collections.singletonList("muzukanji_admin"));
        formData.put("password", Collections.singletonList("password"));
        formData.put("client_id", Collections.singletonList("muzukanji-rest-api"));

        String result = webClient.post()
            .uri(authorizationURI)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(BodyInserters.fromFormData(formData))
            .retrieve()
            .bodyToMono(String.class)
            .block();

        JacksonJsonParser jsonParser = new JacksonJsonParser();
        return "Bearer " + jsonParser.parseMap(result).get("access_token").toString();
    }

    @Test
    public void WhenGetKanji_ReturnOk() {
        kanjiRepository.save(TEST_KANJI);

        when().get(KANJI_ENDPOINT)
                .then().statusCode(HttpStatus.OK.value());
    }

    @Test
    public void WhenGetById_ReturnOkAndKanji() {
        kanjiRepository.save(TEST_KANJI);
        updateKanjiId();

        Response response = when().get(KANJI_ENDPOINT + "/" + kanjiId);
        response.then().statusCode(HttpStatus.OK.value());
        Assertions.assertThat(response.body().as(Kanji.class))
            .isNotNull()
            .isEqualTo(TEST_KANJI);
    }

    @Test
    public void WhenGetKanji_ReturnOkAndJsonPage() {
        List<Kanji> savedKanji = List.of(TEST_KANJI, TEST_KANJI_2);
        kanjiRepository.saveAll(savedKanji);

        when().get(KANJI_ENDPOINT)
            .then()
            .statusCode(HttpStatus.OK.value())
            .body("data", Matchers.notNullValue())
            .body("data.size()", Matchers.is(savedKanji.size()))
            .body("data[0].kanji", Matchers.equalTo(TEST_KANJI.getKanji()))
            .body("data[0].meanings", Matchers.equalTo(TEST_KANJI.getMeanings()));
    }

    @Test
    public void GivenAuthenticatedUser_WhenCreateKanji_ReturnCreatedAndKanjiDto() throws URISyntaxException {
        Kanji expectedKanji = TEST_KANJI;

        Response response = given().header("Authorization", getKeycloakBearerToken())
            .when()
            .contentType("application/json")
            .body(TEST_KANJI_DTO)
            .post(KANJI_ENDPOINT);
        response.then().statusCode(HttpStatus.CREATED.value());
        updateKanjiId();
        expectedKanji.setId(kanjiId);
        Assertions.assertThat(response.body().as(Kanji.class))
            .isNotNull()
            .isEqualTo(expectedKanji);
    }

    @Test
    public void GivenAuthenticatedUser_WhenUpdateKanji_ReturnOkAndKanji() throws URISyntaxException {
        kanjiRepository.save(TEST_KANJI);
        updateKanjiId();

        Kanji expectedOutputKanji = TEST_KANJI_2;
        expectedOutputKanji.setId(kanjiId);

        Response response = given().header("Authorization", getKeycloakBearerToken())
            .when()
            .contentType("application/json")
            .body(TEST_KANJI_DTO_2)
            .put(KANJI_ENDPOINT + "/" + kanjiId);

        response.then().statusCode(HttpStatus.OK.value());
        Assertions.assertThat(response.body().as(Kanji.class))
            .isNotNull()
            .isEqualTo(expectedOutputKanji);
    }

    @Test
    public void GivenAuthenticatedUser_WhenDeleteKanji_ReturnOkAndMessage() throws URISyntaxException {
        kanjiRepository.save(TEST_KANJI);
        updateKanjiId();
        String expectedMessage = String.format(KANJI_DELETED_MESSAGE, kanjiId);

        given().header("Authorization", getKeycloakBearerToken())
            .when()
            .contentType("application/json")
            .delete(KANJI_ENDPOINT + "/" + kanjiId)
            .then()
            .statusCode(HttpStatus.OK.value())
            .body(Matchers.is(expectedMessage));

        Optional<Kanji> removedKanji = kanjiRepository.findById(kanjiId);
        Assertions.assertThat(removedKanji).isEmpty();
    }

    @Test
    public void WhenGetById_NotInDatabase_ReturnNotFound() {
        when().get(KANJI_ENDPOINT + "/" + kanjiId)
            .then().statusCode(HttpStatus.NOT_FOUND.value());
    }

    @ParameterizedTest
    @ValueSource(strings = {"aaa", "-1", "0"})
    public void GivenInvalidId_WhenGetById_ReturnBadRequest(String invalidId) {
        when().get(KANJI_ENDPOINT + "/" + invalidId)
                .then().statusCode(HttpStatus.BAD_REQUEST.value());
    }

    static Stream<Arguments> generateValidFilterParams() {
        List<String> params = ImmutableList.of(
            "kanji:一","meaning:One, One Radical (no.1)",
            "kunyomi:ひと-, ひと.つ","kunyomiRomaji:hito-, hito.tsu",
            "onyomi:イチ, イツ","onyomiRomaji:ichi, itsu",
            "minStrokes:1","maxStrokes:1",
            "minJlptLevel:N5","maxJlptLevel:N5",
            "minJyoyoGrade:1","maxJyoyoGrade:1",
            "minUsage:1","maxUsage:2"
        );

        Set<String> concatenatedSet = new HashSet<>();
        Set<Set<String>> resultSet = new HashSet<>();

        for (String param : params) {
            concatenatedSet.add(param);

            resultSet.add(Set.of(param));
            resultSet.add(Set.copyOf(concatenatedSet));
        }
        Stream<Arguments> resultStream = Stream.of();
        for (Set<String> combination : resultSet) {
            if (combination.isEmpty()) continue;
            resultStream = Stream.concat(
                resultStream,
                Stream.of(Arguments.of(combination))
            );
        }
        return resultStream;
    }

    @ParameterizedTest
    @MethodSource("generateValidFilterParams")
    public void GivenValidFilterParams_WhenGetKanji_ReturnOkAndKanjiPage(Set<String> params) {
        boolean dataSizeIsTwo = false;
        List<String> paramsNotFilteringTheSecondKanji
                = List.of("maxJlptLevel:N5", "minUsage:1", "minStrokes:1", "minJyoyoGrade:1","maxJyoyoGrade:1");

        List<Kanji> savedKanji = List.of(TEST_KANJI, TEST_KANJI_2);
        kanjiRepository.saveAll(savedKanji);

        RequestSpecification result = given();
        for (String param : params) {
            if (params.size() == 1 && paramsNotFilteringTheSecondKanji.contains(param)) dataSizeIsTwo = true;
            String[] parts = param.split(":");
            result.queryParam(parts[0], parts[1]);
        }

        ValidatableResponse response = result.when().get(KANJI_ENDPOINT)
            .then()
            .statusCode(HttpStatus.OK.value())
            .body("data", Matchers.notNullValue());
        if (dataSizeIsTwo) response.body("data.size()", Matchers.is(2));
        else response.body("data.size()", Matchers.is(1));
        response.body("data[0].kanji", Matchers.equalTo(TEST_KANJI.getKanji()))
            .body("data[0].meanings", Matchers.equalTo(TEST_KANJI.getMeanings()));

    }

    @ParameterizedTest
    @CsvSource(
        value = {
            "kanji:aaa",
            "minJlptLevel:aaa","maxJlptLevel:aaa",
            "kunyomi:aaa","kunyomiRomaji:あああ","onyomi:aaa","onyomiRomaji:あああ",
            "minStrokes:-1","minStrokes:35","minStrokes:aaa",
            "maxStrokes:-1","maxStrokes:35","maxStrokes:aaa",
            "minJyoyoGrade:-1","minJyoyoGrade:35","minJyoyoGrade:aaa",
            "maxJyoyoGrade:-1","maxJyoyoGrade:35","maxJyoyoGrade:aaa",
            "minUsage:-1","minUsage:3500","minUsage:aaa",
            "maxUsage:-1","maxUsage:3500","maxUsage:aaa"
        },
        delimiter = ':'
    )
    public void GivenInvalidParams_WhenGetKanji_ReturnBadRequest(String param, String paramValue) {
        List<Kanji> savedKanji = List.of(TEST_KANJI, TEST_KANJI_2);
        kanjiRepository.saveAll(savedKanji);
        given().queryParam(param, paramValue)
            .when()
            .get(KANJI_ENDPOINT)
            .then()
            .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @ParameterizedTest
    @CsvSource(
        value = {
            "kanji:一","meaning:aaaa",
            "kunyomi:あああ","kunyomiRomaji:hito-, hito.tsu",
            "onyomi:あああ","onyomiRomaji:ichi, itsu",
            "minStrokes:8","maxStrokes:1",
            "minJlptLevel:N5","maxJlptLevel:N1",
            "minJyoyoGrade:4","maxJyoyoGrade:1",
            "minUsage:100","maxUsage:1"
        },
        delimiter = ':'
    )
    public void GivenValidParams_WhenGetKanji_NotInDatabase_ReturnNoContent(String param, String paramValue) {
        TEST_KANJI_2.setJyoyoGradeTaught(2);
        kanjiRepository.save(TEST_KANJI_2);

        given().queryParam(param, paramValue)
            .when()
            .get(KANJI_ENDPOINT)
            .then()
            .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    public void GivenUnauthenticatedUser_WhenCreateKanji_ReturnUnauthorised() {
        given().header("Authorization", "123123123")
            .when()
            .contentType("application/json")
            .body(TEST_KANJI_DTO)
            .post(KANJI_ENDPOINT)
            .then()
            .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    public void GivenUnauthenticatedUser_WhenUpdateKanji_ReturnUnauthorised() {
        given().header("Authorization", "123123123")
            .when()
            .contentType("application/json")
            .body(TEST_KANJI_DTO)
            .put(KANJI_ENDPOINT + "/" + 1)
            .then()
            .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    public void GivenUnauthenticatedUser_WhenDeleteKanji_ReturnUnauthorised() {
        given().header("Authorization", "123123123")
            .when()
            .contentType("application/json")
            .delete(KANJI_ENDPOINT + "/" + 1)
            .then()
            .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    public void GivenAuthenticatedUser_WhenCreateKanji_AlreadyExists_ReturnConflict() throws URISyntaxException {
        kanjiRepository.save(TEST_KANJI);

        given().header("Authorization", getKeycloakBearerToken())
            .when()
            .contentType("application/json")
            .body(TEST_KANJI_DTO)
            .post(KANJI_ENDPOINT)
            .then()
            .statusCode(HttpStatus.CONFLICT.value());
    }

    @Test
    public void GivenAuthenticatedUser_WhenUpdateKanji_NotInDatabase_ReturnNotFound() throws URISyntaxException {
        given().header("Authorization", getKeycloakBearerToken())
            .when()
            .contentType("application/json")
            .delete(KANJI_ENDPOINT + "/" + 1)
            .then()
            .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void GivenAuthenticatedUser_WhenDeleteKanji_NotInDatabase_ReturnNotFound() throws URISyntaxException {
        given().header("Authorization", getKeycloakBearerToken())
            .when()
            .contentType("application/json")
            .body(TEST_KANJI_DTO)
            .put(KANJI_ENDPOINT + "/" + 1)
            .then()
            .statusCode(HttpStatus.NOT_FOUND.value());
    }

    static Stream<Arguments> generateInvalidCreatePayload() {
        return Stream.of(
            Arguments.of(KanjiDto.builder().build()),
            Arguments.of(KanjiDto.builder().kanji("aaa").build()),
            Arguments.of(KanjiDto.builder().kanji("力").kunyomi("aaa").build()),
            Arguments.of(KanjiDto.builder().kanji("力").kunyomiRomaji("あああ").build()),
            Arguments.of(KanjiDto.builder().kanji("力").onyomi("aaa").build()),
            Arguments.of(KanjiDto.builder().kanji("力").onyomiRomaji("あああ").build()),
            Arguments.of(KanjiDto.builder().kanji("力").jlptLevel("aaa").build())
        );
    }

    @ParameterizedTest
    @MethodSource("generateInvalidCreatePayload")
    public void GivenAuthenticatedUser_InvalidPayload_WhenCreateKanji_ReturnBadRequest(KanjiDto invalidPayload) throws URISyntaxException {
        given().header("Authorization", getKeycloakBearerToken())
            .when()
            .contentType("application/json")
            .body(invalidPayload)
            .post(KANJI_ENDPOINT)
            .then().statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void GivenAuthenticatedUser_InvalidPayloadType_WhenCreateKanji_ReturnBadRequest() throws URISyntaxException, JSONException {
        JSONObject invalidPayload = new JSONObject();
        invalidPayload.put("?field1", "value1");
        invalidPayload.put("!field2", "value2");
        invalidPayload.put("#field3", "value3");

        given().header("Authorization", getKeycloakBearerToken())
            .when()
            .contentType("application/json")
            .body(invalidPayload.toString())
            .post(KANJI_ENDPOINT)
            .then().statusCode(HttpStatus.BAD_REQUEST.value());
    }

    static Stream<Arguments> generateInvalidUpdatePayload() {
        return Stream.of(
            Arguments.of("0", KanjiDto.builder().build()),
            Arguments.of("-1", KanjiDto.builder().build()),
            Arguments.of("1", KanjiDto.builder().kanji("力").kunyomi("aaa").build()),
            Arguments.of("1", KanjiDto.builder().kanji("力").kunyomiRomaji("あああ").build()),
            Arguments.of("1", KanjiDto.builder().kanji("力").onyomi("aaa").build()),
            Arguments.of("1", KanjiDto.builder().kanji("力").onyomiRomaji("あああ").build()),
            Arguments.of("1", KanjiDto.builder().kanji("力").jlptLevel("aaa").build()),
            Arguments.of("1", KanjiDto.builder().kanji("力").jlptLevel("aaa").build())
        );
    }
    
    @ParameterizedTest
    @MethodSource("generateInvalidUpdatePayload")
    public void GivenAuthenticatedUser_InvalidPayload_WhenUpdateKanji_ReturnBadRequest(String id, KanjiDto invalidPayload) throws URISyntaxException {
        kanjiRepository.save(TEST_KANJI);
        if (Objects.equals(id, "1")) {
            updateKanjiId();
            id = kanjiId.toString();
        }

        given().header("Authorization", getKeycloakBearerToken())
            .when()
            .contentType("application/json")
            .body(invalidPayload)
            .put(KANJI_ENDPOINT + "/" + id)
            .then().statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void GivenAuthenticatedUser_InvalidPayloadType_WhenUpdateKanji_ReturnForbidden() throws URISyntaxException, JSONException {
        kanjiRepository.save(TEST_KANJI);
        updateKanjiId();

        JSONObject invalidPayload = new JSONObject();
        invalidPayload.put("?field1", "value1");
        invalidPayload.put("!field2", "value2");
        invalidPayload.put("#field3", "value3");

        given().header("Authorization", getKeycloakBearerToken())
            .when()
            .contentType("application/json")
            .body(invalidPayload.toString())
            .put(KANJI_ENDPOINT)
            .then().statusCode(HttpStatus.FORBIDDEN.value());
    }

    @ParameterizedTest
    @ValueSource(strings = {"-1", "0", "aaa"})
    public void GivenAuthenticatedUser_InvalidId_WhenDeleteKanji_ReturnBadRequest(String id) throws URISyntaxException {
        given().header("Authorization", getKeycloakBearerToken())
            .when()
            .contentType("application/json")
            .put(KANJI_ENDPOINT + "/" + id)
            .then()
            .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void GivenStoppedDatabase_WhenGetKanji_ReturnInternalServerError() {
        postgres.stop();
        when().get(KANJI_ENDPOINT)
            .then().statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}
