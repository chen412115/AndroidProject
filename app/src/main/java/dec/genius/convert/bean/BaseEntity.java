package dec.genius.convert.bean;
import java.io.Serializable;

/**
 * 实体类基类
 * json格式的字符串和实体类相互转换
 * @author wfs
 *
 */
public class BaseEntity implements Serializable {
    private Integer status;
    private String message;
    private String result;
    private Integer page;//当前页
    private Integer count_page;//页数
    private Integer limit;//每页条数

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getCount_page() {
        return count_page;
    }

    public void setCount_page(Integer count_page) {
        this.count_page = count_page;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
