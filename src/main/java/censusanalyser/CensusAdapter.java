package censusanalyser;

import com.opencsv.bean.CsvToBean;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.StreamSupport;

abstract class CensusAdapter {

    List<CensusDTO> censusList=new ArrayList<>();
   abstract Map<String, CensusDTO> loadCensusData(String... csvFilePath);
    public <E>Map<String, CensusDTO> loadCensusData(Class<E> className, String csvFilePath) {
        Map<String, CensusDTO> censusMap=new HashMap<>();
        CsvToBean<E> csvToBean;
        try(Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));) {
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
}

