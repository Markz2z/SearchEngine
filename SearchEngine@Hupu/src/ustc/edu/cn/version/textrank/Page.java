package ustc.edu.cn.version.textrank;

import java.util.ArrayList;

public class Page {
	public String url;
	public String content;
	public String title;
	public ArrayList<String> tags;
	public double points;

	Page(String url, String title, String content, ArrayList<String> tags) {
		this.url = url;
		this.title = title;
		this.content = content;
		this.tags = tags;
	}
}