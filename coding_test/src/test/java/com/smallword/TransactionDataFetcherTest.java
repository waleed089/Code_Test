package com.smallword;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.smallworld.TransactionDataFetcher;
import com.smallworld.data.Transaction;

public class TransactionDataFetcherTest {
	
	private TransactionDataFetcher dataFetcher;
	private List<Transaction> transactions;
	
	@BeforeEach
	public void setUp() {
		transactions = Arrays.asList(
				new Transaction(1, 100.0, "Sender1", 25, "Beneficiary1", 30, 1, false, "Issue1"), // Unsolved issue
				new Transaction(2, 200.0, "Sender2", 35, "Beneficiary2", 40, 2, true, "Issue2"),  // Solved issue
				new Transaction(3, 300.0, "Sender3", 45, "Beneficiary3", 50, null, false, null),  // No issue
                new Transaction(4, 100.0, "Sender4", 25, "Beneficiary4", 30, 4, false, "Issue3"), // Unsolved issue
                new Transaction(5, 200.0, "Sender2", 35, "Beneficiary2", 40, 2, true, "Issue4")  // Solved issue
			);
		
		dataFetcher = Mockito.spy(new TransactionDataFetcher(transactions));
	}
	
	@Test
    public void testGetTotalTransactionAmount() {
        double expectedTotalAmount = 900.0;
        double actualTotalAmount = dataFetcher.getTotalTransactionAmount();
        assertEquals(expectedTotalAmount, actualTotalAmount);
    }

    @Test
    public void testGetTotalTransactionAmountSentBy() {
        double expectedTotalAmount = 100.0; // Sender1's total amount
        double actualTotalAmount = dataFetcher.getTotalTransactionAmountSentBy("Sender1");
        assertEquals(expectedTotalAmount, actualTotalAmount);
    }

    @Test
    public void testGetMaxTransactionAmount() {
    	double expectedMaxAmount = 300.0; // Maximum amount among transactions
        double actualMaxAmount = dataFetcher.getMaxTransactionAmount();
        assertEquals(expectedMaxAmount, actualMaxAmount);
    }

    @Test
    public void testCountUniqueClients() {
        long expectedUniqueClients = 8; // 4 senders and 4 beneficiaries
        long actualUniqueClients = dataFetcher.countUniqueClients();
        assertEquals(expectedUniqueClients, actualUniqueClients);
    }

    @Test
    public void testHasOpenComplianceIssues() {
        assertTrue(dataFetcher.hasOpenComplianceIssues("Sender1")); // Sender2 has an open issue
        assertFalse(dataFetcher.hasOpenComplianceIssues("Sender2")); // Sender1's issue is solved
    }

    @Test
    public void testGetTransactionsByBeneficiaryName() {
        Map<String, List<Transaction>> transactionsByBeneficiary = dataFetcher.getTransactionsByBeneficiaryName();

        assertEquals(4, transactionsByBeneficiary.size());
        assertTrue(transactionsByBeneficiary.containsKey("Beneficiary1"));
        assertTrue(transactionsByBeneficiary.containsKey("Beneficiary2"));
        assertTrue(transactionsByBeneficiary.containsKey("Beneficiary3"));
        assertTrue(transactionsByBeneficiary.containsKey("Beneficiary4"));
        
        List<Transaction> transactionsForBeneficiary1 = transactionsByBeneficiary.get("Beneficiary2");
        assertEquals(2, transactionsForBeneficiary1.size());
        assertEquals(transactions.get(1), transactionsForBeneficiary1.get(0));
        
    }

    @Test
    public void testGetUnsolvedIssueIds() {
        assertEquals(2, dataFetcher.getUnsolvedIssueIds().size()); // Two transactions have unsolved issues
    }

    @Test
    public void testGetAllSolvedIssueMessages() {
        List<String> expectedMessages = Arrays.asList("Issue2", "Issue4"); // Solved issues with messages
        List<String> actualMessages = dataFetcher.getAllSolvedIssueMessages();
        assertEquals(expectedMessages, actualMessages);
    }

    @Test
    public void testGetTop3TransactionsByAmount() {
        List<Transaction> top3Transactions = dataFetcher.getTop3TransactionsByAmount();
        assertEquals(3, top3Transactions.size());
        assertEquals(300.0, top3Transactions.get(0).getAmount());
        assertEquals(200.0, top3Transactions.get(1).getAmount());
        assertEquals(200.0, top3Transactions.get(2).getAmount());
    }

    @Test
    public void testGetTopSender() {
    	Optional<String> optional = dataFetcher.getTopSender();
        assertEquals("Sender2", optional.get()); // Sender2 has the highest total amount
    }

}

