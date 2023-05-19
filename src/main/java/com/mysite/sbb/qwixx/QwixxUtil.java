package com.mysite.sbb.qwixx;

import com.mysite.sbb.qwixx.dto.GameInfoResponse;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Component
public class QwixxUtil {

  long seed = System.currentTimeMillis();
  private Random random = new Random(seed);

  public String getRandomRoomName() {
    List<String>
      pre = Arrays.asList("재미있는", "흥미로운", "모두가 즐기는", "혼돈의", "좌추우돌", "남녀노소 즐기는");
    List<String> post = Arrays.asList("보드게임!", "큐윅스!", "주사위!", "게임방 ^^");
    return pre.get(random.nextInt(pre.size())) + " " + post.get(random.nextInt(post.size()));
  }

  public void rollDice(GameInfoResponse gameInfoResponse) {
    Integer white1 = this.random.nextInt(1, 7);
    Integer white2 = this.random.nextInt(1, 7);
    Integer red = this.random.nextInt(1, 7);
    Integer yellow = this.random.nextInt(1, 7);
    Integer green = this.random.nextInt(1, 7);
    Integer blue = this.random.nextInt(1, 7);

    gameInfoResponse.setWhite1(white1);
    gameInfoResponse.setWhite2(white2);
    gameInfoResponse.setRed(red);
    gameInfoResponse.setYellow(yellow);
    gameInfoResponse.setGreen(green);
    gameInfoResponse.setBlue(blue);

    gameInfoResponse.setWhiteSum(white1 + white2);
    gameInfoResponse.setRedSum1(white1 + red);
    gameInfoResponse.setRedSum2(white2 + red);
    gameInfoResponse.setYellowSum1(white1 + yellow);
    gameInfoResponse.setYellowSum2(white2 + yellow);
    gameInfoResponse.setGreenSum1(white1 + green);
    gameInfoResponse.setGreenSum2(white2 + green);
    gameInfoResponse.setBlueSum1(white1 + blue);
    gameInfoResponse.setBlueSum2(white2 + blue);
  }
}
