package lab3;

import java.io.Serializable;

public class Message
        implements Serializable {
    private static int id = 0;
    private int messageId;
    private String content;

    public Message(String content) {
        this.content = content;
        Message.id++;
        messageId = Message.id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public int getId() {
        return messageId;
    }

    public String toString() {
        return "Message-" + messageId + ": " + content;
    }
}
