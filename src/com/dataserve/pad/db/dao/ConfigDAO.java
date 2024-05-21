package com.dataserve.pad.db.dao;


import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.Set;

import com.dataserve.pad.bean.ConfigBean;
import com.dataserve.pad.db.AbstractDAO;
import com.dataserve.pad.db.DatabaseException;


public class ConfigDAO  extends AbstractDAO{

	public ConfigDAO() throws DatabaseException {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Set<ConfigBean> fetchAllConfigs() throws DatabaseException{
		Set<ConfigBean> configs = new LinkedHashSet<ConfigBean>();
		try {
			stmt = con.prepareStatement("SELECT NAME, ISNULL(VALUE, '') VALUE, COMMENT FROM CONFIG ORDER BY NAME");
			rs = stmt.executeQuery();
			while (rs.next()) {	
				ConfigBean bean = new ConfigBean();
				bean.setName(rs.getString("NAME"));
				bean.setValue(rs.getString("VALUE"));
				bean.setComment(rs.getString("COMMENT"));
				configs.add(bean);
			}
		}catch (SQLException e) {
			throw new DatabaseException("Error fetching record from table CONFIG", e);
		} finally {
			safeClose();
			releaseResources();
		}
		return configs;
	}
	
	public ConfigBean fetchConfig(String name) throws DatabaseException{
		try {
			stmt = con.prepareStatement("SELECT NAME,VALUE,COMMENT FROM CONFIG WHERE NAME = ?");
			stmt.setString(1, name);
			rs = stmt.executeQuery();
			ConfigBean bean = null;
			if (rs.next()) {		
				bean = new ConfigBean();
				bean.setName(rs.getString("NAME"));
				bean.setValue(rs.getString("VALUE"));
				bean.setComment(rs.getString("COMMENT"));
				return bean;
			} else {
				return bean;
			}
		} catch (SQLException e) {
			throw new DatabaseException("Error fetching Config with name '" + name + "' from table CONFIG", e);
		} finally {
			safeClose();
			releaseResources();
		}
	}
	
	public int addConfig (ConfigBean bean)  throws DatabaseException{
		 try {
	        	String name = bean.getName().trim();
	        	if (name != null && !name.trim().equals("")) {
					stmt = con.prepareStatement("SELECT NAME FROM CONFIG WHERE NAME = ?");
		        	stmt.setString(1, bean.getName());
		        	rs = stmt.executeQuery();
		        	if (rs.next()) {
		        		this.updateConfig(bean);
		        	}else {
		        		stmt = con.prepareStatement("INSERT INTO CONFIG(NAME, VALUE,COMMENT) VALUES (?, ?,?)");
			        	stmt.setNString(1, bean.getName().trim());
			        	stmt.setString(2, bean.getValue().trim());
			        	stmt.setString(3, bean.getComment().trim());
			            stmt.executeUpdate();
			        	if (rs.next()) {
			        		return rs.getInt(1);
			        	}
		        	}
				}
	            return 0;
	        } catch (SQLException e) {
	            throw new DatabaseException("Error adding new record to table CONFIG", e);
	        } finally {
	            safeClose();
	            releaseResources();
	        }
	}
	
	private int updateConfig (ConfigBean bean) throws DatabaseException{
	try {
		stmt = con.prepareStatement("UPDATE CONFIG SET VALUE = ? WHERE NAME = ?");
    	stmt.setString(1, bean.getValue().trim());
    	stmt.setString(2, bean.getName().trim());
        return stmt.executeUpdate();
    } catch (SQLException e) {
        throw new DatabaseException("Error updating table CONFIG", e);
    } finally {
        safeClose();
        releaseResources();
    }
	}
	
}
