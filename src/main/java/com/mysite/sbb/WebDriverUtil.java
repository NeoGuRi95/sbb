package com.mysite.sbb;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.mysite.sbb.tennis.Coat;
import com.mysite.sbb.tennis.CoatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebDriverUtil {

    private final CoatRepository coatRepository;

    private String WEB_DRIVER_ID = "webdriver.chrome.driver";
    @Value("${webdriver.path}")
    private String WEB_DRIVER_PATH;

    private static final String homeUrl = "https://yeyak.seoul.go.kr/web/main.do";
    private static final String postUrl = "https://yeyak.seoul.go.kr/web/reservation/selectReservView.do?rsv_svc_id=";
    private final List<String> coatIdList = Arrays.asList(
            "S201030105206531192", "S201030105601087749", "S201030145802586611", "S210302233348803598", 
            "S210302233656019242", "S210319185351345647", "S210319192158999179");


    public ChromeOptions getOption() {
        ChromeOptions options = new ChromeOptions();
        // options.setCapability("ignoreProtectedModeSettings", true);
        // options.addArguments("headless");
        // options.addArguments("no-sandbox");
        // options.addArguments("disable-dev-shm-usage");
        // options.addArguments("--lang=ko");
        // options.setCapability("ignoreProtectedModeSettings", true);
        // options.addArguments("--disable-popup-blocking");             //팝업 X
        // options.addArguments("headless");                             //브라우저 안띄움
        // options.addArguments("--disable-gpu");			            //gpu X
        // options.addArguments("--blink-settings=imagesEnabled=false"); //이미지 다운 X
        return options;
    }

    @Scheduled(cron = "0 0/30 * * * ?") // 30분 마다
    public void getBoramaeCoatInfo() throws InterruptedException {
        log.info("reservation information of Boramae tennis coat crawling start");
        System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);
        WebDriver driver = new ChromeDriver(getOption());

        driver.get(homeUrl);
        Thread.sleep(1000); // 브라우저 로딩 대기
        driver.manage().window().maximize();

        WebElement lang = driver.findElement(By.cssSelector(".language"));
        lang.click();
        WebElement kor = driver.findElement(By.cssSelector(".language a")); // 한국어로 변경
        kor.click();

        List<Coat> coatInfoList = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        for (int i = 0; i < coatIdList.size(); i++) {
            int coatNumber = i + 1;
            String coatId = coatIdList.get(i);

            driver.get(postUrl + coatId); // n번 코트 예약 페이지로 이동
            Thread.sleep(1000); // 브라우저 로딩 대기

            WebElement pop_x = driver.findElement(By.className("pop_x")); // 팝업 닫기
            pop_x.click();

            List<WebElement> trList = driver.findElements(By.cssSelector(".tbl_cal tbody td"));
            for (WebElement element : trList) {
                String date = element.getAttribute("id");
                if (date.contains("calendar")) { // 존재하는 일자이면
                    date = date.substring(9); // 날짜만 파싱
                    WebElement num = element.findElement(By.className("num"));
                    if (num.getText().equals(" ")) continue; // 평일은 넘어감
                    coatInfoList.add(Coat.builder()
                                            .id(Integer.parseInt(coatNumber + date))
                                            .coatNumber(coatNumber)
                                            .date(LocalDate.parse(date, formatter))
                                            .reservation(num.getText())
                                            .name("보라매")
                                            .link(postUrl + coatId)
                                            .modifyDate(LocalDateTime.now())
                                            .build());
                }
            }
        }

        driver.close();	//탭 닫기
        driver.quit();	//브라우저 닫기

        coatRepository.saveAll(coatInfoList);
        log.info("reservation information of Boramae tennis coat crawling end");
    }
}
