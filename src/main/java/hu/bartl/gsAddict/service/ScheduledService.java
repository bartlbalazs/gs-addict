package hu.bartl.gsAddict.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class ScheduledService {

    private final ScraperService scraperService;
    private final StorageService storageService;
    private final EmailService emailService;

    @Scheduled(cron = "0 */5 * * * *")
    public void lookForChanges() {
        log.debug("Scraper triggered");
        var newestAds = scraperService.getAds();
        var newAds = storageService.getNewAds(newestAds);
        emailService.sendNotification(newAds);
        storageService.storeAds(newAds);
    }
}
