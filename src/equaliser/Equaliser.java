/*
 * This file is part of Equaliser.
 *
 *  Equaliser is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  Equaliser is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Equaliser.  If not, see <http://www.gnu.org/licenses/>.
 *  (c) copyright Desmond Schmidt 2015
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
    static boolean createGUI = false;
    static boolean readArgs( String[] args )
    {
        boolean sane = args.length>0;
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
                    case 'g':
                        createGUI = true;
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
            GUI g;
            Digester d;
            if  ( createGUI )
                g = new GUI();
            else
                d = new Digester(args[args.length-1],patFile,remedyFile,null);
        }
        else
            System.out.println(
                "usage: java -jar Equaliser.jar [-l] [-p patFile] "
                +"[-r remedyFile] [-R] <xml-folder or xml-file>\n"
                +"       -l: list locations for each error\n"
                +"       -R: print remedy for each error\n"
                +"       -p patFile: specify pattern file to override default\n"
                +"       -r remedyFile: specify remedy file to override default"
                );
    }
    
}
