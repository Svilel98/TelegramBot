package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.model.Update;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.NotificationTask;
import pro.sky.telegrambot.repository.NotificationTaskRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TelegramBotUpdateMessage {
    final NotificationTaskRepository notificationTaskRepository;
    private Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);


    public TelegramBotUpdateMessage(NotificationTaskRepository notificationTaskRepository) {
        this.notificationTaskRepository = notificationTaskRepository;
    }

    public void saveMessage(Update update) {
        String message = update.message().text();
        Pattern pattern = Pattern.compile("(?<date>[0-9\\.\\:\\s]{16})" +
                "(?<message>(\\s)([\\W+]+))");
        Matcher matcher = pattern.matcher(message);
        if (matcher.matches()) {
            Long idChat = update.message().chat().id();
            LocalDateTime dateMessage = LocalDateTime.parse(matcher.group("date").trim(),
                    DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
            String messageFromBot = matcher.group("message").trim();
            notificationTaskRepository.save(new NotificationTask(idChat, messageFromBot, dateMessage));
        } else {
            System.out.println("Неккореткные данные");
        }
    }
}
