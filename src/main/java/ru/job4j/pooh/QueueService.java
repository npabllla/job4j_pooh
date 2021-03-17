package ru.job4j.pooh;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class QueueService implements Service {
    private final Map<String, ConcurrentLinkedQueue<Resp>> responses = new ConcurrentHashMap<>();

    @Override
    public Resp process(Req req) {
        if (req.getMethod().equals("GET")) {
            responses.putIfAbsent(req.getMode(), new ConcurrentLinkedQueue<>());
            Resp response = responses.get(req.getMethod()).poll();
            if (response == null) {
                return new Resp("Response body is empty", 200);
            }
            return response;
        } else if (req.getMethod().equals("POST")) {
            responses.putIfAbsent(req.getMode(), new ConcurrentLinkedQueue<>());
            Resp response = new Resp(req.getText(), 200);
            responses.get(req.getMode()).offer(response);
            return response;
        } else {
            return new Resp("Bad request", 400);
        }
    }
}