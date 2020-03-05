package censusanalyser;

import com.opencsv.bean.CsvToBean;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class CensusLoader {
    Map<String, CensusDTO> censusMap=new HashMap<>();
    List<CensusDTO> censusList=new ArrayList<>();
    public Map<String, CensusDTO> loadCensusData(CensusAnalyser.Country country, String[] csvFilePath) {
        if(country.equals(CensusAnalyser.Country.INDIA))
            return this.loadCensusData(IndiaCensusCSV.class,csvFilePath);
        else if(country.equals(CensusAnalyser.Country.US))
            return this.loadCensusData(USCensusCSV.class,csvFilePath);
        else
            throw new CensusAnalyserException("Incorrect Country", CensusAnalyserException.ExceptionType.INVALID_COUNTRY);
    }
   public <E>Map<String, CensusDTO> loadCensusData(Class<E> className, String ... csvFilePath) {

        CsvToBean<E> csvToBean;
        try(Reader reader = Files.newBufferedReader(Paths.get(csvFilePath[0]));) {
            ICSVBuilder icsvBuilder= CSVBuilderFactory.createBuilder();
            Iterator<E> stateCensus=icsvBuilder.getCSVIterator(reader,className);

            Iterable<E> stateCodeCSV= ()-> stateCensus;
            if(className.getName().equals("censusanalyser.IndiaCensusCSV")){
                StreamSupport.stream(stateCodeCSV.spliterator(),false)
                        .map(IndiaCensusCSV.class::cast)
                        .forEach(code->censusMap.put(code.state,new CensusDTO(code)));}
            if(className.getName().equals("censusanalyser.USCensusCSV")){
                StreamSupport.stream(stateCodeCSV.spliterator(),false)
                        .map(USCensusCSV.class::cast)
                        .forEach(code->censusMap.put(code.stateUs,new CensusDTO(code)));}
if(csvFilePath.length==1){
    return censusMap;
}
            this.loadIndiaStateData(censusMap,csvFilePath[1]);
            return censusMap;
        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        }
        catch (IllegalStateException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.UNABLE_TO_PARSE);
        }
    }
    private int loadIndiaStateData(Map<String, CensusDTO> censusMap, String indiaStateCode) {
        CsvToBean<IndiaStateCodeCSV> csvToBean;
        try(Reader reader = Files.newBufferedReader(Paths.get(indiaStateCode));) {
            ICSVBuilder icsvBuilder=CSVBuilderFactory.createBuilder();
            Iterator<IndiaStateCodeCSV> codeIterator=icsvBuilder.getCSVIterator(reader,IndiaStateCodeCSV.class);
            Iterable<IndiaStateCodeCSV> stateCodeCSV= ()-> codeIterator;
            StreamSupport.stream(stateCodeCSV.spliterator(),false)
                    .filter(indiaStateCodeCSV -> this.censusMap.get(indiaStateCodeCSV)!=null)
                    .forEach(code-> this.censusMap.get(code).stateCode=code.stateCode);
            censusList= this.censusMap.values().stream().collect(Collectors.toList());
            return censusList.size();

        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        }
        catch (IllegalStateException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.UNABLE_TO_PARSE);
        }
    }


}
