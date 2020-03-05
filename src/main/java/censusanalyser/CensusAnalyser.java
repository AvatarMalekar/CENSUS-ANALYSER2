package censusanalyser;

import com.google.gson.Gson;
import com.opencsv.bean.CsvToBean;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class CensusAnalyser<E> {
    List<CensusDTO> censusList=null;
    Map<String, CensusDTO> censusMap=null;

    public CensusAnalyser() {
        censusList=new ArrayList<>();
        censusMap=new HashMap<>();
    }

    public int loadIndiaCensusData(String csvFilePath){

        CsvToBean<IndiaCensusCSV> csvToBean;
        try(Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));) {
            ICSVBuilder icsvBuilder=CSVBuilderFactory.createBuilder();
            Iterator<IndiaCensusCSV> stateCensus=icsvBuilder.getCSVIterator(reader,IndiaCensusCSV.class);
            while(stateCensus.hasNext()){
                CensusDTO tempObj=new CensusDTO(stateCensus.next());
                this.censusMap.put(tempObj.state,tempObj);
            }
            censusList=censusMap.values().stream().collect(Collectors.toList());
           return censusMap.size();
        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        }
        catch (IllegalStateException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.UNABLE_TO_PARSE);
        }
    }

    public int loadIndiaStateData(String indiaStateCode) {
        CsvToBean<IndiaStateCodeCSV> csvToBean;
        try(Reader reader = Files.newBufferedReader(Paths.get(indiaStateCode));) {
            ICSVBuilder icsvBuilder=CSVBuilderFactory.createBuilder();
           Iterator<IndiaStateCodeCSV> codeIterator=icsvBuilder.getCSVIterator(reader,IndiaStateCodeCSV.class);
           Iterable<IndiaStateCodeCSV> stateCodeCSV= ()-> codeIterator;
           StreamSupport.stream(stateCodeCSV.spliterator(),false)
                   .filter(indiaStateCodeCSV -> censusMap.get(indiaStateCodeCSV)!=null)
                   .forEach(code->censusMap.get(code).stateCode=code.stateCode);
            return censusMap.size();

        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        }
        catch (IllegalStateException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.UNABLE_TO_PARSE);
        }
    }
   private <E>int getCount(Iterator<E> censusCSVIterator) {
        Iterable<E> censusIterator=()-> censusCSVIterator;
        int namOfEateries = (int) StreamSupport.stream(censusIterator.spliterator(),false).count();
        return namOfEateries;
    }

    public String getStateWiseSortCensusData() {
            if(censusMap.size()==0 || censusMap==null){
                throw new CensusAnalyserException(CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
            }
            Comparator<CensusDTO> censusCSVComparator=Comparator.comparing(StateCensus->StateCensus.state);
            this.sort(censusCSVComparator);
            String json=new Gson().toJson(censusList);
            return json;

    }

    private void sort(Comparator<CensusDTO> censusCSVComparator) {
        for(int i=0;i<censusList.size()-1;i++){
            for(int j=0;j<censusList.size()-i-1;j++){
                CensusDTO census1=censusList.get(j);
                CensusDTO census2=censusList.get(j+1);
                if(censusCSVComparator.compare(census1,census2)>0){
                    censusList.set(j,census2);
                    censusList.set(j+1,census1);
                }
            }
        }
    }

    public String getPopulationWiseSortCensusData() {
        if(censusMap.size()==0 || censusMap==null){
            throw new CensusAnalyserException(CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        Comparator<CensusDTO> censusCSVComparator=Comparator.comparing(StateCensus->StateCensus.population);
        this.sort(censusCSVComparator);
        String json=new Gson().toJson(censusList);
        return json;
    }

    public String getDensityWiseSortCensusData() {
        if(censusMap.size()==0 || censusMap==null){
            throw new CensusAnalyserException(CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        Comparator<CensusDTO> censusCSVComparator=Comparator.comparing(StateCensus->StateCensus.populationDensity);
        this.sort(censusCSVComparator);
        String json=new Gson().toJson(censusList);
        return json;
    }


    public int loadUSCensusData(String usCensusCsvFilePath) {
        CsvToBean<USCensusCSV> csvToBean;
        try(Reader reader = Files.newBufferedReader(Paths.get(usCensusCsvFilePath));) {
            ICSVBuilder icsvBuilder=CSVBuilderFactory.createBuilder();
            Iterator<USCensusCSV> usCensus=icsvBuilder.getCSVIterator(reader,USCensusCSV.class);
            while(usCensus.hasNext()){
                CensusDTO tempObj=new CensusDTO(usCensus.next());
                this.censusMap.put(tempObj.state,tempObj);
            }
            censusList=censusMap.values().stream().collect(Collectors.toList());
            return censusMap.size();
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

