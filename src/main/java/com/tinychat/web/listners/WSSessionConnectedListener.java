package com.tinychat.web.listners;

import com.tinychat.app.ChatApplication;
import com.tinychat.app.commands.ToggleUserConnectionCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;

import java.security.Principal;

@Component
public class WSSessionConnectedListener implements ApplicationListener<SessionConnectedEvent> {
    @Autowired
    ChatApplication chatApplication;

    @Override
    public void onApplicationEvent(SessionConnectedEvent sessionConnectedEvent) {
        Principal principal = sessionConnectedEvent.getUser();
        if (principal == null) {
            return;
        }

        String username = principal.getName();
        if (username == null) {
            return;
        }

        chatApplication.execute(new ToggleUserConnectionCommand(true, username));
    }
}
