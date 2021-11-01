package com.example.rest.dotpay;

import com.example.rest.dotpay.model.PaymentEntry;
import io.smallrye.mutiny.Uni;
import lombok.val;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.Collections;
import java.util.List;

@Path("/s2/login/api/purchase_history/")
public class MockResource {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<List<PaymentEntry>> api(@QueryParam("email") String email) {
        return Uni.createFrom().item(Collections.singletonList(createPaymentEntry()));
    }

    private PaymentEntry createPaymentEntry() {
        val entry = new PaymentEntry();
        entry.setDate("2018-06-09T16:54:15.855823");
        entry.setAmount("8.29");
        entry.setCurrency("PLN");
        entry.setStatus("completed");
        entry.setRequestIp("188.146.162.128");
        entry.setMcc("4112");
        entry.setChannel("73");
        entry.setMerchantId("5d51e2dc7bc76cf9841d82ba404d134646bdbd97");

        return entry;
    }
}
