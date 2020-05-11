
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.jsoup.Jsoup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.Random;


public class searchImageBot extends TelegramLongPollingBot {

    public void onUpdateReceived(Update update) {

        Random r = new Random();
        String newIP= r.nextInt(256) + "." + r.nextInt(256) + "." + r.nextInt(256) + "." + r.nextInt(256);

        String text=update.getMessage().getText();
        System.out.println(text);

        if(text.contains(" ")){
            String [] text1=text.split(" ");
            for(String t:text1){
                text += t;
            }
        }

        SendMessage message=new SendMessage();
        if (!text.equals("/start")) {
            try {

                String url="https://yandex.com.tr/gorsel/search?from=tabbar&text=" + text.trim();
                Document doc = Jsoup.connect(url.trim())
                        .userAgent("Mozilla/5.0 (X11; Ubuntu; Linux x86_64) AppleWebKit/537.36 " +
                                "(KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36 " +
                                "RuxitSynthetic/1.0 v4979915562 t38550")
                        .timeout(10 * 1000)
                        .header("Connection", "keep-alive")
                        .header("X-Forwarded-For", newIP)
                        .header("Referer", url.trim())
                        .header("Accept",
                                "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
                        .header("Cache-Control", "max-age=0")
                        .header("Content-Type", "application/json; charset=utf-8")
                        .get();

                System.out.println(newIP);

                Elements imageElements = doc.select("img.serp-item__thumb");

                int sayac = 1;
                for (Element imageElement : imageElements) {
                    //make sure to get the absolute URL using abs: prefix
                    String strImageURL = imageElement.attr("abs:src");

                    message.setText(strImageURL);
                    message.setChatId(update.getMessage().getChatId());

                    execute(message);

                    Thread.sleep(400);
                    sayac++;
                    if (sayac == 6) break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TelegramApiException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public String getBotUsername() {
        return "resimAraBot";
    }

    public String getBotToken() {
        return "1104413650:AAGu6JghQL7v1YhOejT_A3tHlcarB1ithfA";
    }
}
