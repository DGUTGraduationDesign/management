package cn.management.enums;

/**
 * 文件类型枚举类
 * @author ZhouJiaKai
 * @date 2018-04-24
 */
public enum CatalogTypeEnum {

    DIR(0, "目录"),

    DOC(1, "文档"),

    XLS(2, "表格"),

    PPT(3, "幻灯片"),

    TXT(4, "幻灯片"),

    ZIP(5, "压缩文件"),

    OTHER(6, "其它");

    private Integer value;

    private String name;

    private CatalogTypeEnum(Integer value, String name) {
        this.value = value;
        this.name = name;
    }

    public static String getName(Integer value) {
        for (BespeakStatusEnum bespeakStatusEnum : BespeakStatusEnum.values()) {
            if (bespeakStatusEnum.getValue().equals(value)) {
                return bespeakStatusEnum.getName();
            }
        }
        return null;
    }

    public Integer getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public void setName(String name) {
        this.name = name;
    }

}
