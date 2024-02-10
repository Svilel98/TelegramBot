package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.NotificationTask;
import pro.sky.telegrambot.repository.NotificationTaskRepository;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    TelegramBotUpdateMessage telegramBotUpdateMessage;
    private final NotificationTaskRepository notificationTaskRepository;

    public TelegramBotUpdatesListener(TelegramBotUpdateMessage telegramBotUpdateMessage, NotificationTaskRepository notificationTaskRepository) {
        this.telegramBotUpdateMessage = telegramBotUpdateMessage;
        this.notificationTaskRepository = notificationTaskRepository;
    }

    private Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    @Autowired
    private TelegramBot telegramBot;

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            logger.info("Processing update: {}", update);
            String message = update.message().text();
            String start = "/start";
            Long chatId = update.message().chat().id();
            if (message != null) {
                if (update.message().text().equals(start)) {
                    SendMessage messageNew = new SendMessage(chatId, "Добро пожаловать!");
                    SendResponse response = telegramBot.execute(messageNew);
                    response.isOk();
                } else {
                    telegramBotUpdateMessage.saveMessage(update);
                }
            }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    @Scheduled(cron = "0 0/1 * * * *")
    public void returnMessage() {
        LocalDateTime time = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        List<NotificationTask> notificationTaskByDate = notificationTaskRepository.findAllNotificationTaskByDate(time);
        notificationTaskByDate.forEach(item -> {
            SendMessage messageNew = new SendMessage(item.getChatId(), item.getMessage());
            telegramBot.execute(messageNew);
        });
    }
}
