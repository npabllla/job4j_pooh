package ru.job4j.pooh;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TopicService implements Service {
    private final Map<String,  Map<Integer, ConcurrentLinkedQueue<Resp>>> responses
            = new ConcurrentHashMap<>();

    @Override
    public Resp process(Req req) {
        if (req.getMethod().equals("GET")) {
            Map<Integer, ConcurrentLinkedQueue<Resp>> idQueuePairs =
                    responses.computeIfAbsent(req.getMode(), e -> new ConcurrentHashMap<>());
            idQueuePairs.putIfAbsent(req.getId(), new ConcurrentLinkedQueue<>());
            Resp response = responses.get(req.getMode()).get(req.getId()).poll();
            if (response == null) {
                return new Resp("Response body is empty", 200);
            }
            return response;
        } else if (req.getMethod().equals("POST")) {
            Resp response = new Resp(req.getText(), 200);
            if (responses.computeIfPresent(
                    req.getMode(),
                    (key, value) -> {
                        Map<Integer, ConcurrentLinkedQueue<Resp>> iqQueuePairs
                                = responses.get(req.getMode());
                        for (Map.Entry<Integer, ConcurrentLinkedQueue<Resp>> entry : iqQueuePairs.entrySet()) {
                            ConcurrentLinkedQueue<Resp> queue = entry.getValue();
                            queue.offer(response);
                        }
                        return value;
                    }
            ) != null) {
                return response;
            }
            Map<Integer, ConcurrentLinkedQueue<Resp>> newIdQueueMap = new ConcurrentHashMap<>();
            responses.put(req.getMode(), newIdQueueMap);
            return response;
        } else {
            return new Resp("Bad request", 400);
        }
    }
}