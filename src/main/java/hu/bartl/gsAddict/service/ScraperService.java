package hu.bartl.gsAddict.service;

import hu.bartl.gsAddict.config.ScraperConfig;
import hu.bartl.gsAddict.model.Ad;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

import static java.util.function.Predicate.not;
import static org.jsoup.Connection.Method.GET;

@Service
@AllArgsConstructor
@Slf4j
public class ScraperService {

    private final ScraperConfig config;

    @SneakyThrows
    public Set<Ad> getAds() {
        var connection = Jsoup.connect(config.getUrl()).method(GET);
        String html = new String(connection.execute().bodyAsBytes(), "utf-8");
        var newestAdsPage = Jsoup.parse(html);
        var adDivs = getAdDivs(newestAdsPage);
        Set<Ad> ads = adDivs.stream()
                .map(this::convertToAd)
                .filter(this::isInterrestingChange)
                .filter(not(this::shouldExclude))
                .collect(Collectors.toSet());

        log.debug("{} ads downloaded.", ads.size());
        return ads;
    }

    private boolean isInterrestingChange(Ad ad) {
        return config.getInclude()
                .stream().map(String::toLowerCase)
                .filter(exp -> ad.getName().toLowerCase().contains(exp))
                .findAny()
                .isPresent();
    }

    private boolean shouldExclude(Ad ad) {
        return config.getExclude()
                .stream().map(String::toLowerCase)
                .filter(exp -> ad.getName().toLowerCase().contains(exp))
                .findAny()
                .isPresent();
    }

    public Elements getAdDivs(Document newestAdsPage) {
        var hirdeteslist = newestAdsPage.getElementById("hirdeteslist");
        return hirdeteslist.getElementsByClass("hirdeteslist_hirdetes hirdeteslist_hirdetes");
    }

    public Ad convertToAd(Element element) {
        var imgStyle = element.getElementsByTag("img").first().attr("style");
        var priceElement = element.getElementsByClass("hirdeteslist_ar").first();

        return Ad.builder()
                .name(element.getElementsByClass("hirdetes_list_cim").first().ownText())
                .price(priceElement != null ? priceElement.ownText() : null)
                .image(imgStyle.substring(22, imgStyle.length() - 32))
                .url(element.getElementsByTag("a").first().absUrl("href"))
                .build();
    }
}
