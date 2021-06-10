package admin_api;/*
 * CreateData 21.05.2021 Denis
 * Copyright (c) 2021 WINGS.
 */

import config.BaseTest;
import org.junit.jupiter.api.BeforeEach;

import java.util.ArrayList;

import static com.codeborne.selenide.Selenide.open;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

public class CreateAdminData extends BaseTest {
    public String partnerIdStr,
    providerIdStr;

    @BeforeEach
    public void beforeFunction() {
        open("");

    }
    public String createClient (String clientName) {
        given()
                .contentType("application/json;charset=UTF-8")
                .header("Authorization", tokenAdmin)
                .body("{\n" +
                        "     \"name\" : \"" + clientName + "\",\n" +
                        "     \"transports\" : [\n" +
                        "               { \"id\" : 1,\n" +
                        "\"multisignature\" : true,\n" +
                        "\"on_moderation\" : false\n" +
                        "               },\n" +
                        "               { \"id\" : 2,\n" +
                        "\"multisignature\" : true,\n" +
                        "\"on_moderation\" : false\n" +
                        "               },\n" +
                        "               { \"id\" : 3,\n" +
                        "\"multisignature\" : true,\n" +
                        "\"on_moderation\" : false\n" +
                        "               },\n" +
                        "               { \"id\" : 6,\n" +
                        "\"multisignature\" : true,\n" +
                        "\"on_moderation\" : false\n" +
                        "               }\n" +
                        "               ],\n" +
                        "        \"prepaid\" : true,\n" +
                        "        \"status\" : 1\n" +
                        "}             ")
                .when()
                .post("http://192.168.128.215/acapi/partners/")
                .then()
                .log().everything()
                .statusCode(200)
                .body("status", is("Success"));
        System.out.println("\n Get partners list");
        ArrayList<String> partnerId;
        partnerId = given()
                .contentType("application/json;charset=UTF-8")
                .header("Authorization", tokenAdmin)
                .param("name", clientName)
                .when()
                .get("http://192.168.128.215/acapi/partners/")
                .then()
                .extract().path("partners.id");
        //Преобразуем partnerId в строку, убираем скобки

        partnerIdStr = partnerId.toString();
        partnerIdStr = partnerIdStr.substring(1, partnerIdStr.length() - 1);
        System.out.println("partnerIdStr = " + partnerIdStr);

        return partnerIdStr;
    }


    public void createSender(String senderAddress, String partnerIdStr) {
        System.out.println("senderAddress is " + senderAddress+" and psrtnerId is " + partnerIdStr);
        given()
                .contentType("application/json;charset=UTF-8")
                .header("Authorization", tokenAdmin)
                .body("{\n" +
                        "\t\"partner_id\" : "+partnerIdStr+",\n" +
                        "\t\"transport_id\" : 1,\n" +
                        "\t\"originator\" : \""+senderAddress+"\",\n" +
                        "\t\"status_id\" :  1,\n" +
                        "\t\"description\" : \"\"\n" +
                        "}")
                .when()
                .post("http://192.168.128.215/acapi/originators/")
                .then()
                .log().everything()
                .statusCode(200);

    }

    public String createServiceProvides(String smsProviderName) {
        given()
                .contentType("application/json;charset=UTF-8")
                .header("Authorization", tokenAdmin)
                .body("{\n" +
                        "    \"name\": \"" + smsProviderName + "\",\n" +
                        "    \"status\": 1,\n" +
                        "    \"parameters\": [\n" +
                        "        {\n" +
                        "            \"name\": \"MAIN_GATEWAY\",\n" +
                        "            \"protocol_parameter_id\": 1,\n" +
                        "            \"protocol_id\": 1,\n" +
                        "            \"protocol_name\": \"SMPP\",\n" +
                        "            \"value\": null\n" +
                        "        },\n" +
                        "        {\n" +
                        "            \"name\": \"RESERVE_GATEWAY\",\n" +
                        "            \"protocol_parameter_id\": 2,\n" +
                        "            \"protocol_id\": 1,\n" +
                        "            \"protocol_name\": \"SMPP\",\n" +
                        "            \"value\": null\n" +
                        "        }\n" +
                        "    ],\n" +
                        "    \"transport_ids\": [\n" +
                        "        1\n" +
                        "    ],\n" +
                        "    \"sms_protocol_id\": 1\n" +
                        "}")
                .when()
                .post("http://192.168.128.215/acapi/service_providers")
                .then()
                .log().all()
                .statusCode(200)
                .body("status", is("Success"));

        ArrayList<String> providerId;
        providerId = given()
                .contentType("application/json;charset=UTF-8")
                .header("Authorization", tokenAdmin)
                .param("name", smsProviderName)
                .when()
                .get("http://192.168.128.215/acapi/service_providers/")
                .then()
                .extract().path("service_providers.id");

        providerIdStr = providerId.toString();
        providerIdStr = providerIdStr.substring(1, providerIdStr.length() - 1);
        System.out.println("providerIdStr " + providerIdStr);

        return providerIdStr;

    }

    public void createSMSTariffZoneProvider(String serviceProviderId, String clientName) {
        given()
                .contentType("application/json;charset=UTF-8")
                .header("Authorization", tokenAdmin)
                .body("{\n" +
                        "\"name\" : \"Provider tariff zone for " + clientName + "\",\n" +
                        "\"msisdn\" :\"7\",\n" +
                        "\"mcc\" : \"250\",\n" +
                        "\"mnc\" : \"01\",\n" +
                        "\"price\" : \"01.23\",\n" +
                        "\"status\" : 1\n" +
                        "} ")
                .when()
                .post("http://192.168.128.215/acapi/sms/service_providers/" + serviceProviderId + "/tariff_zones")
                .then()
                .log().all()
                .statusCode(200);
    }

    public void createSMSTariffZoneGroupsProvider(String serviceProviderId) {
        given()
                .contentType("application/json;charset=UTF-8")
                .header("Authorization", tokenAdmin)
                .body("{\n" +
                        "\t\"charging\":\"DELIVERY\",\n" +
                        "\t\"routing\":\"MSISDN\"\n" +
                        "}")
                .when()
                .post("http://192.168.128.215/acapi/sms/service_providers/" + serviceProviderId + "/tariff_zone_groups")
                .then()
                .log().all()
                .statusCode(200);
    }

    public void createSMSTariffZoneGroupsPartner(String partnerId) {
        given()
                .contentType("application/json;charset=UTF-8")
                .header("Authorization", tokenAdmin)
                .body("{\n" +
                        "    \"charging\": \"SENDING\",\n" +
                        "    \"routing\": \"MCC/MNC\",\n" +
                        "    \"is_all_operators_allowed\": true\n" +
                        "}")
                .when()
                .post("http://192.168.128.215/acapi/sms/partners/" + partnerId + "/tariff_zone_groups")
                .then()
                .log().all()
                .statusCode(200);
    }

    public void createSMSTariffZonePartner(String partnerId, String clientName) {
        given()
                .contentType("application/json;charset=UTF-8")
                .header("Authorization", tokenAdmin)
                .body("{\n" +
                        "\"name\" : \"Partner tariff zone for " + clientName + "\",\n" +
                        "\"msisdn\" :\"7\",\n" +
                        "\"mcc\" : \"250\",\n" +
                        "\"mnc\" : \"01\",\n" +
                        "\"price\" : \"01.23\",\n" +
                        "\"status\" : 1\n" +
                        "} ")
                .when()
                .post("http://192.168.128.215/acapi/sms/partners/" + partnerId + "/tariff_zones")
                .then()
                .log().all()
                .statusCode(200);
    }

    public void createPartnerUsers(String partnerEmail, String clientName, String partnerLkName, String partnerPassword) {
        given()
                .contentType("application/json;charset=UTF-8")
                .header("Authorization", tokenAdmin)
                .body("{\n" +
                        "\t\"name\" : \"" + partnerLkName + "\",\n" +
                        "\t\"email\" : \"" + partnerEmail + "\",\n" +
                        "\t\"partner\" : \"" + clientName + "\",\n" +
                        "\t\"password\" : \"" + partnerPassword + "\",\n" +
                        "\t\"timezone_id\" : 4,\n" +
                        "\t\"show_messages_text\" : 0,\n" +
                        "\t\"is_check_pass\" : 0,\n" +
                        "\t\"status\" : 1\n" +
                        "}\n")
                .when()
                .post("http://192.168.128.215/acapi/partner_users/")
                .then()
                .log().all()
                .statusCode(200);
    }
}

