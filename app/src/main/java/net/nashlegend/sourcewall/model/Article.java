package net.nashlegend.sourcewall.model;

import android.text.TextUtils;

import net.nashlegend.sourcewall.request.api.APIBase;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

/**
 * Created by NashLegend on 2014/9/16 0016
 */
public class Article extends AceModel {

    private String id = "";
    private String title = "";
    private String url = "";
    private String imageUrl = "";
    private String author = "";
    private String authorID = "";
    private String authorAvatarUrl = "";
    private String subjectName = "";
    private String subjectKey = "";
    private String date = "";
    private int commentNum = 0;
    private int likeNum = 0;
    private String summary = "";
    private String content = "";
    private ArrayList<UComment> hotComments = new ArrayList<UComment>();
    private ArrayList<UComment> comments = new ArrayList<UComment>();
    private boolean desc = false;

    public static Article fromJsonSimple(JSONObject jo) throws Exception {
        Article article = new Article();
        article.setId(jo.optString("id"));
        article.setCommentNum(jo.optInt("replies_count"));
        article.setAuthor(APIBase.getJsonObject(jo, "author").optString("nickname"));
        article.setAuthorID(APIBase.getJsonObject(jo, "author").optString("url").replaceAll("\\D+", ""));
        article.setAuthorAvatarUrl(jo.getJSONObject("author").getJSONObject("avatar").optString("large").replaceAll("\\?.*$", ""));
        article.setDate(APIBase.parseDate(jo.optString("date_published")));
        article.setSubjectName(APIBase.getJsonObject(jo, "subject").optString("name"));
        article.setSubjectKey(APIBase.getJsonObject(jo, "subject").optString("key"));
        article.setUrl(jo.optString("url"));
        article.setImageUrl(jo.optString("small_image"));
        article.setSummary(jo.optString("summary"));
        article.setTitle(jo.optString("title"));
        return article;
    }

    public static Article fromHtmlDetail(String id, String html) throws Exception {
        Article article = new Article();
        Document doc = Jsoup.parse(html);
        //replaceAll("line-height: normal;","");只是简单的处理，以防止Article样式不正确，字体过于紧凑
        //可能还有其他样式没有被我发现，所以加一个 TODO
        String articleContent = doc.getElementById("articleContent").outerHtml().replaceAll("line-height: normal;", "");
        String copyright = doc.getElementsByClass("copyright").outerHtml();
        article.setContent(articleContent + copyright);
        int likeNum = Integer.valueOf(doc.getElementsByClass("recom-num").get(0).text().replaceAll("\\D+", ""));
        // 其他数据已经在列表取得，按理说这里只要合过去就行了，
        // 但是因为有可能从其他地方进入这个页面，所以下面的数据还是要取
        // 但是可以尽量少取，因为很多数据基本已经用不到了
        article.setId(id);
        Elements infos = doc.getElementsByClass("content-th-info");
        if (infos != null && infos.size() == 1) {
            Element info = infos.get(0);
            Elements infoSubs = info.getElementsByTag("a");//记得见过不是a的
            if (infoSubs != null && infoSubs.size() > 0) {
                //                    String authorId = info.getElementsByTag("a").attr("href").replaceAll("\\D+", "");
                String author = info.getElementsByTag("a").text();
                article.setAuthor(author);
                //                    article.setAuthorID(authorId);
            }
            Elements meta = info.getElementsByTag("meta");
            if (meta != null && meta.size() > 0) {
                String date = APIBase.parseDate(info.getElementsByTag("meta").attr("content"));
                article.setDate(date);
            }
        }
        //            String num = doc.select(".cmts-title").select(".cmts-hide").get(0).getElementsByClass("gfl").get(0).text().replaceAll("\\D+", "");
        //            article.setCommentNum(Integer.valueOf(num));
        article.setTitle(doc.getElementById("articleTitle").text());
        //            article.setLikeNum(likeNum);
        return article;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        if (TextUtils.isEmpty(title)) {
            url = "科学人--果壳网";
        }
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        if (!TextUtils.isEmpty(id) && TextUtils.isEmpty(url)) {
            url = "http://www.guokr.com/article/" + id + "/";
        }
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthorID() {
        return authorID;
    }

    public void setAuthorID(String authorID) {
        this.authorID = authorID;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getSubjectKey() {
        return subjectKey;
    }

    public void setSubjectKey(String subjectKey) {
        this.subjectKey = subjectKey;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(int commentNum) {
        this.commentNum = commentNum;
    }

    public String getSummary() {
        if (TextUtils.isEmpty(summary)) {
            summary = getTitle();
        }
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ArrayList<UComment> getHotComments() {
        return hotComments;
    }

    public void setHotComments(ArrayList<UComment> hotComments) {
        this.hotComments = hotComments;
    }

    public ArrayList<UComment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<UComment> comments) {
        this.comments = comments;
    }

    public String getAuthorAvatarUrl() {
        return authorAvatarUrl;
    }

    public void setAuthorAvatarUrl(String authorAvatarUrl) {
        this.authorAvatarUrl = authorAvatarUrl;
    }

    public int getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(int likeNum) {
        this.likeNum = likeNum;
    }

    public boolean isDesc() {
        return desc;
    }

    public void setDesc(boolean desc) {
        this.desc = desc;
    }
}
