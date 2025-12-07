package com.ttknp.api;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import java.util.List;
import java.util.Map;

/** No need @Service and @Autowired because i'll inject tru super(jdbcTemplate,usefulJdbcService) key word */
public abstract class JdbcExecuteHelper  {

    private final JdbcTemplate jdbcTemplate;

    public JdbcExecuteHelper(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     What JdbcExecuteHelper can do ?
     1. Select all
     2. Select one
     3. Select count
     4. Insert ******
     5. Update ******
     6. Delete ******
     7. Execute as script ******
    */

    /** Note, U can only be String , Integer , ... any types but not an Object like POJO */
    private <U> List<U> executeQueryForList(String sql ,Class<U> aTypeClass, Object... params) {
        return jdbcTemplate.queryForList(sql, aTypeClass,params);
    }

    private <T> List<T> executeQuery(String sql, RowMapper<T> rowMapper, Object... params) { // RowMapper as Mapping auto
        return jdbcTemplate.query(sql, rowMapper, params);
    }

    private <T> T executeQuery(String sql, ResultSetExtractor<T> resultSetExtractor, Object... params) { // RowMapper as Mapping auto
        return jdbcTemplate.query(sql, resultSetExtractor, params);
    }

    /** Note , T can be list or object */
    private <T> T executeQueryResultSetExtractor(String sql, ResultSetExtractor<T> resultSetExtractor, Object... params) { // ResultSetExtractor as Mapping on own
        return jdbcTemplate.query(sql, resultSetExtractor, params);
    }

    private <T> T executeQueryForObject(String sql, RowMapper<T> rowMapper, Object... params) {
        return jdbcTemplate.queryForObject(sql, rowMapper, params);
    }

    /** Note, Class<T> aClassType for specify return type */
    private <U> U executeQueryForObjectProperty(String sql, Class<U> aClassType, Object... params) {
        return jdbcTemplate.queryForObject(sql, aClassType, params);
    }

    /** Note, update works for insert & delete & update statements */
    private Integer executeUpdateForObject(String sql, Object... params) {
        return jdbcTemplate.update(sql, params);
    }





    // ------------  For query(ies) no return  ------------
    public void executeQueryForStatement(String sql) {
        jdbcTemplate.execute(sql);
    }





    // ------------ Select as List ------------
    public List<Map<String, Object>> executeQueryForList(String sql , Object... params) { // queryForList, it works for rows, but not recommend, the mapping in Map may not same as the object, need casting.
        return jdbcTemplate.queryForList(sql,params);
    }

    public <T> List<T> executeQueryByBeanPropertyRowMapper(String sql, BeanPropertyRowMapper<T> beanPropertyRowMapper) {
        return executeQuery(sql , beanPropertyRowMapper ,null);
    }

    public <T> T executeQueryByResultSetExtractor(String sql, ResultSetExtractor<T> resultSetExtractor) {
        return executeQuery(sql , resultSetExtractor );
    }

    public <T> List<T> executeQueryByBeanPropertyRowMapperParams(String sql, BeanPropertyRowMapper<T> beanPropertyRowMapper, Object  ...params) {
        return executeQuery(sql, beanPropertyRowMapper , params);
    }

    public <U> List<U> executeQueryForListProperty(String sql, Class<U> aClass) {
        return executeQueryForList(sql, aClass,null);
    }

    public <U> List<U> executeQueryForListPropertyParams(String sql, Class<U> aClass, Object  ...params) {
        return executeQueryForList(sql, aClass , params);
    }






    // ------------ Select as An Object ------------
    public <T> T executeQueryForObjectByBeanPropertyRowMapper(String sql, BeanPropertyRowMapper<T> beanPropertyRowMapper) {
        return executeQueryForObject(sql, beanPropertyRowMapper ,null);
    }

    public <T> T executeQueryForObjectByBeanPropertyRowMapperParams(String sql, BeanPropertyRowMapper<T> beanPropertyRowMapper , Object  ...params) {
        return executeQueryForObject(sql, beanPropertyRowMapper ,params);
    }




    // ------------ Select as An Property (as select count,select one column) ------------
    public <U> U executeQueryForObjectPropertyNoMapping(String sql, Class<U> aClassType) {
        return executeQueryForObjectProperty(sql, aClassType ,null);
    }

    public <U> U executeQueryForObjectPropertyParamsNoMapping(String sql, Class<U> aClassType , Object  ...params) {
        return executeQueryForObjectProperty(sql, aClassType ,params);
    }




    // ------------ Select as List Or Object  ------------
    public <T> T executeResultSetExtractor(String sql, ResultSetExtractor<T> resultSetExtractor) {
        return executeQueryResultSetExtractor(sql, resultSetExtractor ,null);
    }

    public <T> T executeResultSetExtractorParams(String sql, ResultSetExtractor<T> resultSetExtractor,Object  ...params) {
        return executeQueryResultSetExtractor(sql , resultSetExtractor , params);
    }




    // ------------ Insert ------------
    /** SimpleJdbcInsert class simplifies writing code to execute SQL INSERT statement, i.e. So you don’t have to write lengthy and tedious SQL Insert statement anymore just specify the table name, column names and parameter values. */
    public <T> Integer executeSimpleInsertByBeanPropertySqlParameterSource(String schema, String table, String autoGenerateColumnName, String[] usingColumns,T object) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        simpleJdbcInsert.withSchemaName(schema);
        simpleJdbcInsert.withTableName(table);

        if (autoGenerateColumnName != null) {
            simpleJdbcInsert.usingGeneratedKeyColumns(autoGenerateColumnName); // if you have auto generate number don't forget set it all
        }

        simpleJdbcInsert.usingColumns(usingColumns);

        // As you can see, you don’t even have to specify parameter names and values, as long as you provide an object that has attributes same as the column names in the database table.
        return simpleJdbcInsert.execute(new BeanPropertySqlParameterSource(object));
    }

    public Integer executeUpdateForInsert(String sql,Object  ...params) {
        return executeUpdateForObject(sql , params);
    }

    public Integer executeUpdateForInsert(String sql,List<Object> params) {
        return executeUpdateForObject(sql , params.toArray()); // have to convert to array as Object ...params
    }





    // ------------ Update ------------
    public Integer executeUpdateForUpdate(String sql,Object  ...params) {
        return executeUpdateForObject(sql , params);
    }

    public Integer executeUpdateForUpdate(String sql,List<Object> params) {
        return executeUpdateForObject(sql , params.toArray());
    }




    // ------------ Delete ------------
    public Integer executeUpdateForDelete(String sql,Object  ...params) {
        return executeUpdateForObject(sql , params);
    }

}