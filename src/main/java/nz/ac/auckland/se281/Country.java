package nz.ac.auckland.se281;

// Country class is used to take in the data for a country and create a Country instance containing
// all its information
public class Country {
  //
  private String countryName;
  private String continent;
  private int countryCost;

  public Country(String countryName, String continent, int countryCost) {
    this.countryName = countryName;
    this.continent = continent;
    this.countryCost = countryCost;
  }

  public String getCountryName() {
    return this.countryName;
  }

  public String getCountryContinent() {
    return this.continent;
  }

  public String getCountryCost() {
    return Integer.toString(this.countryCost);
  }
}
