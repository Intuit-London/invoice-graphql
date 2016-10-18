package com.intuit.workshop.invoicing.graphql.relay

import graphql.relay.Base64
import graphql.relay.Relay

class GlobalIdHelper {

    static String id(String type, String id) {
        return Base64.toBase64("$type:$id");
    }

    static Relay.ResolvedGlobalId fromId(String globalId) {
        String[] split = Base64.fromBase64(globalId).split(":", 2);
        assert split.size() == 2, "Malformed Global ID"
        return new Relay.ResolvedGlobalId(split[0], split[1]);
    }
}
