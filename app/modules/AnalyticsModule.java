package app.modules;

import app.util.*;
import java.math.BigDecimal;

public class AnalyticsModule {
    public BigDecimal calculateMonthlyBurn(SimpleArrayList<Expenditure> expenditures) {
        if (expenditures == null || expenditures.size() == 0) return BigDecimal.ZERO;
        BigDecimal total = BigDecimal.ZERO;
        SimpleSet<String> months = new SimpleSet<>();
        for (int i = 0; i < expenditures.size(); i++) {
            Expenditure e = expenditures.get(i);
            total = total.add(e.getAmount());
            months.add(e.getDateTime().getYear() + "-" + e.getDateTime().getMonthValue());
        }
        int monthCount = months.size() == 0 ? 1 : months.size();
        return total.divide(BigDecimal.valueOf(monthCount), 2, BigDecimal.ROUND_HALF_UP);
    }

    public String generateCostAnalysis(SimpleArrayList<Expenditure> expenditures) {
        if (expenditures == null || expenditures.size() == 0) return "No expenditures to analyze.";
        BigDecimal total = BigDecimal.ZERO;
        SimpleMap<String, BigDecimal> categoryTotals = new SimpleMap<>();
        for (int i = 0; i < expenditures.size(); i++) {
            Expenditure e = expenditures.get(i);
            total = total.add(e.getAmount());
            String cat = e.getCategory().getName();
            BigDecimal prev = categoryTotals.get(cat);
            categoryTotals.put(cat, prev == null ? e.getAmount() : prev.add(e.getAmount()));
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Total Expenditure: ").append(total).append("\nBy Category:\n");
        for (int i = 0; i < categoryTotals.size(); i++) {
            sb.append("  ").append(categoryTotals.getKeyAt(i)).append(": ").append(categoryTotals.getAt(i)).append("\n");
        }
        return sb.toString();
    }
}