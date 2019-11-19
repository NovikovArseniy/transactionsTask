package com.transactions;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import com.transactions.model.Account;
import com.transactions.model.Transaction;
import com.transactions.model.Order;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.annotation.DirtiesContext;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import org.junit.runner.RunWith;
import org.junit.Test;
import org.junit.Assert;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AccountControllerTests {

    @Autowired
    private MockMvc mockMvc;

    private final double DELTA_DOUBLE = 0.000000001;

    private MvcResult sendGetRequest(String url, Map<String, String> params) throws Exception {
        MockHttpServletRequestBuilder getRequest = get(url);
        for (Map.Entry<String, String> entry : params.entrySet())
            getRequest.param(entry.getKey(), entry.getValue());

        return this.mockMvc.perform(getRequest).andExpect(status().isOk()).andReturn();
    }

    private MvcResult sendGetRequest(String url) throws Exception {
        MockHttpServletRequestBuilder getRequest = get(url);
        return this.mockMvc.perform(getRequest).andExpect(status().isOk()).andReturn();
    }

    private MvcResult sendPostRequest(String url, String body) throws Exception {
        MockHttpServletRequestBuilder postRequest = post(url);
        postRequest.contentType(MediaType.APPLICATION_JSON);
        postRequest.content(body);

        return this.mockMvc.perform(postRequest).andExpect(status().isOk()).andReturn();
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void createSingleAccount() throws Exception {

        Account account = new Account("simple_id", 10);
        ObjectMapper objectMapper = new ObjectMapper();
        String accountJson = objectMapper.writeValueAsString(account);

        sendPostRequest("/account/create", accountJson);

        Map<String, String> params = new HashMap<>();
        params.put("id", "simple_id");

        MvcResult result = sendGetRequest("/account/get", params);
        Assert.assertEquals(accountJson, result.getResponse().getContentAsString());
    }
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void createAccountWithNegativeAmount() throws Exception {

        Account account = new Account("simple_id", -10);
        ObjectMapper objectMapper = new ObjectMapper();
        String accountJson = objectMapper.writeValueAsString(account);

        sendPostRequest("/account/create", accountJson);

        Map<String, String> params = new HashMap<>();
        params.put("id", "simple_id");

        MvcResult result = sendGetRequest("/account/get", params);
        account = new Account();
        accountJson = objectMapper.writeValueAsString(account);
        Assert.assertEquals(accountJson, result.getResponse().getContentAsString());
    }
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void createManyAccounts() throws Exception {

        Random random = new Random();
        List<Account> accounts = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            Account account = new Account(
                    UUID.randomUUID().toString(),
                    random.nextDouble() * 100
            );

            accounts.add(account);
        }

        ObjectMapper objectMapper = new ObjectMapper();
        for (Account account : accounts) {
            String accountJson = objectMapper.writeValueAsString(account);
            sendPostRequest("/account/create", accountJson);
        }

        MvcResult result = sendGetRequest("/account/all");
        String jsonResult = result.getResponse().getContentAsString();

        List<Account> resultAccounts = objectMapper.readValue(jsonResult, new TypeReference<List<Account>>() {});
        Assert.assertTrue(accounts.containsAll(resultAccounts));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void createSingleTransaction() throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();

        Account fromAccount = new Account("from_account_id", 10);
        String fromAccountJson = objectMapper.writeValueAsString(fromAccount);
        sendPostRequest("/account/create", fromAccountJson);

        Account toAccount = new Account("to_account_id", 10);
        String toAccountJson = objectMapper.writeValueAsString(toAccount);
        sendPostRequest("/account/create", toAccountJson);

        Order order = new Order();
        order.setFromAccount(fromAccount.getId());
        order.setToAccount(toAccount.getId());
        order.setAmount(5);

        String transactionJson = objectMapper.writeValueAsString(order);
        MvcResult result = sendPostRequest("/order/transfer", transactionJson);

        String resultJson = result.getResponse().getContentAsString();
        Transaction resultTransaction = objectMapper.readValue(resultJson, Transaction.class);

        Assert.assertEquals(order.getFromAccount(), resultTransaction.getFromAccount());
        Assert.assertEquals(order.getToAccount(), resultTransaction.getToAccount());
        Assert.assertEquals(order.getAmount(), resultTransaction.getAmount(), DELTA_DOUBLE);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void createNegativeTransaction() throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();

        Account fromAccount = new Account("from_account_id", 10);
        String fromAccountJson = objectMapper.writeValueAsString(fromAccount);
        sendPostRequest("/account/create", fromAccountJson);

        Account toAccount = new Account("to_account_id", 10);
        String toAccountJson = objectMapper.writeValueAsString(toAccount);
        sendPostRequest("/account/create", toAccountJson);

        Order order = new Order();
        order.setFromAccount(fromAccount.getId());
        order.setToAccount(toAccount.getId());
        order.setAmount(-5);

        String transactionJson = objectMapper.writeValueAsString(order);
        MvcResult result = sendPostRequest("/order/transfer", transactionJson);

        String resultJson = result.getResponse().getContentAsString();
        Transaction resultTransaction = objectMapper.readValue(resultJson, Transaction.class);

        Transaction transaction = new Transaction();

        Assert.assertEquals(transaction.getFromAccount(), resultTransaction.getFromAccount());
        Assert.assertEquals(transaction.getToAccount(), resultTransaction.getToAccount());
        Assert.assertEquals(transaction.getAmount(), resultTransaction.getAmount(), DELTA_DOUBLE);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void createInvalidTransaction() throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();

        Account fromAccount = new Account("from_account_id", 10);
        String fromAccountJson = objectMapper.writeValueAsString(fromAccount);
        sendPostRequest("/account/create", fromAccountJson);

        Account toAccount = new Account("to_account_id", 10);
        String toAccountJson = objectMapper.writeValueAsString(toAccount);
        sendPostRequest("/account/create", toAccountJson);

        Order order = new Order();
        order.setFromAccount(fromAccount.getId());
        order.setToAccount(toAccount.getId());
        order.setAmount(50);

        String transactionJson = objectMapper.writeValueAsString(order);
        MvcResult result = sendPostRequest("/order/transfer", transactionJson);

        String resultJson = result.getResponse().getContentAsString();
        Transaction resultTransaction = objectMapper.readValue(resultJson, Transaction.class);

        Transaction transaction = new Transaction();

        Assert.assertEquals(transaction.getFromAccount(), resultTransaction.getFromAccount());
        Assert.assertEquals(transaction.getToAccount(), resultTransaction.getToAccount());
        Assert.assertEquals(transaction.getAmount(), resultTransaction.getAmount(), DELTA_DOUBLE);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void createManyTransactions() throws Exception {

        List<Account> accounts = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            Account account = new Account(UUID.randomUUID().toString(), 1000000);
            accounts.add(account);
        }

        ObjectMapper objectMapper = new ObjectMapper();
        for (Account account : accounts) {
            String accountJson = objectMapper.writeValueAsString(account);
            sendPostRequest("/account/create", accountJson);
        }

        double sumAmounts = 0;
        for (Account account : accounts) {
            sumAmounts += account.getAmount();
        }

        Runnable runnable = () -> {

            Random random = new Random();
            String fromAccount = accounts.get(random.nextInt(10)).getId();
            String toAccount = accounts.get(random.nextInt(10)).getId();

            Order order = new Order();
            order.setFromAccount(fromAccount);
            order.setToAccount(toAccount);
            order.setAmount(5);

            try {
                ObjectMapper objectMapperTask = new ObjectMapper();
                String transactionJson = objectMapperTask.writeValueAsString(order);
                sendPostRequest("/order/transfer", transactionJson);
            }
            catch (Exception ex) {
                System.err.println("Send post request failed");
            }
        };

        ExecutorService executor = Executors.newFixedThreadPool(8);

        List<Future> futures = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            Future future = executor.submit(runnable);
            futures.add(future);
        }

        for (Future future : futures)
            future.get();

        MvcResult resultAccount = sendGetRequest("/account/all");
        String jsonResultAccount = resultAccount.getResponse().getContentAsString();

        List<Account> resultAccounts = objectMapper.readValue(jsonResultAccount, new TypeReference<List<Account>>() {});
        double resultSumAmount = 0;
        for (Account account : resultAccounts)
            resultSumAmount += account.getAmount();

        Assert.assertEquals(sumAmounts, resultSumAmount, DELTA_DOUBLE);

        MvcResult resultTransactions = sendGetRequest("/order/history");
        String jsonResultTransactions = resultTransactions.getResponse().getContentAsString();

        List<Transaction> resultListTransactions = objectMapper.readValue(
                jsonResultTransactions,
                new TypeReference<List<Transaction>>() {}
            );

        Assert.assertEquals(resultListTransactions.size(), 1000);
        for (Transaction transaction : resultListTransactions)
            Assert.assertEquals(transaction.getAmount(), 5.0, DELTA_DOUBLE);
    }
}
