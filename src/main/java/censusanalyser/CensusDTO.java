package censusanalyser;

public class CensusDTO {
    public String state;
    public int population;
    public double totalArea;
    public double populationDensity;
    public String stateCode;

    public CensusDTO(IndiaCensusCSV csvObj) {
        state=csvObj.state;
        population=csvObj.population;
        totalArea=csvObj.totalArea;
        populationDensity=csvObj.populationDensity;
    }

    public CensusDTO(USCensusCSV csvObj) {
        stateCode=csvObj.stateCode;
        state=csvObj.stateus;
        population=csvObj.population;
        totalArea=csvObj.totalArea;
        populationDensity=csvObj.populationDensity;

    }
}
