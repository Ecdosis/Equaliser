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
import java.io.File;
import java.util.HashMap; 
import javax.xml.parsers.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;
import java.io.PrintStream;

/**
 *
 * @author desmond
 */
public class SaxParser extends DefaultHandler
{
    SAXParser parser;
    XMLReader xmlReader;
    String file;
    int lineNo;
    HashMap<String,Pattern> map;
    HashMap<String,Error[]> found;
    static int MJS = 0;
    SaxParser( String file ) throws Exception
    {
        this.file = file;
        this.lineNo = 1;
        try
        {
            SAXParserFactory spf = SAXParserFactory.newInstance();
            //spf.;
            spf.setNamespaceAware(true);
            parser = spf.newSAXParser();
            xmlReader = parser.getXMLReader();
            xmlReader.setContentHandler(this);
            xmlReader.setErrorHandler(new MyErrorHandler(System.err));
        }
        catch ( Exception e )
        {
            throw new Exception(e);
        }
    }
    /**
     * Digest a single XML file
     * @param map a map of XML templates
     * @param found the map of found element/attribute patterns
     */
    void digest( HashMap<String,Pattern> map, 
        HashMap<String,Error[]> found ) throws Exception
    {
        this.map = map;
        this.found = found;
        xmlReader.parse(convertToFileURL(file));     
    }
    private static String convertToFileURL(String filename) 
    {
        String path = new File(filename).getAbsolutePath();
        path = path.replaceAll("%","%25");
        path = path.replaceAll("#","%23");
        if (File.separatorChar != '/')
            path = path.replace(File.separatorChar, '/');
        if (!path.startsWith("/"))
            path = "/" + path;
        return "file:" + path;
    }
    public void startElement(String namespaceURI, String localName,
        String qName, Attributes atts) throws SAXException 
    {
        boolean matched = false;
        Pattern pat = this.map.get(localName);
        if ( pat != null )
        {
            if ( pat.matches(atts) )
            {
                pat.inc();
                matched = true;
            }
        }
        if ( !matched )
        {
            if ( found.containsKey(localName) )
            {
                Error[] list = found.get(localName);
                boolean present = false;
                for ( int i=0;i<list.length;i++ )
                {
                    if ( list[i].equalsAtts(atts) )
                    {
                        present = true;
                        list[i].addLocation(new Location(file,lineNo));
                        break;
                    }
                }
                if ( !present )
                {
                    Error[] newList = new Error[list.length+1];
                    for ( int i=0;i<list.length;i++ )
                        newList[i] = list[i];
                    newList[list.length] = new Error(atts,
                        new Location(file,lineNo));
                    found.put( localName, newList );
                }
            }
            else
            {
                Error err = new Error(atts,new Location(this.file,this.lineNo));
                Error[] list = new Error[1];
                list[0] = err;
                found.put( localName, list );
            }
        }
    }
    public void characters(char[] ch, int start, int length) {
        int end = start+length;
        for ( int i=start;i<end;i++ )
            if ( ch[i] == '\n' )
                this.lineNo++;
    }
    public void ignorableWhitespace(char[] ch, int start, int length) {
        int end = start+length;
        for ( int i=start;i<end;i++ )
            if ( ch[i] == '\n' )
                this.lineNo++;
    }
     private static class MyErrorHandler implements ErrorHandler 
    {
        private PrintStream out;

        MyErrorHandler(PrintStream out) 
        {
            this.out = out;
        }

        private String getParseExceptionInfo(SAXParseException spe) {
            String systemId = spe.getSystemId();

            if (systemId == null) {
                systemId = "null";
            }

            String info = "URI=" + systemId + " Line=" 
                + spe.getLineNumber() + ": " + spe.getMessage();

            return info;
        }
        public void warning(SAXParseException spe) throws SAXException {
            out.println("Warning: " + getParseExceptionInfo(spe));
        }
        public void error(SAXParseException spe) throws SAXException {
            String message = "Error: " + getParseExceptionInfo(spe);
            throw new SAXException(message);
        }
        public void fatalError(SAXParseException spe) throws SAXException {
            String message = "Fatal Error: " + getParseExceptionInfo(spe);
            throw new SAXException(message);
        }
    }
}
