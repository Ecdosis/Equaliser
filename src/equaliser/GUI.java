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
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JPanel;
import javax.swing.JCheckBox;
import javax.swing.JButton;
import javax.swing.AbstractButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import javax.swing.BorderFactory;
import javax.swing.ScrollPaneConstants;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
/**
 *
 * @author desmond
 */
public class GUI 
{
    JFrame frame;
    JTextArea console;
    JScrollPane scroller;
    GUI()
    {
        Dimension scr = Toolkit.getDefaultToolkit().getScreenSize();
        int width = scr.width/3;
        int height = scr.height/2;
        frame = new JFrame("Equaliser");
        frame.setPreferredSize(new Dimension(width,height));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(true);
        JButton openButton = new JButton("Open...");
        openButton.setPreferredSize(new Dimension(width/4,20));
        openButton.addMouseListener(new OpenFileListener());
        JPanel bar = new JPanel();
        bar.add(new JLabel("XML file or folder:"));
        bar.add( openButton );
        JPanel middle = new JPanel();
        JCheckBox locations = new JCheckBox("list locations");
        locations.addActionListener(new ListLocationListener());
        locations.setSelected(Equaliser.printLocations);
        JCheckBox remedies = new JCheckBox("suggest remedies");
        remedies.addActionListener(new PrintRemediesListener());
        remedies.setSelected(Equaliser.printRemedies);
        middle.add( locations);
        middle.add( remedies );
        frame.add(middle,BorderLayout.CENTER);
        frame.getContentPane().add(bar, BorderLayout.NORTH);
        console = new JTextArea();
        console.setEditable(false);
        console.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        scroller = new JScrollPane();
        scroller.setViewportView(console);
        scroller.setPreferredSize(new Dimension(width,height*4/5));
        scroller.setAutoscrolls(true);
        scroller.setVerticalScrollBarPolicy(
            ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED );
        console.setPreferredSize(new Dimension(width,height*4/5));
        frame.add( scroller, BorderLayout.SOUTH);
        frame.addComponentListener(new ResizeAdapter());
        frame.pack();
        frame.setVisible(true);
    }
    class XMLFileFolderFilter extends FileFilter
    {
        public boolean 	accept(File f)
        {
            if ( f.isDirectory() )
                return true;
            else if ( f.getName().endsWith(".xml") )
                return true;
            else
                return false;
        }
        public String getDescription()
        {
            return "select XML files and folders";
        }
    }
    class OpenFileListener extends MouseAdapter
    {
        public void mouseClicked(MouseEvent e) 
        {
            JFileChooser chooser = new JFileChooser();
            XMLFileFolderFilter filter = new XMLFileFolderFilter();
            chooser.setFileFilter(filter);
            chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            int returnVal = chooser.showOpenDialog(frame);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File f = chooser.getSelectedFile();
                if ( f.isDirectory() )
                    console.append("Directory "+f.getName()+":\n");
                else
                    console.append("File "+f.getName()+":\n");
                Digester d = new Digester(
                    f.getName(),
                    Equaliser.patFile,Equaliser.remedyFile,
                    console);
            }
        }
    }
    class ListLocationListener implements ActionListener
    {
        ListLocationListener()
        {
        }
        public void actionPerformed(ActionEvent actionEvent) 
        {
            AbstractButton abstractButton = (AbstractButton) actionEvent.getSource();
            boolean selected = abstractButton.getModel().isSelected();
            Equaliser.printLocations = selected;
        }
    }
    class PrintRemediesListener implements ActionListener
    {
        PrintRemediesListener()
        {
        }
        public void actionPerformed(ActionEvent actionEvent) 
        {
            AbstractButton abstractButton = (AbstractButton) actionEvent.getSource();
            boolean selected = abstractButton.getModel().isSelected();
            Equaliser.printRemedies = selected;
        }
    }
    class ResizeAdapter extends ComponentAdapter
    {
        public void componentResized(ComponentEvent e)
        {
            if ( scroller != null )
            {
                scroller.setPreferredSize(new Dimension(frame.getWidth(),
                    frame.getHeight()*4/5));
            }
            if ( console != null )
            {
                console.setPreferredSize(new Dimension(frame.getWidth(),
                    frame.getHeight()*4/5));
            }
        }
    }
}
