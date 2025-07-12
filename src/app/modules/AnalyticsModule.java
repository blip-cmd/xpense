package app.modules;

import java.util.TreeMap;
import java.util.Map;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.List;
import java.util.ArrayList;

/**
 * Provides financial analytics and reporting functionality
 * TODO: Implement the complete AnalyticsModule class
 */
public class AnalyticsModule {
	// TODO: Add fields for analytics
	private TreeMap<String, Double> monthlySpending;
	private PriorityQueue<CategorySpending> topCategories;

	/**
		* Constructor for AnalyticsModule
		* TODO: Implement constructor with proper initialization
		*/
	public AnalyticsModule() {
		// TODO: Initialize analytics module
		monthlySpending = new TreeMap<>();
		topCategories = new PriorityQueue<>(Comparator.comparingDouble(cs -> -cs.amount));
	}

	/**
		* Calculate monthly burn rate
		* TODO: Implement monthly burn rate calculation
		* @param expenditures list of expenditures
		* @return monthly burn rate
		*/
	public double calculateMonthlyBurn(List<Expenditure> expenditures) {
		// TODO: Implement monthly burn calculation
		if (expenditures == null || expenditures.isEmpty()) return 0.0;
		double total = 0.0;
		for (Expenditure e : expenditures) {
			total += e.getAmount().doubleValue();
		}
		return total / getDistinctMonths(expenditures);
	}

	/**
		* Generate cost analysis report
		* TODO: Implement cost analysis
		* @param expenditures list of expenditures
		* @return formatted cost analysis string
		*/
	public String generateCostAnalysis(List<Expenditure> expenditures) {
		// TODO: Implement cost analysis
		if (expenditures == null || expenditures.isEmpty()) return "No expenditures to analyze.";
		double total = 0.0;
		Map<String, Double> categoryTotals = new TreeMap<>();
		for (Expenditure e : expenditures) {
			total += e.getAmount().doubleValue();
			categoryTotals.put(e.getCategory(), categoryTotals.getOrDefault(e.getCategory(), 0.0) + e.getAmount());
		}
		StringBuilder sb = new StringBuilder();
		sb.append("Total Expenditure: ").append(String.format("%.2f", total)).append("\n");
		sb.append("By Category:\n");
		for (Map.Entry<String, Double> entry : categoryTotals.entrySet()) {
			sb.append("  ").append(entry.getKey()).append(": ").append(String.format("%.2f", entry.getValue())).append("\n");
		}
		return sb.toString();
	}

	/**
		* Get affordability insights
		* TODO: Implement affordability analysis
		* @param monthlyIncome user's monthly income
		* @param monthlyExpenses current monthly expenses
		* @return affordability insights string
		*/
	public String getAffordabilityInsights(double monthlyIncome, double monthlyExpenses) {
		// TODO: Implement affordability insights
		if (monthlyIncome <= 0) return "Invalid income.";
		double ratio = monthlyExpenses / monthlyIncome;
		if (ratio < 0.5) {
			return "You are saving a healthy portion of your income.";
		} else if (ratio < 0.8) {
			return "Your expenses are moderate. Consider increasing your savings.";
		} else {
			return "Warning: Your expenses are high compared to your income!";
		}
	}

	/**
		* Get spending trends over time
		* TODO: Implement trend analysis
		* @param expenditures list of expenditures
		* @return trend analysis string
		*/
	public String getSpendingTrends(List<Expenditure> expenditures) {
		// TODO: Implement trend analysis
		if (expenditures == null || expenditures.isEmpty()) return "No data for trends.";
		Map<String, Double> monthTotals = new TreeMap<>();
		for (Expenditure e : expenditures) {
			String month = e.getMonth(); // Assumes Expenditure has getMonth() returning "YYYY-MM"
			monthTotals.put(month, monthTotals.getOrDefault(month, 0.0) + e.getAmount());
		}
		StringBuilder sb = new StringBuilder();
		sb.append("Monthly Spending Trends:\n");
		for (Map.Entry<String, Double> entry : monthTotals.entrySet()) {
			sb.append("  ").append(entry.getKey()).append(": ").append(String.format("%.2f", entry.getValue())).append("\n");
		}
		return sb.toString();
	}

	/**
		* Get top spending categories
		* TODO: Implement category ranking
		* @param expenditures list of expenditures
		* @param topN number of top categories to return
		* @return list of top categories
		*/
	public List<String> getTopSpendingCategories(List<Expenditure> expenditures, int topN) {
		// TODO: Implement category ranking
		if (expenditures == null || expenditures.isEmpty() || topN <= 0) return new ArrayList<>();
		Map<String, Double> categoryTotals = new TreeMap<>();
		for (Expenditure e : expenditures) {
			categoryTotals.put(e.getCategory(), categoryTotals.getOrDefault(e.getCategory(), 0.0) + e.getAmount());
		}
		List<Map.Entry<String, Double>> sorted = new ArrayList<>(categoryTotals.entrySet());
		sorted.sort((a, b) -> Double.compare(b.getValue(), a.getValue()));
		List<String> result = new ArrayList<>();
		for (int i = 0; i < Math.min(topN, sorted.size()); i++) {
			result.add(sorted.get(i).getKey());
		}
		return result;
	}

	// Helper method to count distinct months in expenditures
	private int getDistinctMonths(List<Expenditure> expenditures) {
		if (expenditures == null || expenditures.isEmpty()) return 1;
		// Assumes Expenditure has getMonth() returning "YYYY-MM"
		return (int) expenditures.stream().map(Expenditure::getMonth).distinct().count();
	}

	// Helper class for category spending
	private static class CategorySpending {
		String category;
		double amount;

		CategorySpending(String category, double amount) {
			this.category = category;
			this.amount = amount;
		}
	}

	// TODO: Add more methods as needed

}