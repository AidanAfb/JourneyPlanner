package nz.ac.auckland.se281;

import java.util.List;

/** This class is the main entry point. */
public class MapEngine {

  public MapEngine() {
    // add other code here if you wan
    loadMap(); // keep this mehtod invocation
  }

  /** invoked one time only when constracting the MapEngine class. */
  private void loadMap() {

    List<String> countries = Utils.readCountries();
    List<String> adjacencies = Utils.readAdjacencies();

    // Adding the countries from the countries.csv into the ArrayList with their correct information
    // For loop to itterate through each line in the countries List
    for (String line : countries) {
      // Seperating the info on the line by the commas
      String[] info = line.split(",");
      String countryName = info[0];
      String countryContinent = info[1];
      int countryCost = Integer.parseInt(info[2]);
      // Creating a country instance from the country info
      Country country = new Country(countryName, countryContinent, countryCost);
    }
  }

  /** this method is invoked when the user run the command info-country. */
  public void showInfoCountry() {}

  /** this method is invoked when the user run the command route. */
  public void showRoute() {}
}
