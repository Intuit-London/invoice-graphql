package com.intuit.workshop.invoicing.domain.entity.id

import com.intuit.workshop.invoicing.domain.entity.Entity
import graphql.relay.Base64
import graphql.relay.Relay
import org.springframework.util.Assert

class GlobalIdHelper {

    static String id(String type, String id) {
        return Base64.toBase64("$type:$id");
    }

    static String entityId(Entity entity, String entityId) {
        String type = EntityType.fromEntity(entity)
        Assert.notNull(type, "Can not find type for the given entity")
        return id(type, entityId)
    }

    static Relay.ResolvedGlobalId fromId(String globalId) {
        String[] components = parse(globalId)
        return new Relay.ResolvedGlobalId(components[0], components[1]);
    }

    static void validate(String globalId) {
        if (globalId) {
            parse(globalId)
        }
    }

    private static List<String> parse(String globalId) {
        String[] split = Base64.fromBase64(globalId).split(":", 2);
        Assert.isTrue(split.size() == 2, "Malformed Global ID")
        return split
    }
}
