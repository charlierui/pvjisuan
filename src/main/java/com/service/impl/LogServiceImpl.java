package com.service.impl;

import java.sql.Timestamp;

import com.bean.LogBean;
import com.dao.LogDao;
import com.service.LogService;

public class LogServiceImpl implements LogService {
	private LogDao logDao;

	public void setLogDao(LogDao logDao) {
		this.logDao = logDao;
	}

	
	public LogBean getLatestLog(String sessionId, String ip)
			throws Exception {
		return logDao.getLatestLog(sessionId, ip);
	}

	
	public void saveLog(LogBean logBean) throws Exception {
		logDao.saveLog(logBean);
	}

	
	public void updateLog(String id, long stayTime) throws Exception {
		logDao.updateLog(id, stayTime);
	}


	public int getPV(Timestamp startTime, Timestamp endTime) throws Exception {
		return logDao.getPV(startTime, endTime);
	}


	public int getUV(Timestamp startTime, Timestamp endTime) throws Exception {
		return logDao.getUV(startTime, endTime);
	}

}
