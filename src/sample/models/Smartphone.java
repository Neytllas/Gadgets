package sample.models;

public class Smartphone extends Gadget
{
    public enum Type {android, ios, windows;} // какие типы
    public Type type; // тип смартфона
    public Boolean withSimSlot; // количество слотов под sim
    public int batteryPower; // мощность батереи

    public Smartphone(int displaySize, String title, Type type, int batteryPower ,Boolean withSimSlot)
    {
        super(displaySize, title);
        this.type = type;
        this.withSimSlot = withSimSlot;
        this.setBatteryPower(batteryPower);
    }

    private void setBatteryPower (int batteryPower)
    {
        this.batteryPower = batteryPower;
    }

    public int getBatteryPower()
    {
        return batteryPower;
    }

    @Override
    public  String getDescription()
    {
        String typeSring = "";
        switch (this.type)
        {
            case android:
                typeSring = "android";
                break;
            case ios:
                typeSring = "ios";
                break;
            case windows:
                typeSring = "windows";
                break;
        }
        return String.format("Смартфон %s", typeSring);
    }
}
