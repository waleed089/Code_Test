package com.smallworld.dataload;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smallworld.TransactionDataFetcher;
import com.smallworld.data.Transaction;

public class DataLoader {

	public static void main(String ar[]) {
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		try {
			List<Transaction> transactions = objectMapper.readValue(
						new File("transactions.json"),
						objectMapper.getTypeFactory().constructCollectionType(List.class, Transaction.class)
					);
			
			TransactionDataFetcher transactionDataFetcher = new TransactionDataFetcher(transactions);
			
            System.out.println("Get Total Transaction Amount: " + transactionDataFetcher.getTotalTransactionAmount() +"\n");
            
            System.out.println("Total Transaction Amount Sent By Tom Shelby: " + transactionDataFetcher.getTotalTransactionAmountSentBy("Tom Shelby") +"\n");
            
            System.out.println("Get Max Transaction Amount: " + transactionDataFetcher.getMaxTransactionAmount() +"\n");
            
            System.out.println("Get Count of Unique Clients: " + transactionDataFetcher.countUniqueClients());
            
            System.out.println("Open Compliance Issues for Grace Burgess: " + transactionDataFetcher.hasOpenComplianceIssues("Grace Burgess") +"\n");
            
            // Get and print transactions by beneficiary name
           Map<String, List<Transaction>> transactionsByBeneficiary = transactionDataFetcher.getTransactionsByBeneficiaryName();
            System.out.println("Transactions by Beneficiary Name:");
            transactionsByBeneficiary.forEach((beneficiary,beneficiaryTransactions) -> {
                System.out.println("\nBeneficiary Name: " + beneficiary);
                beneficiaryTransactions.forEach(transaction -> System.out.println(transaction.toString()));
            });

            // Get and print unsolved issue IDs
            System.out.println("Get Unsolved Issue IDs: " + transactionDataFetcher.getUnsolvedIssueIds() +"\n");

            // Get and print all solved issue messages
            System.out.println("Get Solved Issue Messages: " + transactionDataFetcher.getAllSolvedIssueMessages() +"\n");
            
            List<Transaction> top3Transactions = transactionDataFetcher.getTop3TransactionsByAmount();
            top3Transactions.forEach(transaction -> System.out.println(transaction.toString()));

            // Get and print the top sender
            System.out.println("\nGet Top Sender: " + transactionDataFetcher.getTopSender());
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
