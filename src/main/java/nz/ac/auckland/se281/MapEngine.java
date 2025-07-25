package nz.ac.auckland.se281;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

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
            country.getCountryCostString(),
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

    Country startingCountry;
    Country endingCountry;

    while (true) {
      MessageCli.INSERT_SOURCE.printMessage();
      startCountry = Utils.scanner.nextLine();
      formattedStartCountry = Utils.capitalizeFirstLetterOfEachWord(startCountry);
      try {
        // Calling the getCountry method which will return either a Country object if found or an
        // exception if not found
        startingCountry = getCountry(formattedStartCountry);
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
        // Calling the getCountry method which will return either a Country object if the entered
        // end country or an exception if not found
        endingCountry = getCountry(formattedEndCountry);
        // breaking out of the while loop once a country has successfully been found
        break;
        // Catch block in case an exception is thrown when trying to find the entered country
      } catch (NoCountryFound e) {
        // Printing the appropriate error message before repeating the while loop to ask for a
        // country
        MessageCli.INVALID_COUNTRY.printMessage(formattedEndCountry);
      }
    }

    // Checking if the start and end destinations are the same location and printing the appropriate
    // responce if they are.
    if (formattedStartCountry.equals(formattedEndCountry)) {
      MessageCli.NO_CROSSBORDER_TRAVEL.printMessage();
    } else {
      // Creating a list to store the locations found in the findShortestPath method
      List<String> shortestPath = findShortestPath(formattedStartCountry, formattedEndCountry);
      MessageCli.ROUTE_INFO.printMessage(shortestPath.toString());

      // If statement to determine whether the start and destination countries are neighbours
      if (shortestPath.size() == 2) {
        // If they are neighbours then the fuel cost will be 0
        MessageCli.FUEL_INFO.printMessage("0");
        // If statement to see if the neighbouring countries are located on the same continent
        if (startingCountry.getCountryContinent().equals(endingCountry.getCountryContinent())) {
          // If they are on the same continent then the continent info will display a singular value
          MessageCli.CONTINENT_INFO.printMessage(
              "[" + startingCountry.getCountryContinent() + " (0)]");
          MessageCli.FUEL_CONTINENT_INFO.printMessage(
              startingCountry.getCountryContinent() + " (0)");
        } else {
          // If the two neighbouring countries are on seperate continents then the continent info
          // will have to include both of the continents
          MessageCli.CONTINENT_INFO.printMessage(
              "["
                  + startingCountry.getCountryContinent()
                  + " (0), "
                  + endingCountry.getCountryContinent()
                  + " (0)]");
          MessageCli.FUEL_CONTINENT_INFO.printMessage(
              startingCountry.getCountryContinent() + " (0)");
        }
      } else if (shortestPath.size() > 2) {
        int fuelUsed = 0;
        // Creating a linked has map so the continents visited are kept in order, and both the
        // continent name and total fuel can be tracked
        Map<String, Integer> continentsVisitedMap = new LinkedHashMap<>();
        // For loop to itterate through all of the locations along the path
        for (int i = 0; i < shortestPath.size(); i++) {
          // Using try-catch to safetly get the country object for the country being visited
          try {
            // Calling the getCountry method which will return either a Country object if found or
            // an exception if not found
            Country country = getCountry(shortestPath.get(i));

            // If statements so the start and end points don't add to the fuel costs
            if (i == 0) {
              // code to add continent started in to the hash map but not add any fuel cost
              continentsVisitedMap.putIfAbsent(country.getCountryContinent(), 0);
            } else if ((i >= 1) && (i < (shortestPath.size() - 1))) {
              // System.out.println(country.getCountryName()); Code used to test
              // Adding the fuel cost of the country
              fuelUsed += country.getCountryCostInt();
              // If statement to check if the continent has already been added to the list, if it
              // has then the fuel cost is added to the total fuel cost for that continent, if not
              // then the new continent is added
              if (!continentsVisitedMap.containsKey(country.getCountryContinent())) {
                continentsVisitedMap.put(
                    country.getCountryContinent(), country.getCountryCostInt());
              } else {
                continentsVisitedMap.put(
                    country.getCountryContinent(),
                    continentsVisitedMap.get(country.getCountryContinent())
                        + country.getCountryCostInt());
              }
            } else if (i == (shortestPath.size() - 1)) {
              // code to add continent of destination if new but not adding any fuel cost
              if (!continentsVisitedMap.containsKey(country.getCountryContinent())) {
                continentsVisitedMap.putIfAbsent(country.getCountryContinent(), 0);
              }
            }
          } catch (NoCountryFound e) {
            MessageCli.INVALID_COUNTRY.printMessage(formattedEndCountry);
          }
        }
        MessageCli.FUEL_INFO.printMessage(Integer.toString(fuelUsed));

        // Creating a new list so that the info about continents and there fuel used can be printed
        // in the correct format
        List<String> formattedContinents = new ArrayList<>();

        // Creating a Map.Entry to keep track of the most expensive continent that has been
        // travelled through
        Map.Entry<String, Integer> mostExpensive = null;

        // For loop to add each continent and fuel to the list
        for (Map.Entry<String, Integer> entry : continentsVisitedMap.entrySet()) {
          // Adding the information in the correct format with spacing and parenthasis
          formattedContinents.add(entry.getKey() + " (" + entry.getValue() + ")");

          // If statements to compare the fuel cost of the current continent to the saved most
          // expensive continent
          if (mostExpensive == null) {
            // for the first continent it will enter the current one
            mostExpensive = entry;
          } else {
            // Replacing the most expensive continent if a more expensive one is found
            if (mostExpensive.getValue() < entry.getValue()) {
              mostExpensive = entry;
            }
          }
        }
        // Printing the final message with the COntinent info
        MessageCli.CONTINENT_INFO.printMessage(formattedContinents.toString());

        // Converting the Map.Entry for the most expensive continent into a string so that it can be
        // printed
        String formattedMostExpensive =
            (mostExpensive.getKey() + " (" + mostExpensive.getValue() + ")");
        // Printing the most fuel expensive continent travelled through
        MessageCli.FUEL_CONTINENT_INFO.printMessage(formattedMostExpensive);
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

  // findShortestPath method performs a Breadth-First Search to find the shortest path between the
  // start and end destinations based on the adjacencyMap
  private List<String> findShortestPath(String startPoint, String endPoint) {
    // Queue stores the path being taken to reach the destination country
    Queue<List<String>> queue = new LinkedList<>();
    // Visited set means we know not to go back to a country we have already been to, resulting in
    // an infinite loop
    Set<String> visited = new HashSet<>();

    // Creating the start of the path and marking it as the starting point
    List<String> startPath = new ArrayList<>();
    startPath.add(startPoint);
    queue.add(startPath);
    visited.add(startPoint);

    // While loop to continue to follow the paths to find the shortest route
    while (!queue.isEmpty()) {
      List<String> path = queue.poll();
      // Storing the most recent country on the path
      String current = path.get(path.size() - 1);

      // Checking if we have reached the target destination in our path
      if (current.equals(endPoint)) {
        // Returning the path if the destination is reached
        return path;
      }

      // For loop to iterate through each of the neighbouring countries at the end of the current
      // path
      for (String neighbor : adjacencyMap.get(current)) {
        // If the neighbour hasn't been visited yet then it will add it to the path list and
        // continue the search
        if (!visited.contains(neighbor)) {
          // Marking the new country as having been checked
          visited.add(neighbor);
          // Creating a new copy of the current path
          List<String> newPath = new ArrayList<>(path);
          // Adding the new country on to the end of the path
          newPath.add(neighbor);
          queue.add(newPath);
        }
      }
    }
    // If no path is found then it will return null
    return null;
  }
}
