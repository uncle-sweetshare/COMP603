/*
 * COMP603/03 Project 2, Group 6. Marina Newman 14873443 and Erin Thomas 21145466 THIS IS PROJECT 2 WITH THE DATABASE AND GUI. only changed the constructor
 */

package p06_14873443_21145466;

public class Show
{
    private String name;
    private String summary;
    private double price;
    private String[] dates;
    
    //removed 'void' from constructor
    public Show()
    {
        String[] dates = new String[4]; //4 possible dates per show
    }
    
    //Getters & setters below:
    public String getName()
    {
        return this.name;
    }

    public String getSummary()
    {
        return this.summary;
    }

    public double getPrice()
    {
        return this.price;
    }

    public String[] getDates()
    {
        return dates;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setSummary(String summary)
    {
        this.summary = summary;
    }

    public void setPrice(double price)
    {
        this.price = price;
    }

    public void setDates(String[] dates)
    {
        this.dates = dates;
    }
    
    //toString:
    @Override
    public String toString()
    {
        return this.name;
    }
}