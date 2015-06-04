package ustc.zmk.textrank;

import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;

import java.io.*;
import java.util.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/**
 * TextRank关键词提取
 * @author hankcs
 */ 
public class SearchEngine
{
    public static final int nKeyword = 5;
    /**
     * 阻尼系数（ＤａｍｐｉｎｇＦａｃｔｏｒ），一般取值为0.85
     */
    static final float d = 0.85f;
    /**
     * 最大迭代次数
     */
    static final int max_iter = 200;
    static final float min_diff = 0.001f;

    public SearchEngine()
    {
        // jdk bug : Exception in thread "main" java.lang.IllegalArgumentException: Comparison method violates its general contract!
        System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
    }
    
    public  List<Map.Entry<String, Float>> getKeyword(String title, String content)
    {
        List<Term> termList = ToAnalysis.parse(title + content);
        //termList after tokenize
        List<String> wordList = new ArrayList<String>();
        for (Term t : termList)
        {
            if (shouldInclude(t))
            {
                wordList.add(t.getName());
            }
        }
        //filter by a useless word list ---wordList
        Map<String, Set<String>> words = new HashMap<String, Set<String>>();
        //words is a map, 一个字符串对应给他投分的字符串集合
        Queue<String> que = new LinkedList<String>();
        for (String w : wordList)
        {
            if (!words.containsKey(w))
            {
                words.put(w, new HashSet<String>());
            }
            que.offer(w);
            if (que.size() > 5)
            {
                que.poll();
            }
            
            for (String w1 : que)
            {
                for (String w2 : que)
                {
                    if (w1.equals(w2))
                    {
                        continue;
                    }

                    words.get(w1).add(w2);
                    words.get(w2).add(w1);
                }
            }
            //相互添加前后5个词汇到list中
        }
//        System.out.println(words);
        Map<String, Float> score = new HashMap<String, Float>();
        //a map which map the token in the file to a score
        for (int i = 0; i < max_iter; ++i)
        {
            Map<String, Float> m = new HashMap<String, Float>();
            float max_diff = 0;
            for (Map.Entry<String, Set<String>> entry : words.entrySet())
            {
                String key = entry.getKey();
                Set<String> value = entry.getValue();
                m.put(key, 1 - d);
                for (String other : value)
                {
                    int size = words.get(other).size();
                    if (key.equals(other) || size == 0) continue;
                    m.put(key, m.get(key) + d / size * (score.get(other) == null ? 0 : score.get(other)));
                }
                max_diff = Math.max(max_diff, Math.abs(m.get(key) - (score.get(key) == null ? 0 : score.get(key))));
            }
            score = m;
            if (max_diff <= min_diff) break;
            //迭代已完成，不变动了
        }
        List<Map.Entry<String, Float>> entryList = new ArrayList<Map.Entry<String, Float>>(score.entrySet());
        Collections.sort(entryList, new Comparator<Map.Entry<String, Float>>()
        {
            @Override
            public int compare(Map.Entry<String, Float> o1, Map.Entry<String, Float> o2)
            {
                return (o1.getValue() - o2.getValue() > 0 ? -1 : 1);
            }
        });
        //将分数排序
        String result = "";
        if(entryList.size()>nKeyword)
        {
	        for (int i = 0; i < nKeyword; ++i)
	        {
	            result += entryList.get(i).getKey() + " " + entryList.get(i).getValue()+ "分#";
	        }
        }
        else
        {
        	for (int i = 0; i < entryList.size(); ++i)
	        {
	            result += entryList.get(i).getKey() + " " + entryList.get(i).getValue()+ "分#";
	        }
        }
        return entryList;
    }
    
    //calculate a score for each page
    public  static void the_last_step(String user_input , Page page, float proper)
    {
    	List<Map.Entry<String, Float>> map = new SearchEngine().getKeyword("", page.content);
    	List<Map.Entry<String, Float>> title = new SearchEngine().getKeyword("", page.title);
    	map.forEach( tem -> { if(tem.getKey().equals(user_input))
    							page.points += tem.getValue();   });
    	
    	title.forEach(tem -> {  if(tem.getKey().equals(user_input))  
    									page.points += tem.getValue()*5;   });
    	
    	page.tags.forEach(str -> {  if(str.equals(user_input)) 
    									page.points += 1.5;   });
    }
    
    //sort the score list in a descend sequence
    public static Page[] sort(HashSet<Page> set)
    {
    	double a;
    	Page temp;
    	Page[] list = new Page[set.size()];
    	int num = 0;
    	for(Page page:set)
    	{
    		list[num] = page;
    		num++;
    	}
    	for(int i=set.size()-1;i>0;i--)
    	{
    		for(int j=set.size()-1;j>set.size()-1-i;j--)
    		{
    			if(list[j].points > list[j-1].points)
    			{
    				temp = list[j-1];
    				list[j-1] = list[j];
    				list[j] = temp;
    			}
    		}
    	}
    	return list;
    }
    
    public static void teams(List<Map.Entry<String, Float>> map)
    {
    	HashSet<String> teamSet = new HashSet<String>();
    	teamSet.add("马刺");
    	teamSet.add("灰熊");
    	teamSet.add("小牛");
    	teamSet.add("鹈鹕");
    	teamSet.add("森林狼");
    	teamSet.add("掘金");
    	teamSet.add("爵士");
    	teamSet.add("开拓者");
    	teamSet.add("雷霆");
    	teamSet.add("国王");
    	teamSet.add("太阳");
    	teamSet.add("湖人");
    	teamSet.add("快船");
    	teamSet.add("勇士");
    	teamSet.add("热");
    	teamSet.add("魔术队");
    	teamSet.add("老鹰");
    	teamSet.add("奇才");
    	teamSet.add("黄蜂");
    	teamSet.add("活塞");
    	teamSet.add("步行者");
    	teamSet.add("骑士");
    	teamSet.add("公牛");
    	teamSet.add("雄鹿");
    	teamSet.add("凯尔特人");
    	teamSet.add("76人");
    	teamSet.add("尼克斯");
    	teamSet.add("篮网");
    	teamSet.add("猛龙");
    	float num;
    	for(Map.Entry<String, Float> temp:map)
    	{
    		if(teamSet.contains(temp.getKey()))
    		{
    			num = temp.getValue();
    			temp.setValue(num*2);
    		}
    	}
    }
    
    public static void main(String[] args) throws Exception
    {
    	HashSet<Page> set = new HashSet<Page>();
    	Scanner in = new Scanner(System.in);
    	System.out.println("Input what you want to Search :");
    	String user_input = in.nextLine();
    	List<Map.Entry<String, Float>> input_set = new SearchEngine().getKeyword("", user_input);
    	for(Map.Entry<String, Float> map:input_set)
    	{
    		System.out.println(map.getKey()+"points: "+map.getValue());
    	}
    	teams(input_set);
    	for(Map.Entry<String, Float> map:input_set)
    	{
    		System.out.println(map.getKey()+"points: "+map.getValue());
    	}
    	
    	Crawl tt = new Crawl();
		tt.to_crawl.add("http://nba.hupu.com/");
		for(tt.num = 0;tt.num<=3;tt.num++)
		{
			Thread t1 = new Thread(tt,"thread1");
			Thread t2 = new Thread(tt,"thread2");
			Thread t3 = new Thread(tt,"thread3");
			Thread t4 = new Thread(tt,"thread4");
			Thread t5 = new Thread(tt,"thread5");
			t1.start();
			t2.start();
			t3.start();
			t4.start();
			t5.start();
			while(! (!t1.isAlive() && !t2.isAlive() && !t3.isAlive() && !t4.isAlive() && !t5.isAlive()) )
			{
				
			}
			tt.to_crawl = tt.next_depth;
    		tt.next_depth = new Vector<String>(); 
		}
    	set = tt.set;
    	
    	
    	if(set.size()==0)
    		System.out.println("Error!");
    	for(Map.Entry<String, Float> map:input_set)
    	{
	    	for(Page page:set)
	    		the_last_step(map.getKey() , page, map.getValue());
    	}
    	Page[] list = null;
    	
    	list = sort(set);
    	
    	if(list!=null)
    	{
    		for(int i=1;i<=list.length;i++)
    		{
    			System.out.println("No"+i+": "+list[i-1].url+"     Points:"+list[i-1].points);
    		}
    	}
    	in.close();
    }
    
    /**
     * 是否应当将这个term纳入计算，词性属于名词、动词、副词、形容词
     * @param term
     * @return 是否应当
     */
    public boolean shouldInclude(Term term)
    {
    	Vector<String> StopWordDictionary = new Vector<String>();
    	StopWordDictionary.add(">");
    	StopWordDictionary.add("<");
    	StopWordDictionary.add("-");
    	StopWordDictionary.add("/");
    	StopWordDictionary.add("&");
    	StopWordDictionary.add(";");
    	StopWordDictionary.add("\\");
    	StopWordDictionary.add("得到");
    	StopWordDictionary.add("学");
    	StopWordDictionary.add("球");
    	StopWordDictionary.add("至今");
    	StopWordDictionary.add("有");
    	StopWordDictionary.add("可");
    	StopWordDictionary.add("是");
    	StopWordDictionary.add("做");
    	StopWordDictionary.add("不");
    	StopWordDictionary.add("分");
        if (
                term.getNatrue().natureStr.startsWith("n") ||
                term.getNatrue().natureStr.startsWith("v") ||
                term.getNatrue().natureStr.startsWith("d") ||
                term.getNatrue().natureStr.startsWith("a")
                )
        {
            if(!StopWordDictionary.contains(term.getName()))
            {
                return true;
            }
        }

        return false;
    }
    	
}

class Crawl implements Runnable
{
	volatile Vector<String> to_crawl = new Vector<String>();
	volatile Vector<String> crawled = new Vector<String>();
	volatile Vector<String> next_depth = new Vector<String>(); 
	volatile HashSet<Page> set = new HashSet<>();
	
    public static String[] get_content(String page,String start,String end)
	{
		String[] lis = new String[2];
		String str = new String(page);
		String str2,strr = new String("");
		int len = start.length();
		if(str!=null)
		{
			int a = str.indexOf(start);
			if(a>=0)
			{
				str2 = str.substring(a+len);
				int b = str2.indexOf(end);
				if(b>=0)
				{
					strr = str.substring(a+len, a+b+len);
					str = str.substring(a+b+len);
				}
			}
			lis[0] = strr;
			lis[1] = str;
		}
		return lis;
	}

    public static Vector<String> union(Vector<String> ve1,Vector<String> ve2)
	{
		ve2.forEach(link1 -> 
							{
								if(!ve1.contains(link1))
									ve1.add(link1);
							});
		return ve1;
	}
	
	public static String getURLSource(URL url)
    {
        HttpURLConnection conn;
        String htmlSource = new String("");
		try 
		{
			conn = (HttpURLConnection)url.openConnection();
			conn.setRequestMethod("GET");
		    InputStream inStream = null;
			try 
			{
				 inStream = conn.getInputStream();
			} 
			catch (FileNotFoundException e) 
			{
				e.printStackTrace();
			}  
			byte[] data = readInputStream(inStream);
			htmlSource = new String(data);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return htmlSource;
		}
		return htmlSource;
    }
    
    public static byte[] readInputStream(InputStream instream) throws Exception 
    {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[]  buffer = new byte[2048];
        int len = 0;
        while ((len = instream.read(buffer)) != -1)
        {
            outStream.write(buffer,0,len);
        }
        instream.close();
        return outStream.toByteArray();         
    }

	public static Vector<String> get_urls(String page,String start,String end)
	{
		Vector<String> strs = new Vector<String>();
		String str2,strr;
		String page_clone = new String(page);
		while(true)
		{
			int a = page_clone.indexOf(start);
			if(a>0)
			{
				str2 = page_clone.substring(a+7);
				int b = str2.indexOf(end);
				strr = page_clone.substring(a+7, a+b+7);
				if(strr.startsWith("http://voice"))
				{
					if(!strr.endsWith("html"))
					{
						int num = strr.indexOf("html");
						strr = strr.substring(0, num+4);
					}
					if(strr.endsWith("html"))
						strs.add(strr);
				}
				page_clone = page_clone.substring(a+b+10);
			}
			else
				return strs;
		}
	}

	public void run()
	{
    	String page,content,other_page;
    	String t1 = null,t2 = null;
    	ArrayList<String> tags = new ArrayList<>();
		while(!to_crawl.isEmpty())
		{
			if(to_crawl.size()>=1)
			{
				try
				{
					page = to_crawl.remove(to_crawl.size()-1);
		    		if(!crawled.contains(page) && 
		    						(page.startsWith("http://voice.hupu.com/nba/1")||page.equals("http://nba.hupu.com/")))
		    		{
		    			System.out.println("第"+this.num+"轮："+ Thread.currentThread().getName() + " "+page);
		    			try 
		    			{
		    					content = getURLSource(new URL(page));
								next_depth = union(next_depth , get_urls(content," href=\"","\""));
								String article = "";
								String title;
								String[] temp = get_content(content,"<h1 class=\"headline\">", "</h1>");
								title = temp[0];
								content = temp[1];
								
								
								temp = get_content(content,"<div class=\"artical-main-content\">", "</div>");
								content = temp[0];
								other_page = temp[1];
								while(content.contains("<p>") && content.contains("</p>"))
								{
									temp = get_content(content, "<p>", "</p>");
									article += temp[0];
									content = temp[1];
								}
								temp = get_content(other_page,"<span class=\"title\">相关标签：</span>","</div>");
								other_page = temp[0];
								if(other_page!=null)
								{
									while(other_page.contains("<a target=\"_blank\""))
									{
										temp = get_content(other_page,"a target=\"_blank\" href=\"/o/","</a>");
										other_page = temp[1];
										if(temp[0].contains("data-o-id="))
										{
											int n1 = temp[0].indexOf("data-o-id=");
											if(this.num>=0)
											{
												temp[0] = temp[0].substring(n1+10);
												if(temp[0].contains(">"))
												{
													int num1 = temp[0].indexOf(">");
													temp[0] = temp[0].substring(num1+1);
													if(temp[0].contains("-"))
													{
														int n2 = temp[0].indexOf("-");
														t1 = temp[0].substring(0,n2);
														t2 = temp[0].substring(n2+1);
													}
												}
											}
										}
										if(t1!=null)
										{
											tags.add(t1);
											t1 = null;
										}
										if(t2!=null)
										{
											tags.add(t2);
											t2 = null;
										}
										tags.add(new String(temp[0]));
									}
								}
								Page demo = new Page(page,title,article,tags);
					        	set.add(demo);
		    			} 
		    			catch (MalformedURLException e) 
		    			{
							e.printStackTrace();
						} 
		    			catch (Exception e) 
		    			{
		    				e.printStackTrace();
						}
		    			tags = new ArrayList<>();
		    			crawled.add(page);
		    		}
				}
				catch(Exception e)
				{
					
				}
			}
		}
	}
	
	int num = 0;
}

class Page
{
	String url;
	String content;
	String title;
	ArrayList<String> tags;
	double points;
	Page(String url,String title,String content,ArrayList<String> tags)
	{
		this.url = url;
		this.title = title;
		this.content = content;
		this.tags = tags;
	}
}