/*
 * COMP603/03 Project 1, Group 6. Marina Newman 14873443 and Erin Thomas 21145466
 */

package p06_14873443_21145466;

public class PlatinumTicket extends Ticket
{
    public PlatinumTicket(int row)
    {
        super(row);
    }

    @Override
    public double calcPrice(int row)
    {
        price = (10 - row) * 25;
        
        return price;
    }
}