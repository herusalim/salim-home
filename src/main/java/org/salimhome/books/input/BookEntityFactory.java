package org.salimhome.books.input;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class BookEntityFactory {
    private static final String ENTITY_KIND_TEMP = "BukuTmp";
    private static final String ENTITY_KIND = "Buku";
    public static final String PROPERTY_FRONT_COVER_ID = "frontCoverId";
    public static final String PROPERTY_BACK_COVER_ID = "backCoverId";
    public static final String PROPERTY_TITLE = "title";
    private Entity entity;
    private String entityKind;

    public static BookEntityFactory newTempEntity() {
        return new BookEntityFactory(ENTITY_KIND_TEMP);
    }

    public Key createTempEntityKey(long id) {
        return KeyFactory.createKey(entityKind, id);
    }

    public BookEntityFactory(String entityKind) {
        this.entityKind = entityKind;
    }

    public Entity createNewEntity() {
        return new Entity(entityKind);
    }
}
