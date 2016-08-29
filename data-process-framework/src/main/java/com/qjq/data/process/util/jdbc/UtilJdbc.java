package com.qjq.data.process.util.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.jdbc.core.SqlTypeValue;
import org.springframework.jdbc.core.StatementCreatorUtils;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.KeyHolder;

import com.qjq.data.process.util.UtilDate;
import com.mchange.v2.c3p0.PooledDataSource;

/**
 * 
 * @since 0.1.0
 */
public class UtilJdbc {
    protected static final Logger logger = LoggerFactory.getLogger(UtilJdbc.class);

    /**
     * @return <pre>
     * {
     * 	total: 'int:总数',
     * 	busy: 'int:使用数',
     * 	idle: 'int:空闲数',
     * 	unclosed: 'int:',
     * 
     * 	startTime: '初始化时间',
     * 	upTime: 'int:毫秒',
     * 	upTimeHuman: '更容易'
     * }
     * </pre>
     */
    public static Map<String, Object> getDataSourceInfo(JdbcTemplate jdbcTemplate) throws SQLException {
        Map<String, Object> result = new LinkedHashMap<>();
        DataSource ds = jdbcTemplate.getDataSource();
        if (ds instanceof PooledDataSource) {
            PooledDataSource c3p0 = (PooledDataSource) ds;
            result.put("total", c3p0.getNumConnectionsAllUsers());
            result.put("busy", c3p0.getNumBusyConnectionsAllUsers());
            result.put("idle", c3p0.getNumIdleConnectionsAllUsers());
            result.put("unclosed", c3p0.getNumUnclosedOrphanedConnectionsAllUsers());

            result.put("startTime", UtilDate.format(c3p0.getStartTimeMillisDefaultUser()));
            long upTime = c3p0.getUpTimeMillisDefaultUser();
            result.put("upTime", upTime);
            result.put("upTimeHuman", UtilDate.prettyTime(upTime));
        }
        return result;
    }

    public static Set<String> getColumns(ResultSet rs) throws SQLException {
        ResultSetMetaData rsmd = rs.getMetaData();
        Set<String> columns = new HashSet<>();
        for (int index = rsmd.getColumnCount(); index >= 1; index--) {
            columns.add(JdbcUtils.lookupColumnName(rsmd, index));
        }
        return columns;
    }

    // public static String getMySQLJdbcUrl(String host, Integer port, String
    // db) {
    // return "jdbc:mysql://" + host + ":" + (port == null ? 3306 : port) + "/"
    // + (db == null ? "" : db) + "?useUnicode=true&characterEncoding=utf8";
    // }

    public static String placeHolder(int size) {
        StringBuilder buf = new StringBuilder(size * 2);
        for (int i = 0; i < size; i++) {
            buf.append("?,");
        }
        if (buf.length() > 0)
            buf.deleteCharAt(buf.length() - 1);
        return buf.toString();
    }

    // public static String underscoreName(String name) {
    // StringBuilder result = new StringBuilder(name.length() + 5)
    // .append(Character.toLowerCase(name.charAt(0)));
    // for (int i = 1, len = name.length(); i < len; i++) {
    // char ch = name.charAt(i), lower = Character.toLowerCase(ch);
    // if (ch != lower) result.append('_');
    // result.append(lower);
    // }
    // return result.toString();
    // }
    //
    // /**
    // * 为某些数据直接拼接到sql中提供支持，如：int、long
    // */
    // public static <T> String toSqlPart(List<T> datas) {
    // StringBuilder buf = new StringBuilder(datas.size() * 8);
    // for (T data : datas) {
    // buf.append(data).append(',');
    // }
    // if (buf.length() > 0) buf.deleteCharAt(buf.length() - 1);
    // return buf.toString();
    // }

    public static void setValues(PreparedStatement ps, final Object... args) throws SQLException {
        for (int i = 0, pos; i < args.length; i = pos) {
            Object arg = args[i];
            pos = i + 1;
            if (arg instanceof SqlParameterValue) {
                SqlParameterValue paramValue = (SqlParameterValue) arg;
                StatementCreatorUtils.setParameterValue(ps, pos, paramValue, paramValue.getValue());
            } else {
                StatementCreatorUtils.setParameterValue(ps, pos, SqlTypeValue.TYPE_UNKNOWN, arg);
            }
        }
    }

    // public static void cleanupParameters(final Object... args) {
    // StatementCreatorUtils.cleanupParameters(args);
    // }
    public static KeyHolder insertAndReturnKeyHolder(JdbcTemplate jdbcTemplate, final String sql, final Object... args) {
        logger.debug("Insert and return KeyHolder for SQL: {}", sql);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            jdbcTemplate.update(new PreparedStatementCreator() {
                @Override
                public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                    PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                    setValues(ps, args);
                    return ps;
                }
            }, keyHolder);
        } finally {
            StatementCreatorUtils.cleanupParameters(args);
        }
        return keyHolder;
    }

    /** 允许返回null */
    public static <T> T queryForObject(JdbcTemplate jdbcTemplate, String sql, RowMapper<T> rowMapper, Object... args)
            throws DataAccessException {
        List<T> results = jdbcTemplate.query(sql, rowMapper, args);
        return results.isEmpty() ? null : DataAccessUtils.requiredSingleResult(results);
    }

    /** 允许返回null */
    public static <T> T queryForObject(JdbcTemplate jdbcTemplate, String sql, Class<T> clazz, Object... args)
            throws DataAccessException {
        List<T> results = jdbcTemplate.query(sql, new SingleColumnRowMapper<T>(clazz), args);
        return results.isEmpty() ? null : DataAccessUtils.requiredSingleResult(results);
    }

    public static String[] getCols(ResultSet rs) throws SQLException {
        ResultSetMetaData md = rs.getMetaData();
        int colCount = md.getColumnCount();
        String[] cols = new String[colCount];
        for (int i = 0; i < colCount;) {
            cols[i] = md.getColumnName(++i);
        }
        return cols;
    }

    public static List<Map<String, String>> getTables(Connection conn) throws SQLException {
        List<Map<String, String>> tables = new ArrayList<>();

        try (ResultSet rs = conn.getMetaData().getTables(null, null, null, null)) {
            while (rs.next()) {
                Map<String, String> row = new HashMap<>();
                row.put("TABLE_CAT", rs.getString("TABLE_CAT"));
                row.put("TABLE_TYPE", rs.getString("TABLE_TYPE"));
                row.put("TABLE_NAME", rs.getString("TABLE_NAME"));
                tables.add(row);
            }
        }
        return tables;
    }

    public static Set<String> getTableNames(Connection conn) throws SQLException {
        Set<String> tables = new HashSet<>();
        try (ResultSet rs = conn.getMetaData().getTables(null, null, null, null)) {
            while (rs.next()) {
                tables.add(rs.getString("TABLE_NAME"));
            }
        }
        return tables;
    }

    public static boolean execute(Connection conn, String sql) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            return stmt.execute(sql);
        }
    }

    /** 返回count的sql并自动移除order by */
    private static String getCountSql(String sql) {
        int posFrom = sql.indexOf(" from ");
        if (posFrom == -1)
            throw new IllegalArgumentException("SQL中不存在' from ', " + sql);
        String countSql = "select count(*)" + sql.substring(posFrom);

        int posOrderBy = countSql.lastIndexOf(" order by ");
        if (posOrderBy != -1 && countSql.indexOf(")", posOrderBy) == -1)
            countSql = countSql.substring(0, posOrderBy);
        return countSql;
    }

    public static <T> void queryPage(Page<T> page, JdbcTemplate jdbcTemplate, String sql, RowMapper<T> rowMapper, Object... args)
            throws DataAccessException {
        String countSql = getCountSql(sql);
        if (logger.isDebugEnabled())
            logger.debug("分页查询 总记录：sql={}, params={}", countSql, Arrays.toString(args));
        page.setTotal(jdbcTemplate.queryForObject(countSql, Integer.class, args));
        if (page.getPageNo() > page.getMaxNo())
            page.setPageNo(page.getMaxNo());

        Integer[] limits = page.getLimits();
        if (page.getTotal() <= limits[0])
            return;

        String limitSql = sql + " limit ?,?";
        Object[] params = Arrays.copyOf(args, args.length + 2);
        params[params.length - 2] = limits[0];
        params[params.length - 1] = limits[1];
        if (logger.isDebugEnabled())
            logger.debug("分页查询 查询页数据：sql={}, params={}", limitSql, Arrays.toString(params));
        page.setItems(jdbcTemplate.query(limitSql, rowMapper, params));
    }
}
