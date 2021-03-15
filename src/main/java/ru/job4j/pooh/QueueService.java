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
            if (responses.get(req.getMethod()).poll() == null) {
                return new Resp("Response body is empty", 200);
            }
            return responses.get(req.getMode()).poll();
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