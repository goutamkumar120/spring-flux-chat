package com.goutam.chat.spring_flux_chat.service;

import org.redisson.api.RListReactive;
import org.redisson.api.RTopicReactive;
import org.redisson.api.RedissonReactiveClient;
import org.redisson.client.codec.StringCodec;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;

@Service
public class ChatRoomService implements WebSocketHandler {

    private final RedissonReactiveClient client;

    public ChatRoomService(RedissonReactiveClient client) {
        this.client = client;
    }

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        String room = getChatRoomName(session);

        // Redis topic and list
        RTopicReactive topic = client.getTopic(room, StringCodec.INSTANCE);
        RListReactive<String> history = client.getList("history:" + room, StringCodec.INSTANCE);

        // Subscriber: save incoming messages and publish to topic
        Mono<Void> subscriber = session.receive()
                .map(WebSocketMessage::getPayloadAsText)
                .flatMap(msg -> history.add(msg).then(topic.publish(msg)))
                .then();

        // Publisher: replay history first, then live messages
        Flux<WebSocketMessage> publisher = Flux.from(history.iterator())
                .concatWith(topic.getMessages(String.class))
                .map(session::textMessage);

        // Combine subscriber and publisher
        return Mono.when(subscriber, session.send(publisher));
    }

    private String getChatRoomName(WebSocketSession session) {
        URI uri = session.getHandshakeInfo().getUri();
        List<String> rooms = UriComponentsBuilder.fromUri(uri)
                .build()
                .getQueryParams()
                .get("room");
        return (rooms != null && !rooms.isEmpty()) ? rooms.get(0) : "default";
    }
}