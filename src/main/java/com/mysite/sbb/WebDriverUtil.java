package com.mysite.sbb;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.JavascriptExecutor;
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

    @Value("${webdriver.path}")
    private String WEB_DRIVER_PATH;

    private static final String WEB_DRIVER_ID = "webdriver.chrome.driver";
    private static final String RESERVATION_HOME_URL = "https://yeyak.seoul.go.kr/web/main.do";
    private static final String TENNIS_RESERVATION_POST_URL = "https://yeyak.seoul.go.kr/web/reservation/selectReservView.do?rsv_svc_id=";
    private static final List<String> COAT_ID_LIST = Arrays.asList(
            "S201030105206531192", "S201030105601087749", "S201030145802586611", "S210302233348803598",
            "S210302233656019242", "S210319185351345647", "S210319192158999179");
    private static final String YEBIGUN_HOME_URL = "https://www.yebigun1.mil.kr/dmobis/index_main.do";

    public ChromeOptions getDefaultOption() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("headless");
        options.addArguments("no-sandbox");
        options.addArguments("disable-dev-shm-usage");
        return options;
    }

    @Scheduled(cron = "0 0/15 * * * ?") // 15분 마다
    public void getBoramaeCoatInfo() throws InterruptedException {
        log.info("reservation information of Boramae tennis coat crawling start");
        System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);
        WebDriver driver = new ChromeDriver(getDefaultOption());
        JavascriptExecutor executor = (JavascriptExecutor) driver;

        driver.get(RESERVATION_HOME_URL);
        Thread.sleep(1000); // 브라우저 로딩 대기
        driver.manage().window().maximize();

        WebElement lang = driver.findElement(By.cssSelector(".language"));
        executor.executeScript("arguments[0].click();", lang);
        // lang.click();
        WebElement kor = driver.findElement(By.cssSelector(".language a")); // 한국어로 변경
        executor.executeScript("arguments[0].click();", kor);
        // kor.click();

        List<Coat> coatInfoList = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        for (int i = 0; i < COAT_ID_LIST.size(); i++) {
            int coatNumber = i + 1;
            String coatId = COAT_ID_LIST.get(i);

            driver.get(TENNIS_RESERVATION_POST_URL + coatId); // n번 코트 예약 페이지로 이동
            Thread.sleep(1000); // 브라우저 로딩 대기

            WebElement pop_x = driver.findElement(By.className("pop_x")); // 팝업 닫기
            executor.executeScript("arguments[0].click();", pop_x);
            // pop_x.click();

            List<WebElement> trList = driver.findElements(By.cssSelector(".tbl_cal tbody td"));
            for (WebElement element : trList) {
                String date = element.getAttribute("id");
                if (date.contains("calendar")) { // 존재하는 일자이면
                    date = date.substring(9); // 날짜만 파싱
                    WebElement num = element.findElement(By.className("num"));
                    if (num.getText().equals(" "))
                        continue; // 평일은 넘어감
                    coatInfoList.add(Coat.builder()
                            .id(Integer.parseInt(coatNumber + date))
                            .coatNumber(coatNumber)
                            .date(LocalDate.parse(date, formatter))
                            .reservation(num.getText())
                            .name("보라매")
                            .link(TENNIS_RESERVATION_POST_URL + coatId)
                            .modifyDate(LocalDateTime.now())
                            .build());
                }
            }
        }

        driver.close(); // 탭 닫기
        driver.quit(); // 브라우저 닫기

        coatRepository.saveAll(coatInfoList);
        log.info("reservation information of Boramae tennis coat crawling end");
    }

    public void saveCookie(WebDriver driver) {
        driver.get(YEBIGUN_HOME_URL);

        File file = new File("Cookies.data");
        try {
            file.delete();
            file.createNewFile();
            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fileWriter);
            for (Cookie ck : driver.manage().getCookies()) {
                bw.write(ck.getName() + ";" + ck.getValue() + ";" + ck.getDomain() + ";" + ck.getPath() + ";"
                        + ck.getExpiry() + ";" + ck.isSecure());
                bw.newLine();
            }
            bw.close();
            fileWriter.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void getCookie(WebDriver driver) {
        try {
            File file = new File("Cookies.data");
            FileReader fileReader = new FileReader(file);
            BufferedReader br = new BufferedReader(fileReader);
            String strLine;
            while ((strLine = br.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(strLine, ";");
                while (st.hasMoreTokens()) {
                    String name = st.nextToken();
                    String value = st.nextToken();
                    String domain = st.nextToken();
                    String path = st.nextToken();
                    Date expiry = null;
                    String val;
                    SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
                    if (!(val = st.nextToken()).equals("null")) {
                        expiry = dateFormat.parse(val);
                    }
                    Boolean isSecure = new Boolean(st.nextToken()).booleanValue();
                    Cookie ck = new Cookie(name, value, domain, path, expiry, isSecure);
                    log.info(ck.toString());
                    driver.manage().addCookie(ck);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
