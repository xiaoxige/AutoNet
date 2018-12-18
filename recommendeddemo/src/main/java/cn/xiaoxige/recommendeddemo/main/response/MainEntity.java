package cn.xiaoxige.recommendeddemo.main.response;

/**
 * @author by zhuxiaoan on 2018/12/18 0018.
 */
public class MainEntity {

//    List children;

    private Integer courseId;
    private Integer id;
    private String name;
    private String order;
    private Integer parentChapterId;
    private Boolean userControlSetTop;
    private Integer visible;


    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public Integer getParentChapterId() {
        return parentChapterId;
    }

    public void setParentChapterId(Integer parentChapterId) {
        this.parentChapterId = parentChapterId;
    }

    public Boolean getUserControlSetTop() {
        return userControlSetTop;
    }

    public void setUserControlSetTop(Boolean userControlSetTop) {
        this.userControlSetTop = userControlSetTop;
    }

    public Integer getVisible() {
        return visible;
    }

    public void setVisible(Integer visible) {
        this.visible = visible;
    }
}
