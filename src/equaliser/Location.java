/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package equaliser;

/**
 * Represent the location of an error in a file
 * @author desmond
 */
public class Location {
    String file;
    int lineNo;
    Location( String file, int lineNo )
    {
        this.file = file;
        this.lineNo = lineNo;
    }
    public String toString()
    {
        return file+", line "+lineNo;
    }
}
