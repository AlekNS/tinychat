package com.tinychat.web.listners;

import com.tinychat.app.ChatApplication;
import com.tinychat.app.commands.RefreshUserListCommand;
import com.tinychat.app.commands.ToggleUserConnectionCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.security.Principal;

@Component
public class WSSessionDisconnectListener implements ApplicationListener<SessionDisconnectEvent> {
    @Autowired
    ChatApplication chatApplication;

    @Override
    public void onApplicationEvent(SessionDisconnectEvent sessionDisconnectEvent) {
        Principal principal = sessionDisconnectEvent.getUser();
        if (principal == null) {
            return;
        }

        String username = principal.getName();
        if (username == null) {
            return;
        }

        if (chatApplication.execute(new ToggleUserConnectionCommand(false, username))) {
            chatApplication.execute(new RefreshUserListCommand());
        }
    }
}
