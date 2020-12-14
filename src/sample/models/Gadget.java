package sample.models;

public class Gadget
{
    public int displaySize; // размер дисплея
    public String title; // название

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
