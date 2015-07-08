package ustc.edu.cn.version.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import ustc.edu.cn.property.Property;
import ustc.edu.cn.version.bean.Page;

public class DBOperate {
	private Page page;
	
	public DBOperate() {}
	
	public DBOperate(Page page) {
		this.setPage(page);
	}
	
	public void save() {		
		Connection conn = null;
		Statement stmt = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(Property.URL, Property.USER, Property.PWD);
			stmt = conn.createStatement();
			String sql = "insert into page values('" + page.getUrl() + "','" + page.getContent() + "','" + page.getTitle() + "','" +
					page.getTags() + "'," + page.getPoints() + ")"; 
			stmt.execute(sql);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				stmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public ArrayList<Page> query(String keyword) {
		ArrayList<Page> pages = new ArrayList<Page>();
		Page page = null;
		Connection conn = null;
		Statement stmt = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(Property.URL, Property.USER, Property.PWD);
			stmt = conn.createStatement();
			String sql = "select page.URL, page.CONTENT, page.TITLE, page.TAGS, page.POINT from page "
					+ "where TAGS like '%" + keyword +"%'"; 
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()) {
				page = new Page();
				page.setUrl(rs.getString("URL"));
				page.setContent(rs.getString("CONTENT"));
				page.setTitle(rs.getString("TITLE"));
				//SPLIT TAG
				String tagStr = rs.getString("TAGS");
				String[] tags = tagStr.split(",");
				for (String str : tags) {
					page.getTags().add(str);
				}
				page.setPoints(rs.getDouble("POINT"));
				pages.add(page);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				stmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return pages;
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}
}
