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
        System.out.println(partnerIdStr);

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

    public String createServiceProvides() {
        given()
                .contentType("application/json;charset=UTF-8")
                .header("Authorization", tokenAdmin)
                .body("{ \n" +
                        "        \"name\" : \"YOTASMS1\",\n" +
                        "        \"transport_ids\" : [1],\n" +
                        "        \"parameters\" : [\n" +
                        "\t\t{\n" +
                        "        \"protocol_parameter_id\": 1,\n" +
                        "        \"protocol_id\" : 1,\n" +
                        "        \"value\" : \"logicasmpp3\"\n" +
                        "},\n" +
                        "{\n" +
                        "        \"protocol_parameter_id\": 2,\n" +
                        "        \"protocol_id\" : 1,\n" +
                        "        \"value\" : \"reservesms2\"\n" +
                        "}\n" +
                        "],\n" +
                        "\"status\" : 1\n" +
                        "}")
                .when()
                .post("http://192.168.128.215/acapi/service_providers")
                .then()
                .log().everything()
                .statusCode(200)
                .body("status", is("Success"));

//        ArrayList<String> providerId;
//        providerId = given()
//                .contentType("application/json;charset=UTF-8")
//                .header("Authorization", tokenAdmin)
//                .param("name", clientName)
//                .when()
//                .get("http://192.168.128.215/acapi/service_providers/")
//                .then()
//                .extract().path("partners.id");
//
//        partnerIdStr = providerId.toString();
//        partnerIdStr = partnerIdStr.substring(1, partnerIdStr.length() - 1);
//        System.out.println(partnerIdStr);

        return providerIdStr;

    }

    public void createSMSTariffZoneGroupsPartner(String partner_id) {
        given()
                .contentType("application/json;charset=UTF-8")
                .header("Authorization", tokenAdmin)
                .body("{\n" +
                        "\"name\" : \"TEST5\",\n" +
                        "\"msisdn\" :\"7\",\n" +
                        "\"mcc\" : \"250\",\n" +
                        "\"mnc\" : \"01\",\n" +
                        "\"price\" : \"01.23\",\n" +
                        "\"status\" : 1\n" +
                        "} ")
                .when()
                .post("http://192.168.128.215/acapi/sms/service_providers/"+partner_id+"/tariff_zones")
                .then()
                .log().everything()
                .statusCode(200);
    }
}

