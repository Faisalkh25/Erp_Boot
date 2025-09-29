package com.erp.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.erp.service.LeaveBalanceService;

@Service
public class LeaveBalanceScheduler {

    private LeaveBalanceService leaveBalanceService;

    public LeaveBalanceScheduler(LeaveBalanceService leaveBalanceService) {
        this.leaveBalanceService = leaveBalanceService;
    }

    @Scheduled(cron = "0 0 0 1 * ?")
    public void creditMonthlyLeave() {
        leaveBalanceService.creditMonthlyLeaveForAll();
    }
}
