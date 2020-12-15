package sample.models;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class GadgetModel
{
    Class<? extends Gadget> gadgetFilter = Gadget.class;
    ArrayList<Gadget> gadgetList = new ArrayList<>();
    private int counter = 1; // счетчик

    //функциональный интерфейс, общение между моделью и контроллером
    public interface DataChangedListener
    {
        void dataChanged(ArrayList<Gadget> gadgetList);
    }

    public void  saveToFile(String path)
    {
        try (Writer writer = new FileWriter(path))
        {
            ObjectMapper mapper = new ObjectMapper();
            mapper.writerFor(new TypeReference<ArrayList<Gadget>>(){})
                    .withDefaultPrettyPrinter()
                    .writeValue(writer, gadgetList);
        }

        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private ArrayList<DataChangedListener> dataChangedListener = new ArrayList<>();
    public void  addDataChangedListener(DataChangedListener listener)
    {
        this.dataChangedListener.add(listener);
    }

    public void add(Gadget gadget, boolean emit)
    {
        gadget.id = counter;
        counter += 1;

        this.gadgetList.add(gadget);

        if (emit)
        {
            this.emitDataChanged();
        }

    }

    public void add(Gadget gadget)
    {
        this.gadgetList.add(gadget);

        for (DataChangedListener listener: dataChangedListener)
        {
            listener.dataChanged(gadgetList);
        }
    }

    // метод иммитации загрузки данных
    public void load()
    {
        gadgetList.clear();

        this.add(new Tablet(250,"Lenovo Tab4",true, 255), false);
        this.add(new Notebook(650, "Lenovo", false, 64,2000), false);
        this.add(new Smartphone(120, "Lenovo", Smartphone.Type.android, 200, false), false);

        this.emitDataChanged();
    }

    public void delete(int id)
    {
        for (int i = 0; i < this.gadgetList.size(); ++i)
        {
            if (this.gadgetList.get(i).id == id)
            {
                this.gadgetList.remove(i);
                break;
            }
        }

        this.emitDataChanged();
    }

    private void emitDataChanged()
    {
        for (DataChangedListener listener: dataChangedListener)
        {
            ArrayList<Gadget> filteredList = new ArrayList<>(
                    gadgetList.stream()
                              .filter(gadget -> gadgetFilter.isInstance(gadget))
                              .collect(Collectors.toList())
            );
            listener.dataChanged(filteredList);
        }
    }

    public  void edit (Gadget gadget)
    {
        for (int i = 0; i < this.gadgetList.size(); ++i)
        {
            if (this.gadgetList.get(i).id == gadget.id)
            {
                this.gadgetList.set(i, gadget);
                break;
            }
        }

        this.emitDataChanged();
    }

    public void loadFromFile (String path)
    {
        try (Reader reader = new FileReader(path))
        {
            // создаем сериализатор
            ObjectMapper mapper = new ObjectMapper();

            // читаем из файла
            gadgetList = mapper.readerFor(new TypeReference<ArrayList<Gadget>>() { })
                    .readValue(reader);

            this.counter = gadgetList.stream()
                    .map(gadget -> gadget.id)
                    .max(Integer::compareTo)
                    .orElse(0) + 1;
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        this.emitDataChanged();
    }

    public void setGadgetFilter(Class<? extends Gadget> gadgetFilter)
    {
        this.gadgetFilter = gadgetFilter;

        this.emitDataChanged();
    }
}
