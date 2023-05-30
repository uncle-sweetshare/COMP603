/*
 * COMP603/03 Project 2, Group 6. Marina Newman 14873443 and Erin Thomas 21145466 THIS IS PROJECT 2 WITH THE DATABASE AND GUI. minor changes, no direct DB stuff
 */

package p06_14873443_21145466;

import java.util.ArrayList;

public class User
{
    private String name;
    private String password;
    private ArrayList<Show> showsBooked;
    
    //Constructor to create new User object with username & password
    public User(String name, String password)
    {
        this.name = name;
        this.password = password;
        this.showsBooked = new ArrayList();
    }    
    
    public String getName()
    {
        return name;
    }
    
    public String getPassword()
    {
        return password;
    }
    
    //added this
    public ArrayList<Show> getShowsBooked() {
        return showsBooked;
    }
    
    //and this - stored the booked shows in arraylist
    public void bookShow(Show show) {
        showsBooked.add(show);
    }
    
    //getting ahead of myself and adding this in case we want to add something to cancel a booking
    public void cancelBooking(Show show) {
        showsBooked.remove(show);
    }

    //toString:
    @Override
    public String toString()
    {
        return "User{" + "name=" + name + ", password=" + password + "}";
    }
}