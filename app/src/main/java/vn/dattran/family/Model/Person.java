package vn.dattran.family.Model;

import com.google.gson.annotations.SerializedName;

public class Person {
    @SerializedName("name")
    private String name;
    @SerializedName("id")
    private int id;
    @SerializedName("parent")
    private int parent;
    @SerializedName("family")
    private int family;
    @SerializedName("level")
    private int level;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParent() {
        return parent;
    }

    public void setParent(int parent) {
        this.parent = parent;
    }

    public int getFamily() {
        return family;
    }

    public void setFamily(int family) {
        this.family = family;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public String toString() {
        return "name=" + name + ", parent=" + parent + ", family=" + family + ", level=" + level;
    }

}
