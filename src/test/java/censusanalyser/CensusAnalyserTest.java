package censusanalyser;

import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class CensusAnalyserTest {
    CensusAnalyser censusAnalyser;
    private static final String INDIA_CENSUS_CSV_FILE_PATH = "./src/test/resources/IndiaStateCensusData.csv";
    private static final String WRONG_CSV_FILE_PATH = "./src/main/resources/IndiaStateCensusData.cv";
    private static final String INDIA_STATE_CODE="./src/test/resources/IndiaStateCode.csv";

    @Test
    public void givenIndianCensusCSVFileReturnsCorrectRecords() {
        try {
            int numOfRecords = censusAnalyser.loadIndiaCensusData(INDIA_CENSUS_CSV_FILE_PATH);
            Assert.assertEquals(29,numOfRecords);
        } catch (CensusAnalyserException e) { }
    }

    @Test
    public void givenIndiaCensusData_WithWrongFile_ShouldThrowException() {
        try {
            ExpectedException exceptionRule = ExpectedException.none();
            exceptionRule.expect(CensusAnalyserException.class);
            censusAnalyser.loadIndiaCensusData(WRONG_CSV_FILE_PATH);
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM,e.type);
        }
    }

    @Test
    public void givenIndiaStateCode_withCorrectFile_ShouldReturn_ExpectedValue() {
        try {
            censusAnalyser.loadIndiaCensusData(WRONG_CSV_FILE_PATH);
            int numOfRecords = censusAnalyser.loadIndiaStateData(INDIA_STATE_CODE);
            Assert.assertEquals(29,numOfRecords);
        } catch (CensusAnalyserException e) { }
    }

    @Test
    public void givenIndiaStateCode_withCorrectFilePath_ShouldReturn_ExpectedValue() {
        try {
            ExpectedException exceptionRule = ExpectedException.none();
            exceptionRule.expect(CensusAnalyserException.class);
            censusAnalyser.loadIndiaStateData(WRONG_CSV_FILE_PATH);
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM,e.type);
        }
    }

    @Test
    public void givenIndiaStateCensus_WhenSortedOnState_ShouldReturnSortedResult() {
        censusAnalyser.loadIndiaCensusData(INDIA_CENSUS_CSV_FILE_PATH);
        String sortedCensusData=censusAnalyser.getStateWiseSortCensusData();
        IndiaCensusCSV[] censusCsv=new Gson().fromJson(sortedCensusData,IndiaCensusCSV[].class);
        Assert.assertEquals("Andhra Pradesh",censusCsv[0].state);
    }

    @Test
    public void givenIndiaStateCensus_WhenSortedOnPopulation_ShouldReturnSortedResult() {
        censusAnalyser.loadIndiaCensusData(INDIA_CENSUS_CSV_FILE_PATH);
        String sortedCensusData=censusAnalyser.getPopulationWiseSortCensusData();
        IndiaCensusCSV[] censusCsv=new Gson().fromJson(sortedCensusData,IndiaCensusCSV[].class);
        Assert.assertEquals(199812341,censusCsv[censusCsv.length-1].population);
    }

    @Test
    public void givenIndiaStateCensus_WhenSortedOnDensity_ShouldReturnSortedResult() {
        censusAnalyser.loadIndiaCensusData(INDIA_CENSUS_CSV_FILE_PATH);
        String sortedCensusData=censusAnalyser.getDensityWiseSortCensusData();
        IndiaCensusCSV[] censusCsv=new Gson().fromJson(sortedCensusData,IndiaCensusCSV[].class);
        Assert.assertEquals(103804637,censusCsv[censusCsv.length-1].population);
    }

    @Before
    public void setUp() {
        censusAnalyser = new CensusAnalyser();
    }
}
