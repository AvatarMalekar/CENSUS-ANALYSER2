package censusanalyser;

import com.opencsv.bean.CsvToBean;
import jdk.vm.ci.meta.MemoryAccessProvider;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class IndianCensusAdapter extends CensusAdapter {
    @Override
    public Map<String, CensusDTO> loadCensusData(String ...csvFilePath) {
        Map<String,CensusDTO> censusMap=new HashMap<>();
        censusMap=super.loadCensusData(IndiaCensusCSV.class,csvFilePath[0]);
        if(csvFilePath.length==1)
        return censusMap;
       return this.loadIndiaStateData(censusMap,csvFilePath[1]);
  }
    private Map<String, CensusDTO> loadIndiaStateData(Map<String, CensusDTO> censusMap, String indiaStateCode) {
        CsvToBean<IndiaStateCodeCSV> csvToBean;
        try(Reader reader = Files.newBufferedReader(Paths.get(indiaStateCode));) {
            ICSVBuilder icsvBuilder=CSVBuilderFactory.createBuilder();
            Iterator<IndiaStateCodeCSV> codeIterator=icsvBuilder.getCSVIterator(reader,IndiaStateCodeCSV.class);
            Iterable<IndiaStateCodeCSV> stateCodeCSV= ()-> codeIterator;
            StreamSupport.stream(stateCodeCSV.spliterator(),false)
                    .filter(indiaStateCodeCSV -> censusMap.get(indiaStateCodeCSV)!=null)
                    .forEach(code-> censusMap.get(code).stateCode=code.stateCode);
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
