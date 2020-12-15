package sample.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
@JsonIgnoreProperties({"description"})

@JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, include= JsonTypeInfo.As.PROPERTY, property="@class")
public class Gadget
{
    public Gadget () {};

    public int displaySize; // размер дисплея
    public String title; // название
    public Integer id = null; // идентификатор

    public Gadget(int display_size, String title)
    {
        this.setDisplaySize(display_size);
        this.setTitle(title);
    }

    @Override
    public String toString()
    {
        return String.format("%s: %s размер дисплея", this.getTitle(), this.getDisplaySize());
    }

    public int getDisplaySize()
    {
        return displaySize;
    }

    public void setDisplaySize(int display_size)
    {
        this.displaySize = display_size;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getDescription()
    {
        return "";
    }
}
