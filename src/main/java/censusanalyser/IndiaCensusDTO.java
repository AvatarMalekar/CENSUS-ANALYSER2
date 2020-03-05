package censusanalyser;

public class IndiaCensusDTO {
    public String state;
     public int population;
     public int areaInSqMtr;
     public int densityPerSqMtr;
     public int stateCode;

    public IndiaCensusDTO(IndiaCensusCSV csvObj) {
        state=csvObj.state;
        population=csvObj.population;
        areaInSqMtr=csvObj.areaInSqKm;
        densityPerSqMtr=csvObj.densityPerSqKm;
    }
}
