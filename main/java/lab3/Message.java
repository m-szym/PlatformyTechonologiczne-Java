package lab3;

import java.io.Serializable;

public class Message
        implements Serializable {
    private static final int id = 0;
    private String content;

    public Message(String content) {
        this.content = content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public int getId() {
        return id;
    }

    public String toString() {
        return "Message-" + id + ": " + content;
    }
}
