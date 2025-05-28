package nz.ac.auckland.se281;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** This class is the main entry point. */
public class MapEngine {

  private boolean validCountryInput = false;

  private Map<String, Country> countryMap = new HashMap<>();
  private Map<String, List<String>> adjacencyMap = new HashMap<>();

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

      // Adding the current country to the countryMap, with the string name as the key and the
      // Country object as the value
      countryMap.put(countryName, country);
      // Creating a new empty list for the adjacent countries, with the country name as the key and
      // the list of neighbours as the value
      adjacencyMap.put(countryName, new ArrayList<>());
    }

    // For loop to ittereate through each line in the adjacencies.csv file
    for (String line : adjacencies) {
      // Splitting the information on that line by the commas
      String[] adjacentCountries = line.split(",");
      // The first element in the list is the name of the country we are looking at
      String country = adjacentCountries[0];
      // Converting the list with all the neighbouring countries and the one we are looking at and
      // changing it to just a list of the neighbours
      List<String> neighbouringCountries =
          Arrays.asList(Arrays.copyOfRange(adjacentCountries, 1, adjacentCountries.length));
      // Adding all of the neighbours for the given country to the adjacencyMap
      adjacencyMap.get(country).addAll(neighbouringCountries);
    }
  }

  /** this method is invoked when the user run the command info-country. */
  public void showInfoCountry() {
    // While loop to make continue to prompt the user if they enter a county no in the list
    while (!validCountryInput) {
      String wantedCountry;
      String formattedCountry;
      // Printing the relevant message to ask the user for the country they want information about
      MessageCli.INSERT_COUNTRY.printMessage();

      // Using scanner to get the users input and saving it to the relevant variable
      wantedCountry = Utils.scanner.nextLine();
      // Trimming the users input and ensuring th efirst letter is capitalised
      formattedCountry = Utils.capitalizeFirstLetterOfEachWord(wantedCountry.trim());

      // try-catch block in order to find the country the user has entered and responding
      // appropriately depending on if it is found or not
      try {
        // Calling the getCountry method which will return either a Country object if found or an
        // exception if not found
        Country country = getCountry(formattedCountry);
        // Getting the list of neighbouring countries from the adjacencyMap for the entered country
        List<String> neightbouringCountries = adjacencyMap.get(formattedCountry);
        // If the country is successfully found then it will print an appropriate success message,
        // with the country name, continent, fuel cost, and list of neighbours
        MessageCli.COUNTRY_INFO.printMessage(
            country.getCountryName(),
            country.getCountryContinent(),
            country.getCountryCost(),
            neightbouringCountries.toString());
        // breaking out of the while loop once a country has successfully been found
        break;
        // Catch block in case an exception is thrown when trying to find the entered country
      } catch (NoCountryFound e) {
        // Printing the appropriate error message before repeating the while loop to ask for a
        // country
        MessageCli.INVALID_COUNTRY.printMessage(formattedCountry);
      }
    }
  }

  /** this method is invoked when the user run the command route. */
  public void showRoute() {
    String startCountry;
    String formattedStartCountry;
    String endCountry;
    String formattedEndCountry;
    while (true) {
      MessageCli.INSERT_SOURCE.printMessage();
      startCountry = Utils.scanner.nextLine();
      formattedStartCountry = Utils.capitalizeFirstLetterOfEachWord(startCountry);
      try {
        // Calling the getCountry method which will return either a Country object if found or an
        // exception if not found
        Country country = getCountry(formattedStartCountry);
        // Getting the list of neighbouring countries from the adjacencyMap for the entered country
        List<String> neightbouringCountries = adjacencyMap.get(formattedStartCountry);
        // If the country is successfully found then it will print the starting country
        System.out.println(formattedStartCountry);
        // breaking out of the while loop once a country has successfully been found
        break;
        // Catch block in case an exception is thrown when trying to find the entered country
      } catch (NoCountryFound e) {
        // Printing the appropriate error message before repeating the while loop to ask for a
        // country
        MessageCli.INVALID_COUNTRY.printMessage(formattedStartCountry);
      }
    }
    while (true) {
      MessageCli.INSERT_DESTINATION.printMessage();
      endCountry = Utils.scanner.nextLine();
      formattedEndCountry = Utils.capitalizeFirstLetterOfEachWord(endCountry);
      try {
        // Calling the getCountry method which will return either a Country object if found or an
        // exception if not found
        Country country = getCountry(formattedEndCountry);
        // Getting the list of neighbouring countries from the adjacencyMap for the entered country
        List<String> neightbouringCountries = adjacencyMap.get(formattedEndCountry);
        // If the country is successfully found then it will print the end country
        System.out.println(formattedEndCountry);
        // breaking out of the while loop once a country has successfully been found
        break;
        // Catch block in case an exception is thrown when trying to find the entered country
      } catch (NoCountryFound e) {
        // Printing the appropriate error message before repeating the while loop to ask for a
        // country
        MessageCli.INVALID_COUNTRY.printMessage(formattedEndCountry);
      }
    }
  }

  // getCountry method used to try and get the Country object from the users input, while
  // accomidating for exceptions
  private Country getCountry(String countryWanted) throws NoCountryFound {
    // Checking if the country we are looking for is found on the countryMap
    if (!countryMap.containsKey(countryWanted)) {
      // If no matching country is found then throwing an exception
      throw new NoCountryFound(countryWanted);
    }
    // If the country if found then returning the Country object
    return countryMap.get(countryWanted);
  }
}
