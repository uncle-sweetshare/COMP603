/*
 * COMP603/03 Project 1, Group 6. Marina Newman 14873443 and Erin Thomas 21145466
 */

package p06_14873443_21145466;

public class StandardTicket extends Ticket
{ 
    public StandardTicket(int row)
    {
        super(row);
    }

    @Override
    public double calcPrice(int row)
    {
        price = 120.00;
        
        return price;
    }   
}