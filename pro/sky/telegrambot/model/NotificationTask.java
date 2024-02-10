package pro.sky.telegrambot.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class NotificationTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private Long chatId;
    private String message;
    private LocalDateTime date;

    public NotificationTask(int id, Long idChat, String message, LocalDateTime date) {
        this.id = id;
        this.chatId = idChat;
        this.message = message;
        this.date = date;
    }

    public NotificationTask(Long idChat, String message, LocalDateTime date) {
        this.chatId = idChat;
        this.message = message;
        this.date = date;
    }

    public NotificationTask() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long idChat) {
        this.chatId = idChat;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NotificationTask that = (NotificationTask) o;
        return id == that.id && chatId == that.chatId && Objects.equals(message, that.message) && Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, chatId, message, date);
    }
}
