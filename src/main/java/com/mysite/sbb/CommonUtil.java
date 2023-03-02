package com.mysite.sbb;

import java.util.Random;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.stereotype.Component;

@Component
public class CommonUtil {

    private Random rnd = new Random();

    public String markdown(String markdown) {
        Parser parser = Parser.builder().build();
        Node document = parser.parse(markdown);
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        return renderer.render(document);
    }

    public String createTempPassword() {
        StringBuilder key = new StringBuilder();

        for (int i = 0; i < 8; i++) { // 8자리
            int index = rnd.nextInt(3);
            switch (index) {
                case 0:
                    key.append((char) ((rnd.nextInt(26)) + 97));
                    // a~z (ex. 1+97=98 => (char)98 = 'b')
                    break;
                case 1:
                    key.append((char) ((rnd.nextInt(26)) + 65));
                    // A~Z
                    break;
                case 2:
                    key.append((rnd.nextInt(10)));
                    // 0~9
                    break;
                default:
                    key.append((char) ((rnd.nextInt(26)) + 97));
                    // a~z
                    break;
            }
        }

        return key.toString();
    }
}
