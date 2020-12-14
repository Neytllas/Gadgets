package sample.models;

public class Notebook extends Gadget
{
    public Boolean withKeyBacklight; // подсветка клавиатуры

    public int numberOfCores; // количество ядер

    public int hardDiskSpace; // объем жесткого диска

    public Notebook(int displaySize, String title, Boolean withKeyBacklight, int numberOfCores, int hardDiskSpace)
    {
        super(displaySize, title);
        this.withKeyBacklight = withKeyBacklight;
        this.setHardDiskSpace(hardDiskSpace);
        this.setNumberOfCores(numberOfCores);
    }

    private void setNumberOfCores (int numberOfCores)
    {
        this.numberOfCores = numberOfCores;
    }

    public int getNumberOfCores()
    {
        return numberOfCores;
    }

    private void setHardDiskSpace (int hardDiskSpace)
    {
        this.hardDiskSpace = hardDiskSpace;
    }

    public int getHardDiskSpace()
    {
        return hardDiskSpace;
    }

    @Override
    public String getDescription()
    {
        String isKeyBacklightString = this.withKeyBacklight ? " клавиатура с подсветкой " : " клавиатура без подсветки ";
        return String.format("Ноутбук %s" , isKeyBacklightString);
    }


}
