package censusanalyser;

import com.opencsv.bean.CsvBindByName;

public class USCensusCSV {
    @CsvBindByName(column = "State Id", required = true)
    public String stateId;

    @CsvBindByName(column = "State", required = true)
    public String stateUs;

    @CsvBindByName(column = "Population", required = true)
    public int populationUs;

    @CsvBindByName(column = "Total area", required = true)
    public double totalAreaUs;

    @CsvBindByName(column = "Population Density", required = true)
    public double populationDensityUs;

    @Override
    public String toString() {
        return "USCensusCSV{" +
                "State Id='" + stateId + '\'' +
                "State='" + stateUs + '\'' +
                ", Population='" + populationUs + '\'' +
                ", AreaInSqKm='" + totalAreaUs + '\'' +
                ", DensityPerSqKm='" + populationDensityUs + '\'' +
                '}';
    }
}
