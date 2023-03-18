package com.mycelium.local.repository.errorlog;

import com.mycelium.local.repository.jsonlog.JsonLog;

import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.data.annotation.Relation;
import io.micronaut.data.model.naming.NamingStrategies;

@MappedEntity(namingStrategy = NamingStrategies.Raw.class)
public class ErrorLog {
    @Id
    @GeneratedValue
    public Integer id;
    public String message;
    public int jsonLogId;

    @Relation(value = Relation.Kind.MANY_TO_ONE)
    public JsonLog jsonLog;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getJsonLogId() {
        return jsonLogId;
    }

    public void setJsonLogId(int jsonLogId) {
        this.jsonLogId = jsonLogId;
    }

    public JsonLog getJsonLog() {
        return jsonLog;
    }

    public void setJsonLog(JsonLog jsonLog) {
        this.jsonLog = jsonLog;
    }

}
