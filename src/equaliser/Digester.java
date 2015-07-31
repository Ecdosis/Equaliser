/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package equaliser;
import java.io.FileInputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Set;
import java.util.Iterator;
/**
 * This class manages the digesting of XML files to determine which 
 * elements and attributes are present and to compare them to a set 
 * of default templates, for mapping to MMLs
 * @author desmond
 */
public class Digester 
{
    HashMap<String,Pattern> matches;
    HashMap<String,Remedy[]> remedies;
    HashMap<String,Error[]> errors;
    /**
     * Read the patterns form a file
     * @param patFile the path to the patterns
     * @return an array of pattern strings
     */
    String[] readPatFile( String patFile )
    {
        if ( patFile==null )
            return Pattern.defaultSet;
        else
        {
            File src = new File( patFile );
            if ( !src.exists() )
                return Pattern.defaultSet;
            else
            {
                String spec = "";
                try
                {
                    FileInputStream fis = new FileInputStream(src);
                    byte[] data = new byte[(int)src.length()];
                    fis.read( data );
                    spec = new String( data, "UTF-8" );
                }
                catch ( Exception e )
                {
                    e.printStackTrace(System.out);
                }
                return spec.split("\n");
            }
        }
    }
    /**
     * Read the patterns form a file
     * @param patFile the path to the patterns
     * @return an array of remedy definitions
     */
    String[] readRemedyFile( String remedyFile )
    {
        if ( remedyFile==null )
            return Remedy.defaultSet;
        else
        {
            File src = new File( remedyFile );
            if ( !src.exists() )
                return Remedy.defaultSet;
            else
            {
                String spec = "";
                try
                {
                    FileInputStream fis = new FileInputStream(src);
                    byte[] data = new byte[(int)src.length()];
                    fis.read( data );
                    spec = new String( data, "UTF-8" );
                }
                catch ( Exception e )
                {
                    e.printStackTrace(System.out);
                }
                return spec.split("\n");
            }
        }
    }
    /**
     * Create a digester 
     * @param path 
     */
    public Digester( String path, String patFile, String remedyFile )
    {
        File f = new File(path);
        this.matches = new HashMap<String,Pattern>();
        this.remedies = new HashMap<String,Remedy[]>();
        String[] pats = readPatFile(patFile);
        String[] rems = readRemedyFile(remedyFile);
        for ( int i=0;i<pats.length;i++ )
        {
            Pattern pat = new Pattern(pats[i]);
            this.matches.put( pat.getElement(), pat );
        }
        for ( int i=0;i<rems.length;i++ )
        {
            Remedy r = new Remedy(rems[i] );
            Remedy[] list = remedies.get( r.elemName );
            if ( list == null )
            {
                Remedy[] l = new Remedy[1];
                l[0] = r;
                remedies.put( r.elemName, l );
            }
            else
            {
                Remedy[] l = new Remedy[list.length+1];
                for ( int j=0;j<list.length;j++ )
                    l[j] = list[j];
                l[list.length] = r;
                remedies.put( r.elemName, l );
            }
        }
        this.errors = new HashMap<String,Error[]>();
        if ( f.isDirectory() )
            digestDir(f);
        else
            digestFile(f.getPath());
        printFound();
    }
    final void digestDir( File dir )
    {
        File[] files = dir.listFiles();
        for ( int i=0;i<files.length;i++ )
        {
            if ( files[i].isFile() && files[i].getName().endsWith(".xml") )
                digestFile(files[i].getPath());
            else if ( files[i].isDirectory() )
                digestDir( files[i] );
        }
    }
    final void digestFile( String file )
    {
        try
        {
            SaxParser sp = new SaxParser( file );
            sp.digest(this.matches,this.errors);
        }
        catch ( Exception e )
        {
            e.printStackTrace(System.out);
        }
    }
    Remedy findRemedy( String name, Error err )
    {
        Set<String> keys = remedies.keySet();
        Iterator<String> iter = keys.iterator();
        while ( iter.hasNext() )
        {
            String key = iter.next();
            Remedy[] rems = remedies.get(key);
            for ( int i=0;i<rems.length;i++ )
            {
                if ( rems[i].match(name,err.attrs) )
                    return rems[i];
            }
        }
        return null;
    }
    void printFound()
    {
        if ( errors.size() > 0 )
        {
            System.out.println("Pattern errors:");
            Set<String> keys = errors.keySet();
            Iterator<String> iter = keys.iterator();
            while ( iter.hasNext() )
            {
                String key = iter.next();
                Error[] list = errors.get(key);
                for ( int i=0;i<list.length;i++ )
                {
                    if ( i==0 )
                    {
                        System.out.print(key+" ");
                        System.out.println(list[i].toString());
                    }
                    else if ( list[i].numAttrs()>0 )
                    {
                        for ( int j=0;j<key.length();j++ )
                            System.out.print(" ");
                        System.out.print(" ");
                        System.out.println( list[i].toString());
                    }
                    else
                    {
                        for ( int j=0;j<key.length();j++ )
                            System.out.print(" ");
                        System.out.println("[no attrs]");
                    }
                    if ( Equaliser.printLocations )
                    {
                        Location[] locs = list[i].getLocations();
                        for ( int k=0;k<locs.length;k++ )
                        {
                            System.out.print("          ");
                            System.out.println(locs[k].toString());
                        }
                    }
                }
                if ( list.length==0 )
                    System.out.println(key);
            }
            if ( errors.size()>0 && Equaliser.printRemedies )
            {
                System.out.println("Suggested remedies:");
                keys = errors.keySet();
                iter = keys.iterator();
                while ( iter.hasNext() )
                {
                    String key = iter.next();
                    Error[] errs = errors.get(key);
                    for ( int i=0;i<errs.length;i++ )
                    {
                        Remedy r = findRemedy( key, errs[i] );
                        String err = errs[i].toString().trim();
                        if ( r != null )
                        {
                            if ( err.length()==0 )
                                System.out.println(key+": "+r.getMessage());
                            else
                                System.out.println(key+" "+err+": "+r.getMessage());
                        }
                        else if ( err.length()==0 )
                            System.out.println(key+": ask");
                        else
                            System.out.println(key+" "+err+": ask");
                    }
                }
            }
        }
        else
            System.out.println("No errors found!");
    }
}
