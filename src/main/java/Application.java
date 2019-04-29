

public class Application {
    public static void main(final String[] args) {
        try {

            final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

            final DocumentBuilder builder = factory.newDocumentBuilder();

            final Document document = builder.newDocument();

            final Element allCities = document.createElement("Cities");
            document.appendChild(allCities);

            String csvFile = "src/cities.csv";
            BufferedReader br;
            String line;
            String cvsSplitBy = ",";

            br = new BufferedReader(new FileReader(csvFile));

            /* we skip the first line (header) */
            line = br.readLine();

            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] entry = line.split(cvsSplitBy);

                String latitude = "";
                String longitude = "";
                String city = entry[8];
                String state = entry[9];

                /* We treat latitude */
                for (int i = 0; i < 3; ++i) {
                    String data = entry[i];
                    String sign = "";
                    /* We check if it's the first part because we have a different length */
                    if (i == 0) {
                        /* we look if it's north or south to add the sign */
                        if (entry[3].charAt(0) == 'N') {
                            sign = "+";
                        } else if (entry[3].charAt(0) == 'S') {
                            sign = "-";
                        }

                        latitude += sign + String.format("%3s", data).replace(' ', '0') + ".";
                    } else {
                        latitude += String.format("%2s", data).replace(' ', '0');
                    }
                }

                /* we treat longitude */
                for (int i = 4; i < 7; ++i) {
                    String data = entry[i];
                    String sign = "";

                    /* We check if it's the first part because we have a different length */
                    if (i == 4) {
                        /* we look if it's east or west to add the sign */
                        if (entry[7].charAt(0) == 'E') {
                            sign = "+";
                        } else if (entry[7].charAt(0) == 'W') {
                            sign = "-";
                        }

                        longitude += sign + String.format("%3s", data).replace(' ', '0') + ".";
                    } else {
                        longitude += String.format("%2s", data).replace(' ', '0');
                    }
                }
                /* We create the comment and add it to our root tag */
                final Comment comment = document.createComment(city + "/" + state);
                allCities.appendChild(comment);

                /* We create the "City" tag and add it to root tag */
                final Element cityTag = document.createElement("City");
                allCities.appendChild(cityTag);

                /* We create tags, we write the informations about the city in it and we add
                it to the "City" tag.
                 */
                final Element nameTag = document.createElement("Name");
                nameTag.appendChild(document.createTextNode(city));
                cityTag.appendChild(nameTag);
                final Element stateTag = document.createElement("State");
                stateTag.appendChild(document.createTextNode(state));
                cityTag.appendChild(stateTag);
                final Element coordinateTag = document.createElement("Coordinates");
                coordinateTag.appendChild(document.createTextNode(latitude + "," + longitude));
                cityTag.appendChild(coordinateTag);
            }

            final TransformerFactory transformerFactory = TransformerFactory.newInstance();
            final Transformer transformer = transformerFactory.newTransformer();
            final DOMSource source = new DOMSource(document);
            final StreamResult sortie = new StreamResult(new File("src/cities_out.xml"));

            //prologue
            transformer.setOutputProperty(OutputKeys.VERSION, "1.0");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");

            //formatage
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "3");

            //sortie
            transformer.transform(source, sortie);

        } catch (final ParserConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
