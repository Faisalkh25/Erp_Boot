package com.erp.util;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class LeaveCalculator {

    public static double calculateLeaveQuantity(LocalDate fromDate, LocalDate toDate, int fromSession, int toSession) {
        if (fromDate == null || toDate == null)
            return 0.0;

        long daysBetween = ChronoUnit.DAYS.between(fromDate, toDate);

        // same day leave
        if (daysBetween == 0) {
            return (toSession - fromSession + 1) * 0.25;
        }

        double total = 0.0;

        total += (4 - fromSession + 1) * 0.25;

        // Last day (1 → toSession)
        total += (toSession) * 0.25;

        // Full middle days
        if (daysBetween > 1) {
            total += (daysBetween - 1);
        }

        return total;
    }

}
