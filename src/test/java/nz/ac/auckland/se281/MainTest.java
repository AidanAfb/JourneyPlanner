package nz.ac.auckland.se281;

import static nz.ac.auckland.se281.Main.Command.*;
import static nz.ac.auckland.se281.MessageCli.*;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({MainTest.Task1.class, MainTest.Task2.class})
public class MainTest {

  @FixMethodOrder(MethodSorters.NAME_ASCENDING)
  public static class Task1 extends SysCliTest {

    public Task1() {
      super(Main.class);
    }

    @Test
    public void T1_01_info_india() throws Exception {
      runCommands(INFO_COUNTRY, "India");
      assertContains(
          COUNTRY_INFO.getMessage("India", "Asia", "3", "[Middle East, Afghanistan, China, Siam]"));
    }

    @Test
    public void T1_02_info_invalid_then_brazil() throws Exception {
      runCommands(INFO_COUNTRY, "hello", "Brazil");
      assertContains(INVALID_COUNTRY.getMessage("Hello"));
      assertContains(
          COUNTRY_INFO.getMessage(
              "Brazil", "South America", "2", "[Venezuela, North Africa, Argentina, Peru]"));
    }

    @Test
    public void T1_03_info_case_sensitive_irkutsk() throws Exception {
      runCommands(INFO_COUNTRY, "irkuTsK", "irkutsk");
      assertDoesNotContain(INVALID_COUNTRY.getMessage("irkuTsK"));
      assertContains(INVALID_COUNTRY.getMessage("IrkuTsK"));
      assertContains(
          COUNTRY_INFO.getMessage(
              "Irkutsk", "Asia", "4", "[Siberia, Yakutsk, Kamchatka, Mongolia]"));
    }

    @Test
    public void T1_04_info_multiple_invalid_then_alaska() throws Exception {
      runCommands(INFO_COUNTRY, "h", "w", "j", "Alaska");
      assertContains(INVALID_COUNTRY.getMessage("H"));
      assertContains(INVALID_COUNTRY.getMessage("W"));
      assertContains(INVALID_COUNTRY.getMessage("J"));
      assertContains(
          COUNTRY_INFO.getMessage(
              "Alaska", "North America", "1", "[Alberta, Northwest Territory, Kamchatka]"));
    }

    @Test
    public void T1_05_info_congo_and_siam() throws Exception {
      runCommands(INFO_COUNTRY, "Congo", INFO_COUNTRY, "Siam");
      assertContains(
          COUNTRY_INFO.getMessage(
              "Congo", "Africa", "1", "[North Africa, East Africa, South Africa]"));
      assertContains(COUNTRY_INFO.getMessage("Siam", "Asia", "9", "[India, China, Indonesia]"));
    }

    @Test
    public void T1_06_info_newguinea_siam_madagascar() throws Exception {
      runCommands(INFO_COUNTRY, "New Guinea", INFO_COUNTRY, "Siam", INFO_COUNTRY, "Madagascar");
      assertContains(
          COUNTRY_INFO.getMessage(
              "New Guinea", "Australia", "2", "[Indonesia, Eastern Australia, Western Australia]"));
      assertContains(COUNTRY_INFO.getMessage("Siam", "Asia", "9", "[India, China, Indonesia]"));
      assertContains(
          COUNTRY_INFO.getMessage("Madagascar", "Africa", "4", "[East Africa, South Africa]"));
    }
  }

  @FixMethodOrder(MethodSorters.NAME_ASCENDING)
  public static class Task2 extends SysCliTest {

    public Task2() {
      super(Main.class);
    }

    @Test
    public void T2_01_no_crossborder_same_country_alberta() throws Exception {
      runCommands(ROUTE, "Alberta", "Alberta");
      assertContains(NO_CROSSBORDER_TRAVEL.getMessage());
      assertDoesNotContain("The fastest route is: ");
    }

    @Test
    public void T2_02_no_crossborder_same_country_egypt() throws Exception {
      runCommands(ROUTE, "Egypt", "Egypt");
      assertContains(NO_CROSSBORDER_TRAVEL.getMessage());
      assertDoesNotContain("You will spend this amount of fuel for your journey");
    }

    @Test
    public void T2_03_route_direct_india_siam() throws Exception {
      runCommands(ROUTE, "India", "Siam");
      assertContains(ROUTE_INFO.getMessage("[India, Siam]"));
    }

    @Test
    public void T2_04_route_invalid_then_valid_start_country() throws Exception {
      runCommands(ROUTE, "inDiA", "India", "Siam");
      assertContains(INVALID_COUNTRY.getMessage("InDiA"));
      assertContains(ROUTE_INFO.getMessage("[India, Siam]"));
    }

    @Test
    public void T2_05_route_multiple_invalid_then_valid() throws Exception {
      runCommands(ROUTE, "inDiA", "India", "hello", "world", "Siam");
      assertContains(INVALID_COUNTRY.getMessage("InDiA"));
      assertContains(INVALID_COUNTRY.getMessage("Hello"));
      assertContains(INVALID_COUNTRY.getMessage("World"));
      assertContains(ROUTE_INFO.getMessage("[India, Siam]"));
    }

    @Test
    public void T2_06_route_length_3_congo_egypt() throws Exception {
      runCommands(ROUTE, "Congo", "Egypt");
      assertContains(ROUTE_INFO.getMessage("[Congo, North Africa, Egypt]"));
    }

    @Test
    public void T2_07_route_length_4_argentina_congo() throws Exception {
      runCommands(ROUTE, "argentina", "congo");
      assertContains(ROUTE_INFO.getMessage("[Argentina, Brazil, North Africa, Congo]"));
    }

    @Test
    public void T2_08_route_length_4_congo_argentina() throws Exception {
      runCommands(ROUTE, "congo", "argentina");
      assertContains(ROUTE_INFO.getMessage("[Congo, North Africa, Brazil, Argentina]"));
    }

    @Test
    public void T2_09_route_long_eastern_aus_gb() throws Exception {
      runCommands(ROUTE, "Eastern Australia", "Great Britain");
      assertContains(
          ROUTE_INFO.getMessage(
              "[Eastern Australia, New Guinea, Indonesia, Siam, India, Middle East, Ukraine,"
                  + " Scandinavia, Great Britain]"));
    }

    @Test
    public void T2_10_continent_info_europe_asia_aus() throws Exception {
      runCommands(ROUTE, "Great Britain", "Eastern Australia");
      assertContains(
          ROUTE_INFO.getMessage(
              "[Great Britain, Scandinavia, Ukraine, Ural, China, Siam, Indonesia, New Guinea,"
                  + " Eastern Australia]"));
      assertContains(CONTINENT_INFO.getMessage("[Europe (10), Asia (22), Australia (5)]"));
    }

    @Test
    public void T2_11_continent_info_single_asia() throws Exception {
      runCommands(ROUTE, "Ural", "Japan");
      assertContains(ROUTE_INFO.getMessage("[Ural, Siberia, Mongolia, Japan]"));
      assertContains(CONTINENT_INFO.getMessage("[Asia (18)]"));
    }

    @Test
    public void T2_12_continent_info_africa_europe_asia() throws Exception {
      runCommands(ROUTE, "North Africa", "Kamchatka");
      assertContains(
          ROUTE_INFO.getMessage(
              "[North Africa, Southern Europe, Ukraine, Ural, Siberia, Yakutsk, Kamchatka]"));
      assertContains(CONTINENT_INFO.getMessage("[Africa (0), Europe (11), Asia (33)]"));
    }

    @Test
    public void T2_13_fuel_info_japan_alberta() throws Exception {
      runCommands(ROUTE, "Japan", "Alberta");
      assertContains(ROUTE_INFO.getMessage("[Japan, Kamchatka, Alaska, Alberta]"));
      assertContains(FUEL_INFO.getMessage("7"));
    }

    @Test
    public void T2_14_fuel_info_zero_cost_greenland_iceland() throws Exception {
      runCommands(ROUTE, "Greenland", "Iceland");
      assertContains(ROUTE_INFO.getMessage("[Greenland, Iceland]"));
      assertContains(FUEL_INFO.getMessage("0"));
    }

    @Test
    public void T2_15_fuel_info_ural_venezuela() throws Exception {
      runCommands(ROUTE, "Ural", "Venezuela");
      assertContains(
          ROUTE_INFO.getMessage(
              "[Ural, Ukraine, Southern Europe, North Africa, Brazil, Venezuela]"));
      assertContains(
          CONTINENT_INFO.getMessage("[Asia (0), Europe (11), Africa (5), South America (2)]"));
      assertContains(FUEL_INFO.getMessage("18"));
    }

    @Test
    public void T2_16_fuel_continent_max_europe() throws Exception {
      runCommands(ROUTE, "Ural", "Brazil");
      assertContains(
          ROUTE_INFO.getMessage("[Ural, Ukraine, Southern Europe, North Africa, Brazil]"));
      assertContains(
          CONTINENT_INFO.getMessage("[Asia (0), Europe (11), Africa (5), South America (0)]"));
      assertContains(FUEL_INFO.getMessage("16"));
      assertContains(FUEL_CONTINENT_INFO.getMessage("Europe (11)"));
    }

    @Test
    public void T2_17_fuel_continent_max_north_america() throws Exception {
      runCommands(ROUTE, "Argentina", "Japan");
      assertContains(
          ROUTE_INFO.getMessage(
              "[Argentina, Peru, Venezuela, Central America, Western United States, Alberta,"
                  + " Alaska, Kamchatka, Japan]"));
      assertContains(
          CONTINENT_INFO.getMessage("[South America (7), North America (15), Asia (6)]"));
      assertContains(FUEL_INFO.getMessage("28"));
      assertContains(FUEL_CONTINENT_INFO.getMessage("North America (15)"));
    }

    @Test
    public void T2_18_fuel_continent_max_australia() throws Exception {
      runCommands(ROUTE, "Western Australia", "Siam");
      assertContains(ROUTE_INFO.getMessage("[Western Australia, Indonesia, Siam]"));
      assertContains(CONTINENT_INFO.getMessage("[Australia (3), Asia (0)]"));
      assertContains(FUEL_INFO.getMessage("3"));
      assertContains(FUEL_CONTINENT_INFO.getMessage("Australia (3)"));
    }
  }

  @FixMethodOrder(MethodSorters.NAME_ASCENDING)
  public static class YourTests extends SysCliTest {

    public YourTests() {
      super(Main.class);
    }

    /**
     * This test checks if the information for all countries is printed correctly. It runs the
     * INFO_COUNTRY command for each country and verifies the output. This test also ensures that
     * the information for each country is accurate and contains the correct continent, fuel cost,
     * and neighboring countries, ensuring that the order of the neighboring countries is consistent
     * with the expected output.
     *
     * @throws Exception
     */
    @Test
    public void T1_01_info_all_countries() throws Exception {
      runCommands(
          INFO_COUNTRY, "Afghanistan",
          INFO_COUNTRY, "Alaska",
          INFO_COUNTRY, "Alberta",
          INFO_COUNTRY, "Argentina",
          INFO_COUNTRY, "Brazil",
          INFO_COUNTRY, "Central America",
          INFO_COUNTRY, "China",
          INFO_COUNTRY, "Congo",
          INFO_COUNTRY, "East Africa",
          INFO_COUNTRY, "Eastern Australia",
          INFO_COUNTRY, "Eastern United States",
          INFO_COUNTRY, "Egypt",
          INFO_COUNTRY, "Great Britain",
          INFO_COUNTRY, "Greenland",
          INFO_COUNTRY, "Iceland",
          INFO_COUNTRY, "India",
          INFO_COUNTRY, "Indonesia",
          INFO_COUNTRY, "Irkutsk",
          INFO_COUNTRY, "Japan",
          INFO_COUNTRY, "Kamchatka",
          INFO_COUNTRY, "Madagascar",
          INFO_COUNTRY, "Middle East",
          INFO_COUNTRY, "Mongolia",
          INFO_COUNTRY, "New Guinea",
          INFO_COUNTRY, "North Africa",
          INFO_COUNTRY, "Northern Europe",
          INFO_COUNTRY, "Northwest Territory",
          INFO_COUNTRY, "Ontario",
          INFO_COUNTRY, "Peru",
          INFO_COUNTRY, "Quebec",
          INFO_COUNTRY, "Scandinavia",
          INFO_COUNTRY, "Siam",
          INFO_COUNTRY, "Siberia",
          INFO_COUNTRY, "South Africa",
          INFO_COUNTRY, "Southern Europe",
          INFO_COUNTRY, "Ukraine",
          INFO_COUNTRY, "Ural",
          INFO_COUNTRY, "Venezuela",
          INFO_COUNTRY, "Western Australia",
          INFO_COUNTRY, "Western Europe",
          INFO_COUNTRY, "Western United States",
          INFO_COUNTRY, "Yakutsk");

      assertContains(
          COUNTRY_INFO.getMessage(
              "Afghanistan", "Asia", "1", "[Ukraine, Ural, China, India, Middle East]"));
      assertContains(
          COUNTRY_INFO.getMessage(
              "Alaska", "North America", "1", "[Alberta, Northwest Territory, Kamchatka]"));
      assertContains(
          COUNTRY_INFO.getMessage(
              "Alberta",
              "North America",
              "2",
              "[Alaska, Northwest Territory, Ontario, Western United States]"));
      assertContains(COUNTRY_INFO.getMessage("Argentina", "South America", "1", "[Peru, Brazil]"));
      assertContains(
          COUNTRY_INFO.getMessage(
              "Brazil", "South America", "2", "[Venezuela, North Africa, Argentina, Peru]"));
      assertContains(
          COUNTRY_INFO.getMessage(
              "Central America",
              "North America",
              "3",
              "[Western United States, Venezuela, Eastern United States]"));
      assertContains(
          COUNTRY_INFO.getMessage(
              "China", "Asia", "2", "[Afghanistan, Ural, Siberia, Mongolia, Siam, India]"));
      assertContains(
          COUNTRY_INFO.getMessage(
              "Congo", "Africa", "1", "[North Africa, East Africa, South Africa]"));
      assertContains(
          COUNTRY_INFO.getMessage(
              "East Africa",
              "Africa",
              "2",
              "[Egypt, Madagascar, South Africa, Congo, Middle East, North Africa]"));
      assertContains(
          COUNTRY_INFO.getMessage(
              "Eastern Australia", "Australia", "1", "[New Guinea, Western Australia]"));
      assertContains(
          COUNTRY_INFO.getMessage(
              "Eastern United States",
              "North America",
              "4",
              "[Western United States, Ontario, Quebec, Central America]"));
      assertContains(
          COUNTRY_INFO.getMessage(
              "Egypt", "Africa", "3", "[Southern Europe, Middle East, East Africa, North Africa]"));
      assertContains(
          COUNTRY_INFO.getMessage(
              "Great Britain",
              "Europe",
              "1",
              "[Iceland, Scandinavia, Northern Europe, Western Europe]"));
      assertContains(
          COUNTRY_INFO.getMessage(
              "Greenland",
              "North America",
              "5",
              "[Northwest Territory, Ontario, Quebec, Iceland]"));
      assertContains(
          COUNTRY_INFO.getMessage(
              "Iceland", "Europe", "2", "[Greenland, Great Britain, Scandinavia]"));
      assertContains(
          COUNTRY_INFO.getMessage("India", "Asia", "3", "[Middle East, Afghanistan, China, Siam]"));
      assertContains(
          COUNTRY_INFO.getMessage(
              "Indonesia", "Australia", "3", "[Siam, New Guinea, Western Australia]"));
      assertContains(
          COUNTRY_INFO.getMessage(
              "Irkutsk", "Asia", "4", "[Siberia, Yakutsk, Kamchatka, Mongolia]"));
      assertContains(COUNTRY_INFO.getMessage("Japan", "Asia", "5", "[Kamchatka, Mongolia]"));
      assertContains(
          COUNTRY_INFO.getMessage(
              "Kamchatka", "Asia", "6", "[Yakutsk, Alaska, Japan, Mongolia, Irkutsk]"));
      assertContains(
          COUNTRY_INFO.getMessage("Madagascar", "Africa", "4", "[East Africa, South Africa]"));
      assertContains(
          COUNTRY_INFO.getMessage(
              "Middle East",
              "Asia",
              "7",
              "[Ukraine, Afghanistan, India, Egypt, East Africa, Southern Europe]"));
      assertContains(
          COUNTRY_INFO.getMessage(
              "Mongolia", "Asia", "8", "[Siberia, Irkutsk, Kamchatka, Japan, China]"));
      assertContains(
          COUNTRY_INFO.getMessage(
              "New Guinea", "Australia", "2", "[Indonesia, Eastern Australia, Western Australia]"));
      assertContains(
          COUNTRY_INFO.getMessage(
              "North Africa",
              "Africa",
              "5",
              "[Western Europe, Southern Europe, Egypt, Congo, Brazil, East Africa]"));
      assertContains(
          COUNTRY_INFO.getMessage(
              "Northern Europe",
              "Europe",
              "3",
              "[Great Britain, Ukraine, Southern Europe, Western Europe, Scandinavia]"));
      assertContains(
          COUNTRY_INFO.getMessage(
              "Northwest Territory",
              "North America",
              "6",
              "[Alaska, Greenland, Ontario, Alberta]"));
      assertContains(
          COUNTRY_INFO.getMessage(
              "Ontario",
              "North America",
              "7",
              "[Northwest Territory, Greenland, Quebec, Eastern United States, Western United"
                  + " States, Alberta]"));
      assertContains(
          COUNTRY_INFO.getMessage("Peru", "South America", "4", "[Brazil, Argentina, Venezuela]"));
      assertContains(
          COUNTRY_INFO.getMessage(
              "Quebec", "North America", "8", "[Ontario, Greenland, Eastern United States]"));
      assertContains(
          COUNTRY_INFO.getMessage(
              "Scandinavia", "Europe", "4", "[Great Britain, Ukraine, Northern Europe, Iceland]"));
      assertContains(COUNTRY_INFO.getMessage("Siam", "Asia", "9", "[India, China, Indonesia]"));
      assertContains(
          COUNTRY_INFO.getMessage(
              "Siberia", "Asia", "10", "[Ural, Yakutsk, Irkutsk, Mongolia, China]"));
      assertContains(
          COUNTRY_INFO.getMessage(
              "South Africa", "Africa", "6", "[Congo, East Africa, Madagascar]"));
      assertContains(
          COUNTRY_INFO.getMessage(
              "Southern Europe",
              "Europe",
              "5",
              "[Western Europe, Northern Europe, Ukraine, Egypt, North Africa, Middle East]"));
      assertContains(
          COUNTRY_INFO.getMessage(
              "Ukraine",
              "Europe",
              "6",
              "[Scandinavia, Ural, Afghanistan, Middle East, Southern Europe, Northern Europe]"));
      assertContains(
          COUNTRY_INFO.getMessage("Ural", "Asia", "11", "[Ukraine, Siberia, Afghanistan, China]"));
      assertContains(
          COUNTRY_INFO.getMessage(
              "Venezuela", "South America", "3", "[Central America, Brazil, Peru]"));
      assertContains(
          COUNTRY_INFO.getMessage(
              "Western Australia", "Australia", "4", "[Indonesia, New Guinea, Eastern Australia]"));
      assertContains(
          COUNTRY_INFO.getMessage(
              "Western Europe",
              "Europe",
              "7",
              "[Great Britain, Northern Europe, Southern Europe, North Africa]"));
      assertContains(
          COUNTRY_INFO.getMessage(
              "Western United States",
              "North America",
              "9",
              "[Alberta, Ontario, Eastern United States, Central America]"));
      assertContains(
          COUNTRY_INFO.getMessage("Yakutsk", "Asia", "12", "[Siberia, Kamchatka, Irkutsk]"));
    }

    /**
     * This test checks if the INFO_COUNTRY command works correctly with different casing variations
     * for the country name "Northwest Territory", where these variations should NOT result in the
     * information for the country being printed.
     *
     * @throws Exception
     */
    @Test
    public void T1_02_info_all_casing_variations() throws Exception {
      // runCommands(INFO_COUNTRY, "Northwest Territory");
      runCommands(
          INFO_COUNTRY,
          "northwest-territory",
          "nORTHWEST tERRITORY",
          "nOrThWeSt tErRiToRy",
          "northwest territorY",
          "Iceland"); // different country to ensure the command actually ends
      assertDoesNotContain(
          COUNTRY_INFO.getMessage(
              "Northwest Territory",
              "North America",
              "6",
              "[Alaska, Greenland, Ontario, Alberta]"));
    }

    /**
     * This test checks if the INFO_COUNTRY command works correctly with different casing variations
     * for the country name "Northwest Territory", where these variations should result in the
     * information for the country being printed.
     *
     * @throws Exception
     */
    @Test
    public void T1_03_info_correct_casing_variations() throws Exception {
      runCommands(
          INFO_COUNTRY, "Northwest Territory",
          INFO_COUNTRY, "northwest Territory",
          INFO_COUNTRY, "northwest territory",
          INFO_COUNTRY, "Iceland"); // different country to ensure the command actually ends
      assertContains(
          COUNTRY_INFO.getMessage(
              "Northwest Territory",
              "North America",
              "6",
              "[Alaska, Greenland, Ontario, Alberta]"));
      assertDoesNotContain("ERROR! This country was not found");
    }

    /**
     * This test checks if the ROUTE command works correctly with different casing variations for
     * the country name "Northwest Territory", where these variations should NOT result in the route
     * being printed.
     *
     * @throws Exception
     */
    @Test
    public void T2_01_route_all_casing_variations() throws Exception {
      runCommands(
          ROUTE,
          "northwest-territory",
          "nORTHWEST tERRITORY",
          "nOrThWeSt tErRiToRy",
          "northwest territorY",
          "Iceland", // different country to generate a route
          "Great Britain"); // different country to generate a route
      assertContains(INVALID_COUNTRY.getMessage("Northwest-territory"));
      assertContains(INVALID_COUNTRY.getMessage("NORTHWEST TERRITORY"));
      assertContains(INVALID_COUNTRY.getMessage("NOrThWeSt TErRiToRy"));
      assertContains(INVALID_COUNTRY.getMessage("Northwest TerritorY"));
      assertContains(ROUTE_INFO.getMessage("[Iceland, Great Britain]"));
      assertDoesNotContain(ROUTE_INFO.getMessage("[Northwest Territory, Greenland, Iceland]"));
      assertDoesNotContain(
          ROUTE_INFO.getMessage("[Northwest Territory, Greenland, Iceland, Great Britain]"));
    }

    /**
     * This test checks if the ROUTE command works correctly with different casing variations for
     * the country name "Northwest Territory", where these variations should result in the route
     * being printed.
     *
     * @throws Exception
     */
    @Test
    public void T2_02_route_correct_casing_variations() throws Exception {
      runCommands(
          ROUTE,
          "Northwest Territory",
          "northwest Territory",
          ROUTE,
          "northwest territory",
          "Iceland");
      assertContains(NO_CROSSBORDER_TRAVEL.getMessage());
      assertContains(ROUTE_INFO.getMessage("[Northwest Territory, Greenland, Iceland]"));
      assertDoesNotContain("ERROR! This country was not found");
    }

    /**
     * This test checks if the ROUTE command works correctly with two countries that are in the same
     * continent, ensuring that the route is printed correctly and the continent information is
     * accurate.
     *
     * @throws Exception
     */
    @Test
    public void T2_03_route_correct_continent_fuel_two_countries() throws Exception {
      runCommands(ROUTE, "Iceland", "Great Britain");
      assertContains(ROUTE_INFO.getMessage("[Iceland, Great Britain]"));
      assertContains(CONTINENT_INFO.getMessage("[Europe (0)]"));
      assertContains(FUEL_INFO.getMessage("0"));
      assertContains(FUEL_CONTINENT_INFO.getMessage("Europe (0)"));
      assertDoesNotContain(CONTINENT_INFO.getMessage("[null (0)]"));
      assertDoesNotContain(FUEL_CONTINENT_INFO.getMessage("null (0)"));
    }

    /**
     * This test checks if the ROUTE command correctly identifies and handles invalid inputs, which
     * are when the country given is not a valid country. It ensures that the invalid inputs are
     * correctly identified and that the route information is printed correctly, even when the input
     * contains multiple invalid countries.
     *
     * @throws Exception
     */
    @Test
    public void T2_04_route_correct_input_grabbing() throws Exception {
      runCommands(
          ROUTE, "great", "britain", "Great Britain", "Western", "Europe", "Western Australia");
      assertContains(INVALID_COUNTRY.getMessage("Great"));
      assertContains(INVALID_COUNTRY.getMessage("Britain"));
      assertContains(INVALID_COUNTRY.getMessage("Western"));
      assertContains(INVALID_COUNTRY.getMessage("Europe"));
      assertContains(
          ROUTE_INFO.getMessage(
              "[Great Britain, Scandinavia, Ukraine, Ural, China, Siam, Indonesia, Western"
                  + " Australia]"));
      assertDoesNotContain(ROUTE_INFO.getMessage("[Great Britain, Western Europe]"));
    }

    /**
     * This test checks if the ROUTE command returns the first found shortest route, even if there
     * is a cheaper route available that is not the first one found. It ensures that the route
     * information is printed correctly and that the shortest route is prioritized over the cheapest
     * route.
     *
     * @throws Exception
     */
    @Test
    public void T2_05_route_shortest_not_cheapest_route() throws Exception {
      runCommands(ROUTE, "Ukraine", "Kamchatka");
      // the shortest path (first one found in the tie)
      assertContains(ROUTE_INFO.getMessage("[Ukraine, Ural, Siberia, Yakutsk, Kamchatka]"));
      // same length, but cheaper, but should not be found since it was not the first one found
      assertDoesNotContain(
          ROUTE_INFO.getMessage("[Ukraine, Afghanistan, China, Mongolia, Kamchatka]"));
    }

    /**
     * This test checks if the ROUTE command correctly identifies and handles cases where the source
     * and destination countries are the same, ensuring that no cross-border travel is required and
     * that ONLY appropriate message is printed.
     *
     * @throws Exception
     */
    @Test
    public void T2_06_route_same_country() throws Exception {
      runCommands(ROUTE, "Alberta", "Alberta");
      assertContains(NO_CROSSBORDER_TRAVEL.getMessage());
      assertDoesNotContain("The fastest route is: ");
      assertDoesNotContain("You will spend this amount of fuel for your journey: ");
      assertDoesNotContain("You will visit the following continents: ");
      assertDoesNotContain("The continent where you will spend the most fuel is: ");
    }

    /**
     * This test checks if the ROUTE command correctly prints the route information when two
     * countries are neighbours.
     *
     * @throws Exception
     */
    @Test
    public void T2_07_route_neighbours() throws Exception {
      runCommands(ROUTE, "Alberta", "Northwest Territory");
      assertContains(ROUTE_INFO.getMessage("[Alberta, Northwest Territory]"));
      assertContains(FUEL_INFO.getMessage("0"));
      assertContains(CONTINENT_INFO.getMessage("[North America (0)]"));
      assertContains(FUEL_CONTINENT_INFO.getMessage("North America (0)"));
    }

    /**
     * This test checks if the ROUTE command correctly prints the route information when two
     * countries are neighbours, but when the continents of these countries are different.
     *
     * @throws Exception
     */
    @Test
    public void T2_08_route_neighbours_different_continents() throws Exception {
      runCommands(ROUTE, "Alaska", "Kamchatka");
      assertContains(ROUTE_INFO.getMessage("[Alaska, Kamchatka]"));
      assertContains(FUEL_INFO.getMessage("0"));
      assertContains(CONTINENT_INFO.getMessage("[North America (0), Asia (0)]"));
      assertContains(FUEL_CONTINENT_INFO.getMessage("North America (0)"));
    }

    /**
     * This test checks if when the ROUTE command is used, the route information is stored
     * correctly, in that continents that are repeated in the route are not counted multiple times.
     *
     * @throws Exception
     */
    @Test
    public void T2_09_route_repeating_continents() throws Exception {
      runCommands(ROUTE, "Ural", "Middle East");
      assertContains(ROUTE_INFO.getMessage("[Ural, Ukraine, Middle East]"));
      assertContains(CONTINENT_INFO.getMessage("[Asia (0), Europe (6)]"));
      assertContains(FUEL_INFO.getMessage("6"));
      assertContains(FUEL_CONTINENT_INFO.getMessage("Europe (6)"));
    }
  }
}
