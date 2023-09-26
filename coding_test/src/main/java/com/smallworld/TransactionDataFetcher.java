package com.smallworld;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.smallworld.data.Transaction;

public class TransactionDataFetcher {
	
	private List<Transaction> transactions;
	
	public TransactionDataFetcher(List<Transaction> transactions) {
		this.transactions = transactions;
	}

    /**
     * Returns the sum of the amounts of all transactions
     */
    public double getTotalTransactionAmount() {
    	return transactions.stream()
		    		.mapToDouble(Transaction::getAmount)
		    		.sum();
    }

    /**
     * Returns the sum of the amounts of all transactions sent by the specified client
     */
    public double getTotalTransactionAmountSentBy(String senderFullName) {
    	return	transactions.stream()
		    		.filter(transaction -> senderFullName.equals(transaction.getSenderFullName()))
		    		.mapToDouble(Transaction::getAmount)
		    		.sum();
    }

    /**
     * Returns the highest transaction amount
     */
    public double getMaxTransactionAmount() {
        return transactions.stream()
        			.mapToDouble(Transaction::getAmount)
        			.max()
        			.orElse(0.0);
    }

    /**
     * Counts the number of unique clients that sent or received a transaction
     */
    public long countUniqueClients() {
        return transactions.stream()
        			.flatMap(transaction -> List.of(transaction.getSenderFullName(), transaction.getBeneficiaryFullName()).stream())
        			.distinct()
        			.count();
    }

    /**
     * Returns whether a client (sender or beneficiary) has at least one transaction with a compliance
     * issue that has not been solved
     */
    public boolean hasOpenComplianceIssues(String clientFullName) {
        return transactions.stream()
        			.anyMatch( transaction -> 
        							(clientFullName.equals(transaction.getSenderFullName()) ||
        									clientFullName.equals(transaction.getBeneficiaryFullName())) &&
        							!transaction.isIssueSolved()
        					);
    }

    /**
     * Returns all transactions indexed by beneficiary name
     */
    public Map<String, List<Transaction>> getTransactionsByBeneficiaryName() {
        return transactions.stream()
        		.collect(Collectors.groupingBy(Transaction::getBeneficiaryFullName));
    }

    /**
     * Returns the identifiers of all open compliance issues
     */
    public Set<Integer> getUnsolvedIssueIds() {
        return transactions.stream()
        			.filter(transation -> transation.getIssueId() != null && !transation.isIssueSolved())
        			.map(Transaction::getIssueId)
        			.collect(Collectors.toSet());
    }

    /**
     * Returns a list of all solved issue messages
     */
    public List<String> getAllSolvedIssueMessages() {
        return transactions.stream()
        			.filter(transaction -> transaction.isIssueSolved() && transaction.getIssueMessage() != null)
        			.map(Transaction::getIssueMessage)
        			.collect(Collectors.toList());
    }

    /**
     * Returns the 3 transactions with the highest amount sorted by amount descending
     */
    public List<Transaction> getTop3TransactionsByAmount() {
        return transactions.stream()
        			.sorted((t1,t2) -> Double.compare(t2.getAmount(), t1.getAmount()))
        			.limit(3)
        			.collect(Collectors.toList());
    }

    /**
     * Returns the senderFullName of the sender with the most total sent amount
     */
    public Optional<String> getTopSender() {
        return Optional.of(transactions.stream()
        			.collect(Collectors.groupingBy(Transaction::getSenderFullName, Collectors.summingDouble(Transaction::getAmount)))
        			.entrySet()
        			.stream()
        			.max((entry1, entry2) -> Double.compare(entry1.getValue(), entry2.getValue()))
        			.map(Map.Entry::getKey)
        			.orElse(null));
    }

}
