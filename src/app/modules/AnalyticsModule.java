package app.modules;

import java.util.TreeMap;
import java.util.PriorityQueue;
import java.util.Map;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;
import java.math.BigDecimal;

/**
	* Provides financial analytics and reporting functionality
	* TODO: Implement the complete AnalyticsModule class
	*/
public class AnalyticsModule {
	// Stores monthly spending data
	private TreeMap<String, Double> monthlySpending;

	// Priority queue to track top spending categories
	private PriorityQueue<CategorySpending> topCategories;

	/**
		* Constructor for AnalyticsModule
		* Initializes the monthlySpending map and topCategories queue.
		*/
	public AnalyticsModule() {
		monthlySpending = new TreeMap<>();
		topCategories = new PriorityQueue<>(Comparator.comparingDouble(cs -> -cs.amount));
	}

	// ------------------------------------------------------------------------

	/**
		* Calculate monthly burn rate.
		* Sums all expenditures and divides by the number of distinct months.
		* Returns 0.0 if the list is empty or null.
		*
		* @param expenditures list of expenditures
		* @return monthly burn rate
		*/
	public double calculateMonthlyBurn(List<Expenditure> expenditures) {
		if (expenditures == null || expenditures.isEmpty()) return 0.0;
		double total = 0.0;
		for (Expenditure e : expenditures) {
			total += e.getAmount().doubleValue();
		}
		return total / getDistinctMonths(expenditures);
	}

	// ------------------------------------------------------------------------

	/**
		* Generate cost analysis report.
		* Calculates total expenditure and breakdown by category.
		* Returns a formatted string with totals.
		*
		* @param expenditures list of expenditures
		* @return formatted cost analysis string
		*/
	public String generateCostAnalysis(List<Expenditure> expenditures) {
		if (expenditures == null || expenditures.isEmpty()) return "No expenditures to analyze.";
		double total = 0.0;
		TreeMap<String, Double> categoryTotals = new TreeMap<String, Double>();
		for (Expenditure e : expenditures) {
			total += e.getAmount().doubleValue();
			String categoryName = e.getCategory().getName();
			Double prev = categoryTotals.get(categoryName);
			if (prev == null) prev = 0.0;
			categoryTotals.put(categoryName, prev + e.getAmount().doubleValue());
		}
		StringBuilder sb = new StringBuilder();
		sb.append("Total Expenditure: ").append(String.format("%.2f", total)).append("\n");
		sb.append("By Category:\n");
		for (Map.Entry<String, Double> entry : categoryTotals.entrySet()) {
			sb.append("  ").append(entry.getKey()).append(": ").append(String.format("%.2f", entry.getValue())).append("\n");
		}
		return sb.toString();
	}

	// ------------------------------------------------------------------------

	/**
		* Get affordability insights.
		* Compares monthly expenses to income and returns a message about savings.
		*
		* @param monthlyIncome user's monthly income
		* @param monthlyExpenses current monthly expenses
		* @return affordability insights string
		*/
	public String getAffordabilityInsights(double monthlyIncome, double monthlyExpenses) {
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

	// ------------------------------------------------------------------------

	/**
		* Get spending trends over time.
		* Aggregates expenditures by month and returns a formatted trend report.
		*
		* @param expenditures list of expenditures
		* @return trend analysis string
		*/
	public String getSpendingTrends(List<Expenditure> expenditures) {
		if (expenditures == null || expenditures.isEmpty()) return "No data for trends.";
		TreeMap<String, Double> monthTotals = new TreeMap<>();
		for (Expenditure e : expenditures) {
			String month = e.getMonth(); // Assumes Expenditure has getMonth() returning "YYYY-MM"
			Double prev = monthTotals.get(month);
			if (prev == null) prev = 0.0;
			monthTotals.put(month, prev + e.getAmount().doubleValue());
		}
		StringBuilder sb = new StringBuilder();
		sb.append("Monthly Spending Trends:\n");
		for (Map.Entry<String, Double> entry : monthTotals.entrySet()) {
			sb.append("  ").append(entry.getKey()).append(": ").append(String.format("%.2f", entry.getValue())).append("\n");
		}
		return sb.toString();
	}

	// ------------------------------------------------------------------------

	/**
		* Get top spending categories.
		* Ranks categories by total expenditure and returns the top N category names.
		*
		* @param expenditures list of expenditures
		* @param topN number of top categories to return
		* @return list of top categories
		*/
	public List<String> getTopSpendingCategories(List<Expenditure> expenditures, int topN) {
		if (expenditures == null || expenditures.isEmpty() || topN <= 0) return new ArrayList<>();
		TreeMap<String, Double> categoryTotals = new TreeMap<>();
		for (Expenditure e : expenditures) {
			String catName = e.getCategory().getName();
			Double prev = categoryTotals.get(catName);
			if (prev == null) prev = 0.0;
			categoryTotals.put(catName, prev + e.getAmount().doubleValue());
		}
		List<Map.Entry<String, Double>> sorted = new ArrayList<>(categoryTotals.entrySet());
		sorted.sort((a, b) -> Double.compare(b.getValue(), a.getValue()));
		List<String> result = new ArrayList<>();
		for (int i = 0; i < Math.min(topN, sorted.size()); i++) {
			result.add(sorted.get(i).getKey());
		}
		return result;
	}

	// ------------------------------------------------------------------------

	/**
		* Generate affordability insights based on expenditures and available balance.
		* Uses monthly burn rate and total balance to estimate affordability.
		*
		* @param expenditures list of expenditures
		* @param totalBalance total available balance
		* @return affordability insights string
		*/
	public String generateAffordabilityInsights(List<Expenditure> expenditures, BigDecimal totalBalance) {
		if (expenditures == null || expenditures.isEmpty()) {
			return "No expenditures data available for affordability analysis.";
		}

		double monthlyExpenses = calculateMonthlyBurn(expenditures);
		double monthlyIncome = totalBalance.doubleValue(); // Simplified assumption

		return getAffordabilityInsights(monthlyIncome, monthlyExpenses);
	}

	// ------------------------------------------------------------------------

	/**
		* Generate category breakdown report.
		* Delegates to generateCostAnalysis for category breakdown.
		*
		* @param expenditures list of expenditures
		* @return category breakdown string
		*/
	public String generateCategoryBreakdown(List<Expenditure> expenditures) {
		return generateCostAnalysis(expenditures);
	}

	// ------------------------------------------------------------------------

	/**
		* Generate spending trends report.
		* Delegates to getSpendingTrends for trend analysis.
		*
		* @param expenditures list of expenditures
		* @return spending trends string
		*/
	public String generateSpendingTrends(List<Expenditure> expenditures) {
		return getSpendingTrends(expenditures);
	}

	// ------------------------------------------------------------------------

	/**
		* Helper method to count distinct months in expenditures.
		* Uses Expenditure.getMonth() to determine unique months.
		*
		* @param expenditures list of expenditures
		* @return number of distinct months
		*/
	private int getDistinctMonths(List<Expenditure> expenditures) {
		if (expenditures == null || expenditures.isEmpty()) return 1;
		// Assumes Expenditure has getMonth() returning "YYYY-MM"
		return (int) expenditures.stream().map(Expenditure::getMonth).distinct().count();
	}

	// ------------------------------------------------------------------------

	/**
		* Helper class for category spending.
		* Used for ranking categories by spending.
		*/
	private static class CategorySpending {
		String category;
		double amount;

		CategorySpending(String category, double amount) {
			this.category = category;
			this.amount = amount;
		}
	}

}