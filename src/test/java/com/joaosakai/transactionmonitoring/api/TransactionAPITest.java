package com.joaosakai.transactionmonitoring.api;

import com.joaosakai.transactionmonitoring.model.Statistic;
import com.joaosakai.transactionmonitoring.model.Transaction;
import com.joaosakai.transactionmonitoring.service.StatisticService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@SpringBootTest
public class TransactionAPITest {

    private MockMvc mockMvc;
    private StatisticService service;

    @Before
    public void setup() {
        service = mock(StatisticService.class);
        mockMvc = standaloneSetup(new TransactionAPI(service)).build();
    }

    @Test
    public void testGetStatistics() throws Exception {
        when(service.getStatistics()).thenReturn(new Statistic());

        mockMvc.perform(get("/statistics"))
                .andExpect(status().isOk());
    }

    @Test
    public void testNoContent() throws Exception {

        when(service.addTransaction(isA(Transaction.class))).thenReturn(false);

        mockMvc.perform(post("/transactions")
                .content("{\n" +
                        "  \"amount\": 50,\n" +
                        "  \"timestamp\": 1521765204291 \n" +
                        "}").contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testAddTransaction() throws Exception {
        final long timestamp = Instant.now().toEpochMilli();

        when(service.addTransaction(isA(Transaction.class))).thenReturn(true);

        mockMvc.perform(post("/transactions")
                .content("{  \"amount\": 50,  \"timestamp\": "+timestamp+" }")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isCreated());
    }



}
