/*
 * COMP603/03 Project 1, Group 6. Marina Newman 14873443 and Erin Thomas 21145466
 */

package p06_14873443_21145466;

abstract class Ticket {
    
    double price;
    private int row;
        
    public Ticket(int row)
    {
        price = 0.00;
        this.row = row;
    }
    
    /* CalcPrice method:
     * Calculates the price for a ticket depending on seat location. Different prices
     * are categorised as Platinum, Gold or Standard tickets.
    */
    abstract public double calcPrice(int row);
    
    public String toString()
    {
        return ""+this.price;
    }
}