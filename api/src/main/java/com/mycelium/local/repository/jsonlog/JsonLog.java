package com.mycelium.local.repository.jsonlog;

import java.util.Date;
import java.util.List;

import com.mycelium.local.repository.errorlog.ErrorLog;
import com.mycelium.local.repository.user.User;

import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.data.annotation.Relation;
import io.micronaut.data.model.naming.NamingStrategies;

@MappedEntity(namingStrategy = NamingStrategies.UpperCase.class)
public class JsonLog {
    @Id
    @GeneratedValue
    public Integer id;
    public Date when;
    public boolean operation;
    public String archiveName;
    public int success;
    public int error;

    @Relation(value = Relation.Kind.ONE_TO_MANY, mappedBy = "jsonLog")
    public List<ErrorLog> errorLogs;

    @Relation(value = Relation.Kind.MANY_TO_ONE)
    public User user;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getWhen() {
        return when;
    }

    public void setWhen(Date when) {
        this.when = when;
    }

    public boolean isOperation() {
        return operation;
    }

    public void setOperation(boolean operation) {
        this.operation = operation;
    }

    public String getArchiveName() {
        return archiveName;
    }

    public void setArchiveName(String archiveName) {
        this.archiveName = archiveName;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<ErrorLog> getErrorLogs() {
        return errorLogs;
    }

    public void setErrorLogs(List<ErrorLog> errorLogs) {
        this.errorLogs = errorLogs;
    }
}
