/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package equaliser;

/**
 * Equalise theXML markup in a folderfull of XML
 * @author desmond
 */
public class Equaliser 
{
    static boolean printLocations = false;
    static boolean printRemedies = false;
    static String patFile = null;
    static String remedyFile = null;
    static boolean readArgs( String[] args )
    {
        boolean sane = true;
        for ( int i=0;i<args.length;i++ )
        {
            if ( args[i].startsWith("-") && args[i].length()>1 )
            {
                switch ( args[i].charAt(1) )
                {
                    case 'l':
                        printLocations = true;
                        break;
                    case 'p':
                        if ( args.length-2 > i )
                            patFile = args[i+1];
                        else
                            sane = false;
                        break;
                    case 'r':
                        if ( args.length-2 > i )
                            remedyFile = args[i+1];
                        else
                            sane = false;
                        break;
                    case 'R':
                        printRemedies = true;
                        break;
                }
            }
        }
        return sane;
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if ( readArgs(args) )
        {
            Digester d = new Digester(args[args.length-1],patFile,remedyFile);
        }
        else
            System.out.println(
                "usage: java Equaliser [-l] [-p patFile] "
                +"[-r remedyFile] [-R] <folder or file>\n"
                +"       -l: list locations for each error\n"
                +"       -p patFile: specify pattern file to override default\n"
                +"       -r remedyFile: speify remedy file to override default\n"
                +"       -R: print remedies for any faults");
    }
    
}
