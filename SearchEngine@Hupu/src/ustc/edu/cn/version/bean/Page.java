package ustc.edu.cn.version.bean;

import java.util.ArrayList;

public class Page {
	private String url;
	private String content;
	private String title;
	private ArrayList<String> tags;
	private double points;
	public Page(){}
	public Page(String url, String title, String content, ArrayList<String> tags) {
		this.url = url;
		this.title = title;
		this.content = content;
		this.tags = tags;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public ArrayList<String> getTags() {
		return tags;
	}

	public void setTags(ArrayList<String> tags) {
		this.tags = tags;
	}

	public double getPoints() {
		return points;
	}

	public void setPoints(double points) {
		this.points = points;
	}
}