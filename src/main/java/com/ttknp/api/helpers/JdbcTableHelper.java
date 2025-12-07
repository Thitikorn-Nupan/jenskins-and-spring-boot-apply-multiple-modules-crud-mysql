package com.ttknp.api.helpers;

import org.springframework.data.relational.core.mapping.Table;
import org.springframework.stereotype.Component;

@Component
public class JdbcTableHelper {

    /** get schema and table name */
    public String getSchemaAndTableNameOnTableAnnotation(Class<?> entityClass) {
        Table tableAnnotation = entityClass.getAnnotation(Table.class);
        if (tableAnnotation != null) {
            return tableAnnotation.schema()+"."+tableAnnotation.name();
        }
        throw new RuntimeException("Schema & Table " + entityClass.getSimpleName() + " has no @Table annotation");
    }

    /** get table name */
    public String getTableNameOnTableAnnotation(Class<?> entityClass) {
        Table tableAnnotation = entityClass.getAnnotation(Table.class);
        if (tableAnnotation != null) {
            return tableAnnotation.name();
        }
        throw new RuntimeException("Table " + entityClass.getSimpleName() + " has no @Table annotation");
    }

    /** get schema name */
    public String getSchemaNameOnTableAnnotation(Class<?> entityClass) {
        Table tableAnnotation = entityClass.getAnnotation(Table.class);
        if (tableAnnotation != null) {
            return tableAnnotation.schema();
        }
        throw new RuntimeException("Schema " + entityClass.getSimpleName() + " has no @Table annotation");
    }

}