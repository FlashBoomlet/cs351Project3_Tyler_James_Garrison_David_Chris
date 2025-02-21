package IO.XMLparsers;

import model.AtomicRegion;
import model.MapPoint;
import model.Region;
import org.xml.sax.*;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author david
 *         created: 2015-01-29
 *         <p/>
 *         description:
 */
public class KMLParser extends RegionParserHandler
{
  /*
}
  private List<Region> regions;

  private String regName;
  private String flagLocation;
  private String cleanCoordString;

  private boolean nameTag;
  private boolean flagTag;
  private boolean coordTag;
  
  
  @Override
  public List<Region> getRegionList()
  {
    return regions;
  }

  @Override
  public void startDocument() throws SAXException
  {
    regions = new ArrayList<>();
  }

  @Override
  public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException
  {
    switch(qName)
    {
      case "name":
        nameTag = true;
        break;
      case "Icon":
        flagTag = true;
        break;
      case "coordinates":
        coordTag = true;
        cleanCoordString = "";
        break;
    }
  }

  @Override
  public void characters(char[] ch, int start, int length) throws SAXException
  {
    if(nameTag)
    {
      regName = new String(ch, start, length);
    }
    if(flagTag)
    {
      // Start 1 forward and 1 back to avoid quotes
      if( ch[0] == '\"') flagLocation = new String(ch, start+1, length-2);
    }
    if(coordTag)
    {
      cleanCoordString += new String(ch, start, length);
    }
  }

  @Override
  public void endElement(String uri, String localName, String qName) throws SAXException
  {
    switch (qName)
    {
      case "name":
        nameTag = false;
        break;
      case "Icon":
        flagTag = false;
        break;
      case "coordinates":
        coordTag = false;
        Region r = new AtomicRegion();
        r.setName(regName);
        r.setFlag(flagLocation);
        r.setPerimeter(parseCoordString());
        regions.add(r);
    }
  }
  
  private List<MapPoint> parseCoordString()
  {
    List <MapPoint> l = new ArrayList<>();
    
    for(String s : cleanCoordString.trim().split("\\s+"))
    {
      String nums[] = s.split(",");
      double lon = Double.parseDouble(nums[0]);
      double lat = Double.parseDouble(nums[1]);
      l.add(new MapPoint(lat, lon));
    }
    return l;
  }

  public static Collection<Region> getRegionsFromFile(String filePath)
  {
    try
    {
      XMLReader kmlReader = 
              SAXParserFactory.newInstance().newSAXParser().getXMLReader();
      KMLParser kmlParser = new KMLParser();
      kmlReader.setContentHandler(kmlParser);
      kmlReader.parse(new InputSource(new FileInputStream(filePath)));
      return kmlParser.getRegionList();
      
    } 
    /* I had no idea this was legal.
     * Bad form, but works for proof of kml parsing *  */


/*
  catch (SAXException | ParserConfigurationException | IOException e)
    {
      e.printStackTrace();
    }
    return null;
  }
*/
}
