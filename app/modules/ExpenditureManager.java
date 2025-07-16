package app.modules;

import app.util.*;
import java.math.BigDecimal;

public class ExpenditureManager {
    private final SimpleArrayList<Expenditure> expenditures;

    public ExpenditureManager() {
        this.expenditures = new SimpleArrayList<>();
    }

    public boolean addExpenditure(Expenditure expenditure) {
        if (expenditure == null || !expenditure.isValid()) return false;
        for (Expenditure e : expenditures) {
            if (e.getId().equalsIgnoreCase(expenditure.getId())) return false;
        }
        expenditures.add(expenditure);
        return true;
    }

    public SimpleArrayList<Expenditure> getAllExpenditures() {
        return expenditures;
    }

    public BigDecimal getTotalAmount() {
        BigDecimal total = BigDecimal.ZERO;
        for (Expenditure exp : expenditures) {
            total = total.add(exp.getAmount());
        }
        return total;
    }
}