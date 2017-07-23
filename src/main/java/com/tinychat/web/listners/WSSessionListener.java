package com.tinychat.web.listners;

import com.tinychat.app.chat.ChatApplicationFacade;
import com.tinychat.app.chat.commands.RefreshUserListCommand;
import com.tinychat.app.chat.commands.ToggleUserConnectionCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.security.Principal;

@Component
public class WSSessionListener {

    @Autowired
    private ChatApplicationFacade chatApplication;

    @EventListener
    public void handleSessionConnectedEvent(SessionConnectedEvent sessionConnectedEvent) {
        String username = this.getUsername(sessionConnectedEvent.getUser());
        if (username == null) {
            return;
        }

        chatApplication.execute(new ToggleUserConnectionCommand(true, username));
    }

    @EventListener
    public void handleDisconnectEvent(SessionDisconnectEvent sessionDisconnectEvent) {
        String username = this.getUsername(sessionDisconnectEvent.getUser());
        if (username == null) {
            return;
        }

        if (chatApplication.execute(new ToggleUserConnectionCommand(false, username))) {
            chatApplication.execute(new RefreshUserListCommand());
        }
    }

    private String getUsername(Principal principal) {
        if (principal == null) {
            return null;
        }

        return principal.getName();
    }

}
