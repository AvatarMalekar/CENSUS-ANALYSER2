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
        totalArea=csvObj.areaInSqKm;
        populationDensity=csvObj.densityPerSqKm;
    }

    public CensusDTO(USCensusCSV csvObj) {
        stateCode=csvObj.stateId;
        state=csvObj.stateUs;
        population=csvObj.populationUs;
        totalArea=csvObj.totalAreaUs;
        populationDensity=csvObj.populationDensityUs;

    }
}
