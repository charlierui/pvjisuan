package com.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.bean.LogBean;
import com.dao.LogDao;

public class LogDaoImpl implements LogDao {

	private Logger logger = Logger.getLogger(LogDaoImpl.class);
	
	private JdbcTemplate jdbcTemplate;
	
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public void saveLog(LogBean logBean) throws Exception {
		String sql = "insert into log(id, session_id, ip, page, access_time, stay_time) " +
				"values(?, ?, ?, ?, ?, ?)";
		Object[] params = new Object[]{logBean.getId(), logBean.getSessionId(), 
				logBean.getIp(), logBean.getPage(), logBean.getAccessTime(), logBean.getStayTime()};
		
		logger.info(sql);
		this.jdbcTemplate.update(sql, params);
	}
	
	@SuppressWarnings("unchecked")
	public LogBean getLatestLog(String sessionId, String ip)
			throws Exception {
		String sql = "select * from log where session_id = ? and ip = ? order by access_time desc limit 0, 1 ";
		logger.info(sql);
		
		List<LogBean> list = this.jdbcTemplate.query(sql, new Object[]{sessionId, ip}, new RowMapper() {
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException
			{
				LogBean logBean = new LogBean();
				logBean.setId(rs.getString("ID"));
				logBean.setSessionId(rs.getString("SESSION_ID"));
				logBean.setIp(rs.getString("IP"));
				logBean.setPage(rs.getString("PAGE"));
				logBean.setAccessTime(rs.getTimestamp("ACCESS_TIME"));
				logBean.setStayTime(rs.getInt("STAY_TIME"));
				return logBean;
			}});
		
		LogBean logBean = null;
		if(list != null && !list.isEmpty()){
			logBean = list.get(0);
		}
		return logBean;
	}

	public void updateLog(String id, long stayTime) throws Exception {
		String sql = "update log set stay_time = ? where id = ? ";
		
		
		Object[] params = new Object[]{stayTime, id};
		logger.info(sql+" params:"+id +" staytime:"+stayTime);

		this.jdbcTemplate.update(sql, params);
	}

	public int getPV(Timestamp startTime, Timestamp endTime) throws Exception {
		String sql = "select count(*) from log where access_time >= ? and access_time < ? ";
		logger.info(sql+ startTime+" - "+endTime);
		
		int pv = this.jdbcTemplate.queryForInt(sql, new Object[]{startTime, endTime});
		return pv;
	}

	public int getUV(Timestamp startTime, Timestamp endTime) throws Exception {
		String sql = "select count(ip) from(select distinct ip from log where access_time >= ? and access_time < ?) log_temp";
		logger.info(sql+ startTime+" - "+endTime);
		
		int uv = this.jdbcTemplate.queryForInt(sql, new Object[]{startTime, endTime});
		return uv;
	}

}
