package com.ttknp.api.select;


import com.ttknp.api.JdbcExecuteHelper;
import com.ttknp.api.helpers.JdbcTableHelper;
import com.ttknp.api.helpers.ReadSQLStatementHelper;
import com.ttknp.api.sql.SQLSyntax;
import com.ttknp.api.sql_order_by_and_where.SqlOrderByHelper;
import com.ttknp.api.sql_order_by_and_where.SqlWhereHelper;
import com.ttknp.api.utility.JdbcReadSQLFileHelper;
import com.ttknp.api.validates.ValidateHelperService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class JdbcSelectHelper<T> extends JdbcExecuteHelper {

    private static final Logger log = LoggerFactory.getLogger(JdbcSelectHelper.class);
    private final JdbcTableHelper jdbcTableHelper;
    private final ReadSQLStatementHelper readSQLFileAsStatement;
    private final JdbcReadSQLFileHelper jdbcReadSQLFileHelper;

    @Autowired
    public JdbcSelectHelper(JdbcTemplate jdbcTemplate, JdbcTableHelper jdbcTableHelper,ReadSQLStatementHelper readSQLFileAsStatement, JdbcReadSQLFileHelper jdbcReadSQLFileHelper) {
        super(jdbcTemplate);
        this.jdbcTableHelper = jdbcTableHelper;
        this.readSQLFileAsStatement = readSQLFileAsStatement;
        this.jdbcReadSQLFileHelper = jdbcReadSQLFileHelper;
    }

    // ------------ Dynamic select statement ------------
    // ------------ Select as List ------------
    ///  (0) select start by bean class (no mapper)
    public List<Map<String, Object>> selectAllAsMap(Class<T> aBeanClass) {
        StringBuilder stringBuilder = new StringBuilder()
                .append(SQLSyntax.SELECT_START)
                .append(jdbcTableHelper.getTableNameOnTableAnnotation(aBeanClass));
        return executeQueryForList(stringBuilder.toString());
    }

    /// (1) Description of process get key and value from object see on JdbcInsertUpdateDeleteHelper->Dynamic insert statement
    public List<T> selectAll(Class<T> aBeanClass, SqlOrderByHelper<T> sqlOrderByHelper, SqlWhereHelper<T> sqlWhereHelper, T model) {
        StringBuilder stringBuilder = new StringBuilder()
                .append(SQLSyntax.SELECT_START)
                .append(jdbcTableHelper.getTableNameOnTableAnnotation(aBeanClass));
        String alias = SQLSyntax.ALIAS;
        log.debug("before stringBuilder thru implements = {}", stringBuilder.toString()); // Ex, select * from h2_shop.customers

        if (sqlWhereHelper != null) {

            HashMap<String, Object> params = new HashMap<>();
            List<Field> fields = new ArrayList<>();
            Class<T> aClass = (Class<T>) model.getClass();

            stringBuilder
                    .append(" ")
                    .append(alias)
                    .append(" ")
                    .append(SQLSyntax.WHERE_TRUE);

            sqlWhereHelper.appendWhere(stringBuilder, alias, model); // call implement
            log.debug("after stringBuilder thru implement (not replace value)= {}", stringBuilder.toString()); // Ex, select * from h2_shop.customers alias where 1 = 1 and alias.full_name = {full_name}  and alias.birthday = {birthday}  and alias.level = {level}

            // Note! This way for java pojo that have subclass

            if (aClass.getSuperclass() != null) { // **** Case class have primary key on subclass
                Class<?> superclass = aClass.getSuperclass();
                for (Field field : superclass.getDeclaredFields()) {
                    fields.add(field);
                }
            }

            for (Field field : aClass.getDeclaredFields()) { // **** Case class have no primary key on subclass
                fields.add(field);
            }

            for (Field field : fields) {
                field.setAccessible(true);
                Column columnAnnotation = null;
                String name = null;
                Object value = null;

                if (field.isAnnotationPresent(Column.class)) {
                    columnAnnotation = field.getAnnotation(Column.class);
                }

                if (columnAnnotation != null) { // Case camel case
                    name = columnAnnotation.value();
                }
                else { // Case snake case
                    name = field.getName();
                }

                try {
                    value = field.get(model);
                    if (value != null) {
                        params.put("{" + name + "}", value);
                    }
                }
                catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
                // Now params (HasMap) has all field & value then replace value to key on stringBuilder
            }

            replaceAssignValuesByHasMap(stringBuilder, params);
            log.debug("after stringBuilder thru implement (replaced value) = {}", stringBuilder.toString()); // select * from h2_shop.customers alias where 1 = 1 and alias.full_name = 'Lon Slider'  and alias.birthday = '1989-01-29'  and alias.level = 'A+'

        } // End where helper

        if (sqlOrderByHelper != null ) {

            if (sqlWhereHelper != null) {
                stringBuilder
                        .append(" order by ");
            }

            if (sqlWhereHelper == null) {
                stringBuilder
                        .append(" ")
                        .append(alias)
                        .append(" order by ");
            }

            sqlOrderByHelper.appendOrderBy(stringBuilder, alias, null);
            log.debug("after stringBuilder thru implement (on where helper) = {}", stringBuilder.toString());

        }
        // Now sql already query ex,
        // select * from h2_shop.customers alias where 1 = 1  and alias.birthday = '1989-01-29'  order by  alias.level desc, alias.full_name asc limit 10
        // select * from h2_shop.customers alias where 1 = 1  and alias.birthday = '1989-01-29'
        // select * from h2_shop.customers order by  alias.level desc, alias.full_name asc limit 10
        // select * from h2_shop.customers
        return executeQueryByBeanPropertyRowMapper(stringBuilder.toString(), new BeanPropertyRowMapper<T>(aBeanClass));
    }

    /// (2) it's same (1) but add custom alias key
    public List<T> selectAll(Class<T> aBeanClass, SqlOrderByHelper<T> sqlOrderByHelper, SqlWhereHelper<T> sqlWhereHelper,String sqlAlias, T model) {
        StringBuilder stringBuilder = new StringBuilder()
                .append(SQLSyntax.SELECT_START)
                .append(jdbcTableHelper.getTableNameOnTableAnnotation(aBeanClass));
        String alias;
        log.debug("before stringBuilder thru implements = {}", stringBuilder.toString()); // select * from h2_shop.customers

        if (ValidateHelperService.isNotEmptyString(sqlAlias)) {
             alias = sqlAlias;
        } else {
            alias = "";
        }

        if (sqlWhereHelper != null) {

            HashMap<String, Object> params = new HashMap<>();
            List<Field> fields = new ArrayList<>();
            Class<T> aClass = (Class<T>) model.getClass();

            stringBuilder
                    .append(" ")
                    .append(alias)
                    .append(" ")
                    .append(SQLSyntax.WHERE_TRUE);

            sqlWhereHelper.appendWhere(stringBuilder, alias, model);
            log.debug("after stringBuilder thru implement (not replace value)= {}", stringBuilder.toString()); // select * from h2_shop.customers alias where 1 = 1 and alias.full_name = {full_name}  and alias.birthday = {birthday}  and alias.level = {level}

            // This way for java pojo that have subclass
            if (aClass.getSuperclass() != null) { // Case class have primary key on subclass
                Class<?> superclass = aClass.getSuperclass();
                for (Field field : superclass.getDeclaredFields()) {
                    fields.add(field);
                }
            }

            for (Field field : aClass.getDeclaredFields()) { // Case class have no primary key on subclass
                fields.add(field);
            }

            for (Field field : fields) {
                field.setAccessible(true);
                Column columnAnnotation = null;
                String name = null;
                Object value = null;

                if (field.isAnnotationPresent(Column.class)) {
                    columnAnnotation = field.getAnnotation(Column.class);
                }

                if (columnAnnotation != null) {
                    name = columnAnnotation.value();
                }
                else {
                    name = field.getName();
                }

                try {
                    value = field.get(model);
                    if (value != null) {
                        params.put("{" + name + "}", value);
                    }
                }
                catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
                // Now params has all field & value then replace value to key on stringBuilder
            }

            replaceAssignValuesByHasMap(stringBuilder, params);
            log.debug("after stringBuilder thru implement (replaced value) = {}", stringBuilder.toString()); // select * from h2_shop.customers alias where 1 = 1 and alias.full_name = 'Lon Slider'  and alias.birthday = '1989-01-29'  and alias.level = 'A+'

        } // End where helper

        if (sqlOrderByHelper != null ) {

            if (sqlWhereHelper != null) {
                stringBuilder
                        .append(" order by ");
            }

            if (sqlWhereHelper == null) {
                stringBuilder
                        .append(" ")
                        .append(alias)
                        .append(" order by ");
            }

            sqlOrderByHelper.appendOrderBy(stringBuilder, alias, null);
            log.debug("after stringBuilder thru implement (on where helper) = {}", stringBuilder.toString());

        }

        // Now sql already query ex,
        // select * from h2_shop.customers alias where 1 = 1  and alias.birthday = '1989-01-29'  order by  alias.level desc, alias.full_name asc limit 10
        // select * from h2_shop.customers alias where 1 = 1  and alias.birthday = '1989-01-29'
        // select * from h2_shop.customers order by  alias.level desc, alias.full_name asc limit 10
        // select * from h2_shop.customers
        return executeQueryByBeanPropertyRowMapper(stringBuilder.toString(), new BeanPropertyRowMapper<T>(aBeanClass));
    }

    /// (3) no where helper only order by helper
    public List<T> selectAll(Class<T> aBeanClass, SqlOrderByHelper<T> sqlOrderByHelper) {
        StringBuilder stringBuilder = new StringBuilder()
                .append(SQLSyntax.SELECT_START)
                .append(jdbcTableHelper.getTableNameOnTableAnnotation(aBeanClass));
        log.debug("before stringBuilder thru implement = {}",stringBuilder.toString()); // Ex, select * from h2_shop.customers
        if (sqlOrderByHelper != null) {
            String alias = SQLSyntax.ALIAS;
            stringBuilder.append(" as ")
                    .append(alias)
                    .append(" order by ");
            // Ex, select * from h2_shop.customers as alias order by
            /**
                Note!! appendOrderBy(...) works on CustomerController class or class that implement SqlOrderByHelper interface
                Ex, sqlOrderByHelper = ((stringBuilder, alias, model) -> {...}); on CustomerController.getAllCustomersOrderBy(...);
            */
            sqlOrderByHelper.appendOrderBy(stringBuilder, alias, null);
            log.debug("after stringBuilder thru implement = {}",stringBuilder.toString()); // Ex, select * from h2_shop.customers ORDER BY FULL_NAME asc,...
        }

        return executeQueryByBeanPropertyRowMapper(stringBuilder.toString(), new BeanPropertyRowMapper<T>(aBeanClass));
    }

    /// (4) no where helper only order by helper but you have to add sql statement on own
    public List<T> selectAll(Class<T> aBeanClass,StringBuilder stringBuilderSql, SqlOrderByHelper<T> sqlOrderByHelper) {
        HashMap<String,Object> params = new HashMap<>();
        if (sqlOrderByHelper != null) {
            String alias = SQLSyntax.ALIAS;
            // create sql for order by as dynamic
            StringBuilder stringBuilderOrderBy = new StringBuilder();
            stringBuilderOrderBy.append(" as ")
                    .append(alias)
                    .append(" order by ");
            // after thru implement = select * from h2_shop.customers as alias order by alias.level desc limit 100;
            sqlOrderByHelper.appendOrderBy(stringBuilderOrderBy, alias, null);

            // replace stringBuilderOrderBy as string to [SQL_CONDITION] on stringBuilderSql
            params.put("[SQL_CONDITION]", stringBuilderOrderBy.toString());
        }
        else {
            params.put("[SQL_CONDITION]", "");
        }
        replaceAssignValuesByHasMap(stringBuilderSql, params);
        // Ex, SELECT * FROM H2_SHOP.CUSTOMERS Or SELECT * FROM H2_SHOP.CUSTOMERS  as alias order by  alias.level desc limit 100;
        return executeQueryByBeanPropertyRowMapper(stringBuilderSql.toString(), new BeanPropertyRowMapper<T>(aBeanClass));
    }

    ///  (5) select start by bean class (auto mapper)
    public List<T> selectAll(Class<T> aBeanClass) {
        StringBuilder stringBuilder = new StringBuilder()
                .append(SQLSyntax.SELECT_START)
                .append(jdbcTableHelper.getTableNameOnTableAnnotation(aBeanClass));
        return executeQueryByBeanPropertyRowMapper(stringBuilder.toString(), new BeanPropertyRowMapper<T>(aBeanClass));
    }

    ///  (6)  read statement on sql file by root path (resource/...)
    public List<T> readStatementAndSelectAll(Class<T> aBeanClass,Class<?> aClass,String sqlFileName) {
        StringBuilder stringBuilder = null;
        try {
            stringBuilder = jdbcReadSQLFileHelper.readSQLFileOnRootPath(aClass, sqlFileName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return executeQueryByBeanPropertyRowMapper(stringBuilder.toString(), new BeanPropertyRowMapper<T>(aBeanClass));
    }

    ///  (7)  read statement on sql file by abs path (B:/A/B/C/...)
    public List<T> readStatementAndSelectAll(Class<T> aBeanClass,String sqlFileName) {
        StringBuilder stringBuilder = null;
        try {
            stringBuilder = jdbcReadSQLFileHelper.readSQLFileOnAbsPath(sqlFileName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return executeQueryByBeanPropertyRowMapper(stringBuilder.toString(), new BeanPropertyRowMapper<T>(aBeanClass));
    }

    ///  (8)  it's same (7) but have replace params
    public List<T> readStatementAndReplaceParamsAndSelectAll(Class<T> aBeanClass,String sqlFileName,HashMap<String,String> params) {
        StringBuilder stringBuilder = null;
        String sql = null;
        try {
            stringBuilder = jdbcReadSQLFileHelper.readSQLFileOnAbsPath(sqlFileName);
            sql = stringBuilder.toString();
            int paramCount = params.size();
            String[] keys = new String[paramCount];
            String[] values = new String[paramCount];
            int i = 0;
            for (String key : params.keySet()) {
                keys[i] = key;
                i++;
            }
            i=0;
            for (String value : params.values()) {
                values[i] = value;
                i++;
            }
            for (i = 0; i < paramCount ; i++) {
                sql = sql.replace(keys[i], values[i]); // replace and update
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return executeQueryByBeanPropertyRowMapper(sql, new BeanPropertyRowMapper<T>(aBeanClass));
    }

    ///  (9)  it's same (6) but have replace params
    public List<T> readStatementAndReplaceParamsAndSelectAll(Class<T> aBeanClass,Class<?> aClass,String sqlFileName,HashMap<String,String> params) {
        StringBuilder stringBuilder = null;
        String sql = null;
        try {
            stringBuilder = jdbcReadSQLFileHelper.readSQLFileOnRootPath(aClass, sqlFileName);
            sql = stringBuilder.toString();
            int paramCount = params.size();
            String[] keys = new String[paramCount];
            String[] values = new String[paramCount];
            int i = 0;
            for (String key : params.keySet()) {
                keys[i] = key;
                i++;
            }
            i=0;
            for (String value : params.values()) {
                values[i] = value;
                i++;
            }
            for (i = 0; i < paramCount ; i++) {
                sql = sql.replace(keys[i], values[i]); // replace and update
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return executeQueryByBeanPropertyRowMapper(sql, new BeanPropertyRowMapper<T>(aBeanClass));
    }

    ///  (10) it's same (5) but have to add sql statement on own
    public List<T> selectAll(String sql,Class<T> aBeanClass) {
        return executeQueryByBeanPropertyRowMapper(sql, new BeanPropertyRowMapper<T>(aBeanClass));
    }

    ///  (11) select start by bean class (auto mapper) and have where like
    public List<T> selectAllWhereLikeAColumn(Class<T> aBeanClass, String columnName, Object value) {
        StringBuilder stringBuilder = new StringBuilder()
                .append(SQLSyntax.SELECT_START)
                .append(jdbcTableHelper.getTableNameOnTableAnnotation(aBeanClass))
                .append(" where "+columnName+" like ")
                .append(SQLSyntax.ASSIGN);
        value = "%"+value.toString()+"%";
        // Ex, sql = select * from H2_SCHOOL.STUDENTS where level like ? , value = %B%
        return executeQueryByBeanPropertyRowMapperParams(stringBuilder.toString(), new BeanPropertyRowMapper<T>(aBeanClass), value);
    }

    ///  (12) select all only column by bean class
    public <U> List<U> selectAllOnlyColumn(Class<T> aBeanClass, Class<U> aTypeClass ,String columnName) {
        StringBuilder stringBuilder = new StringBuilder()
                .append("select ")
                .append(columnName)
                .append(" from ")
                .append(jdbcTableHelper.getTableNameOnTableAnnotation(aBeanClass));
        return executeQueryForListProperty(stringBuilder.toString(),aTypeClass);
    }

    ///  (13) select all only column by bean class and have where like
    public <U> List<U> selectAllOnlyColumnWhereLikeAColumn(Class<T> aBeanClass, Class<U> aTypeClass ,String columnName,String uniqColumName, Object uniqValue) {
        StringBuilder stringBuilder = new StringBuilder()
                .append("select ")
                .append(columnName)
                .append(" from ")
                .append(jdbcTableHelper.getTableNameOnTableAnnotation(aBeanClass))
                .append(" where "+uniqColumName+" like ")
                .append(SQLSyntax.ASSIGN);
        uniqValue = "%"+uniqValue.toString()+"%";
        return executeQueryForListPropertyParams(stringBuilder.toString(),aTypeClass,uniqValue);
    }






    // ------------ Select as An Object ------------
    ///  (14) select one by bean class have where
    public T selectOne(Class<T> aBeanClass, String uniqColumnName, Object uniqValue) {
        StringBuilder stringBuilder = new StringBuilder()
                .append(SQLSyntax.SELECT_START)
                .append(jdbcTableHelper.getTableNameOnTableAnnotation(aBeanClass))
                .append(" where ")
                .append(uniqColumnName+" ")
                .append(SQLSyntax.ASSIGN_EQUAL);
        return executeQueryForObjectByBeanPropertyRowMapperParams(stringBuilder.toString(), new BeanPropertyRowMapper<T>(aBeanClass), uniqValue);
    }

    ///  (15) it's same (14) but you have to add sql statement on own
    public T selectOne(String sql , Class<T> aBeanClass, Object uniqValue) {
        return executeQueryForObjectByBeanPropertyRowMapperParams(sql, new BeanPropertyRowMapper<T>(aBeanClass), uniqValue);
    }

    ///  (16) it's same (14) but on sql statement have to done before query
    public T selectOne(String sql , Class<T> aBeanClass) {
        return executeQueryForObjectByBeanPropertyRowMapper(sql, new BeanPropertyRowMapper<T>(aBeanClass));
    }






    // ------------ Select as An Property ------------
    ///  (17) select only column
    public <U> U selectOneOnlyColumn(Class<T> aBeanClass, Class<U> aTypeClass ,String columnName,String uniqColumName, Object uniqValue) {
        StringBuilder stringBuilder = new StringBuilder()
                .append("select ")
                .append(columnName)
                .append(" from ")
                .append(jdbcTableHelper.getTableNameOnTableAnnotation(aBeanClass))
                .append(" where "+uniqColumName)
                .append(SQLSyntax.ASSIGN_EQUAL);
        return executeQueryForObjectPropertyParamsNoMapping(stringBuilder.toString(),aTypeClass,uniqValue);
    }

    ///  (18) select count have where
    public Integer selectCount( Class<T> aBeanClass , String uniqColumnName, Object uniqValue) {
        StringBuilder stringBuilder = new StringBuilder()
                .append(SQLSyntax.SELECT_COUNT)
                .append(jdbcTableHelper.getTableNameOnTableAnnotation(aBeanClass))
                .append(" where ")
                .append(uniqColumnName)
                .append(SQLSyntax.ASSIGN_EQUAL);
        return executeQueryForObjectPropertyParamsNoMapping(stringBuilder.toString(),Integer.class,uniqValue);
    }

    ///  (19) it's same (18) but no where
    public Integer selectCount( Class<T> aBeanClass ) {
        StringBuilder stringBuilder = new StringBuilder()
                .append(SQLSyntax.SELECT_COUNT)
                .append(jdbcTableHelper.getTableNameOnTableAnnotation(aBeanClass));
        return executeQueryForObjectPropertyNoMapping(stringBuilder.toString(),Integer.class);
    }







    // ------------ Select as List Or Object  ------------
    ///  (20) select start by bean class (own mapper)
    public <U> U selectBoth(Class<T> aBeanClass, ResultSetExtractor<U> resultSetExtractor) {
        StringBuilder stringBuilder = new StringBuilder()
                .append(SQLSyntax.SELECT_START)
                .append(jdbcTableHelper.getTableNameOnTableAnnotation(aBeanClass));
        return executeResultSetExtractor(stringBuilder.toString(), resultSetExtractor);
    }

    ///  (21) select start by bean class (own mapper) have where like
    public <U> U selectBothWhereLikeAColumn(Class<T> aBeanClass, ResultSetExtractor<U> resultSetExtractor,String columnName, Object value) {
        StringBuilder stringBuilder = new StringBuilder()
                .append(SQLSyntax.SELECT_START)
                .append(jdbcTableHelper.getTableNameOnTableAnnotation(aBeanClass))
                .append(" where "+columnName+" like ")
                .append(SQLSyntax.ASSIGN);
        value = "%"+value.toString()+"%";
        return executeResultSetExtractorParams(stringBuilder.toString(), resultSetExtractor, value);
    }







    // ------------ Select as List Or Object  ------------
    /// Note T can be list  (22) select start join by bean class (own mapper)
    public <T,S> T selectRelation(Class<T> aBeanClass, Class<S> aSubBeanClass, String uniqKeyMain, String uniqKeySub, ResultSetExtractor<T> resultSetExtractor) {
        StringBuilder stringBuilder = new StringBuilder()
                .append(SQLSyntax.SELECT_START)
                .append(jdbcTableHelper.getTableNameOnTableAnnotation(aBeanClass)+" m ")
                .append("JOIN "+jdbcTableHelper.getTableNameOnTableAnnotation(aSubBeanClass)+" s ")
                .append("ON s."+uniqKeySub+" = m."+uniqKeyMain+" ");
        return executeQueryByResultSetExtractor(stringBuilder.toString(), resultSetExtractor);
    }

    // Cases have a relation table
    /// (23) select start joins by bean class (own mapper)
    public <T,S> T selectRelation(Class<T> aBeanClass, Class<S> aSubBeanClass, String tableRelationName, String uniqKeyMain, String uniqKeySub, ResultSetExtractor<T> resultSetExtractor) {
        StringBuilder stringBuilder = new StringBuilder()
                .append(SQLSyntax.SELECT_START)
                .append(jdbcTableHelper.getTableNameOnTableAnnotation(aBeanClass)+" m ")
                .append("JOIN "+tableRelationName+" r ")
                .append("ON r."+uniqKeyMain+" = m."+uniqKeyMain+" ")
                .append("JOIN ")
                .append(jdbcTableHelper.getTableNameOnTableAnnotation(aSubBeanClass)+" s ")
                .append("ON r."+uniqKeySub+" = s."+uniqKeySub);
        return executeQueryByResultSetExtractor(stringBuilder.toString(), resultSetExtractor);
    }

    /// (24) get sql as statement on root start on (resource/sql/)
    public StringBuilder getStatement(String filename) {
        StringBuilder stringBuilderSQL = readSQLFileAsStatement.readFileAsStatement(filename);
        return stringBuilderSQL;
    }



    // Helpers for execute***
    private static void replaceAssignValuesByHasMap(StringBuilder stringBuilderSQL, HashMap<String,Object> params) {
        String sql = stringBuilderSQL.toString();
        int paramCount = params.size();
        String[] keys = new String[paramCount];
        Object[] values = new Object[paramCount];
        int i = 0;
        for (String key : params.keySet()) {
            keys[i] = key;
            i++;
        }
        i=0;
        for (Object value : params.values()) {
            values[i] = value;
            i++;
        }
        for (i = 0; i < paramCount ; i++) {
            Object value = values[i];
            String key = keys[i];
            if (key.startsWith("[")) {
                if (value == null ) {
                    sql = sql.replace(key,"null");
                } else {
                    sql = sql.replace(key, value.toString());
                }
            }
            else {
                if ( value == null ) {
                    sql = sql.replace(key, "null"); // replace without ' '
                }
                if ( isNumeric(value) || isBool(value) ) {
                    sql = sql.replace(key, value.toString()); // replace without ' '
                }
                else {
                    sql = sql.replace(key, "'"+value+"'"); // replace with ' '
                }
            }

        }
        stringBuilderSQL.setLength(0); // way to clear string builder
        stringBuilderSQL.append(sql);
    }

    private static boolean isNumeric(Object str) {
        if (str == null || str.toString().isEmpty()) {
            return false;
        }
        try {
            Double.parseDouble(str.toString()); // Or Integer.parseInt(str) for integers
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static boolean isBool(Object str) {
        if (str == null || str.toString().isEmpty()) {
            return false;
        }
        try {
            return str instanceof Boolean;
        } catch (IllegalArgumentException | NullPointerException e ) {
            return false;
        }
    }




}