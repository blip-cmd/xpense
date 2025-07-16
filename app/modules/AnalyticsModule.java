package app.modules;

import app.util.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

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
        return total.divide(BigDecimal.valueOf(monthCount), 2, RoundingMode.HALF_UP);
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
        sb.append("Total Expenditure: GHc ").append(total).append("\nBy Category:\n");
        for (int i = 0; i < categoryTotals.size(); i++) {
            sb.append("  ").append(categoryTotals.getKeyAt(i)).append(": GHc ").append(categoryTotals.getAt(i)).append("\n");
        }
        return sb.toString();
    }
    
    // Enhanced financial analysis methods
    public BigDecimal calculateWeeklyBurn(SimpleArrayList<Expenditure> expenditures) {
        if (expenditures == null || expenditures.size() == 0) return BigDecimal.ZERO;
        BigDecimal total = BigDecimal.ZERO;
        SimpleSet<String> weeks = new SimpleSet<>();
        
        for (int i = 0; i < expenditures.size(); i++) {
            Expenditure e = expenditures.get(i);
            total = total.add(e.getAmount());
            LocalDate date = e.getDateTime().toLocalDate();
            int weekOfYear = date.getDayOfYear() / 7;
            weeks.add(date.getYear() + "-W" + weekOfYear);
        }
        
        int weekCount = weeks.size() == 0 ? 1 : weeks.size();
        return total.divide(BigDecimal.valueOf(weekCount), 2, RoundingMode.HALF_UP);
    }
    
    public String generateProfitabilityForecast(SimpleArrayList<Expenditure> expenditures, 
                                               BigDecimal projectedRevenue, int forecastMonths) {
        BigDecimal monthlyBurn = calculateMonthlyBurn(expenditures);
        BigDecimal totalProjectedCosts = monthlyBurn.multiply(BigDecimal.valueOf(forecastMonths));
        BigDecimal projectedProfit = projectedRevenue.subtract(totalProjectedCosts);
        
        StringBuilder sb = new StringBuilder();
        sb.append("=== PROFITABILITY FORECAST (").append(forecastMonths).append(" months) ===\n");
        sb.append("Monthly Burn Rate: GHc ").append(monthlyBurn).append("\n");
        sb.append("Projected Total Costs: GHc ").append(totalProjectedCosts).append("\n");
        sb.append("Projected Revenue: GHc ").append(projectedRevenue).append("\n");
        sb.append("Projected Profit: GHc ").append(projectedProfit).append("\n");
        
        if (projectedProfit.compareTo(BigDecimal.ZERO) > 0) {
            sb.append("Status: PROFITABLE\n");
        } else {
            sb.append("Status: LOSS EXPECTED\n");
        }
        
        return sb.toString();
    }
    
    public String analyzeBuildingMaterialCosts(SimpleArrayList<Expenditure> expenditures, 
                                             BigDecimal targetHousePrice) {
        BigDecimal materialCosts = BigDecimal.ZERO;
        int materialCount = 0;
        
        // Calculate costs for construction-related categories
        for (int i = 0; i < expenditures.size(); i++) {
            Expenditure e = expenditures.get(i);
            String category = e.getCategory().getName().toLowerCase();
            String phase = e.getPhase().toLowerCase();
            
            if (phase.contains("construction") || 
                category.contains("cement") || category.contains("steel") || 
                category.contains("brick") || category.contains("material") ||
                category.contains("concrete") || category.contains("lumber")) {
                materialCosts = materialCosts.add(e.getAmount());
                materialCount++;
            }
        }
        
        BigDecimal materialPercentage = BigDecimal.ZERO;
        if (targetHousePrice.compareTo(BigDecimal.ZERO) > 0) {
            materialPercentage = materialCosts.divide(targetHousePrice, 4, RoundingMode.HALF_UP)
                                            .multiply(BigDecimal.valueOf(100));
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("=== BUILDING MATERIAL COST ANALYSIS ===\n");
        sb.append("Total Material Costs: GHc ").append(materialCosts).append("\n");
        sb.append("Material Expenditures Count: ").append(materialCount).append("\n");
        sb.append("Target House Price: GHc ").append(targetHousePrice).append("\n");
        sb.append("Material Cost as % of House Price: ").append(materialPercentage).append("%\n");
        
        if (materialPercentage.compareTo(BigDecimal.valueOf(40)) > 0) {
            sb.append("WARNING: Material costs exceed 40% of house price - may affect affordability\n");
        } else if (materialPercentage.compareTo(BigDecimal.valueOf(25)) > 0) {
            sb.append("CAUTION: Material costs are 25-40% of house price - monitor closely\n");
        } else {
            sb.append("GOOD: Material costs are within acceptable range (<25%)\n");
        }
        
        return sb.toString();
    }
    
    public String generatePhaseAnalysis(SimpleArrayList<Expenditure> expenditures) {
        if (expenditures == null || expenditures.size() == 0) return "No expenditures to analyze.";
        
        SimpleMap<String, BigDecimal> phaseTotals = new SimpleMap<>();
        SimpleMap<String, Integer> phaseCounts = new SimpleMap<>();
        
        for (int i = 0; i < expenditures.size(); i++) {
            Expenditure e = expenditures.get(i);
            String phase = e.getPhase();
            
            BigDecimal prevTotal = phaseTotals.get(phase);
            Integer prevCount = phaseCounts.get(phase);
            
            phaseTotals.put(phase, prevTotal == null ? e.getAmount() : prevTotal.add(e.getAmount()));
            phaseCounts.put(phase, prevCount == null ? 1 : prevCount + 1);
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("=== EXPENDITURE BY PHASE ===\n");
        for (int i = 0; i < phaseTotals.size(); i++) {
            String phase = phaseTotals.getKeyAt(i);
            BigDecimal total = phaseTotals.getAt(i);
            Integer count = phaseCounts.get(phase);
            sb.append(phase).append(": GHc ").append(total).append(" (").append(count).append(" expenses)\n");
        }
        
        return sb.toString();
    }
}