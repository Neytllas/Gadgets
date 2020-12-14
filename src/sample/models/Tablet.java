package sample.models;

public class Tablet extends Gadget
{
    public Boolean camera; // наличие камеры
    public int dpi; // dpi экрана

    public Tablet(int displaySize, String title, Boolean camera, int dpi)
    {
        super(displaySize, title);
        this.camera = camera;
        this.setDPI(dpi);
    }

    private void setDPI (int dpi)
    {
        this.dpi = dpi;
    }

    public int getDPI()
    {
        return dpi;
    }

    @Override
    public String getDescription()
    {
        String isCameraString = this.camera ? " с камерой" : "без камеры";
        return String.format("Планшет %s" , isCameraString);
    }
}
