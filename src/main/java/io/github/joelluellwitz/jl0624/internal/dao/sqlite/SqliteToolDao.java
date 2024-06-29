/**
 * Copyright (C) Joel Luellwitz 2024
 */
package io.github.joelluellwitz.jl0624.internal.dao.sqlite;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import io.github.joelluellwitz.jl0624.internal.dao.api.ToolDao;
import io.github.joelluellwitz.jl0624.internal.dao.api.ToolDto;
import jakarta.persistence.EntityManager;

/**
 * TODO: Documents.
 */
@Repository
public class SqliteToolDao implements ToolDao {

    private final EntityManager entityManager;

    // set up constructor injection
    @Autowired
    public SqliteToolDao(final EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    // See method documentation in interface definition.
    @Override
    public ToolDto getToolByCode(final String toolCode) {
        //currentSession
        return null;
    }

    // See method documentation in interface definition.
    @Override
    public List<ToolDto> listToolsSortedByToolCode() {
        // TODO Auto-generated method stub
        return null;
    }
}