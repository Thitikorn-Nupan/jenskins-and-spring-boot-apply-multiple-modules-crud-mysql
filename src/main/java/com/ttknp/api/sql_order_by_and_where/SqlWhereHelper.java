package com.ttknp.api.sql_order_by_and_where;

/// New concept for helping to build sql statement with order by clause
public interface SqlWhereHelper<T> {
    /**
     Note ! stringBuilder is sql statement as select * from table_name
     you can call appendOrderBy(...) like this:
     SqlOrderByHelper<T> = ((stringBuilder, alias, model) -> {
        stringBuilder.append(" column_name ASC ");
        ... But won't do first
     });
     it'll do after this SqlOrderByHelper<T> sqlOrderByHelper is param on some method
     like this: public List<T> selectAll(Class<T> aBeanClass, SqlOrderByHelper<T> sqlOrderByHelper) {
        Now assume stringBuilder is select * from table_name
        then you can do like this:
        sqlOrderByHelper.appendOrderBy(stringBuilder, alias, null);
        then stringBuilder is select * from table_name order by column_name ASC
     }
    */
    void appendWhere(StringBuilder stringBuilder, String alias, T model) ;
}