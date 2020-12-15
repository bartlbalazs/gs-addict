package hu.bartl.gsAddict.service;

import com.google.common.collect.Sets;
import hu.bartl.gsAddict.model.Ad;
import hu.bartl.gsAddict.repository.AdRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@AllArgsConstructor
@Slf4j
public class StorageService {

    private final AdRepository adRepository;

    public Set<Ad> getNewAds(Set<Ad> newestAds) {
        Set<Ad> result = Sets.newHashSet(newestAds);
        result.removeAll(Sets.newHashSet(adRepository.findAll()));

        log.debug("{} new ads found.", result.size());
        return result;
    }

    public void storeAds(Set<Ad> ads) {
        if (!ads.isEmpty()) {
            log.debug("Storing ads: {}", ads);
            adRepository.saveAll(ads);
        }
    }
}
