package censusanalyser;

import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class CensusAnalyserTest {
    private static final String INDIA_CENSUS_CSV_FILE_PATH = "./src/test/resources/IndiaStateCensusData.csv";
    private static final String WRONG_CSV_FILE_PATH = "./src/main/resources/IndiaStateCensusData.cv";
    private static final String INDIA_STATE_CODE="./src/test/resources/IndiaStateCode.csv";
    private static final String US_CENSUS_CSV_FILE_PATH="./src/test/resources/USCensusData (1).csv";

    @Test
    public void givenIndianCensusCSVFileReturnsCorrectRecords() {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser();
            int numOfRecords = censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA,INDIA_CENSUS_CSV_FILE_PATH);
            Assert.assertEquals(29,numOfRecords);
        } catch (CensusAnalyserException e) { }
    }

    @Test
    public void givenIndiaCensusData_WithWrongFile_ShouldThrowException() {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser();
            ExpectedException exceptionRule = ExpectedException.none();
            exceptionRule.expect(CensusAnalyserException.class);
            censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA,WRONG_CSV_FILE_PATH);
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM,e.type);
        }
    }

    @Test
    public void givenIndiaStateCode_withCorrectFile_ShouldReturn_ExpectedValue() {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser();
            int numOfRecords= censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA,INDIA_CENSUS_CSV_FILE_PATH,INDIA_STATE_CODE);
            Assert.assertEquals(29,numOfRecords);
        } catch (CensusAnalyserException e) { }
    }

    @Test
    public void givenIndiaStateCode_withCorrectFilePath_ShouldReturn_ExpectedValue() {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser();
            ExpectedException exceptionRule = ExpectedException.none();
            exceptionRule.expect(CensusAnalyserException.class);
            censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA,INDIA_CENSUS_CSV_FILE_PATH,WRONG_CSV_FILE_PATH);
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM,e.type);
        }
    }

    @Test
    public void given_USCensusCSVFileReturns_CorrectNoOfRecords() {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser();
            int numOfRecords = censusAnalyser.loadCensusData(CensusAnalyser.Country.US,US_CENSUS_CSV_FILE_PATH);
            Assert.assertEquals(51,numOfRecords);
        }catch (CensusAnalyserException e) { }
    }

    @Test
    public void givenIndiaStateCensus_WhenSortedOnState_ShouldReturnSortedResult() {
            CensusAnalyser censusAnalyser = new CensusAnalyser();
            censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA,INDIA_CENSUS_CSV_FILE_PATH);
            String sortedCensusData=censusAnalyser.getSortedCensusData(CensusAnalyser.CountrySort.STATE);
            IndiaCensusCSV[] censusCsv=new Gson().fromJson(sortedCensusData,IndiaCensusCSV[].class);
            Assert.assertEquals("Andhra Pradesh",censusCsv[0].state);
    }

    @Test
    public void givenIndiaStateCensus_WhenSortedOnPopulation_ShouldReturnSortedResult() {
        CensusAnalyser censusAnalyser = new CensusAnalyser();
        censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA,INDIA_CENSUS_CSV_FILE_PATH);
        String sortedCensusData=censusAnalyser.getSortedCensusData(CensusAnalyser.CountrySort.POPULATION);
        IndiaCensusCSV[] censusCsv=new Gson().fromJson(sortedCensusData,IndiaCensusCSV[].class);
        Assert.assertEquals(199812341,censusCsv[censusCsv.length-1].population);
    }

    @Test
    public void givenUSStateCensus_WhenSortedOnState_ShouldReturnSortedResult() {
        CensusAnalyser censusAnalyser = new CensusAnalyser();
        censusAnalyser.loadCensusData(CensusAnalyser.Country.US,US_CENSUS_CSV_FILE_PATH);
        String sortedCensusData=censusAnalyser.getSortedCensusData(CensusAnalyser.CountrySort.STATE);
        IndiaCensusCSV[] censusCsv=new Gson().fromJson(sortedCensusData,IndiaCensusCSV[].class);
        Assert.assertEquals("Alabama",censusCsv[0].state);
    }

    @Test
    public void givenUSStateCensus_WhenSortedOnDensity_ShouldReturnSortedResult() {
        CensusAnalyser censusAnalyser = new CensusAnalyser();
        censusAnalyser.loadCensusData(CensusAnalyser.Country.US,US_CENSUS_CSV_FILE_PATH);
        String sortedCensusData=censusAnalyser.getSortedCensusData(CensusAnalyser.CountrySort.DENSITY);
        USCensusCSV[] censusCsv=new Gson().fromJson(sortedCensusData,USCensusCSV[].class);
        Assert.assertEquals(3805.61,censusCsv[censusCsv.length-1].populationDensity,0.0);
    }
  
    @Test
    public void givenUSStateCensus_WhenSortedOnPopulation_ShouldReturnSortedResult() {
        CensusAnalyser censusAnalyser = new CensusAnalyser();
        censusAnalyser.loadCensusData(CensusAnalyser.Country.US,US_CENSUS_CSV_FILE_PATH);
        String sortedCensusData=censusAnalyser.getSortedCensusData(CensusAnalyser.CountrySort.POPULATION);
        IndiaCensusCSV[] censusCsv=new Gson().fromJson(sortedCensusData,IndiaCensusCSV[].class);
        Assert.assertEquals(37253956,censusCsv[censusCsv.length-1].population);
    }

    @Test
    public void givenUSStateCensus_WhenSortedOnArea_ShouldReturnSortedResult() {
        CensusAnalyser censusAnalyser = new CensusAnalyser(CensusAnalyser.Country.INDIA);
        censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA,INDIA_CENSUS_CSV_FILE_PATH);
        String sortedCensusData=censusAnalyser.getSortedCensusData(CensusAnalyser.CountrySort.AREA);
        IndiaCensusCSV[] censusCsv=new Gson().fromJson(sortedCensusData,IndiaCensusCSV[].class);
        Assert.assertEquals(3702.0,censusCsv[0].totalArea,0.00);
    }

    @Test
    public void givenIndiaStateCensus_WhenSortedOnDensity_ShouldReturnSortedResult() {
        CensusAnalyser censusAnalyser = new CensusAnalyser();
        censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA,INDIA_CENSUS_CSV_FILE_PATH);
        String sortedCensusData=censusAnalyser.getSortedCensusData(CensusAnalyser.CountrySort.DENSITY);
        IndiaCensusCSV[] censusCsv=new Gson().fromJson(sortedCensusData,IndiaCensusCSV[].class);
        Assert.assertEquals(1102.0,censusCsv[censusCsv.length-1].populationDensity,0.00);
    }
    @Test
    public void givenIndiaStateCensus_WhenSortedOnArea_ShouldReturnSortedResult() {
        CensusAnalyser censusAnalyser = new CensusAnalyser();
        censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA,INDIA_CENSUS_CSV_FILE_PATH);
        String sortedCensusData=censusAnalyser.getSortedCensusData(CensusAnalyser.CountrySort.AREA);
        IndiaCensusCSV[] censusCsv=new Gson().fromJson(sortedCensusData,IndiaCensusCSV[].class);
        Assert.assertEquals(342239.0,censusCsv[censusCsv.length-1].totalArea,0.00);
    }

    @Test
    public void givenIndiaCensusAndUsCensus_ComparedOnDensity_ShouldReturn_MaximumDensityState() {
        CensusAnalyser censusAnalyser = new CensusAnalyser();
        double graterValueOfDensity;
        censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA,INDIA_CENSUS_CSV_FILE_PATH);
        String sortedCensusData=censusAnalyser.getSortedCensusData(CensusAnalyser.CountrySort.DENSITY);
        IndiaCensusCSV[] censusCsv=new Gson().fromJson(sortedCensusData,IndiaCensusCSV[].class);
        censusAnalyser.loadCensusData(CensusAnalyser.Country.US,US_CENSUS_CSV_FILE_PATH);
        String sortedCensusDataUS=censusAnalyser.getSortedCensusData(CensusAnalyser.CountrySort.DENSITY);
        USCensusCSV[] censusCsvUS=new Gson().fromJson(sortedCensusDataUS,USCensusCSV[].class);
        if(censusCsv[censusCsv.length-1].populationDensity>censusCsvUS[censusCsvUS.length-1].populationDensity)
            graterValueOfDensity=censusCsv[censusCsv.length-1].populationDensity;
        else
            graterValueOfDensity=censusCsvUS[censusCsvUS.length-1].populationDensity;
        Assert.assertEquals(3805.61,graterValueOfDensity,0.0);
    }
}
