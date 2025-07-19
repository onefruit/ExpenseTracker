package com.prabinsoft.expense.service;

import com.prabinsoft.expense.entity.Profile;
import com.prabinsoft.expense.repo.ProfileRepository;
import com.prabinsoft.expense.service.expense.ExpenseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final ProfileRepository profileRepository;
    private final EmailService emailService;
    private final ExpenseService expenseService;

    @Scheduled(cron = "0 0 22 * * *", zone = "IST")
    public void sendDailyIncomeExpenseReminder() {
        log.info("Job started: sendDailyIncomeExpenseReminder()");

        List<Profile> profile = profileRepository.findAll();
        for (Profile p : profile) {
            String body = "Hi " + p.getFullName();
            emailService.sendMail(p.getEmail(), "Daily reminders", body);
        }
        log.info("Job FInished: sendDailyIncomeExpenseReminder()");

    }

}
