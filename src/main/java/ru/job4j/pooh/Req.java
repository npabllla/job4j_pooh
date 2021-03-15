package ru.job4j.pooh;

public class Req {
    private String method;
    private String mode;
    private String text;
    private Integer id;

    public Req(String request) {
        requestParser(request);
    }

    private void requestParser(String content) {
        String[] splittedRequest = content.split(" ");
        String[] splittedUrl = splittedRequest[1].split("/");
        this.method = splittedRequest[0];
        this.mode = splittedUrl[1];
        if (splittedRequest.length > 2) {
            this.text = splittedRequest[3];
        } else {
            this.text = null;
        }
        if (splittedUrl.length == 4) {
            this.id = Integer.parseInt(splittedUrl[3]);
        } else {
            this.id = null;
        }
    }

    public String getMethod() {
        return method;
    }

    public String getMode() {
        return mode;
    }

    public String getText() {
        return text;
    }

    public Integer getId() {
        return id;
    }
}