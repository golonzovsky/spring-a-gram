package com.greglturnquist.springagram.backend.service;

import static com.greglturnquist.springagram.backend.config.WebSocketConfiguration.*;

import com.greglturnquist.springagram.backend.domain.Gallery;
import com.greglturnquist.springagram.backend.domain.Item;
import com.greglturnquist.springagram.backend.domain.User;
import com.greglturnquist.springagram.backend.domain.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleAfterDelete;
import org.springframework.data.rest.core.annotation.HandleAfterLinkDelete;
import org.springframework.data.rest.core.annotation.HandleAfterLinkSave;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.core.mapping.ResourceMappings;
import org.springframework.hateoas.EntityLinks;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Because the published application context events are, in fact, synchronous, this handler is able to
 * hold up {@link Item} creation until the {@link User}
 * can be retrieved and populated.
 * <p>
 * Since this is in the same thread of execution as the original REST call,
 * {@link SecurityContextHolder} can be used to retrieve the username,
 * and hence do the user lookup.
 */
@Component
@RepositoryEventHandler(Item.class)
public class SpringDataRestEventHandler {

    private static final Logger log = LoggerFactory.getLogger(SpringDataRestEventHandler.class);

    private final UserRepository repository;
    private final EntityLinks entityLinks;
    private final ResourceMappings resourceMappings;
    private final RepositoryRestConfiguration config;
    private final StringRedisTemplate redis;
    private final SimpMessagingTemplate websocket;

    @Autowired
    public SpringDataRestEventHandler(UserRepository repository, StringRedisTemplate redis, EntityLinks entityLinks,
                                      ResourceMappings resourceMappings, RepositoryRestConfiguration config,
                                      SimpMessagingTemplate websocket) {

        this.repository = repository;
        this.redis = redis;
        this.entityLinks = entityLinks;
        this.resourceMappings = resourceMappings;
        this.config = config;
        this.websocket = websocket;
    }

    @HandleBeforeCreate
    public void applyUserInformationUsingSecurityContext(Item item) {

        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = repository.findByName(name);
        if (user == null) {
            User newUser = new User();
            newUser.setName(name);
            user = repository.save(newUser);
        }
        item.setUser(user);
    }

    @HandleAfterCreate
    public void notifyAllClientsAboutNewItem(Item item) {

        log.info("Just created new item " + item);
        publish("backend.newItem", pathFor(item));
    }

    @HandleAfterDelete
    public void notifyAllClientsAboutItemDeletion(Item item) {

        log.info("Just deleted item " + item);
        publish("backend.deleteItem", pathFor(item));
    }

    @HandleAfterLinkDelete
    public void notifyAllClientsWhenRemovedFromGallery(Item item, Object obj) {

        log.info("Item " + item + " just had an afterLinkDelete...");
        log.info("Related object => " + obj);
        publish("backend.removeItemFromGallery-item", pathFor(item));
        publish("backend.removeItemFromGallery-gallery", pathFor((Gallery) obj));
    }

    @HandleAfterLinkSave
    public void notifyAllClientsWhenAddedToGallery(Item item, Object obj) {

        log.info("Item " + item + " just had an afterLinkSave...");
        publish("backend.addItemToGallery-item", pathFor(item));
        publish("backend.addItemToGallery-gallery", pathFor(item.getGallery()));
    }

    private void publish(String routingKey, String message) {

        redis.convertAndSend(MESSAGE_PREFIX + "/" + routingKey, message);
        websocket.convertAndSend(MESSAGE_PREFIX + "/" + routingKey, message);
    }

    private String pathFor(Item item) {

        return entityLinks.linkForSingleResource(item.getClass(),
                item.getId()).toUri().getPath();
    }

    private String pathFor(Gallery gallery) {
        return entityLinks.linkForSingleResource(gallery.getClass(),
                gallery.getId()).toUri().getPath();
    }

}
